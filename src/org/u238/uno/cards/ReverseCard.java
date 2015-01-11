package org.u238.uno.cards;

import org.u238.uno.state.GameStateServer;

public class ReverseCard extends SpecialCard {
	private static final long serialVersionUID = 1L;
	
	public ReverseCard(Color color) {
		super(REVERSE);
		this.color = color;
	}
	
	@Override
	public void doCardAction(GameStateServer gs) {
		// Reverse game direction
		gs.changePlayDirection();
	}

	@Override
	public boolean canSetColor() {
		return false;
	}

	@Override
	public String makeString() {
		return this.color.name + " Reverse";
	}
}
