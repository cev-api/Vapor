package gg.vape.tutorial;

import gg.vape.Vapor;
import gg.vape.tutorial.TutorialState;
import gg.vape.tutorial.TutorialWelcomePanel;
import gg.vape.ui.click.component.GuiClickListener;

class TutorialWelcomeSkipAllClickHandler
implements GuiClickListener {
    final TutorialWelcomePanel F;

    @Override
    public void onPrimaryClick() {
        Vapor.INSTANCE.getTutorialManager().setState(TutorialState.COMPLETED_ALL);
    }

    TutorialWelcomeSkipAllClickHandler(TutorialWelcomePanel tutorialWelcomePanel) {
        this.F = tutorialWelcomePanel;
    }
}
