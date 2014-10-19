package com.senders.padlock.api.constants;

import java.util.Arrays;

/**
 * CharacterSet is an enum of basic standard US Keyboard Characters in UPPERCASE
 * This traditional CharacterSet allows us to restrict our passwords to a specific standard set
 * @since 1.0
 */
public enum CharacterSet {
    HEX("0123456789ABCDEF".toCharArray()),
    NUMERIC("0123456789".toCharArray()),
    ALPHA_NUMERIC("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()),
    ALPHA("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()),
    PRINTABLE("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$%^&*()_+-=[]\\{}|;':\",./<>?".toCharArray()),
    PRINTABLE_WITH_SPACES("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$%^&*()_+-=[]\\{}|;':\",./<>? ".toCharArray());
    private char[] charSet;
    private CharacterSet(char[] charSet){
        this.charSet = charSet;
    }

    public char[] getCharacters(){
        return Arrays.copyOf(this.charSet, this.charSet.length);
    }

    @Override
    public String toString(){ return new String(this.charSet); }

}
