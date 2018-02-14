#include "card.hpp"

using namespace std;

Card::Card (string sign) {
	this->sign = sign;
	this->reversed = false;
}
		
string Card::getSign() {
	return this->sign;
}

void Card::setSign(string sign) {
	this->sign = sign;
}

bool Card::isReversed() {
	return reversed;
}

void Card::reverse(bool reversed) {
	this->reversed = reversed;
}
