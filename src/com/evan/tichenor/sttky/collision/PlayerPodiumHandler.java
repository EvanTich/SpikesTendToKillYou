package com.evan.tichenor.sttky.collision;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.evan.tichenor.sttky.EntityType;
import com.evan.tichenor.sttky.component.PlayerControl;

import static com.evan.tichenor.sttky.Config.VAR_WON;

public class PlayerPodiumHandler extends CollisionHandler {

    public PlayerPodiumHandler() {
        super(EntityType.PLAYER, EntityType.WIN_PODIUM);
    }

    @Override
    protected void onCollision(Entity player, Entity podium) {
        if(player.getComponent(PlayerControl.class).onGround())
            FXGL.getGameState().setValue(VAR_WON, true);
    }
}
