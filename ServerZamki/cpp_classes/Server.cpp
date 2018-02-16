#include <cstdio>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <sys/poll.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <unistd.h>
#include <errno.h>


#include "table.cpp"
#include "card.cpp"

#define SERVER_PORT 1234
#define POLL_TIMEOUT 30000
#define MAX_FDS 200


struct pollfd fds[MAX_FDS];
int nfds = 1;
int values[MAX_FDS][5];
int value[MAX_FDS];

int main(int argc, char *argv[]) {
    short server_port = SERVER_PORT;
    if (argc > 1) {
        char *p;
        long int conv = strtol(argv[1], &p, 10);
        if (*p == 0 && (unsigned short) conv == conv) {
            server_port = (short) conv;
        } else {
            fprintf(stderr, "Invalid argument: %s\n", argv[1]);
            exit(1);
        }
    }

    int listen_desc = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_desc < 0) { //socket failed
        perror("socket() failed");
        exit(2);
    }

    const int one = 1;
    int rc = setsockopt(listen_desc, SOL_SOCKET, SO_REUSEADDR, (char*) &one, sizeof (one));
    if (rc < 0) { //setsockopt failed
        perror("setsockopt() failed");
        close(listen_desc);
        exit(3);
    }

    Table* table = new Table();
    //table->printCards('n');
    //table->printCards('k');

    struct sockaddr_in addr = {};
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = htonl(INADDR_ANY);
    addr.sin_port = htons(server_port);
    rc = bind(listen_desc, (struct sockaddr*) &addr, sizeof (addr));
    if (rc < 0) { //bind failed
        perror("bind() failed");
        close(listen_desc);
        exit(4);
    }

    rc = listen(listen_desc, 32);
    if (rc < 0) { //listen failed
        perror("listen() failed");
        close(listen_desc);
        exit(5);
    }

    fds[0].fd = listen_desc;
    fds[0].events = POLLIN;

    printf("READY!\n");

    while (1) {
        rc = poll(fds, nfds, POLL_TIMEOUT);

        if (rc < 0) { //poll failed
            perror("poll() failed");
            break;
        }

        if (rc == 0) { //poll timeout //continue
            printf("poll() timed out.\n");
            continue;
        }

        if (fds[0].revents & POLLIN) {
            int new_desc = accept(listen_desc, NULL, NULL);
            if (new_desc < 0) {
                perror("accept() failed");
                break;
            }
            printf("new connection accepted...\n");
            fds[nfds].fd = new_desc;
            fds[nfds].events = POLLIN;
            fds[nfds].revents = 0;
            values[nfds] = 0;
            nfds++;
        }

        for (int i = 1; i < nfds; i++) {
            int close_conn = 0;

            if (fds[i].revents & POLLERR) { //socket error close connection
                printf("socket error, closing connection...\n");
                close_conn = 1;
            } else if (fds[i].revents & POLLIN) {
                char buffer[5];
                
                rc = read(fds[i].fd, &buffer, 5*sizeof (char));
                if (rc < 0) { //read failed
                    perror("read() failed");
                    close_conn = 1;
                } else if (rc == 0) {
                    close_conn = 1;
                } else {

                    
                    
                    

                    rc = write(fds[i].fd, &buffer, 5*sizeof (char));
                    if (rc < 0) { //write failed
                        perror("write() failed");
                        close_conn = 1;
                    }
                }
            }

            if (close_conn) { //close connection
                printf("closing connection...\n");
                close(fds[i].fd);
                fds[i].fd *= -1;
            }
        }

        int n = nfds;
        nfds = 1;
        for (int i = 1; i < n; i++) {
            if (fds[i].fd < 0)
                continue;
            fds[nfds] = fds[i];
            values[nfds] = values[i];
            nfds++;
        }
    }

    return 0;
}
