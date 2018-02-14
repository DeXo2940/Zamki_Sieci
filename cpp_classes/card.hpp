#include <string>
#ifndef CARD_H
#define CARD_H
using namespace std;

class Card
{
	string sign;
       bool reversed;
	public:
		Card (string sign);
		string getSign();
		void setSign(string sign);
		bool isReversed();
		void reverse(bool reversed);

};

#endif /* CARD_H */ 
