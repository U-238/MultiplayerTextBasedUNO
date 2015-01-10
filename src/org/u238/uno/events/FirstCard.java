package org.u238.uno.events;

import org.u238.uno.GameStateClient;
import org.u238.uno.GameStateServer;
import org.u238.uno.cards.Card;

public class FirstCard extends GameEvent {
	private static final long serialVersionUID = 1L;
	public Card card;

	public FirstCard(Card card) {
		this.card = card;
	}
	
	@Override
	public void subMakePrivate() {
	}

	@Override
	public String makeString() {
		String output = "The first card on the pile is " + card.toString();
		return output;
	}

	@Override
	public GameEvent clone() {
		FirstCard e = new FirstCard(card);
		copyParentAttrs(e);
		return e;
	}

	@Override
	public void doEventServer(GameStateServer gs) {
	}

	@Override
	public void doEventClient(GameStateClient gs) {
		gs.topCard = card;
	}
}
