package org.u238.uno.events;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.u238.uno.GameStateClient;
import org.u238.uno.GameStateServer;
import org.u238.uno.Player;
import org.u238.uno.cards.Card;

public class DrawCard extends GameEvent {
	private static final long serialVersionUID = 1L;
	public int numCardsDrawn;
	public LinkedList<Card> cardsDrawn = null;
	public boolean insufficientCards = false;
	
	public DrawCard() {
		numCardsDrawn = 1;
	}
	
	public DrawCard(Player p, int numCardsDrawn) {
		this.setPlayer(p);
		this.numCardsDrawn = numCardsDrawn;
	}

	@Override
	public void subMakePrivate() {
		cardsDrawn = null;
	}

	@Override
	public String makeString() {
		String output = playerName + " drew " + String.valueOf(numCardsDrawn) + " card";
		if (numCardsDrawn != 1)
			output = output + "s";
		if (cardsDrawn != null) {
			output = output + ": ";
			for (Card c : cardsDrawn) {
				output = output + c.toString() + ", ";
			}
			output = output.substring(0, output.length() - 2);
		}
		if (insufficientCards) {
			output = output + " (insufficient cards in deck!)";
		}
		return output;
	}

	@Override
	public GameEvent clone() {
		DrawCard e = new DrawCard();
		e.numCardsDrawn = this.numCardsDrawn;
		e.cardsDrawn = this.cardsDrawn;
		e.insufficientCards = this.insufficientCards;
		copyParentAttrs(e);
		return e;
	}

	@Override
	public void doEventServer(GameStateServer gs) {
		cardsDrawn = new LinkedList<Card>();
		for (int i=0; i<numCardsDrawn; i++) {
			try {
				cardsDrawn.add(gs.deck.pop());
			} catch (NoSuchElementException e) {
				numCardsDrawn = cardsDrawn.size();
				insufficientCards = true;
			}
		}
		for (Card c : cardsDrawn) {
			player.hand.add(c);
		}
	}

	@Override
	public void doEventClient(GameStateClient gs) {
		if (this.isYou) {
			for (Card c : cardsDrawn) {
				gs.hand.add(c);
			}
		}
	}
}
