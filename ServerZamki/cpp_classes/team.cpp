#include "team.hpp"
#include <cstdio>

Team::Team(int id, char color) {
    this->id = id;
    this->color = color;
    this->castle = *new Castle();
    this->size = 0;
}

int Team::getId() {
    return this->id;
}

char Team::getColor() {
    return this->color;
}

void Team::addToTeam(int nfds) {
    membersNfds.push_back(nfds);
    awaited.push_back(false);
    this->size += 1;
}

void Team::removeFromTeam(int nfds) {
    for (unsigned int i = 0; i < membersNfds.size(); ++i) {
        if (membersNfds.at(i) == nfds) {
            membersNfds.erase(membersNfds.begin() + i);
            awaited.erase(awaited.begin() + i);
            this->size -= 1;
            break;
        }
    }
}

Castle Team::getCastle() {
    return this->castle;
}

int Team::getSize() {
    return this->size;
}

void Team::updateNfds(int nfds, int newNfds) {
    for (unsigned int i = 0; i < membersNfds.size(); ++i) {
        if (membersNfds.at(i) == nfds) {
            //membersNfds[i]=newNfds;
            membersNfds.at(i) = newNfds;
            //printf("Team: %d\tUpdate: %d->%d\tActual: %d\n",id,nfds,newNfds,membersNfds.at(i));
            break;
        }
    }
}

bool Team::isInTeam(int nfds) {
    for (unsigned int i = 0; i < membersNfds.size(); ++i) {
        if (membersNfds.at(i) == nfds) {
            return true;
        }
    }
    return false;
}

void Team::addCard(Card card) {
    this->castle.addCard(card);
}

void Team::printfNfds() {
    for (unsigned int i = 0; i<this->membersNfds.size(); ++i) {
        printf("%d: ", this->membersNfds.at(i));
    }
    printf("\n");
}

int Team::getMember(int possition) {
    return membersNfds.at(possition);
}

int Team::getTurn() {
    if (turn >= size) {
        turn = 0;
    }
    return turn;
}

void Team::incTurn() {
    turn += 1;
    if (turn >= size) {
        turn = 0;
    }
}

void Team::await(bool set) {
    if (set == true) {
        for (unsigned int i = 0; i < awaited.size(); ++i) {
            awaited.at(i) = true;
        }
    } else {
        for (unsigned int i = 0; i < awaited.size(); ++i) {
            awaited.at(i) = false;
        }
    }
}

void Team::unAwait(int number) {
    awaited.at(number) = false;
}

bool Team::isAwaited(int number) {
    return awaited.at(number);
}

void Team::makeAwait(int number) {
    awaited.at(number) = true;
}

int Team::posOfNfds(int nfds) {
    for (unsigned int i = 0; i < membersNfds.size(); ++i) {
        if (membersNfds.at(i) == nfds) {
            return i;
        }
    }
}

bool Team::isEveryoneDone() {
    for (unsigned int i = 0; i < awaited.size(); ++i) {
        if (awaited.at(i) == true) {
            return false;
        }
    }
    return true;
}



