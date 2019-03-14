package com.evan.tichenor.sttky.component;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.evan.tichenor.sttky.STTKYFactory;
import javafx.geometry.Point2D;

import static com.evan.tichenor.sttky.Config.BULLET_COLOR;

public class BulletControl extends Component {
    private Point2D velocity;
    private double rotation;

    public BulletControl(double rotation, double speed) {
        this.rotation = rotation;
        velocity = new Point2D(
                speed * Math.cos(Math.toRadians(rotation)),
                speed * Math.sin(Math.toRadians(rotation))
        );
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translate(velocity.multiply(tpf));
    }

    @Override
    public void onRemoved() {
        // spawn boom boom
        FXGL.getGameWorld().addEntity(
                STTKYFactory.newExplosion(rotation + 90, entity.getCenter(), BULLET_COLOR, true)
        );
    }
}
