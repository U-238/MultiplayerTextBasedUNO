package org.u238.uno;

import java.io.Serializable;

public class Color implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int INT_NONE = 0;
	public static final int INT_RED = 1;
	public static final int INT_BLUE = 2;
	public static final int INT_YELLOW = 3;
	public static final int INT_GREEN = 4;
	
	public static final Color NONE = new Color(INT_NONE, "None");
	public static final Color RED = new Color(INT_RED, "Red");
	public static final Color BLUE = new Color(INT_BLUE, "Blue");
	public static final Color YELLOW = new Color(INT_YELLOW, "Yellow");
	public static final Color GREEN = new Color(INT_GREEN, "Green");
	
	public int color;
	public String name;
	
	public Color(int color, String name) {
		this.color = color;
		this.name = name;
	}
	
	public boolean equals(Object o) {
		Color c = (Color) o;
		return this.color == c.color;
	}
}
