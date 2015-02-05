package org.u238.uno.events;

import java.io.Serializable;

import org.u238.uno.state.GameStateClient;
import org.u238.uno.state.GameStateServer;
import org.u238.uno.state.Player;

public abstract class GameEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	public Player player;
	public String playerName;
	public boolean isYou = false;
	
	public void setPlayer (Player p) {
		this.player = p;
		playerName = p.name;
	}
	
	public void makeYou() {
		playerName = "You";
		isYou = true;
	}
	
	public void makePrivate() {
		player = null;
		subMakePrivate();
	}
	
	public void copyParentAttrs(GameEvent e) {
		e.player = this.player;
		e.playerName = this.playerName;
		e.isYou = this.isYou;
	}
	
	public abstract void subMakePrivate();
	
	public abstract GameEvent clone();
	
	public abstract String makeString();
	
	public abstract void doEventServer(GameStateServer gs);

	public abstract void doEventClient(GameStateClient gs);

	public String toString() {
		return makeString();
	}
}
