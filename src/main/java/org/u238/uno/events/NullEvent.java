package org.u238.uno.events;

import org.u238.uno.state.GameStateClient;
import org.u238.uno.state.GameStateServer;

public class NullEvent extends GameEvent {
	private static final long serialVersionUID = 1L;
	
	public NullEvent() {
	}

	@Override
	public void subMakePrivate() {
	}

	@Override
	public GameEvent clone() {
		NullEvent e = new NullEvent();
		copyParentAttrs(e);
		return e;
	}

	@Override
	public String makeString() {
		return "NullEvent";
	}

	@Override
	public void doEventServer(GameStateServer gs) {
	}

	@Override
	public void doEventClient(GameStateClient gs) {
	}
}
