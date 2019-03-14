package com.evan.tichenor.sttky.component;

import com.almasb.fxgl.entity.component.Component;

/**
 * This allows the pause functionality that the original component didn't do.
 */
public class ExpireClean2Component extends Component {

    private double duration;

    public ExpireClean2Component(double duration) {
        this.duration = duration;
    }

    @Override
    public void onUpdate(double tpf) {
        duration -= tpf;
        if(duration <= 0) {
            if(entity != null)
                entity.removeFromWorld();
        }
    }
}
