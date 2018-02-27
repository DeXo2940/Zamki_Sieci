#include "table.hpp"

Table::Table() {
    srand(time(NULL));
    for (int i = 2; i < 47; ++i) {
        this->table.push_back(*new Card(i));
    }
    this->shuffleTable(10000);
}

void Table::shuffleTable(int howMuch) {
    int cardPos = 0;
    Card* tmpCard = new Card(0);
    for (int i = 0; i < howMuch; ++i) {
        cardPos = rand() % this->table.size();
        *tmpCard = this->getCard(cardPos);
        this->table.erase(this->table.begin() + cardPos);
        this->table.push_back(*tmpCard);
    }
}

void Table::printCards(char how) {
    if (how == 'n') {
        for (unsigned int i = 0; i<this->table.size(); ++i) {
            printf("%d ", this->getCard(i).getSign());
        }
    } else {
        for (unsigned int i = 0; i<this->table.size(); ++i) {
            printf("Karta %d:\t%d\n", i, this->getCard(i).getSign());
        }
    }
    printf("\n");
}

int Table::getSize() {
    return this->table.size();
}

vector<Card> Table::getTable() {
    return this->table;
}

Card Table::getCard(int possition) {
    return this->table.at(possition);
}

void Table::removeCard(int possition) {
    //this->table.at(possition).setSign(0);
    table.erase(table.begin() + possition);
}

