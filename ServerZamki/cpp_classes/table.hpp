#ifndef TABLE_HPP
#define TABLE_HPP
#include <vector>
#include <stdlib.h> 
#include <time.h> 
#include <cstdio>
#include "card.hpp"

using namespace std;

class Table {
    vector <Card> table;
public:
    Table();
    vector<Card> getTable();
    Card getCard(int possition);
    void printCards(char how);
private:
    void shuffleTable(int howMuch);
};

#endif /* TABLE_HPP */ 
