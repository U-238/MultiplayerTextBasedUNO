package org.u238.uno.cards;

import org.u238.uno.state.GameStateServer;

public class DrawTwoCard extends SpecialCard {
	private static final long serialVersionUID = 1L;
	
	public DrawTwoCard() {
		super(DRAWTWO);
	}
	
	@Override
	public void doCardAction(GameStateServer gs) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canSetColor() {
		return false;
	}

	@Override
	public String makeString() {
		return this.color.name + " Draw Two";
	}
}
