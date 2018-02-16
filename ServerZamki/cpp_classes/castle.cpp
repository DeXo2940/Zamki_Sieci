#include "castle.hpp"

Castle::Castle() {
    this->size = 0;
    this->addCard(new Card(1));
}

int Castle::getSize() {
    return this->size;
}

void Castle::incSize() {
    this->size += 1;
}

void Castle::addCard(Card card) {
    this->castle.push_back(card);
    this->incSize();
}


