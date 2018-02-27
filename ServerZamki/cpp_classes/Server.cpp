#include <cstdio>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <sys/poll.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <unistd.h>
#include <errno.h>

#include <stdlib.h> 
#include <time.h> 

#include "table.cpp"
#include "card.cpp"
#include "team.cpp"

#define NUMBER_OF_TEAMS 2
#define SERVER_PORT 1234
#define POLL_TIMEOUT 30000
#define MAX_FDS 200


struct pollfd fds[MAX_FDS];
int nfds = 1;
Team * teams[NUMBER_OF_TEAMS];
Table* table;
vector<int> readyList;
bool ready = false;

void updateFds();
bool contains(char com[5], char ch);
bool isValid(char com[5]);
int emptyTeam();
void printfTeamSizes();
void writeError(int number, bool isError);
void sendToAll(char buffer[], bool toLast);
int sumSize();
void sendToTeam(char buffer[], int teamNumber);
int getReadyMrServer(int argc, char *argv[]);
bool acceptNew(int listen_desc);
//update fds

void updateFds() {
    int n = nfds;
    nfds = 1;
    for (int i = 1; i < n; ++i) {
        if (fds[i].fd < 0)
            continue;
        fds[nfds] = fds[i];
        for (unsigned int j = 0; j < NUMBER_OF_TEAMS; ++j) {
            if (teams[j]->isInTeam(i)) {
                teams[j]->updateNfds(i, nfds);
                break;
            }
        }
        nfds++;
    }
}
//check if com contains char

bool contains(char com[5], char ch) {
    for (int i = 0; i < 5; ++i) {
        if (com[i] == ch) {
            return true;
        }
    }
    return false;
}
//check if com is valid (first alnd last character match)

bool isValid(char com[5]) {
    if (com[0] == com[4]) {
        return true;
    }
    return false;
}
//return empty team number

int emptyTeam() {
    for (unsigned int i = 0; i < NUMBER_OF_TEAMS; ++i) {
        if (teams[i]->getSize() == 0) {
            //printf("Empty team: %d\n", teams[i]->getId());
            return i;
        }
    }
    return -1;
}
//print size of each team

void printfTeamsSizes() {
    for (unsigned int i = 0; i < NUMBER_OF_TEAMS; ++i) {
        printf("Team %d size: %d\n", teams[i]->getId(), teams[i]->getSize());
    }
}

bool acceptNew(int listen_desc) {
    int new_desc = accept(listen_desc, NULL, NULL);
    bool ok = true;
    if (new_desc < 0) {
        perror("accept() failed");
        return false;
    }
    printf("New connection accepted...\n");
    fds[nfds].fd = new_desc;
    fds[nfds].events = POLLIN;
    fds[nfds].revents = 0;
    //dołącz do drużyny
    unsigned int teamNumber;
    for (teamNumber = 1; teamNumber < NUMBER_OF_TEAMS; ++teamNumber) {
        if (teams[teamNumber - 1]->getSize() <= teams[teamNumber]->getSize()) {
            break;
        }
    }
    teamNumber -= 1;
    teams[teamNumber]->addToTeam(nfds);
    //kolor i numer drużyny
    char buffer[6] = {'t', 'n', 'c', '0', 't', '\n'};
    buffer[1] = '0' + teams[teamNumber]->getId();
    buffer[2] = teams[teamNumber]->getColor();
    int rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
    //kolor drugiego teamu
    if (rc < 0) { //write failed
        ok = false;
    } else {
        buffer[0] = buffer[4] = 'n';
        for (unsigned int i = 0; i < NUMBER_OF_TEAMS && ok == true; ++i) {
            if (teamNumber == i) continue;
            buffer[1] = '0' + teams[i]->getId();
            buffer[2] = buffer[3] = teams[i]->getColor();
            rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
            if (rc < 0) { //write failed
                ok = false;
            }
        }
    }
    //ilość graczy w teamach i stan zamków
    for (unsigned int i = 0; i < NUMBER_OF_TEAMS && ok == true; ++i) {
        buffer[0] = buffer[4] = 's';
        buffer[1] = '0' + teams[i]->getId();
        buffer[2] = '0' + teams[i]->getSize() / 10;
        buffer[3] = '0' + teams[i]->getSize() % 10;
        rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
        //wielkość zamków
        if (rc < 0) { //write failed
            ok = false;
        } else {
            buffer[0] = buffer[4] = 'c';
            buffer[1] = '0' + teams[i]->getId();
            buffer[2] = '0' + teams[i]->getCastle().getSize() / 10;
            buffer[3] = '0' + teams[i]->getCastle().getSize() % 10;
            rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
            if (rc < 0) { //write failed
                ok = false;
            }
        }
        //stan zamków
        for (int j = 0; j < teams[i]->getCastle().getSize() && ok == true; ++j) {
            buffer[0] = buffer[4] = 'z';
            buffer[2] = '0' + teams[i]->getCastle().getCard(j)->getSign() / 10;
            buffer[3] = '0' + teams[i]->getCastle().getCard(j)->getSign() % 10;
            rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
            if (rc < 0) { //write failed
                ok = false;
            }
        }
    }
    //stan stołu - ilość kart
    buffer[0] = buffer[4] = 'j';
    buffer[1] = '0' + table->getSize() / 10;
    buffer[2] = '0' + table->getSize() % 10;
    buffer[3] = '0';
    rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
    if (rc < 0) { //write failed
        ok = false;
    }
    //koniec komunikatów, uznaj się za poprawnie dodanego 
    if (ok == true) {
        buffer[0] = buffer[4] = 'x';
        buffer[1] = buffer[2] = buffer[3] = '0';
        rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
        if (rc < 0) { //write failed
            ok = false;
        }
    }
    //READY R000R do gracza jeśli server is ready else add to ReadyList
    if (ready == false && ok == true) {
        readyList.push_back(nfds);
    } else if (ready == true && ok == true) {
        buffer[0] = buffer[4] = 'R';
        rc = write(fds[nfds].fd, &buffer, 6 * sizeof (char));
        if (rc < 0) {
            ok = false;
        }
    }
    if (ok == false) { //write failed
        writeError(nfds, true);
    } else {
        nfds++;
        //do wszystkich że dołączył nowy gracz
        buffer[0] = buffer[4] = 's';
        buffer[1] = '0' + teams[teamNumber]->getId();
        buffer[2] = '0' + teams[teamNumber]->getSize() / 10;
        buffer[3] = '0' + teams[teamNumber]->getSize() % 10;
        sendToAll(buffer, false);

    }
    //READY (R000R) do wszystkich oczekujących - dodanych wczesniej
    if (ready == false && nfds >= 3 && emptyTeam() == -1) {
        printf("Server ready for game!\n");
        ready = true;
        buffer[0] = buffer[4] = 'R';
        buffer[1] = buffer[2] = buffer[3] = '0';
        for (unsigned int i = 0; i < readyList.size(); ++i) {
            int num = readyList.at(i);
            rc = write(fds[num].fd, &buffer, 6 * sizeof (char));
            if (rc < 0) { //write failed
                writeError(num, true);
            }
        }
    }
    return true;
}

//handle error in write()

void writeError(int number, bool isErrror) {
    if (isErrror == true) {
        perror("write() failed");
    }
    printf("closing connection...\n");
    close(fds[number].fd);
    fds[number].fd *= -1;
    for (unsigned int teamNumber = 0; teamNumber < NUMBER_OF_TEAMS; ++teamNumber) {
        if (teams[teamNumber]->isInTeam(number)) {
            teams[teamNumber]->removeFromTeam(number);
            char buffer[6] = {'s', 't', 'n', 'n', 's', '\n'};
            buffer[1] = '0' + teams[teamNumber]->getId();
            buffer[2] = '0' + teams[teamNumber]->getSize() / 10;
            buffer[3] = '0' + teams[teamNumber]->getSize() % 10;
            updateFds();
            //do każdej osoby stan teamu z którego ktoś odszedł
            sendToAll(buffer, true);
            break;
        }
    }
}
//send buffer to all connected users    /all except last one

void sendToAll(char buffer[], bool toLast) {
    int rc = 0;
    for (int j = 1; (j < nfds && toLast == true) || (toLast == false && j < nfds - 1); j++) {
        rc = write(fds[j].fd, buffer, 6 * sizeof (char));
        if (rc < 0) { //write failed
            updateFds();
            writeError(j, true);
        }
    }
}
//send buffer to everyone in team

void sendToTeam(char buffer[], int teamNumber) {
    int rc = 0;
    for (int j = 0; j < teams[teamNumber]->getSize(); ++j) {
        rc = write(fds[teams[teamNumber]->getMember(j)].fd, buffer, 6 * sizeof (char));
        if (rc < 0) { //write failed
            updateFds();
            writeError(j, true);
        }
    }
}
//count sum of teams players

int sumSize() {
    int sum = 0;
    for (unsigned int i = 0; i < NUMBER_OF_TEAMS; ++i) {
        sum += teams[i]->getSize();
    }
    return sum;
}

int getReadyMrServer(int argc, char *argv[]) {
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
    return listen_desc;
}

int main(int argc, char *argv[]) {
    //int team = 0;
    //int phase = 0;

    int rc;
    int listen_desc = getReadyMrServer(argc, argv);

    table = new Table();
    //utwórz teamy, nadaj im "kolory"
    char colors[] = {'r', 'g', 'b', 'y', 'p', 'o', 'w'};
    if (NUMBER_OF_TEAMS < 2 || NUMBER_OF_TEAMS > 4 || NUMBER_OF_TEAMS>sizeof (colors) / sizeof (*colors)) {
        perror("Can't create teams");
        exit(13);
    }
    for (unsigned int i = 0; i < NUMBER_OF_TEAMS; ++i) {
        char color;
        do {
            color = colors[rand() % sizeof (colors) / sizeof (*colors)];
        } while (color == 0);
        teams[i] = new Team(i + 1, color);
        for (unsigned int j = 0; j<sizeof (colors) / sizeof (*colors); ++j) {
            if (colors[j] == color) {
                colors[j] = 0;
                break;
            }
        }
        printf("Team: %d\tColor: %c\n", teams[i]->getId(), teams[i]->getColor());
    }
    printf("Server ready!\n");

    bool end = false;
    while (end == false) {
        rc = poll(fds, nfds, POLL_TIMEOUT);
        if (rc < 0) { //poll failed
            perror("poll() failed");
            break;
        }
        if (rc == 0) { //poll timeout //continue
            printf("poll() timed out.\n");
            continue;
        }
        //accept new connection
        if (fds[0].revents & POLLIN) {
            end = acceptNew(listen_desc);
            if (end == true) {
                break;
            }
        }

        for (int i = 1; i < nfds; i++) {
            int close_conn = 0;

            if (fds[i].revents & POLLERR) { //socket error close connection
                printf("socket error, closing connection...\n");
                close_conn = 1;
            } else if (fds[i].revents & POLLIN) {
                char buffer[6];
                rc = read(fds[i].fd, &buffer, 5 * sizeof (char));
                if (rc < 0) { //read failed
                    perror("read() failed");
                    close_conn = 1;
                } else if (rc == 0) {
                    close_conn = 1;
                } else {
                    buffer[5] = '\0';
                    if (rc < 5 || contains(buffer, '\n') || !isValid(buffer)) { //invalid
                        if (buffer[0] != '\n') {
                            buffer[0] = buffer[4] = 'e';
                            buffer[1] = buffer[2] = buffer[3] = 'r';
                            buffer[5] = '\n';
                            rc = write(fds[i].fd, &buffer, 6 * sizeof (char));
                            printf("Invalid input\n");
                            if (rc < 0) { //write failed
                                perror("write() failed");
                                close_conn = 1;
                            }
                        }
                    } else {

                        printf("Readed: %s\n", buffer);

                        buffer[5] = '\n';
                        rc = write(fds[i].fd, &buffer, 6 * sizeof (char));
                        if (rc < 0) { //write failed
                            perror("write() failed");
                            close_conn = 1;
                        }
                    }
                }
            }
            //close connection
            if (close_conn == 1) {
                writeError(i, false);
            }
        }
        //koniec jeżeli widmo
        if (nfds < sumSize() + 1) {
            printf("Widmo...\n");
            perror("phantom Player\n");
            for (unsigned int i = 0; i < NUMBER_OF_TEAMS; ++i) {
                printf("Team: %d\t", teams[i]->getId());
                teams[i]->printfNfds();
            }
            end = true;
        }
        //koniec gry bo pusty team
        if (ready == true && emptyTeam() != -1) {

            char buffer[6] = {'W', 'i', 'n', '0', 'W', '\n'};
            printf("No players in a team.\n");
            sendToAll(buffer, true);
            end = true;
        }
    }
    printf("Server shutdown.\n");

    return 0;
}
