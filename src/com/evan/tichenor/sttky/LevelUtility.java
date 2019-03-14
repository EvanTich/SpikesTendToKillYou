package com.evan.tichenor.sttky;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 9/15/2017
 *
 * this is basically trash, use if you dare
 * + unusable :)
 */
public class LevelUtility {

    public static final char
            BLOCK = '#'; // #

    // DIRECTIONS AND NO DIRECTION
    public static final int
            NONE    = 1, // ☺
            UP      = 1 << 1,
            DOWN    = 1 << 2,
            LEFT    = 1 << 3,
            RIGHT   = 1 << 4;

    // the most basic plus shape
    public static final char
            BLOCK_SINGLE        = NONE, // no blocks around
            BLOCK_TOP_END       = DOWN, // only the bottom block
            BLOCK_RIGHT_END     = LEFT, // only the left block
            BLOCK_BOTTOM_END    = UP, // only the top block
            BLOCK_LEFT_END      = RIGHT; // only the right block

    public static final char
            BLOCK_CENTER            = UP | DOWN | LEFT | RIGHT,
            BLOCK_TOP               = DOWN | LEFT | RIGHT,
            BLOCK_BOTTOM            = UP | LEFT | RIGHT,
            BLOCK_LEFT              = UP | DOWN | RIGHT,
            BLOCK_RIGHT             = UP | DOWN | LEFT,
            BLOCK_TOP_RIGHT         = DOWN | LEFT,
            BLOCK_TOP_LEFT          = DOWN | RIGHT,
            BLOCK_BOTTOM_RIGHT      = UP | LEFT,
            BLOCK_BOTTOM_LEFT       = UP | RIGHT,
            BLOCK_BEAM_VERTICAL     = UP | DOWN,
            BLOCK_BEAM_HORIZONTAL   = LEFT | RIGHT;

    private static final String[] TEXTURES = {
            "rip 0",
    };

    /**
     * Fixes up level files to prettify them, and then prints it.
     * @param args
     */
    public static void main(String[] args) {
        String levelFile = "/assets/text/level/level1.txt";

        // if it isn't these dimensions, then too bad
        char[][] level = new char[16][26];

        try (BufferedReader reader = new BufferedReader(new FileReader(levelFile))) {
            for(int i = 0; i < level.length; i++) {
                String row = reader.readLine();
                for(int j = 0; j < level[i].length; j++)
                    level[i][j] = row.charAt(j);
            }
        } catch (IOException e) {}

        prettifyLevel(level);

        // ...?
    }

    public static void prettifyLevel(char[][] level) {
        for(int i = 0; i < level.length; i++) {
            for(int j = 0; j < level[i].length; j++) {
                // if blocks are to the top/bottom/left/right of the selected block
                boolean top = false,
                        bottom = false,
                        left  = false,
                        right = false;

                if(i - 1 >= 0)
                    top = level[i - 1][j] == BLOCK;
                if(i + 1 < level.length)
                    bottom = level[i + 1][j] == BLOCK;
                if(j - 1 >= 0)
                    left  = level[i][j - 1] == BLOCK;
                if(j + 1 < level[i].length)
                    right = level[i][j + 1] == BLOCK;

                level[i][j] = (char)((top ? UP : 0) | (bottom ? DOWN : 0) | (left ? LEFT : 0) | (right ? RIGHT : 0));
            }
        }
    }

    public static String getTexture(char code) {
        return TEXTURES[code - 1];
    }
}
