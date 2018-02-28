#ifndef CASTLE_HPP
#define CASTLE_HPP
#include <vector>
#include <cstddef>
#include "card.hpp"
using namespace std;

class Castle {
    vector<Card> castle;
    int size;
public:
    void addCard(Card card);
    Castle();
    int getSize();
    Card* getCard(int n);
    Card* getLastCard();
private:
    void incSize();
};

#endif /* CASTLE_HPP */ 
