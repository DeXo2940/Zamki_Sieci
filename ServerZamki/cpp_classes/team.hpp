#ifndef TEAM_H
#define TEAM_H
#include <vector>
#include <stdlib.h> 

#include "castle.cpp"
using namespace std;

class Team {
    int id;
    char color;
    vector<int> membersNfds;
    int size;
    Castle castle;
public:
    Team(int id,char color);
    char getColor();
    int getId();
    int getSize();
    
    void printfNfds();
    
    void addCard(Card card);
    
    void addToTeam(int nfds);
    void removeFromTeam(int nfds);
    void updateNfds(int nfds,int newNfds);
    bool isInTeam(int nfds);
    
    Castle getCastle();
};

#endif /* TEAM_H */ 
