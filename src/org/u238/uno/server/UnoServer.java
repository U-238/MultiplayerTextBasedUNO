package org.u238.uno.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.u238.uno.events.DrawCard;
import org.u238.uno.events.FirstCard;
import org.u238.uno.events.GameEnd;
import org.u238.uno.events.GameEvent;
import org.u238.uno.events.ReplenishDeck;
import org.u238.uno.events.YourTurn;
import org.u238.uno.state.GameStateServer;
import org.u238.uno.state.Player;

public class UnoServer {
	GameStateServer gs;
	GameEventHandler events;
	ServerSocket serverSocket = null;

	public static void main(String[] args) {
		// Read server port from command-line input
		int server_port = 22278;
		if (args.length > 0) {
			int server_port_tmp = 0;
			try {
				server_port_tmp = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
			}
			if (server_port_tmp > 0 && server_port_tmp < 65536)
				server_port = server_port_tmp;
		}
		
		UnoServer srv = new UnoServer();
		srv.runDaemon(server_port);
	}
	
	void runDaemon(int server_port) {
		// Start listening for connections
		try {
			serverSocket = new ServerSocket(server_port);
		} catch (IOException e) {
			System.err.println("Server could not listen on port " + String.valueOf(server_port));
			System.exit(-1);
		}
		System.out.println("Server listening on port " + String.valueOf(server_port));

		// Get number of players
		int numPlayers = 0;
		try {
			BufferedReader serverConsoleInput = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Number of players: ");
			String numPlayersStr = serverConsoleInput.readLine();
			numPlayers = Integer.valueOf(numPlayersStr).intValue();
			System.out.println("Server will wait for " + String.valueOf(numPlayers) + " players");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		gs = new GameStateServer ();
		events = new GameEventHandler(gs);
		
		gs.setNumPlayers(numPlayers);
		
		try {
			// Accept connections from players
			int playersJoined = 0;
			while (playersJoined < numPlayers) {
				//System.out.println("Waiting for connection");
				Socket newSocket = serverSocket.accept();
				//System.out.println("Got connection");
				gs.newPlayer(newSocket);
				playersJoined++;
			}
			System.out.println("All players joined");
			
			// Set up game
			gs.prepareForGameStart();
			events.handleEvent(new FirstCard(gs.topCard));
			for (Player p : gs.player) {
				events.handleEvent(new DrawCard(p, 7));
			}
			
			// Start game
			boolean running = true;
			GameEvent e;
			while (running) {
				gs.currentPlayerOutputStream().writeObject(new YourTurn());
				e = (GameEvent) gs.currentPlayerInputStream().readObject();
				events.handleEventFromPlayer(e, gs.currentPlayer);
				GameEvent e2;
				while (!gs.eventBuffer.isEmpty()) {
					e2 = gs.eventBuffer.poll();
					events.handleEvent(e2);
				}
				if (gs.activePlayers() == 1) {
					events.handleEvent(new GameEnd());
					running = false;
				} else {
					if (gs.deck.size() < 5) {
						events.handleEvent(new ReplenishDeck());
					}
					gs.moveToNextPlayer();
				}
			}
			
			for (ObjectOutputStream s : gs.playerOutputs) {
				s.close();
			}
			for (ObjectInputStream s : gs.playerInputs) {
				s.close();
			}
			for (Socket s : gs.playerSockets) {
				if (s != null) s.close();
			}
			serverSocket.close();
			
			System.out.println("Server has shut down");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
}
