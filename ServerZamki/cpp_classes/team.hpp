#ifndef TEAM_H
#define TEAM_H
#include <vector>

#include "castle.hpp"
using namespace std;

class Team {
    int id;
    char color;
    vector<int> membersNfds;
    Castle* castle;
public:
    Team(int id,char color);
    char getColor();
    int getId();
    
    void addToTeam(int nfds);
    void removeFromTeam(int nfds);
    
};

#endif /* TEAM_H */ 
