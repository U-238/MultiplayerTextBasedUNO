package org.u238.uno;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.u238.uno.cards.Card;
import org.u238.uno.cards.NumberCard;
import org.u238.uno.events.GameEvent;

public class GameStateServer {
	public Player[] player;
	public Socket[] playerSockets;
	public ObjectInputStream[] playerInputs;
	public ObjectOutputStream[] playerOutputs;
	public Color[] allColors;
	public LinkedList<Card> deck;
	public LinkedList<Card> pile;
	public Card topCard;
	public Player currentPlayer;
	private int currentPlayerId;
	public int totalPlayers;
	public boolean playDirection;
	public Queue<GameEvent> eventBuffer;
	public int nextFinishPosition;
	
	public void setNumPlayers(int numPlayers) {
		totalPlayers = numPlayers;
		player = new Player[totalPlayers];
		playerSockets = new Socket[totalPlayers];
		playerInputs = new ObjectInputStream[totalPlayers];
		playerOutputs = new ObjectOutputStream[totalPlayers];
	}
	
	public void newPlayer(Socket newSocket) {
		int i;
		for (i=0; i<totalPlayers; i++) {
			if (player[i] == null)
				break;
		}
		if (i>=totalPlayers) {
			System.err.println("Too many players, exiting...");
			System.exit(-1);
		}
		try {
			//System.out.println("Hello");
			playerInputs[i] = new ObjectInputStream(newSocket.getInputStream());
			playerOutputs[i] = new ObjectOutputStream(newSocket.getOutputStream());
			//System.out.println("Hello 2");
			String name = (String) playerInputs[i].readObject();
			player[i] = new Player(name);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void moveToNextPlayer() {
		currentPlayerId = nextPlayerId();
		currentPlayer = player[currentPlayerId];
	}
	
	public ObjectInputStream currentPlayerInputStream() {
		return playerInputs[currentPlayerId];
	}

	public ObjectOutputStream currentPlayerOutputStream() {
		return playerOutputs[currentPlayerId];
	}

	public Player nextPlayer() {
		return player[nextPlayerId()];
	}
	
	public int nextPlayerId() {
		int nextPlayerId = currentPlayerId;
		do {
			if (playDirection) {
				nextPlayerId = nextPlayerId + 1;
				if (nextPlayerId >= totalPlayers)
					nextPlayerId = 0;
			} else {
				nextPlayerId = nextPlayerId - 1;
				if (nextPlayerId < 0)
					nextPlayerId = totalPlayers - 1;
			}
		} while (player[nextPlayerId].finished);
		return nextPlayerId;
	}
	
	public void changePlayDirection() {
		playDirection = !playDirection;
	}
	
	public Player getPlayerByName(String pName) {
		Player p = null;
		for (Player p2 : this.player) {
			if (p2.name == pName) {
				p = p2;
				break;
			}
		}
		return p;
	}
	
	public int activePlayers() {
		int activePlayers = 0;
		for (Player p : player) {
			if (!p.finished)
				activePlayers++;
		}
		//System.out.println("Active players: " + activePlayers);
		return activePlayers;
	}
	
	public void prepareForGameStart() {
		eventBuffer = new LinkedList<GameEvent>();
		currentPlayerId = 0;
		currentPlayer = player[currentPlayerId];		
		allColors = new Color[]{Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
		nextFinishPosition = 1;
		
		// Create deck
		LinkedList<Card> tempCards = new LinkedList<Card>();
		
		for (Color c : allColors) {
			for (int i=1; i<10; i++) {
				tempCards.add(new NumberCard(i, c));
				// TODO: Add other color cards
			}
		}
		// TODO: Add wild cards
		
		// Shuffle deck
		deck = new LinkedList<Card>();
		Random rand = new Random();
		int randNum;
		while (tempCards.size() > 0) {
			randNum = rand.nextInt(tempCards.size());
			deck.add(tempCards.get(randNum));
			tempCards.remove(randNum);
		}
		System.out.println(deck.size() + " cards in deck");
		
		// Create pile and turn over first card
		pile = new LinkedList<Card>();
		topCard = deck.pop();
		pile.push(topCard);
	}
}
