package com.evan.tichenor.sttky.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used alongside {@link ImageLevelParser}.
 *
 * @author Evan Tichenor (EvanTich) (evan.tichenor@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SpawnColor {

    /**
     * @return The hex representation of the color, such as "#00ff00ff" (rrggbbaa)
     */
    String value();
}
