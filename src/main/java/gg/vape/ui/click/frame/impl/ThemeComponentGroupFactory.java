package gg.vape.ui.click.frame.impl;

import gg.vape.ui.click.component.ColorDividerComponent;
import gg.vape.ui.click.component.GuiComponent;
import gg.vape.ui.theme.ThemeColors;
import java.util.LinkedHashMap;

public final class ThemeComponentGroupFactory {
    public static GuiComponent[] k(ThemeColors themeColors) { return new GuiComponent[]{new ColorDividerComponent(themeColors.i)}; }
    public static GuiComponent[] E(ThemeColors themeColors) { return new GuiComponent[0]; }
    public static LinkedHashMap<ThemeComponentGroupKey, GuiComponent[]> R(ThemeColors themeColors) { return new LinkedHashMap<>(); }
    private ThemeComponentGroupFactory() {}
}
