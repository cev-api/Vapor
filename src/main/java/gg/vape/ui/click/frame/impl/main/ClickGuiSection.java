package gg.vape.ui.click.frame.impl.main;

public enum ClickGuiSection {
    MODULES("Modules"),
    PROFILES("Profiles");

    private static final /* synthetic */ ClickGuiSection[] R;
    private final String a;

    static {
        String[] stringArray = new String[]{"PROFILES", "Profiles", "Modules", "MODULES"};



        R = new ClickGuiSection[]{MODULES, PROFILES};
    }

    public String A() {
        return this.a;
    }

    private ClickGuiSection(String string2) {
        this.a = string2;
    }
}

