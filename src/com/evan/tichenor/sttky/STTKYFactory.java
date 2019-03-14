package com.evan.tichenor.sttky;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;

import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.evan.tichenor.sttky.component.PlayerControl;
import com.evan.tichenor.sttky.component.TurretControl;
import com.evan.tichenor.sttky.component.TextIndicatorComponent;
import com.evan.tichenor.sttky.parser.ImageEntityFactory;
import com.evan.tichenor.sttky.parser.SpawnColor;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.function.Supplier;

import static com.evan.tichenor.sttky.Config.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 9/15/2017
 */
public class STTKYFactory implements ImageEntityFactory {

    @Spawns("player")
    @SpawnColor(PLAYER_COLOR)
    public Entity newPlayer(SpawnData data) {

        PhysicsComponent phys = new PhysicsComponent();
        phys.setBodyType(BodyType.DYNAMIC);
        phys.setFixtureDef(
                new FixtureDef()
                        .friction(0)
                        .restitution(.05f)
        );

        // line 1222 in game_start
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.setSourceImage(PIXEL.getImage());
        final double size = PLAYER_TEXTURE.getWidth() / 9;
        emitter.setSpawnPointFunction( i -> new Point2D(PLAYER_TEXTURE.getWidth() / 2 - size / 2, PLAYER_TEXTURE.getHeight() / 4));
        emitter.setControl(p -> {
            int life = (int)(10 - (p.life * 30));
            ImageView img = (ImageView) p.getView();
            switch(life) {
                case 0:
                case 2:
                    img.setFitWidth(size);
                    img.setFitHeight(size);
                    // FIXME: change image color without doing this
                    img.setImage(PIXEL.toColor(Color.valueOf("#308DB1")).getImage());
                    break;
                case 3:
                case 4:
                    img.setFitWidth(size * 3 / 4);
                    img.setFitHeight(size * 3 / 4);
                    img.setImage(PIXEL.toColor(Color.valueOf("#39A3CA")).getImage());
                    break;
                case 6:
                    img.setFitWidth(size / 2);
                    img.setFitHeight(size / 2);
                    img.setImage(PIXEL.toColor(Color.valueOf("#57B0D2")).getImage());
                    break;
                case 8:
                    img.setImage(PIXEL.toColor(Color.valueOf("#71BCD9")).getImage());
                    break;
                case 9:
                    img.setFitWidth(size / 4);
                    img.setFitHeight(size / 4);
                    break;
                default:
                    break;
            }

            p.position.x += 1;
            p.position.x += Math.random() * 2 / 2 * SCALE * (Math.random() > .5 ? 1 : -1);

            if(!phys.isMovingX()) {
                p.position.y -= Math.random() * 5 * (Math.random() > .5 ? 1 : -1) + 2;
            } else {
                p.position.y -= Math.random() * 5;
            }
        });
//        emitter.setColor(Color.ALICEBLUE);
//        emitter.setEndColor(Color.LIGHTBLUE);
        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setNumParticles(6);
        emitter.setEmissionRate(.5);
        emitter.setInterpolator(Interpolator.EASE_OUT);
//        emitter.setSize(blockWidth() * .25, blockWidth() * .5);
        emitter.setExpireFunction( i -> Duration.seconds(2d / 6)); // lives for 10 frames

        ParticleComponent part = new ParticleComponent(emitter);

        return Entities.builder()
                .type(EntityType.PLAYER)
                .from(data)
                .renderLayer(RenderLayer.TOP)
                .viewFromNodeWithBBox(PLAYER_TEXTURE.copy())
                .with(phys, part, new CollidableComponent(true), new PlayerControl())
                .build();
    }

    @Spawns("turret")
    @SpawnColor(UP_TURRET_COLOR)
    public Entity newTurret(SpawnData data) {
        return Entities.builder()
                .type(EntityType.TURRET)
                .from(data)
                .viewFromNode(TURRET_TEXTURE.copy())
                .with(new TurretControl())
                .build();
    }

    @SpawnColor(DOWN_TURRET_COLOR)
    public Entity newDownTurret(SpawnData data) {
        return newTurret(data.put("rotation", 180f));
    }

    @SpawnColor(RIGHT_TURRET_COLOR)
    public Entity newRightTurret(SpawnData data) {
        return newTurret(data.put("rotation", 90f));
    }

    @SpawnColor(LEFT_TURRET_COLOR)
    public Entity newLeftTurret(SpawnData data) {
        return newTurret(data.put("rotation", 270f));
    }

    @Spawns("spike")
    @SpawnColor(UP_SPIKE_COLOR)
    public Entity newSpike(SpawnData data) {
        Polygon g = new Polygon(0, BLOCK_SIZE, BLOCK_SIZE / 2f, 0, BLOCK_SIZE, BLOCK_SIZE);
        g.setRotate(data.hasKey("rotation") ? ((Float)data.get("rotation")).doubleValue() : 0d);

        double[] points = new double[6]; // 3 points
        int i = 0;
        for(double d : g.getPoints())
            points[i++] = d;

        return Entities.builder()
                .type(EntityType.SPIKE)
                .from(data)
                .viewFromNode(SPIKE_TEXTURE.copy())
                .bbox(new HitBox(BoundingShape.polygon(points)))
                .with(new CollidableComponent(true))
                .build();
    }

    @SpawnColor(DOWN_SPIKE_COLOR)
    public Entity newDownSpike(SpawnData data) {
        return newSpike(data.put("rotation", 180f));
    }

    @SpawnColor(RIGHT_SPIKE_COLOR)
    public Entity newRightSpike(SpawnData data) {
        return newSpike(data.put("rotation", 90f));
    }

    @SpawnColor(LEFT_SPIKE_COLOR)
    public Entity newLeftSpike(SpawnData data) {
        return newSpike(data.put("rotation", 270f));
    }

    @Spawns("block")
    @SpawnColor(BLOCK_COLOR)
    public Entity newBlock(SpawnData data) {
        Canvas canvas = new Canvas(blockWidth(), blockHeight());

        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setFill(Color.BLACK);
        g.drawImage(BLOCK_TEXTURE.getImage(), 0, 0);

        int sides = data.get("sides"); // basically 0bCCCCTBLR -> [TR, TL, BR, BL, top, bottom, left, right]

        // FIXME: fix dumbfuck corners fucking up

        if(((sides >> 7) & 1) == 1)
            g.fillRect(blockWidth() - blockPixel(), 0, blockPixel(), blockPixel());
        if(((sides >> 6) & 1) == 1)
            g.fillRect(0, 0, blockPixel(), blockPixel());
        if(((sides >> 5) & 1) == 1)
            g.fillRect(blockWidth() - blockPixel(), blockHeight() - blockPixel(), blockPixel(), blockPixel());
        if(((sides >> 4) & 1) == 1)
            g.fillRect(0, blockHeight() - blockPixel(), blockPixel(), blockPixel());
        if(((sides >> 3) & 1) == 1)
            g.fillRect(0, 0, blockWidth(), blockPixel());
        if(((sides >> 2) & 1) == 1)
            g.fillRect(0, blockHeight() - blockPixel(), blockWidth(), blockPixel());
        if(((sides >> 1) & 1) == 1)
            g.fillRect(0, 0, blockPixel(), blockHeight());
        if( (sides       & 1) == 1)
            g.fillRect(blockWidth() - blockPixel(), 0, blockPixel(), blockHeight());

        System.out.printf("%s", Integer.toBinaryString(sides));

        return Entities.builder()
                .type(EntityType.BLOCK)
                .from(data)
                .viewFromNodeWithBBox(canvas) // could be bad
                .with(new PhysicsComponent(), new CollidableComponent(true))
                .build();
    }

    @Spawns("solid_text")
    @SpawnColor(SOLID_TEXT_COLOR)
    public Entity newSolidText(SpawnData data) {
        Entity e = newBlock(data);
        e.addComponent(new TextIndicatorComponent());

        return e;
    }

    @Spawns("text")
    @SpawnColor(TEXT_COLOR)
    public Entity newText(SpawnData data) {
        return Entities.builder()
                .type(EntityType.TEXT_INDICATOR)
                .from(data)
                .with(new TextIndicatorComponent())
                .build();
    }

    @SpawnColor(WIN_PODIUM_COLOR)
    public Entity newWinPodium(SpawnData data) {
        return Entities.builder()
                .type(EntityType.WIN_PODIUM)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(blockWidth(), blockHeight())))
                .with(new CollidableComponent(true))
                .build();
    }

    public static Entity newExplosion(double rotation, Point2D position, Color startColor, boolean bullet) {
        // TODO: gravity on particles eventually
        ParticleEmitter e = ParticleEmitters.newExplosionEmitter(10);

        e.setVelocityFunction(i -> makeVelocity(rotation, bullet));
//        e.setAccelerationFunction(() -> new Point2D(0, 250)); // gravity doesn't work with these trash particles
        e.setExpireFunction(i -> EXPLOSION_DURATION);
        e.setSourceImage(PIXEL.toColor(startColor).getImage());
        e.setColor(startColor);
        e.setStartColor(startColor);
        e.setEndColor(Color.TRANSPARENT);
        e.setNumParticles(32);
        e.setSize(SCALE / 2.25f, SCALE / 1.5f);
        e.setBlendMode(BlendMode.SRC_OVER);

        Entity explosion = Entities.builder()
                .at(position)
                .build();

        ParticleComponent c = new ParticleComponent(e);
        c.setOnFinished(explosion::removeFromWorld);

        explosion.addComponent(c);

        return explosion;
    }

    private static Point2D makeVelocity(double rotation, boolean bullet) {
        Supplier<Double> stuff = () -> 100 * Math.random();
        Point2D velocity = new Point2D(stuff.get() * (Math.random() < .5 ? 1 : -1), -stuff.get());

        if(bullet) {
            // rotate velocity
            rotation = Math.toRadians(rotation);
            double cos = Math.cos(rotation), sin = Math.sin(rotation);
            velocity = new Point2D(
                    velocity.getX() * cos - velocity.getY() * sin,
                    velocity.getX() * sin - velocity.getY() * cos
            );
        }
        return velocity;
    }

    @Override
    public String emptyColor() {
        return "#00000000";
    }

    @Override
    public int blockWidth() {
        return BLOCK_SIZE;
    }

    @Override
    public int blockHeight() {
        return BLOCK_SIZE;
    }

    public int blockPixel() {
        return SCALE;
    }
}
