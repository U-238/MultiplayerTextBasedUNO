package org.u238.uno.events;

import org.u238.uno.GameStateClient;
import org.u238.uno.GameStateServer;
import org.u238.uno.cards.Card;

public class PlaceCard extends GameEvent {
	private static final long serialVersionUID = 1L;
	public Card cardPlayed;
	
	public PlaceCard(Card c) {
		this.cardPlayed = c;
	}

	@Override
	public void subMakePrivate() {
	}

	@Override
	public String makeString() {
		String output = playerName + " played " + cardPlayed.toString();
		//if (cardPlayed.canSetColor())
		//	output = output + ", new color " + newColor.toString();
		return output;
	}

	@Override
	public GameEvent clone() {
		PlaceCard e = new PlaceCard(cardPlayed);
		copyParentAttrs(e);
		return e;
	}

	@Override
	public void doEventServer(GameStateServer gs) {
		// TODO: validate that this card can be played
		if (!player.hand.remove(cardPlayed)) {
			System.err.println("Player played card they don't have!");
			System.err.println(player.name);
			System.err.println(cardPlayed.makeString());
			System.err.println(player.hand.printHand());
			System.exit(-1);
		}
		gs.pile.push(cardPlayed);
		gs.topCard = cardPlayed;
		
		if (player.hand.cards.size() == 0) {
			gs.eventBuffer.add(new PlayerFinished(player));
		}
	}

	@Override
	public void doEventClient(GameStateClient gs) {
		if (this.isYou) {
			gs.hand.remove(cardPlayed);
		}
		gs.topCard = cardPlayed;
	}
}
