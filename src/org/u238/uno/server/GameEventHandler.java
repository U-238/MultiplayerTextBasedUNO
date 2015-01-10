package org.u238.uno.server;

import java.io.IOException;

import org.u238.uno.GameStateServer;
import org.u238.uno.Player;
import org.u238.uno.events.GameEvent;

public class GameEventHandler {
	public GameStateServer gs;
	
	public GameEventHandler(GameStateServer gs) {
		this.gs = gs;
	}
	
	public void handleEventFromPlayer(GameEvent e, Player p) {
		// TODO: validation that player can do this event
		e.setPlayer(p);
		handleEvent(e);
	}
	
	public void handleEvent(GameEvent e) {
		e.doEventServer(gs);
		logEvent(e);
		broadcastEvent(e);
	}
	
	public void logEvent(GameEvent e) {
		System.out.println(e.toString());
	}
	
	public void broadcastEvent(GameEvent e) {
		GameEvent eventYou = e.clone();
		GameEvent eventOther = e.clone();
		Player player = e.player;
		
		eventYou.makeYou();
		eventOther.makePrivate();
		
		try {
			for (int i=0; i<gs.totalPlayers; i++) {
				if (gs.player[i] == player) {
					gs.playerOutputs[i].writeObject(eventYou);
				} else {
					gs.playerOutputs[i].writeObject(eventOther);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
