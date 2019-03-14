package com.evan.tichenor.sttky.parser;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.logging.Logger;
import com.almasb.fxgl.core.reflect.ReflectionUtils;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntitySpawner;
import com.almasb.fxgl.entity.Level;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.parser.LevelParser;
import com.evan.tichenor.sttky.Config;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for levels represented by images with transparency.
 *
 * @author Evan Tichenor (EvanTich) (evan.tichenor@gmail.com)
 */
public class ImageLevelParser implements LevelParser {

    private static Logger log = Logger.get("STTKY.ImageLevelParser");

    private ImageEntityFactory entityFactory;

    private Map<String, EntitySpawner> producers;

    public ImageLevelParser(ImageEntityFactory entityFactory) {
        this.entityFactory = entityFactory;

        producers = new HashMap<>(); // [(color, spawner), ...]

        ReflectionUtils.findMethodsMapToFunctions(entityFactory, SpawnColor.class, EntitySpawner.class)
                .forEach( (color, spawner) -> producers.put(color.value().toUpperCase(), spawner) );
    }

//    /**
//     * Register a [producer] that generates an entity when a
//     * [color] is found during parsing.
//     *
//     * @param color the color for the producer
//     * @param producer is (x: Int, y: Int) -> Entity
//     */
//    public void addEntityProducer(Color color, EntitySpawner producer) {
//        producers.put(color.toString().substring(2), producer);
//    }

    /**
     * Parses a file with given [levelFileName] into a Level object.
     * The file must be located under "assets/textures/". Only
     * the name of the file without the "assets/textures/" is required.
     * It will be loaded by assetLoader.loadTexture() method.
     *
     * @return parsed Level
     */
    @NotNull
    @Override
    public Level parse(String levelFileName) {
        Image levelPNG = FXGL.getAssetLoader().loadTexture(levelFileName).getImage();
        PixelReader pixels = levelPNG.getPixelReader();

        String[][] level = new String[(int) levelPNG.getWidth()][(int) levelPNG.getHeight()];

        for(int i = 0; i < levelPNG.getHeight(); i++) {
            for(int j = 0; j < levelPNG.getWidth(); j++) {
                // 0x00ff00ff -> 00ff00ff
                String color = "#" + pixels.getColor(j, i).toString().substring(2).toUpperCase();

                if(producers.containsKey(color)) {
                    level[j][i] = color;
                } else {
                    level[j][i] = "";
                    if(!color.equals(entityFactory.emptyColor()))
                        log.warning("No producer found for color: " + color);
                }
            }
        }

        List<Entity> entities = new ArrayList<>();

        for(int i = 0; i < level.length; i++)
            for(int j = 0; j < level[i].length; j++)
                if(!level[i][j].equals(""))
                    entities.add(producers.get(level[i][j]).apply(getSpawnData(level, i, j)));

        return new Level(
                (int)levelPNG.getWidth() * entityFactory.blockWidth(),
                (int)levelPNG.getHeight() * entityFactory.blockHeight(),
                entities
        );
    }

    /**
     * Perhaps make this a functional interface
     * @param level
     * @param i
     * @param j
     * @return
     */
    private SpawnData getSpawnData(String[][] level, int i, int j) {
        if(level[i][j].equals(Config.BLOCK_COLOR) || level[i][j].equals(Config.SOLID_TEXT_COLOR))
            return new SpawnData(i * entityFactory.blockWidth(), j * entityFactory.blockWidth())
                .put("sides", getBlockSides(level, i, j));
        return new SpawnData(i * entityFactory.blockWidth(), j * entityFactory.blockWidth());
    }

    private int getBlockSides(String[][] level, int i, int j) {
        int sides = 0; // bit mask stuff

        if(checkSolid(level, i - 1, j + 1)) // TR
            sides |= 0b10000000;
        if(checkSolid(level, i - 1, j - 1)) // TL
            sides |= 0b01000000;
        if(checkSolid(level, i + 1, j + 1)) // BR
            sides |= 0b00100000;
        if(checkSolid(level, i + 1, j - 1)) // BL
            sides |= 0b00010000;
        if(checkSolid(level, i, j - 1)) // top
            sides |= 0b1000;
        if(checkSolid(level, i, j + 1)) // bottom
            sides |= 0b0100;
        if(checkSolid(level, i - 1, j)) // left
            sides |= 0b0010;
        if(checkSolid(level, i + 1, j)) // right
            sides |= 1;

        return sides;
    }

    private boolean checkSolid(String[][] level, int i, int j) {
        return i >= 0 && i < level.length && j >= 0 && j < level[i].length &&
                (!level[i][j].equals(Config.BLOCK_COLOR) && !level[i][j].equals(Config.SOLID_TEXT_COLOR));
    }
}
