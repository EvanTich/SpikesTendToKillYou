package com.evan.tichenor.sttky.parser;

import com.almasb.fxgl.entity.EntityFactory;

/**
 * Used alongside {@link ImageLevelParser}.
 *
 * @author Evan Tichenor (EvanTich) (evan.tichenor@gmail.com)
 */
public interface ImageEntityFactory extends EntityFactory {
    String emptyColor();

    int blockWidth();

    int blockHeight();
}
