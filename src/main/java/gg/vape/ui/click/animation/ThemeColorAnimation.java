package gg.vape.ui.click.animation;

import gg.vape.Vapor;
import gg.vape.ui.click.animation.ColorAnimation;
import java.awt.Color;

public class ThemeColorAnimation
extends ColorAnimation {
    public ThemeColorAnimation(double d, Color color) {
        super(d, color, Vapor.INSTANCE.getClientSettings().guiColor.getMutableColor());
    }

    @Override
    public Color getInterpolatedColor() {
        super.setEndColor(Vapor.INSTANCE.getClientSettings().guiColor.getMutableColor());
        return super.getInterpolatedColor();
    }
}
