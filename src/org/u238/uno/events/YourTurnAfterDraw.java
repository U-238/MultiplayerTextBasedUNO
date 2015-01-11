package org.u238.uno.events;

import org.u238.uno.cards.Card;
import org.u238.uno.state.GameStateClient;
import org.u238.uno.state.GameStateServer;

public class YourTurnAfterDraw extends GameEvent {
	private static final long serialVersionUID = 1L;
	
	public Card card;
	
	public YourTurnAfterDraw(Card card) {
		this.card = card;
	}
	
	@Override
	public void subMakePrivate() {
		this.card = null;
	}

	@Override
	public String makeString() {
		return "YourTurnAfterDraw";
	}

	@Override
	public GameEvent clone() {
		YourTurnAfterDraw e = new YourTurnAfterDraw(this.card);
		copyParentAttrs(e);
		return e;
	}

	@Override
	public void doEventServer(GameStateServer gs) {
	}

	@Override
	public void doEventClient(GameStateClient gs) {
	}
}
