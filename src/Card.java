import java.util.Objects;

/**
						MIT License

		Copyright (c) 2022 Konstantinidis Konstantinos

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
**/

public class Card {
	public char suit;	 //The suit of the card (Diamond/Spade/Club/Heart)
	public int number;	 //The number of the card (0..(N-1))
	public String color; //The color of the card (Black/Red)

	public Card(char suit, int number) {
		this.suit = suit;
		this.number = number;

		if(suit=='S' || suit=='C')
			color = "black";
		else if(suit=='D' || suit=='H')
			color = "red";
		else
			color = "empty";
	}
		
	//This function creates a clone of a card
	@Override
	public Card clone() {
		return new Card(this.suit, this.number);
	}
	
	//This function creates a unique hash code for each object
	@Override
	public int hashCode() {
		return Objects.hash(color, suit, number);
	}
	
	//This function compares 2 cards to see if they are the same or not 
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if((o == null) || (getClass() != o.getClass()))
			return false;
		
		Card card = (Card) o;
		return suit==card.getSuit() && number==card.getNumber() && Objects.equals(color, card.getColor());
	}

	//Getters
	public char getSuit() {return suit;}
	public int getNumber() {return number;}
	public String getColor() {return color;}
}
