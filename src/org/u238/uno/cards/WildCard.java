package org.u238.uno.cards;

import org.u238.uno.state.GameStateServer;

public class WildCard extends ActionCard {
	private static final long serialVersionUID = 1L;
	
	public WildCard() {
		super(WILD);
		this.color = Color.NONE;
	}

	@Override
	public void doCardAction(GameStateServer gs) {
	}

	@Override
	public boolean canSetColor() {
		return true;
	}

	@Override
	public String makeString() {
		String output;
		if (color.equals(Color.NONE))
			output = "Wild";
		else
			output = "Wild, chosen color: " + color.name;
		return output;
	}
	
	@Override
	public boolean canPlaceOn(Card c) {
		return true;
	}
}
