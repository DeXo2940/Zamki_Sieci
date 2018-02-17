#include "table.hpp"

Table::Table() {
    srand(time(NULL));
    for (int i = 2; i < 47; ++i) {
        this->table.push_back(*new Card(i));
    }
    this->shuffleTable(10000);
}

Card Table::getCard(int possition) {
    return this->table.at(possition);
}

void Table::shuffleTable(int howMuch) {
    int cardPos = 0;
    Card* tmpCard;
    for (int i = 0; i < howMuch; ++i) {
        cardPos = rand() % this->table.size();
        *tmpCard = this->getCard(cardPos);
        this->table.erase(this->table.begin() + cardPos);
        this->table.push_back(*tmpCard);
    }
}

void Table::printCards(char how) {
    if (how == 'n') {
        for (int i = 0; i<this->table.size(); ++i) {
            printf("%d ", this->getCard(i).getSign());
        }
    } else {
        for (int i = 0; i<this->table.size(); ++i) {
            printf("Karta %d:\t%d\n", i, this->getCard(i).getSign());
        }
    }
    printf("\n");
}




