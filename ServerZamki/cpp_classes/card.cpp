#include "card.hpp"

using namespace std;

Card::Card(int sign) {
    this->sign = sign;
}

int Card::getSign() {
    return this->sign;
}

void Card::setSign(int sign) {
    this->sign = sign;
}
