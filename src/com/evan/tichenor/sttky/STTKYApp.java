package com.evan.tichenor.sttky;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import com.evan.tichenor.sttky.collision.*;
import com.evan.tichenor.sttky.component.*;
import com.evan.tichenor.sttky.parser.ImageEntityFactory;
import com.evan.tichenor.sttky.parser.ImageLevelParser;
import com.evan.tichenor.sttky.state.WinnerState;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ConcurrentModificationException;
import java.util.Map;

import static com.evan.tichenor.sttky.Config.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 9/14/2017
 *
 * STTKY is short for SpikesTendToKillYou
 * its a mouth-full to type/say that
 */
public class STTKYApp extends GameApplication {

    // TODO list:
    // DONE 0. Fix odd crash on level clear
    //  - Due to player's ground sensor doing some funky stuff
    //  - TODO: fix the concurrent modification exception to fix all these errors
    // .5. Player particles
    // 1. Test win
    //  - make level 17 winnable
    // 2. Make Final time substate
    // 3. make main menu
    // 4. make save feature
    // 5. make "are you sure" game override substate
    // FINAL. Win the game from level 1 (get others to play it too)

    private Level[] levels;
    private String[] levels_text;
    private Text levelTextUI;

    private PlayerControl player;
    private Point2D playerSpawn;
    private boolean playerDied;

    private boolean goNextLevel;

    public void setPlayerDied() {
        playerDied = true;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Spikes Tend To Kill You");
        settings.setVersion("0.9");

        // levels are 16 high x 26 long
        settings.setHeight(16 * BLOCK_SIZE);
        settings.setWidth(26 * BLOCK_SIZE);

        settings.setIntroEnabled(false);
        settings.setCloseConfirmation(false);
        settings.setMenuEnabled(false);

//        settings.setProfilingEnabled(false); // turn off bottom left debug menu

        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if(player != null)
                    player.moveLeft();
            }
        }, LEFT);
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if(player != null)
                    player.moveRight();
            }
        }, RIGHT);
        input.addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                if(player != null)
                    player.jump();
            }
        }, JUMP);
        input.addAction(new UserAction("Jump Alt") {
            @Override
            protected void onActionBegin() {
                if(player != null)
                    player.jump();
            }
        }, JUMP_ALT);

        input.addAction(new UserAction("Next Level yo") {
            @Override
            protected void onActionBegin() {
                goNextLevel();
            }
        }, KeyCode.K);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(VAR_TIMER, 0);
        vars.put(VAR_LEVEL_NUMBER, 0);
        vars.put(VAR_LEVEL_TEXT, "");
        vars.put(VAR_WON, false);
    }

    @Override
    protected void initGame() {
        initAssets();
        initBackground();
    }

    private void initAssets() {
        // the text is always in front of the player, but originally it isn't
        levelTextUI = getUIFactory().newText("", 18);
        levelTextUI.textProperty().bind(getGameState().stringProperty(VAR_LEVEL_TEXT));
        levelTextUI.setFont(FONT.newFont(SCALE * 4.33));
        levelTextUI.setLineSpacing(SCALE * 2);
        levelTextUI.setTextAlignment(TextAlignment.CENTER);

        getGameScene().addUINode(levelTextUI);

        ImageEntityFactory factory = new STTKYFactory();
        getGameWorld().addEntityFactory(factory);

        ImageLevelParser parser = new ImageLevelParser(factory);

        levels = new Level[31];
        for(int i = 0; i < levels.length; i++) {
            levels[i] = parser.parse(String.format("levels/level%d.png", i + 1));
        }

        levels_text = getAssetLoader().loadJSON("level_tips.json", String[].class);
    }

    public void nextLevel() {
        goNextLevel = true;
    }

    private void goNextLevel() {
        int level = getGameState().getInt(VAR_LEVEL_NUMBER);

        try {

            getGameWorld().setLevel(levels[level]);
        } catch(ConcurrentModificationException e) { // fix this stupid error
            // TODO: this is the cause of all problems
            // but I fixed the one that crashes the game
//            e.printStackTrace();
            goNextLevel();
            return;
        }

        // add text
        getGameState().setValue(VAR_LEVEL_TEXT, levels_text[level].toUpperCase());

        Entity pos = null;
        try {
            pos = getGameWorld().getEntitiesByComponent(TextIndicatorComponent.class).get(0);
        } catch(Exception e) {/* no text */}

        if(pos != null) {
            levelTextUI.setX(pos.getX() + BLOCK_SIZE / 3f);
            levelTextUI.setY(pos.getY() + BLOCK_SIZE - levelTextUI.getFont().getSize() / 2);
        }

        try {
            player = getGameWorld().getSingleton(EntityType.PLAYER)
                    .get().getComponent(PlayerControl.class);
            playerSpawn = player.getEntity().getPosition();
        } catch(Exception e) {
            System.out.println("Player not found in level " + (level + 1));
        }

        playerDied = false;

        getGameState().increment(VAR_LEVEL_NUMBER, 1);

        if(level >= 31)
            getGameState().setValue(VAR_LEVEL_NUMBER, 0);
    }

    /**
     * Initializes background and wall.
     */
    private void initBackground() {
        BackgroundImage bgImg = new BackgroundImage(
                BACKGROUND_TEXTURE.getImage(),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        Entity bg = Entities.builder()
                .renderLayer(RenderLayer.BACKGROUND)
                .with(new IrremovableComponent())
                .build();

        Region region = new Region();
        region.setMinSize(getWidth(), getHeight());
        region.setBackground(new Background(bgImg));

        bg.setView(region);

        Entity wall = Entities.makeScreenBounds(BLOCK_SIZE);
        wall.setType(EntityType.LEVEL_END);
        wall.addComponent(new CollidableComponent(true));
        wall.addComponent(new IrremovableComponent());

        getGameWorld().addEntities(bg, wall);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, GRAVITY);

        getPhysicsWorld().addCollisionHandler(new PlayerSpikeHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerBulletHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerEndHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerPodiumHandler());
        getPhysicsWorld().addCollisionHandler(new BlockBulletHandler());
    }

    @Override
    protected void initUI() {
        Font font = FONT.newFont(18);
        Text timeUI = getUIFactory().newText("00:00", 18);
        timeUI.setFont(font);
        timeUI.setTranslateX(10);
        timeUI.setTranslateY(25);

        getGameState().intProperty(VAR_TIMER).addListener((observable, oldValue, newValue) ->
            timeUI.setText(String.format("%02d:%02d", newValue.intValue() / 60, newValue.intValue() % 60))
        );

        Text levelUI = getUIFactory().newText("", 18);
        levelUI.setFont(font);
        levelUI.setTranslateX(.97 * getWidth());
        levelUI.setTranslateY(25);

        getGameState().intProperty(VAR_LEVEL_NUMBER).addListener( (observable, oldValue, newValue) -> {
            if(newValue.intValue() > 10)
                levelUI.setTranslateX(.96 * getWidth());
        });

        levelUI.textProperty().bind(getGameState().intProperty(VAR_LEVEL_NUMBER).asString("%s"));

        getGameScene().addUINodes(timeUI, levelUI);

        getMasterTimer().runAtInterval(
                () -> getGameState().increment(VAR_TIMER, 1),
                Duration.seconds(1)
        );

        goNextLevel(); // THIS REQUIRES levelTextUI!!!
    }

    @Override
    protected void onUpdate(double tpf) {
        if(playerDied) {
            playerDied = false;
            // pause game until player is alive again (keep timer rolling) (i.e. pause most components)
            getGameWorld().getEntities().stream()
                    .filter(e -> e.hasComponent(ExpireClean2Component.class) || e.hasComponent(TurretControl.class))
                    .forEach(e -> e.getComponents().forEach(Component::pause));
            getMasterTimer().runOnceAfter(() -> {
                getGameWorld().getEntities().stream()
                        .filter(e -> e.hasComponent(ExpireClean2Component.class) || e.hasComponent(TurretControl.class))
                        .forEach(e -> e.getComponents().forEach(Component::resume));
                player = getGameWorld().spawn("player", new SpawnData(playerSpawn))
                        .getComponent(PlayerControl.class);
            }, PLAYER_DEATH_PAUSE);
        }

        if(getGameState().getBoolean(VAR_WON)) {
            // if won
            getStateMachine().pushState(new WinnerState());
        }
    }

    @Override
    protected void onPostUpdate(double tpf) {
        if(goNextLevel) {
            goNextLevel = false;
            goNextLevel();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
