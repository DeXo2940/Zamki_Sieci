#ifndef CASTLE_HPP
#define CASTLE_HPP
#include <vector>
#include "card.hpp"
using namespace std;

class Castle {
    vector<Card> castle;
    int size;
public:
    void addCard(Card card);
    Castle();
    int getSize();
private:
    void incSize();
};

#endif /* CASTLE_HPP */ 
