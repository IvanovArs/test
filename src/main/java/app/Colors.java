package app;

import misc.Misc;
import misc.Vector2i;

public class Colors {
    public static final int APP_BACKGROUND_COLOR = Vector2i.getColor(255, 38, 70, 83);
    private Colors() {
        throw new AssertionError("Вызов этого конструктора запрещён");
    }
    public static final int LABEL_TEXT_COLOR = Misc.getColor(64, 255, 255, 255);
    /**
     * цвет подложки панелей
     */
    public static final int PANEL_BACKGROUND_COLOR = Misc.getColor(32, 0, 0, 0);
}
