package org.u238.uno.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.u238.uno.cards.Card;
import org.u238.uno.events.DrawCard;
import org.u238.uno.events.GameEvent;
import org.u238.uno.events.PlaceCard;
import org.u238.uno.state.GameStateClient;

public class UnoClient {
	GameStateClient gs;

	public static void main(String[] args) {
		UnoClient c = new UnoClient();
		c.run();
	}
	
	void run() {
		try {
			BufferedReader serverConsoleInput = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Your name: ");
			String name = serverConsoleInput.readLine();
			//System.out.println("Your name is " + name);
			
			Socket s = new Socket("localhost", 22278);
			
			gs = new GameStateClient ();
			ObjectOutputStream outToServer = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(s.getInputStream());
			
			/*try {
				Thread.sleep(2000);
			} catch (InterruptedException e3) {
				e3.printStackTrace();
			}*/
			//System.out.println("About to write object");
			outToServer.writeObject(name);
			//System.out.println("Wrote object");
			
			boolean running = true;
			GameEvent e;
			while (running) {
				e = (GameEvent) inFromServer.readObject();
				if (e.makeString().equals("YourTurn")) {
					// Your turn
					boolean validInput = true;
					System.out.println("\nIt's your turn!");
					System.out.println("Top card is: " + gs.topCard.makeString());
					System.out.println("Your hand:");
					System.out.print(gs.hand.printHand());
					
					do {
						//System.out.println("");
						System.out.print("Select card to play, or 'p' to pick up a card: ");
						String in = serverConsoleInput.readLine();
						System.out.println(" ");
						if (in.toLowerCase().equals("p")) {
							outToServer.writeObject(new DrawCard());
						} else {
							try {
								int number = Integer.valueOf(in).intValue();
								Card c = gs.hand.cards.get(number-1);
								
								if (!c.canPlaceOn(gs.topCard)) {
									System.out.println("You can't place that card!");
									validInput = false;
								} else {
									//System.out.println(c.makeString());
									outToServer.writeObject(new PlaceCard(c));
									validInput = true;
								}
							} catch (NumberFormatException e1) {
								validInput = false;
							} catch (IndexOutOfBoundsException e2) {
								validInput = false;
							}
						}
					} while (!validInput);
				} else {
					e.doEventClient(gs);
					System.out.println(e.makeString());
				}
				if (gs.gameEnded)
					running = false;
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
}
