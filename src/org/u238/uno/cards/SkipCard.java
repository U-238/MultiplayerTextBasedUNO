package org.u238.uno.cards;

import org.u238.uno.state.GameStateServer;

public class SkipCard extends SpecialCard {
	private static final long serialVersionUID = 1L;
	
	public SkipCard(Color color) {
		super(SKIP);
		this.color = color;
	}
	
	@Override
	public void doCardAction(GameStateServer gs) {
		// Next player will miss their turn
		gs.skipNextPlayer = true;
	}

	@Override
	public boolean canSetColor() {
		return false;
	}

	@Override
	public String makeString() {
		return this.color.name + " Skip";
	}
}
