package org.u238.uno.cards;

import org.u238.uno.state.GameStateServer;

public class WildCard extends SpecialCard {
	private static final long serialVersionUID = 1L;
	
	public WildCard() {
		super(WILD);
	}

	@Override
	public void doCardAction(GameStateServer gs) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canSetColor() {
		return true;
	}

	@Override
	public String makeString() {
		return "Wild (chosen color: " + color.name + ")";
	}
	
	@Override
	public boolean canPlaceOn(Card c) {
		return true;
	}
}
