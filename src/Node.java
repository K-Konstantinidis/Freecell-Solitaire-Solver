import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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

public class Node extends Define implements Comparable<Node>{

	private ArrayList<Card> freecells; //An ArrayList for the 4 freecells
	private ArrayList<Stack<Card>> foundations; //An ArrayList that has a stack of cards in each of the 4 foundations
	private ArrayList<Stack<Card>> tableau; //An ArrayList that has a stack of cards in each of the 8 tableau stacks 
	private HashMap<Card, String> position; //A map with a card and ts position
	public int g; //The g value of the node 
	public int h; //The h value of the node 
	public int f; //The f value of the node 
	private Node parent; //The parent of the node 
	private int i, method;
	private String direction; //The direction of the last move that happened

	public Node() {
		freecells = new ArrayList<>(4);
		foundations = new ArrayList<>(4);
		tableau = new ArrayList<>(8);
		position = new HashMap<>();

		for(i=0; i<4; i++)
			foundations.add(new Stack<>()); //Create 4 stacks
		
		for(i=0; i<S; i++)
			tableau.add(new Stack<>()); //Create 8 stacks
	}
	
	//This function checks if a node is a solution node
	//Output:
	//		true: We found a solution
	//		false: We didn't find a solution
	public boolean isSolution() {
		int i;
		if(!freecells.isEmpty()) //If there is a card in the freecells then it isn't a solution
			return false; 

		for(i=0; i<S; i++)
			if(!tableau.get(i).isEmpty())
				return false; //If there is a card in the tableau then it isn't a solution

		//If the cards aren't in the correct order
		for(i=0; i<4; i++) {
			Card previus = null;
			for(Card c : foundations.get(i)) {
				if(previus == null)
					previus = c; //If it's the first card of the foundation stack
				else
					if(previus.getSuit() != c.getSuit() || previus.getNumber() >= c.getNumber())					
						return false;
			}
		}

		return true;
	}
	
	//This function returns a list with all the children of a node
	//Input:
	//		The method which will be used to solve the puzzle
	//Output:
	//		The List with the children
	public ArrayList<Node> findChildren(int method) {
		ArrayList<Node> children = new ArrayList<Node>();
		
		children.addAll(this.fromFreecells(method));
		children.addAll(this.fromFoundation(method));
		children.addAll(this.fromTableau(method));

		return children;
	}
	
	//This function return a list with all the moves from the freecells to the other positions
	public ArrayList<Node> fromFreecells(int method) {
		int i;
		ArrayList<Node> children = new ArrayList<Node>();
		Card card;
		Node child;
		boolean flag = false;

		//If all the freecells are empty return an empty list
		if(this.getFreecells().isEmpty())
			return children;

		for(Card c : this.getFreecells()) {
			card = c; //Get each card from the freecells

			//Move card to fn
			child = ToFoundation(card, this.getFoundation(card.getSuit()));

			//If move was successful
			if(child != null)
				children.add(child);

			child = null;
			
			//Move to main Tableau
			for(i=0; i<S; i++) {
				Stack<Card> cardStack = this.getTableau().get(i);

				//If stack is empty or the top card of the stack is 1 number bigger than our card and its color is different
				if(cardStack.isEmpty() || ((card.getNumber() == (cardStack.peek().getNumber()-1)) && (!card.getColor().equalsIgnoreCase(cardStack.peek().getColor())))) {
					
					//If its an empty stack and we have already a created a similar child with 
					//a move of the card in an empty stack then no need to create it again
					if(cardStack.isEmpty() && flag)
						continue;

					child = this;

					child.moveToTableau(card, child.getTableau().get(i)); //Move the card to the tableau
					child.setParent(this);			//Set the parent
					child.setMethod(method);		//And the method
					child.setH(child.heuristic());	//And the heuristic value
					child.setF(method, child);		//And the f value(if we have to)
					child.setG(this.getG() + 1);	//And the g value

					//If the stack is empty make flag true
					if(cardStack.isEmpty()){
						child.setDirection("newstack " + card.suit + card.number);
						flag = true;
					} 
					else{
						Card thisCard = cardStack.peek(); //Get the top card of the stack to show where our card went
						child.setDirection("stack " + card.suit + card.number + " " + thisCard.suit + thisCard.number);
					}
					
					//If move was successful
					if(child != null){
						children.add(child);
						child = null;
					}
				}
			}
		}
		return children;
	}
	
	//This function return a list with all the moves from the fn to the other positions
	public ArrayList<Node> fromFoundation(int method) {
		int i,j;
		ArrayList<Node> children = new ArrayList<Node>();
		Stack<Card> fn;
		Card card;
		Node child;
		boolean flag = false;
		
		for(i=0; i<4; i++) {
			fn = this.getFoundations().get(i);
			//If the fn is empty the go to the next one
			if(fn.isEmpty())
				continue;

			card = fn.peek(); //Get the top card of the fn
			
			//Move card to fc
			child = ToFreecell(card);

			//If move was successful
			if(child != null)
				children.add(child);
			
			child=null;

			//Move to main Tableau
			for(j=0; j<S; j++) {
				Stack<Card> cardStack = this.getTableau().get(j);
				
				//If stack is empty or the top card of the stack is 1 number bigger than our card and its color is different
				if(cardStack.isEmpty() || ((card.getNumber() == (cardStack.peek().getNumber()-1)) && (!card.getColor().equalsIgnoreCase(cardStack.peek().getColor())))) {

					//If its an empty stack and we have already a created a similar child with 
					//a move of the card in an empty stack then no need to create it again
					if(cardStack.isEmpty() && flag)
						continue;
					
					child = this;

					child.moveToTableau(card, child.getTableau().get(j)); //Move the card to the tableau
					child.setParent(this);			//Set the parent
					child.setMethod(method);		//And the method
					child.setH(child.heuristic());	//And the heuristic value
					child.setF(method, child);		//And the f value(if we have to)
					child.setG(this.getG() + 1);	//And the g value
					
					//If the stack is empty make flag true
					if(cardStack.isEmpty()){
						child.setDirection("newstack " + card.suit + card.number);
						flag = true;
					} 
					else{
						Card thisCard = cardStack.peek();
						child.setDirection("stack " + card.suit + card.number + " " + thisCard.suit + thisCard.number);
					}
					
					//If move was successful
					if(child != null) {
						children.add(child);
						child = null;
					}
				}
			}
		}
		return children;
	}
	
	//This function return a list with all the moves from the tableau stacks to the other positions
	public ArrayList<Node> fromTableau(int method) {
		int i,j;
		ArrayList<Node> children = new ArrayList<Node>();
		Card card;
		Node child;
		boolean flag = false;

		for(i=0; i<S; i++) {
			Stack<Card> thisStack = this.getTableau().get(i); 
			//If the stack is empty go to the next one
			if(thisStack.isEmpty()) 
				continue;

			card = thisStack.peek(); //Get the top card of the tableau stack

			//Move card to fn
			child = ToFoundation(card, this.getFoundation(card.getSuit()));
			
			//If move was successful
			if(child != null)
				children.add(child);
			
			child = null;
			
			//Move card to fc
			child = ToFreecell(card);
			
			//If move was successful
			if(child != null)
				children.add(child);
			
			child = null;

			//Move to main Tableau
			for(j=0; j<S; j++) {
				//If have the same stack go to the next one
				if(i==j)
					continue;
				
				ArrayList<Stack<Card>> thisTableau = this.getTableau();

				//If this stack has only 1 card the do not move that card
				//in an empty stack because the move will be similar
				if(thisTableau.get(i).size() == 1 && thisTableau.get(j).isEmpty())
					continue;
				
				Stack<Card> cardStack = thisTableau.get(j);
				
				//If stack is empty or the top card of the stack is 1 number bigger than our card and its color is different
				if(cardStack.isEmpty() || ((card.getNumber() == (cardStack.peek().getNumber()-1)) && (!card.getColor().equalsIgnoreCase(cardStack.peek().getColor())))) {
					//If its an empty stack and we have already a created a similar child with 
					//a move of the card in an empty stack then no need to create it again
					if(cardStack.isEmpty() && flag)
						continue;
					
					child = this;

					child.moveToTableau(card, child.getTableau().get(j)); //Move the card to the tableau
					child.setParent(this);			//Set the parent
					child.setMethod(method);		//And the method
					child.setH(child.heuristic());	//And the heuristic value
					child.setF(method, child);		//And the f value(if we have to)
					child.setG(this.getG() + 1);	//And the g value
					
					//If the stack is empty make flag true
					if(cardStack.isEmpty()){
						child.setDirection("newstack " + card.suit + card.number);
						flag = true;
					} 
					else{
						Card thisCard = cardStack.peek();
						child.setDirection("stack " + card.suit + card.number + " " + thisCard.suit + thisCard.number);
					}
					
					//If move was successful
					if(child != null) {
						children.add(child);
						child = null;
					}
				}
			}
		}
		return children;
	}
	
	//This function returns the correct foundation stack for each card suit
	public Stack<Card> getFoundation(char suit){
		if(suit == 'D')
			return this.getFoundations().get(0);
		if(suit == 'S')
			return this.getFoundations().get(1);
		if(suit == 'C')
			return this.getFoundations().get(2);
		if(suit == 'H')
			return this.getFoundations().get(3);
		
		return null;
	}
	
	//This function gets a card and creates a child 
	//by moving the card in a foundation
	//Input: 
	//		Card: card, Stack<Card> fn: A foundation stack
	//Output:
	//		Node: child
	public Node ToFoundation(Card card, Stack<Card> fn) {
		//If fn is empty and we have an Ace trying to go there or
		//if the fn is not empty and the number of our card is 1 no bigger thann the last fn card and the card suits are the same
		if((fn.isEmpty() && card.getNumber() == 0) || (!fn.isEmpty() && ((card.number == (fn.peek().getNumber()+1)) && (card.suit == fn.peek().getSuit())))) {
			Node child = this;

			child.moveToFn(card); 			//Move the card to foundation
			child.setParent(this);			//Set the parent
			child.setMethod(method);		//And the method
			child.setH(child.heuristic());	//And the heuristic value
			child.setF(method, child);		//And the f value(if we have to)
			child.setG(this.getG() + 1);	//And the g value	
			child.setDirection("source " + card.suit + card.number); //And the direction

			return child;
		}
		return null;
	}
	
	//This function gets a card and creates a child 
	//by moving the card in a freecell
	//Input: 
	//		Card: card
	//Output:
	//		Node: child
	public Node ToFreecell(Card card) {
		if(this.freecells.size() < 4){
			Node child = this;
			
			child.moveToFc(card); //Move the card to freecell
			child.setParent(this);			//Set the parent
			child.setMethod(method);		//And the method
			child.setH(child.heuristic());	//And the heuristic value
			child.setF(method, child);		//And the f value(if we have to)
			child.setG(this.getG() + 1);	//And the g value
			child.setDirection("freecell " + card.suit + card.number); //And the direction

			return child;
		}
		return null;
	}
	
	//This function removes the card from its position
	//and moves it to a foundation
	public void moveToFn(Card card) {
		removeCard(card);
		this.getFoundation(card.getSuit()).add(card);
		position.put(card, "source");
	}
	
	//This function removes the card from its position
	//and moves it to the main tableau
	public void moveToTableau(Card card, Stack<Card> stack) {
		removeCard(card);
		stack.add(card);
		position.put(card, "stack");
	}
	
	//This function removes the card from its position
	//and moves it to a freecell
	public void moveToFc(Card card) {
		removeCard(card);
		this.getFreecells().add(card);
		position.put(card, "freecell");
	}
	
	//This function removes a card from its position
	public void removeCard(Card card) {
		if(position.get(card).equalsIgnoreCase("freecell"))
			freecells.remove(card);
		else if(position.get(card).equalsIgnoreCase("source"))
			this.getFoundation(card.getSuit()).remove(card);
		else if(position.get(card).equalsIgnoreCase("stack")){
			for(Stack<Card> s : this.getTableau()){
				if(!s.isEmpty() && s.peek().equals(card)) {
					s.pop();
					break;
				}
			}
		} 
	}

	//Giving a puzzle, this function computes the sum of the manhattan
	//distances between the current positions and the intended positions
	//for all the cards of the puzzle.
	public int heuristic() {
		int score=0;

		for(Card card : this.getFreecells())
			score += manhattan_distance(card, null);

		for(Stack<Card> s : this.getTableau())
			for(Card c : s)
				score += manhattan_distance(c, "tableau");

		return score;
	}

	//Giving a card and its position this function returns the
	//manhattan distance of this card from its intended final position.
	//This function is called whenever there is a card in the tableau
	//or in a freecell. So the score will always be at least 1 (move),
	//as it has to move at least once to go to the source(fn).
	int manhattan_distance(Card card, String position){
		int x, y;

		//Card does not exist -> error
		if(card.number<0 || card.number>=N) { 
			System.out.println("Card does not exist..");
			System.exit(1);
		}

	    //x=1 because the card is not in the foundation
	    //so we have to make at least 1 move to send it there.
	    x = 1;
	    if(card.number == 0)
	    	y = 0;
	    else
	    	y = get_y(card, position);

		return x+y;
	}	

	//Giving a card, this function counts the cards that are supposed to be
	//before this one and AREN'T in the foundation. Then it counts the
	//cards that are on top of this card and have to move before we can move
	//this one. For example, if our card is H5 and isn't in the foundation,
	//but there are the cards H1,H2, then the returning number will be 2.
	//(Because the cards before H5 that are not in the foundation but have to be
	//there before we put H5 are H3,H4->2 cards). After that it will count the
	//cards that are on top of H5 e.g. lets say that the sequence is H5->C5->S3,
	//then the returning number will be 2. So the final return will be 2+2=4 moves.
	int get_y(Card card, String position){
	    int count=0, count2=0, i=0;
	    Stack<Card> stackFound = null;
	    boolean found=false;
	    Stack<Card> fn = this.getFoundation(card.getSuit());

	    count = Math.abs(fn.size() - card.number);
	   
	    //Try to find the card in the tableau to see how many cards
	    //are on top of it.
	    if(position.equals("tableau")) {
	    	for(Stack<Card> stack : this.tableau) {
	    		for(Card c : stack) {
	    			if(c.equals(card)) {
	    				stackFound = stack;
	    				found = true;
	    				break;
	    			}
	    		}
	    		if(found)
	    			break;
	    	}
	    }
	    
	    if(stackFound != null) {
		    for(Card c : stackFound) {
		    	i++;
				if(c.equals(card))
					break;
		    }
		    count2 = stackFound.size() - i;
	    }
		    
		return count+count2;
	}
	
	//This function compares to nodes
	@Override
	public int compareTo(Node node) {
		//If we have the same node no need to check something
		if(this.equals(node))
			return 0;
			
		//For each of the 4 algorithms
		if(method == 1){
			if(this.g > node.getG())
				return 1;
			else if(this.g < node.getG())
				return -1;
			else
				return 1;
		}
		else if(method == 2){
			if (this.g > node.getG())
				return -1;
			else if (this.g < node.getG())
				return 1;
			else
				return -1;
		} 
		else if(method == 3){
			if (this.h > node.getH())
				return 1;
			else if (this.h < node.getH())
				return -1;
			else
				if(this.g < node.getG())
					return -1;
				return 1;
		}
		else{
			if(this.f > node.getF())
				return 1;
			else if(this.f < node.getF())
				return -1;
			else{
				if(this.g < node.getG())
					return -1;
				return 1;
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		int i;
		Stack<Card> oTabStack, thisTabStack;
		//If the object is null
		if(o==null)
			return false;

		Node n = (Node) o;

		//If its the same
		if(this == n)
			return true;

		//If the nodes haven't the same amount of cards in the freecells
		if(this.freecells.size() != n.getFreecells().size())
			return false;

		//If the nodes haven't the same cards in the freecells
		for(i=0; i<this.freecells.size(); i++)
			if(!n.getFreecells().contains(this.freecells.get(i)))
				return false;

		//If the nodes haven't the same amount of cards in the foundations
		//And the cards are exactly the same
		for(i=0; i<4; i++) {
			if(this.foundations.get(i).size() != n.getFoundations().get(i).size())
				return false;

			for(Card c : this.foundations.get(i))
				if(!n.getFoundations().get(i).contains(c))
					return false;
		}

		//If the nodes haven't the same amount of cards in the tableau
		//And the cards are exactly the same
		for (i=0; i<S; i++) {
			oTabStack = n.getTableau().get(i);
			thisTabStack = this.getTableau().get(i);
			
			if(thisTabStack.size() != oTabStack.size())
				return false;

			if ((!thisTabStack.isEmpty() && !oTabStack.isEmpty())&& !thisTabStack.peek().equals(oTabStack.peek()))
				return false;
		}
		return true;

	}
	
	//Getters
	public int getMethod() {return method;}
	public ArrayList<Card> getFreecells() {return freecells;}
	public ArrayList<Stack<Card>> getFoundations() {return foundations;}
	public ArrayList<Stack<Card>> getTableau() {return tableau;}
	public HashMap<Card, String> getPosition() {return position;}
	public int getG() {return g;}
	public int getH() {return h;}
	public int getF() {return f;}
	public String getDirection() {return direction;}
	public Node getParent() {return parent;}
	
	//Setters	
	public void setMethod(int method) {this.method = method;}
	public void setG(int g) {this.g = g;}
	public void setH(int h) {this.h = h;}
	public void setF(int method, Node node) {
		if(method == 4)
			this.f = node.getH() + node.getG();
	}
	public void setParent(Node parent) {this.parent = parent;}
	public void setDirection(String direction) {this.direction = direction;}
}