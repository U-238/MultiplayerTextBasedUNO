package org.u238.uno.tests;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.u238.uno.cards.Card;
import org.u238.uno.cards.Color;
import org.u238.uno.cards.NumberCard;

public class TestCardRules {
	private static Logger log = Logger.getLogger(TestCardRules.class);
	
	@Test()
    public void testCardRules() {
		Card c1, c2;
		
		c1 = new NumberCard(1, Color.GREEN);
		c2 = new NumberCard(1, Color.BLUE);
		Assert.assertTrue(c1.canPlaceOn(c2));

		c1 = new NumberCard(5, Color.GREEN);
		c2 = new NumberCard(7, Color.BLUE);
		Assert.assertFalse(c1.canPlaceOn(c2));

		log.info("Test completed successfully");
	}
}
