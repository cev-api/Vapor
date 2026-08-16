package gg.vape.tutorial;

import gg.vape.Vapor;
import gg.vape.tutorial.TutorialPage;
import gg.vape.ui.click.component.GuiClickListener;

class TutorialActionFinishClickHandler
implements GuiClickListener {
    final TutorialPage w;

    TutorialActionFinishClickHandler(TutorialPage tutorialPage) {
        this.w = tutorialPage;
    }

    @Override
    public void onPrimaryClick() {
        Vapor.INSTANCE.getTutorialManager().completeCurrentPage();
    }
}
