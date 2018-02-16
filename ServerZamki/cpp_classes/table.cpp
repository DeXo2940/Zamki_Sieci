#include "table.hpp"

Table::Table() {
    srand(time(NULL));
    this->table = new vector<Card>;
    for (int i = 2; i < 47; ++i) {
        this->table.push_back(new Card(i));
    }
    this->shuffleTable(5000);
}

Card Table::getCard(int possition) {
    return this->table.at(possition);
}

void Table::shuffleTable(int howMuch) {
    int cardPos1, cardPos2;
    Card tmpCard;
    for (int i = 0; i < howMuch; ++i) {
        cardPos1 = rand() % 45;
        do {
            cardPos2 = rand() % 45;
        } while (cardPos1 == CardPos2);
        tmpCard = this->getCard(cardPos1);
        this->table[cardPos1] = this->table[cardPos2];
        this->table[cardPos2] = tmpCard;
    }
}
