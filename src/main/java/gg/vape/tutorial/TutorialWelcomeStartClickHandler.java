package gg.vape.tutorial;

import gg.vape.Vapor;
import gg.vape.tutorial.TutorialWelcomePanel;
import gg.vape.ui.click.component.GuiClickListener;

class TutorialWelcomeStartClickHandler
implements GuiClickListener {
    final TutorialWelcomePanel F;

    TutorialWelcomeStartClickHandler(TutorialWelcomePanel tutorialWelcomePanel) {
        this.F = tutorialWelcomePanel;
    }

    @Override
    public void onPrimaryClick() {
        Vapor.INSTANCE.getTutorialManager().startTutorial();
    }
}
