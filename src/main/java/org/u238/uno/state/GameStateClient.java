package org.u238.uno.state;

import org.u238.uno.cards.Card;

public class GameStateClient {
	public Hand hand;
	public Card topCard;
	public boolean gameEnded = false;
	
	public GameStateClient() {
		hand = new Hand();
	}
}
