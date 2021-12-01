package com.sithagi.kitkatemoji.emoji;

import java.io.Serializable;

/**
 * EmojiIcon model.
 *
 * @author Chathura Wijesinghe (cdanasiri@gmail.com)
 */
public class Emojicon implements Serializable {
    private static final long serialVersionUID = 1L;
    private String emoji = "";


    private Emojicon() {
    }

    public Emojicon(String emoji) {
        this.emoji = emoji;
    }


    /**
     * create a Emojicon class from a code point.
     *
     * @param codePoint codePoint of the emoji
     * @return Emojicon class with the emoji made from codePoint
     */
    public static Emojicon fromCodePoint(int codePoint) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = newString(codePoint);

        return emoji;
    }

    /**
     * create a Emojicon class from a char code.
     *
     * @param ch charCode of the emoji
     * @return Emojicon class with the emoji made from charCode
     */
    public static Emojicon fromChar(char ch) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = Character.toString(ch);

        return emoji;
    }

    /**
     * create a Emojicon class from a string.
     *
     * @param str emoji as a string
     * @return Emojicon class with the string value as a emoji
     */
    public static Emojicon fromChars(String str) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = str;

        return emoji;
    }

    /**
     * convert codePoint to string.
     *
     * @param codePoint codePoint
     * @return string of the codePoint
     */
    public static String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }

    public String getEmoji() {
        return emoji;
    }

}