package org.u238.uno.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.u238.uno.cards.Card;
import org.u238.uno.cards.Color;
import org.u238.uno.events.DrawCard;
import org.u238.uno.events.GameEvent;
import org.u238.uno.events.NullEvent;
import org.u238.uno.events.PlaceCard;
import org.u238.uno.events.YourTurnAfterDraw;
import org.u238.uno.state.GameStateClient;

public class UnoClient {
	Color[] allColors = new Color[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
	GameStateClient gs;
	boolean sortingEnabled = false;

	public static void main(String[] args) {
		UnoClient c = new UnoClient();
		c.run();
	}
	
	void run() {
		String name = "";
		boolean clientRunning = true;
		while (clientRunning) {
			try {
				BufferedReader serverConsoleInput = new BufferedReader(new InputStreamReader(System.in));
				if (name.equals(""))
					System.out.print("Your name: ");
				else
					System.out.print("Your name (hit enter to use \"" + name + "\"): ");
				String tmpName = serverConsoleInput.readLine();
				if (tmpName.equals("")) {
					if (name.equals("")) {
						System.err.println("You did not enter a name!");
						System.exit(-1);
					} // Else leave name unchanged
				} else {
					name = tmpName;
				}
				
				Socket s = new Socket("localhost", 22278);
				
				gs = new GameStateClient ();
				ObjectOutputStream outToServer = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream inFromServer = new ObjectInputStream(s.getInputStream());
				
				outToServer.writeObject(name);
				
				GameEvent e;
				boolean gameActive = true;
				while (gameActive) {
					e = (GameEvent) inFromServer.readObject();
					if (e.makeString().equals("YourTurn")) {
						// Your turn
						boolean validInput = false;
						System.out.println("\nIt's your turn!");
						System.out.println("Top card is: " + gs.topCard.makeString());
						System.out.println("Your hand:");
						System.out.print(gs.hand.printHand(sortingEnabled));
						
						do {
							System.out.print("Enter number to play card, 'p' to pick up, 's' to toggle sorting: ");
							String in = serverConsoleInput.readLine();
							System.out.println(" ");
							if (in.toLowerCase().equals("s")) {
								if (sortingEnabled) {
									sortingEnabled = false;
									System.out.println("Sorting disabled.");
								} else {
									sortingEnabled = true;
									System.out.println("Sorting enabled.");
									System.out.print(gs.hand.printHand(sortingEnabled));
								}
								validInput = false; // This makes the prompt display again
							} else if (in.toLowerCase().equals("p")) {
								outToServer.writeObject(new DrawCard());
								validInput = true;
							} else {
								try {
									int number = Integer.valueOf(in).intValue();
									Card c = gs.hand.cards.get(number-1);
									
									if (!c.canPlaceOn(gs.topCard)) {
										System.out.println("You can't place that card!");
										validInput = false;									
									} else {
										if (c.canSetColor()) {
											boolean validColorInput = false;
											do {
												try {
													System.out.println("Select new color:");
													int color = 1;
													for (Color col : allColors) {
														System.out.println(color + ": " + col.name);
														color++;
													}
													System.out.print("Your selection: ");
													String colorIn = serverConsoleInput.readLine();
													int colorInNum = Integer.valueOf(colorIn).intValue();
													System.out.println(" ");
													if (colorInNum > 0 && colorInNum < 5) {
														// We indicate the user's chosen color
														// to the server by setting the "color"
														// of the Wild card.
														c.color = allColors[colorInNum - 1];
														validColorInput = true;
													} else {
														validColorInput = false;
													}
												} catch (NumberFormatException e1) {
													validColorInput = false;
												}
											} while (!validColorInput);
										}
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
					} else if (e.makeString().equals("YourTurnAfterDraw")) {
						boolean validTurnAfterDraw = false;
						boolean playCard = false;
						do {
							System.out.print("You drew a " + ((YourTurnAfterDraw) e).card.makeString() + ", which can be played. Play this card? [y/n]: ");
							String ynInput = serverConsoleInput.readLine();
							System.out.println(" ");
							if (ynInput.toLowerCase().equals("y")) {
								playCard = true;
								validTurnAfterDraw = true;
							} else if (ynInput.toLowerCase().equals("n")) {
								playCard = false;
								validTurnAfterDraw = true;
							} else {
								validTurnAfterDraw = false;
							}
						} while (!validTurnAfterDraw);
						if (playCard) {
							outToServer.writeObject(new PlaceCard(((YourTurnAfterDraw) e).card));
						} else {
							outToServer.writeObject(new NullEvent());
						}
					} else {
						e.doEventClient(gs);
						System.out.println(e.makeString());
					}
					if (gs.gameEnded)
						gameActive = false;
				}
				s.close();
				
				boolean validPlayAgainInput = false;
				do {
					System.out.print("Play again? [y/n]: ");
					String ynInput = serverConsoleInput.readLine();
					System.out.println(" ");
					if (ynInput.toLowerCase().equals("y")) {
						validPlayAgainInput = true;
					} else if (ynInput.toLowerCase().equals("n")) {
						clientRunning = false;
						validPlayAgainInput = true;
					} else {
						validPlayAgainInput = false;
					}
				} while (!validPlayAgainInput);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
