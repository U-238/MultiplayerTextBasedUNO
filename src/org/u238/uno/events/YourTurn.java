package org.u238.uno.events;

import org.u238.uno.GameStateClient;
import org.u238.uno.GameStateServer;

public class YourTurn extends GameEvent {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void subMakePrivate() {
	}

	@Override
	public String makeString() {
		return "YourTurn";
	}

	@Override
	public GameEvent clone() {
		YourTurn e = new YourTurn();
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
