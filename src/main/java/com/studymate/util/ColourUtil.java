package com.studymate.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ColourUtil {

    public String createRandomHslColor() {
        int hue = (int) Math.floor(Math.random() * 360);
        return String.format("hsl(%d, 100%%, 80%%)", hue);
    }

}
