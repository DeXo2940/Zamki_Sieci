#include "team.hpp"

Team::Team(int id, char color) {
    this->id = id;
    this->color = color;
    this->castle=new Castle();
}

int Team::getId() {
    return this->id;
}

char Team::getColor() {
    return this->color;
}

void Team::addToTeam(int nfds) {
    membersNfds.push_back(nfds);
}

void Team::removeFromTeam(int nfds) {
    for (int i = 0; i < membersNfds.size(); ++i) {
        if (membersNfds.at(i) == nfds) {
            membersNfds.erase(membersNfds.begin() + i);
            break;
        }
    }
}



