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
    vector<bool> awaited;
    int size;
    Castle castle;
    int turn;
public:
    Team(int id,char color);
    char getColor();
    int getId();
    int getSize();
    int getTurn();
    void incTurn();
    
    int posOfNfds(int nfds);
    
    void printfNfds();
    
    void addCard(Card card);
    int getMember(int possition);
    
    void addToTeam(int nfds);
    void removeFromTeam(int nfds);
    void updateNfds(int nfds,int newNfds);
    bool isInTeam(int nfds);
    
    Castle getCastle();
    
    void await(bool set);
    bool isAwaited(int number);
    void unAwait(int number);
    void makeAwait(int number);
};

#endif /* TEAM_H */ 
