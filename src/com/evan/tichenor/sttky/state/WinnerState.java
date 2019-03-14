package com.evan.tichenor.sttky.state;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.State;
import com.almasb.fxgl.app.SubState;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.evan.tichenor.sttky.Config.FONT;
import static com.evan.tichenor.sttky.Config.WIN_FADE_OUT_TIME;

public class WinnerState extends SubState {

    private Rectangle rect;
    private Text thanks;

    public WinnerState() {
        rect = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.BLACK);
        rect.setOpacity(0);

        thanks = new Text("THANKS FOR PLAYING!");
        thanks.setFont(FONT.newFont(50));
//        thanks.setX(); // TODO
//        thanks.setY(); // TODO
        thanks.setOpacity(0);
    }

    @Override
    protected void onEnter(State prevState) {
        getChildren().addAll(rect, thanks);
    }

    @Override
    protected void onUpdate(double tpf) {
        if(rect.getOpacity() >= 1) {
            // show thanks for playing text TODO: fade in fast (.25), stay for 2s, fade out (.5)
            if(thanks.getOpacity() < 1)
                thanks.setOpacity(1);
        } else {
            // take 2-3 seconds to lerp opacity to 1.0
            rect.setOpacity(rect.getOpacity() + tpf / WIN_FADE_OUT_TIME);
        }

    }
}
