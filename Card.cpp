#include <string>
using namespace std;

class Card {
    private:
        string sign;
        bool reversed;
    public:
		Card (string sign) {
			this->sign = sign;
			this->reversed = false;
		}
		
		string getSign() {
			return sign;
		}

		void setSign(string sign) {
			this->sign = sign;
		}

		bool isReversed() {
			return reversed;
		}

		void reverse(bool reversed) {
			this->reversed = reversed;
		}
};
