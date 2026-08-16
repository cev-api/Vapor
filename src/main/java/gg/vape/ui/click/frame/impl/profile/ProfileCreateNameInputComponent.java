package gg.vape.ui.click.frame.impl.profile;

import gg.vape.Vapor;
import gg.vape.config.Profile;
import gg.vape.ui.click.component.TextInputComponentBase;

public class ProfileCreateNameInputComponent
extends TextInputComponentBase {
    private final Profile profile;

    @Override
    public void submit() {
        if (!this.hasNonBlankText()) {
            this.setText("");
            return;
        }
        String profileName = this.getText();
        Profile existingProfile = Vapor.INSTANCE.getProfilesManager().getProfileByName(profileName);
        if (existingProfile != null) {
            return;
        }
        this.profile.setName(profileName);
        this.profile.setDirty(true);
        Vapor.INSTANCE.getProfilesManager().addProfile(this.profile, true);
        Vapor.INSTANCE.getProfilesManager().setActiveProfile(this.profile);
        this.setText("");
    }

    @Override
    public double C() {
        return 0.0;
    }

    @Override
    public double x() {
        return 0.0;
    }

    @Override
    public double getComponentWidth() {
        return this.A() + 2.5;
    }


    public ProfileCreateNameInputComponent(String placeholder, Profile profile) {
        super(placeholder);
        this.profile = profile;
        this.setShowDisabledOverlay(false);
        this.setMaxLength(48);
    }
}
