package gg.vape.ui.click.frame.impl.profile;

import gg.vape.config.Profile;
import gg.vape.ui.click.component.GlyphIconComponent;
import java.awt.Color;
import org.jetbrains.annotations.Nullable;

/**
 * Profile header glyph retained for layout compatibility.
 * Network/public-profile actions are intentionally disabled in the Fabric build.
 */
public class ProfileGlyphIconPanel extends GlyphIconComponent {
    private Profile profile;

    public ProfileGlyphIconPanel(@Nullable Profile profile) {
        this(profile, 6.0, 8.0);
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        this.refreshVisibility();
    }

    public void refreshVisibility() {
        this.setVisible(false);
    }

    public ProfileGlyphIconPanel(@Nullable Profile profile, double iconWidth, double iconHeight) {
        super("external link hover@2x", iconWidth, iconWidth, iconHeight, iconHeight, null, null, null);
        this.profile = profile;
        this.setNormalColor(Color.WHITE);
        this.setVisible(false);
    }
}