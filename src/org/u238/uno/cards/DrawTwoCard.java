package org.u238.uno.cards;

import org.u238.uno.events.DrawCard;
import org.u238.uno.state.GameStateServer;

public class DrawTwoCard extends ActionCard {
	private static final long serialVersionUID = 1L;
	
	public DrawTwoCard(Color color) {
		super(DRAWTWO);
		this.color = color;
	}
	
	@Override
	public void doCardAction(GameStateServer gs) {
		// Next player will draw two cards and miss their turn
		gs.eventBuffer.add(new DrawCard(gs.nextPlayer(), 2));
		gs.skipNextPlayer = true;
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
