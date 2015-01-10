package org.u238.uno.events;

import org.u238.uno.GameStateClient;
import org.u238.uno.GameStateServer;
import org.u238.uno.Player;

public class PlayerFinished extends GameEvent {
	private static final long serialVersionUID = 1L;
	
	public PlayerFinished() {
	}
	
	public PlayerFinished(Player p) {
		setPlayer(p);
	}

	@Override
	public void subMakePrivate() {
	}

	@Override
	public String makeString() {
		String output;
		if (isYou)
			output = "You're finished!";
		else
			output = playerName + " has finished!";
		return output;
	}

	@Override
	public GameEvent clone() {
		PlayerFinished e = new PlayerFinished();
		copyParentAttrs(e);
		return e;
	}

	@Override
	public void doEventServer(GameStateServer gs) {
		player.finished = true;
		player.finishPosition = gs.nextFinishPosition;
		gs.nextFinishPosition++;
	}

	@Override
	public void doEventClient(GameStateClient gs) {
	}
}
