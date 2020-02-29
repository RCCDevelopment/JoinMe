package net.rccdevelopment.joinme.messages;

public enum PictureCharacter {

	 BLOCK('\u2588'),
	    DARK_SHADE('\u2593'),
	    MEDIUM_SHADE('\u2592'),
	    LIGHT_SHADE('\u2591');

	    private char c;

	    PictureCharacter(char c) {
	        this.c = c;
	    }

	    public char getChar() {
	        return c;
	    }
	}




