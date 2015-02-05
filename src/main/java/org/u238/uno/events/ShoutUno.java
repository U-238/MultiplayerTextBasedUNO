package org.u238.uno.events;

import org.u238.uno.state.GameStateClient;
import org.u238.uno.state.GameStateServer;
import org.u238.uno.state.Player;

public class ShoutUno extends GameEvent {
	private static final long serialVersionUID = 1L;
	
	public ShoutUno() {
	}
	
	public ShoutUno(Player p) {
		setPlayer(p);
	}

	@Override
	public void subMakePrivate() {
	}

	@Override
	public String makeString() {
		String output;
		if (isYou)
			output = "You shout, \"UNO!\"";
		else
			output = playerName + " shouts, \"UNO!\"";
		return output;
	}

	@Override
	public GameEvent clone() {
		ShoutUno e = new ShoutUno();
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
