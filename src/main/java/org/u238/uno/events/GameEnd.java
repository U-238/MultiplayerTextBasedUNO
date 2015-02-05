package org.u238.uno.events;

import java.util.Collections;
import java.util.LinkedList;

import org.u238.uno.state.GameStateClient;
import org.u238.uno.state.GameStateServer;

public class GameEnd extends GameEvent {
	private static final long serialVersionUID = 1L;
	public LinkedList<String> finishedOrder;
	
	@Override
	public void subMakePrivate() {
	}

	@Override
	public String makeString() {
		String output = "Game over! Finished order:\n";
		for (String s : finishedOrder) {
			output = output + "   " + s + "\n";
		}
		return output;
	}

	@Override
	public GameEvent clone() {
		GameEnd e = new GameEnd();
		e.finishedOrder = this.finishedOrder;
		copyParentAttrs(e);
		return e;
	}

	@Override
	public void doEventServer(GameStateServer gs) {
		finishedOrder = new LinkedList<String>();
		for (int i=0; i<gs.totalPlayers; i++) {
			if (gs.player[i].finished) {
				finishedOrder.add(String.format("%3d: %s", gs.player[i].finishPosition, gs.player[i].name));
			} else {
				finishedOrder.add(String.format("DNF: %s", gs.player[i].name));
			}
		}
		Collections.sort(finishedOrder);
	}

	@Override
	public void doEventClient(GameStateClient gs) {
		gs.gameEnded = true;
	}
}
