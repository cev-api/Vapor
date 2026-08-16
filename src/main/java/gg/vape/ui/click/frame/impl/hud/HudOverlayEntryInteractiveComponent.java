package gg.vape.ui.click.frame.impl.hud;

import gg.vape.module.none.ClientSettings;
import gg.vape.ui.click.component.IconGlyphComponent;
import gg.vape.ui.click.component.gui.InteractiveComponent;
import gg.vape.ui.click.frame.Frame;
import gg.vape.unmap.ColorUtil;
import gg.vape.utils.render.GuiRenderPrimitives;
import java.awt.Color;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.Nullable;

public class HudOverlayEntryInteractiveComponent
extends InteractiveComponent {
    private String tooltip;
    @Nullable
    private Runnable action;
    @Nullable
    private Class<? extends Frame> frameClass;
    @Nullable
    private BooleanSupplier selectedSupplier;
    private final IconGlyphComponent icon;


    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
        this.w(tooltip);
    }

    public void setSelectedSupplier(@Nullable BooleanSupplier selectedSupplier) {
        this.selectedSupplier = selectedSupplier;
    }

    public void setFrameClass(@Nullable Class<? extends Frame> frameClass) {
        this.frameClass = frameClass;
    }

    private boolean isSelected() {
        if (this.selectedSupplier != null) {
            return this.selectedSupplier.getAsBoolean();
        }
        if (this.frameClass == null) {
            return false;
        }
        Frame frame = ClientSettings.getFrame(this.frameClass);
        return frame != null && frame.V$src$Z$1xhop3l();
    }

    @Override
    public void H() {
        double d = this.G$src$D$1b2f02a();
        double d2 = this.n();
        double d3 = this.A();
        Color color = ClientSettings.INSTANCE.getAccentColor();
        GuiRenderPrimitives.I(d, d2 - 1.0, d3, d3, ColorUtil.offsetRgb(color, 40.0), false, 4.0f, 1.0f, 8.0f, HudOverlayEntryInteractiveComponent.J.u);
        GuiRenderPrimitives.I(d, d2 + 1.0, d3, d3, ColorUtil.offsetRgb(color, -15.0), false, 4.0f, 1.0f, 8.0f, HudOverlayEntryInteractiveComponent.J.u);
        GuiRenderPrimitives.I(d, d2, d3, d3, color, false, 4.0f, 1.0f, 8.0f, HudOverlayEntryInteractiveComponent.J.u);
        Color color2 = HudOverlayEntryInteractiveComponent.J.W;
        color2 = J.B();
        this.icon.setColor(color2);
        this.icon.K(d + (d3 - 6.0) / 2.0);
        this.icon.S(d2 + (d3 - 6.0) / 2.0);
    }

    public HudOverlayEntryInteractiveComponent(String string, String string2) {
        this.icon = new IconGlyphComponent(string, 6.0f, 6.0f);
        this.icon.setSnapToPixels(true);
        this.icon.o(6.0);
        this.icon.Y(6.0);
        this.tooltip = string2;
        this.w(string2);
        this.o(22.0);
        this.Y(22.0);
        this.setShowDisabledOverlay(false);
        this.setPropagateMouseEvents(true);
        this.addChildren(this.icon);
        this.addClickListener(this::handleClick);
    }

    private void handleClick() {
        this.toggleFrameVisibility();
        if (this.action != null) {
            this.action.run();
        }
    }

    private void toggleFrameVisibility() {
        if (this.frameClass == null) {
            return;
        }
        Frame frame = ClientSettings.getFrame(this.frameClass);
        ClientSettings.showFrame(this.frameClass);
        Frame frame2 = ClientSettings.getFrame(this.frameClass);
        if (frame2 != null) {
            frame2.c(frame2.V$src$Z$1xhop3l());
        }
        if (frame != null && Objects.equals(frame, frame2)) {
            frame.l$src$V$1mibm4x();
        }
    }

    public void setAction(@Nullable Runnable action) {
        this.action = action;
    }

    public String getTooltip() {
        return this.tooltip;
    }
}

