#include "team.hpp"
#include <cstdio>

Team::Team(int id,char color) {
    this->id = id;
    this->color = color;
    this->castle= *new Castle();
    this->size=0;
}

int Team::getId() {
    return this->id;
}

char Team::getColor() {
    return this->color;
}

void Team::addToTeam(int nfds) {
    membersNfds.push_back(nfds);
    this->size+=1;
}

void Team::removeFromTeam(int nfds) {
    for (int i = 0; i < membersNfds.size(); ++i) {
        if (membersNfds.at(i) == nfds) {
            printf("Team: %d\tRem: %d\n",id,membersNfds.at(i));
            membersNfds.erase(membersNfds.begin() + i);
            this->size-=1;
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
    for (int i = 0; i < membersNfds.size(); ++i) {
        if (membersNfds.at(i) == nfds) {
            
            //membersNfds[i]=newNfds;
            membersNfds.at(i)=newNfds;
            printf("Team: %d\tUpdate: %d->%d\tActual: %d\n",id,nfds,newNfds,membersNfds.at(i));
            break;
        }
    }
}

bool Team::isInTeam(int nfds) {
    for(int i=0;i<membersNfds.size(); ++i){
        if(membersNfds.at(i)==nfds){
            return true;
        }
    }
    return false;
}

void Team::addCard(Card card) {
    this->castle.addCard(card);
}

void Team::printfNfds() {
    for(int i=0;i<this->size;++i){
        printf("%d: ",this->membersNfds.at(i));
    }
    printf("\n");
}
