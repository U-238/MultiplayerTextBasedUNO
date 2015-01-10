package org.u238.uno.events;

import java.util.Random;

import org.u238.uno.state.GameStateClient;
import org.u238.uno.state.GameStateServer;

public class ReplenishDeck extends GameEvent {
	private static final long serialVersionUID = 1L;

	@Override
	public void subMakePrivate() {
	}

	@Override
	public GameEvent clone() {
		ReplenishDeck e = new ReplenishDeck();
		copyParentAttrs(e);
		return e;
	}

	@Override
	public String makeString() {
		return "Deck nearly depleted; took all but top card from pile, re-shuffled, and added back to deck.";
	}

	@Override
	public void doEventServer(GameStateServer gs) {
		// Pop off the first card - this stays as the only
		// card in the "pile"
		if (!gs.topCard.equals(gs.pile.pop())) {
			System.err.println("topCard is not the first card on the pile!");
		}

		// Put the rest of the pile into the deck,
		// in a random order
		Random rand = new Random();
		int randNum;
		while (gs.pile.size() > 0) {
			randNum = rand.nextInt(gs.pile.size());
			gs.deck.add(gs.pile.get(randNum));
			gs.pile.remove(randNum);
		}
		
		// Now put the top card back on the pile
		gs.pile.push(gs.topCard);
	}

	@Override
	public void doEventClient(GameStateClient gs) {
	}
}
