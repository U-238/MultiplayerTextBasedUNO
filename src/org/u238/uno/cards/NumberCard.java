package org.u238.uno.cards;

import org.u238.uno.Color;
import org.u238.uno.GameStateServer;

public class NumberCard extends Card {
	private static final long serialVersionUID = 1L;
	public int number;
	
	public NumberCard(int number, Color color) {
		super(NUMBER);
		this.color = color;
		this.number = number;
	}

	@Override
	public void doCardAction(GameStateServer gs) {
	}

	@Override
	public boolean canSetColor() {
		return false;
	}

	@Override
	public String makeString() {
		return this.color.name + " " + String.valueOf(this.number);// + " (id " + String.valueOf(uniqueId) + ")";
	}
	
	@Override
	public boolean canPlaceOn(Card c) {
		if (this.color.color == c.color.color)
			return true;
		if (this.type != NUMBER)
			return false;
		else {
			NumberCard nc = (NumberCard) c;
			return (nc.number == this.number);
		}
	}
}
