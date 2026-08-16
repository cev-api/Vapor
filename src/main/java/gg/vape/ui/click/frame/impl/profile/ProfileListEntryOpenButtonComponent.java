package gg.vape.ui.click.frame.impl.profile;

import gg.vape.Vapor;
import gg.vape.config.Profile;
import gg.vape.module.none.ClientSettings;
import gg.vape.ui.click.component.AnimatedIconButtonComponent;
import org.jetbrains.annotations.Nullable;

public class ProfileListEntryOpenButtonComponent extends AnimatedIconButtonComponent {
    private Profile profile;
    private boolean selectedProfile;
    @Nullable private Runnable afterDelete;

    private void refreshActionMode() {
        if (profile == null) return;
        selectedProfile = Vapor.INSTANCE.getProfilesManager().getActiveProfile().equals(profile);
        setIconResource("newtrash");
        w(selectedProfile ? "You cannot delete your selected profile" : "Delete this profile");
        setVisible(!selectedProfile);
        getBackgroundAnimation().setEndColor(ProfileListEntryOpenButtonComponent.J.d);
    }

    private void handleClick() {
        if (profile == null || selectedProfile) return;
        Vapor.INSTANCE.getProfilesManager().removeProfile(profile);
        if (afterDelete != null) afterDelete.run();
    }

    public ProfileListEntryOpenButtonComponent(Profile profile, @Nullable Runnable afterDelete) {
        super("newtrash", ProfileListEntryOpenButtonComponent.J.d);
        this.profile = profile;
        this.afterDelete = afterDelete;
        setBorderRadius(2.0f);
        setBorderAlpha(1.0f);
        setIconScale(0.85);
        addClickListener(this::handleClick);
        refreshActionMode();
    }

    public ProfileListEntryOpenButtonComponent useOverlayStyle() {
        getBackgroundAnimation().setStartColor(ProfileListEntryOpenButtonComponent.J.l);
        setAnimatedBorderColor(ProfileListEntryOpenButtonComponent.J.l);
        setDisabledOverlayColor(ProfileListEntryOpenButtonComponent.J.m);
        return this;
    }

    @Override public void u() { refreshActionMode(); }
    public void setProfile(Profile profile) { this.profile = profile; refreshActionMode(); }
    public Profile getProfile() { return profile; }
}