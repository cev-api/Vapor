package gg.vape.ui.click.frame.impl.quickactions;

import func.skidline.RectData;
import gg.vape.module.none.ClientSettings;
import gg.vape.module.none.TextGuiSettingsFrame;
import gg.vape.ui.click.GuiMouseEvent;
import gg.vape.ui.click.animation.DoubleAnimation;
import gg.vape.ui.click.component.ColorDividerComponent;
import gg.vape.ui.click.frame.Frame;
import gg.vape.ui.click.frame.impl.ClientSettingsSearchFrame;
import gg.vape.ui.click.frame.impl.profile.PublicProfilesFrameHeaderActionComponent;
import gg.vape.ui.click.frame.impl.quickactions.QuickActionRowComponent;
import gg.vape.ui.click.frame.impl.quickactions.QuickActionsHeaderCloseCallback;
import gg.vape.ui.click.frame.impl.target.TargetInfoSettingsFrame;
import gg.vape.utils.render.GuiRenderPrimitives;
import java.awt.Color;

public class QuickActionsFrame
extends Frame {
    private DoubleAnimation m_;
    private int mG;
    private DoubleAnimation mI = new DoubleAnimation(0.15, 0.0, 100.0);
    private QuickActionRowComponent mF;
    private QuickActionRowComponent mO;

    public QuickActionRowComponent m$src$Lgg_vape_ui_click_frame_impl_quickactions_QuickA$1kmfigl() {
        return this.mF;
    }

    public QuickActionsFrame() {
        this.m_ = new DoubleAnimation(0.15, 0.0, 1.0);
        this.mF = new QuickActionRowComponent("Text GUI", "newtextgui", 0.9, 6);
        this.mO = new QuickActionRowComponent("Target Info", "newtargetinfo", 0.9, 7);
        this.I2 = false;
        this.D(false);
        this.setDisabledOverlayColor(QuickActionsFrame.J.i);
        this.l$src$Lgg_vape_ui_click_layout_ComponentLayout_$di1tij().M(false);
        this.l$src$Lgg_vape_ui_click_layout_ComponentLayout_$di1tij().M("wrap");
        PublicProfilesFrameHeaderActionComponent publicProfilesFrameHeaderActionComponent = new PublicProfilesFrameHeaderActionComponent(this, "newoverlays", "Overlays", 0.7);
        publicProfilesFrameHeaderActionComponent.O$src$Lgg_vape_ui_click_component_SquareIconButtonComp$z3cp96().clearClickListeners();
        publicProfilesFrameHeaderActionComponent.O$src$Lgg_vape_ui_click_component_SquareIconButtonComp$z3cp96().addClickListener(new QuickActionsHeaderCloseCallback(this));
        this.mF.setTargetFrameClass(TextGuiSettingsFrame.class);
        this.mO.setTargetFrameClass(TargetInfoSettingsFrame.class);
        this.Y(publicProfilesFrameHeaderActionComponent);
        this.h(new ColorDividerComponent(QuickActionsFrame.J.l), new Object[0]);
        this.addChildren(this.mF, this.mO);
        this.setVisible(false);
        this.L(false, false);
    }

    @Override
    public void g(GuiMouseEvent guiMouseEvent) {
        if (!this.w$src$Z$e457mb()) {
            this.mG = 3;
        }
    }

    @Override
    public void M() {
    }

    @Override
    public void v() {
    }

    @Override
    public String getName() {
        return "Overlays";
    }

    public static int Y(QuickActionsFrame quickActionsFrame, int n) {
        quickActionsFrame.mG = n;
        return quickActionsFrame.mG;
    }

    @Override
    public void Y() {
        ClientSettingsSearchFrame clientSettingsSearchFrame = ClientSettings.getFrame(ClientSettingsSearchFrame.class);
        if (clientSettingsSearchFrame == null) {
            return;
        }
        if (this.mG == 1) {
            this.mG = 2;
            this.mI.J();
            this.m_.J();
        }
        if (this.mG == 3) {
            this.mG = 4;
            this.mI.J();
            this.m_.J();
        }
        if (this.mG == 4 && this.mI.getInterpolatedValue().doubleValue() == this.mI.getStartValue()) {
            this.mG = 0;
            this.setVisible(false);
            return;
        }
        if (this.m_.getInterpolatedValue().doubleValue() != this.m_.getEndValue()) {
            this.D(true);
            this.h(new RectData(clientSettingsSearchFrame.G$src$D$1b2f02a(), clientSettingsSearchFrame.n(), clientSettingsSearchFrame.A(), clientSettingsSearchFrame.L()));
            this.K(clientSettingsSearchFrame.G$src$D$1b2f02a());
            this.S(clientSettingsSearchFrame.n() + clientSettingsSearchFrame.L() - 2.0 - this.L() * this.m_.getInterpolatedValue());
            this.l$src$V$1mibm4x();
        }
        if (this.G$src$D$1b2f02a() != clientSettingsSearchFrame.G$src$D$1b2f02a() || this.n() != clientSettingsSearchFrame.n()) {
            this.M(clientSettingsSearchFrame.G$src$D$1b2f02a(), clientSettingsSearchFrame.n() + clientSettingsSearchFrame.L() - 2.0 - this.L() * this.m_.getInterpolatedValue());
        }
        this.S(clientSettingsSearchFrame.n() + clientSettingsSearchFrame.L() - 2.0 - this.L() * this.m_.getInterpolatedValue());
        GuiRenderPrimitives.d(clientSettingsSearchFrame.G$src$D$1b2f02a(), clientSettingsSearchFrame.n(), clientSettingsSearchFrame.A(), clientSettingsSearchFrame.L(), new Color(0, 0, 0, this.mI.getInterpolatedValue().intValue()));
    }



    public void w(int n) {
        this.mG = n;
    }

    public int Q$src$I$1o5zb27() {
        return this.mG;
    }
}

