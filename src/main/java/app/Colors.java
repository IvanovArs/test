package app;

import misc.Vector2i;

public class Colors {
    public static final int APP_BACKGROUND_COLOR = Vector2i.getColor(255, 38, 70, 83);
    private Colors() {
        throw new AssertionError("Вызов этого конструктора запрещён");
    }
}
