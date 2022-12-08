package app;

import misc.Misc;

public class Colors {
    public static final int APP_BACKGROUND_COLOR = Misc.getColor(255, 38, 70, 83);
    private Colors() {
        throw new AssertionError("Вызов этого конструктора запрещён");
    }
}
