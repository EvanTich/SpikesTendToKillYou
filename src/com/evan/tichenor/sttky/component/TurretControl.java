package com.evan.tichenor.sttky.component;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.evan.tichenor.sttky.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import static com.evan.tichenor.sttky.Config.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 9/17/2017
 */
public class TurretControl extends Component {

    private Point2D startPos;
    private double timer; // somehow make static b/c all turrets shoot at the same time

    @Override
    public void onAdded() {
        startPos = getEntity().getPosition().add(BLOCK_SIZE / 2f, BLOCK_SIZE / 2f)
                .subtract(3f * SCALE / 2, 3f * SCALE / 2);
    }

    @Override
    public void onUpdate(double deltaTime) {
        timer -= deltaTime;
        if(timer < 0) {
            timer = TURRET_SHOOT_TIME;
            shoot();
        }
    }

    private void shoot() {
        Entities.builder()
                .at(startPos)
                .type(EntityType.BULLET)
                .viewFromNodeWithBBox(new Rectangle(3 * SCALE, 3 * SCALE, BULLET_COLOR))
                .with(new CollidableComponent(true), new ExpireClean2Component(TURRET_SHOOT_TIME),
                        new BulletControl(getEntity().getRotation() - 90, BULLET_SPEED))
                .buildAndAttach();
    }
}
