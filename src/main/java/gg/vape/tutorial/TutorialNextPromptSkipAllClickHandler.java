package gg.vape.tutorial;

import gg.vape.Vapor;
import gg.vape.tutorial.TutorialNextPromptPanel;
import gg.vape.tutorial.TutorialState;
import gg.vape.ui.click.component.GuiClickListener;

public class TutorialNextPromptSkipAllClickHandler
implements GuiClickListener {
    final TutorialNextPromptPanel T;

    @Override
    public void onPrimaryClick() {
        Vapor.INSTANCE.getTutorialManager().setState(TutorialState.COMPLETED_ALL);
    }

    public TutorialNextPromptSkipAllClickHandler(TutorialNextPromptPanel tutorialNextPromptPanel) {
        this.T = tutorialNextPromptPanel;
    }
}
