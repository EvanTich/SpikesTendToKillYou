package com.evan.tichenor.sttky.component;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.evan.tichenor.sttky.STTKYApp;
import com.evan.tichenor.sttky.STTKYFactory;
import javafx.scene.paint.Color;

import static com.evan.tichenor.sttky.Config.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 9/19/2017
 */
public class PlayerControl extends Component {

    private boolean died;
    private PhysicsComponent phys;

    @Override
    public void onAdded() {
        phys = entity.getComponent(PhysicsComponent.class);

        phys.addGroundSensor(new HitBox(BoundingShape.box(entity.getWidth(),  entity.getHeight() + 3)));
    }

    @Override
    public void onRemoved() {
        died = true;
        death();
    }

    @Override
    public void onUpdate(double tpf) {
        phys.setLinearVelocity(
                phys.getVelocityX() * PLAYER_STOP_MOVE_AMOUNT,
                Math.max(phys.getVelocityY(), -TERMINAL_VELOCITY)
        );
    }

    public void moveLeft() {
        if(!died)
            phys.setLinearVelocity(-PLAYER_MOVE_AMOUNT * FPS, phys.getVelocityY());
    }

    public void moveRight() {
        if(!died)
            phys.setLinearVelocity(PLAYER_MOVE_AMOUNT * FPS, phys.getVelocityY());
    }

    public void jump() {
        if(!died && onGround()) {
            phys.setLinearVelocity(phys.getVelocityX(), PLAYER_JUMP_AMOUNT * FPS);
        }
    }

    public boolean onGround() {
        return phys.isOnGround();
    }

    public void death() {
        // play explosion animation

        Entity e = STTKYFactory.newExplosion(entity.getRotation(), entity.getCenter(), Color.BLUE.brighter(), false);
        FXGL.getGameWorld().addEntity(e);

        // wait a sec and then spawn again
        FXGL.<STTKYApp>getAppCast().setPlayerDied();
    }
}
