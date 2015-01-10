package org.u238.uno.state;

import java.io.Serializable;
import java.util.LinkedList;

import org.u238.uno.cards.Card;

public class Hand implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public LinkedList<Card> cards;
	
	public Hand() {
		cards = new LinkedList<Card>();
	}
	
	public void add(Card c) {
		cards.add(c);
	}
	
	public boolean remove(Card c) {
		return cards.remove(c);
	}
	
	public String printHand() {
		String out = "";
		int i=1;
		for (Card c : this.cards) {
			out = out + String.valueOf(i) + ": " + c.makeString() + "\n";
			i++;
		}
		return out;
	}
}
