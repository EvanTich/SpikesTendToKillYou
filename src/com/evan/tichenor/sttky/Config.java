package com.evan.tichenor.sttky;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FontFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 9/14/2017
 */
public class Config {

    public static final int SCALE = 4; // 4x scale
    public static final int BLOCK_SIZE = 9 * SCALE; // this is correct

    public static final float GRAVITY = 3000;
    public static final float TERMINAL_VELOCITY = GRAVITY; // im not sure this is doing anything

    public static final int FPS = 60;

    public static final FontFactory FONT = FXGL.getAssetLoader().loadFont("PressStart2P.ttf");

    // -- GAME VARS --
    public static final String VAR_TIMER = "time";
    public static final String VAR_LEVEL_NUMBER = "level_number";
    public static final String VAR_LEVEL_TEXT = "level_text";
    public static final String VAR_WON = "won";

    public static final KeyCode LEFT = KeyCode.A;
    public static final KeyCode RIGHT = KeyCode.D;
    public static final KeyCode JUMP = KeyCode.W;
    public static final KeyCode JUMP_ALT = KeyCode.SPACE;

    public static final float PLAYER_MOVE_AMOUNT = 60f;
    public static final float PLAYER_JUMP_AMOUNT = GRAVITY / 13.15f; // 0 x, 2 blocks y
    public static final float PLAYER_STOP_MOVE_AMOUNT = .075f;

    public static final Duration PLAYER_DEATH_PAUSE = Duration.seconds(1);
    public static final Duration EXPLOSION_DURATION = Duration.millis(500);

    // -- MENU --
    public static final double WIN_FADE_OUT_TIME = 2.5;

    // -- SPAWNING --
    public static final String PLAYER_COLOR =       "#0026FFFF";
    public static final String BLOCK_COLOR =        "#9F481DFF";
    public static final String SOLID_TEXT_COLOR =   "#FFFFFFFF";
    public static final String TEXT_COLOR =         "#00FFFFFF";

    public static final String UP_SPIKE_COLOR =     "#404040FF";
    public static final String DOWN_SPIKE_COLOR =   "#E5E5E5FF";
    public static final String LEFT_SPIKE_COLOR =   "#A3A3A3FF";
    public static final String RIGHT_SPIKE_COLOR =  "#515151FF";

    public static final String UP_TURRET_COLOR =    "#3F0000FF";
    public static final String DOWN_TURRET_COLOR =  "#D80000FF";
    public static final String LEFT_TURRET_COLOR =  "#A50000FF";
    public static final String RIGHT_TURRET_COLOR = "#720000FF";

    public static final String WIN_PODIUM_COLOR =   "#4CFF00FF";

    // -- TEXTURES --
    public static final Texture PLAYER_TEXTURE = FXGL.getAssetLoader().loadTexture("player.png", BLOCK_SIZE / 1.65f, BLOCK_SIZE / 1.65f);
    public static final Texture TURRET_TEXTURE = FXGL.getAssetLoader().loadTexture("turret.png", BLOCK_SIZE, BLOCK_SIZE);
    public static final Texture SPIKE_TEXTURE = FXGL.getAssetLoader().loadTexture("spike.png", BLOCK_SIZE, BLOCK_SIZE);
    public static final Texture BLOCK_TEXTURE = FXGL.getAssetLoader().loadTexture("block.png", BLOCK_SIZE, BLOCK_SIZE);
    public static final Texture BACKGROUND_TEXTURE = FXGL.getAssetLoader().loadTexture("background.png", BLOCK_SIZE, BLOCK_SIZE);
    public static final Texture PIXEL = FXGL.getAssetLoader().loadTexture("pixel.png", SCALE, SCALE);

    public static final Color BULLET_COLOR = Color.GRAY.darker();

    // -- TURRET VARS --
    public static final double TURRET_SHOOT_TIME = 1.5; // time to shoot next bullet after the last, in s
    public static final double BULLET_SPEED = PLAYER_MOVE_AMOUNT * 25 / 2;


}
