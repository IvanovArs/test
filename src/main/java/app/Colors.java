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
    public static final int MULTILINE_TEXT_COLOR = Misc.getColor(64, 255, 255, 255);
    public static final int FIELD_BACKGROUND_COLOR = Misc.getColor(255, 255, 255, 255);
    /**
     * Цвет текста
     */
    public static final int FIELD_TEXT_COLOR = Misc.getColor(255, 0, 0, 0);
    public static final int BUTTON_COLOR = Misc.getColor(80, 0, 0, 0);
    public static final int CROSSED_COLOR = Misc.getColor(200, 0, 255, 255);
    /**
     * Цвет разности
     */
    public static final int SUBTRACTED_COLOR = Misc.getColor(200, 255, 255, 0);
    public static final int TASK_GRID_COLOR = Misc.getColor(64, 255, 255, 255);
    public static final int STATS_BACKGROUND_COLOR = Misc.getColor(64, 51, 200, 51);
    /**
     * Цвет подложки
     */
    public static final int STATS_COLOR = Misc.getColor(255, 51, 200, 51);
    /**
     * Цвет текста
     */
    public static final int STATS_TEXT_COLOR = Misc.getColor(255, 255, 255, 255);
    public static final int HELP_TEXT = Misc.getColor(255, 255, 255, 255);
    /**
     * Цвет фона
     */
    public static final int HELP_TEXT_BACKGROUND = Misc.getColor(50, 0, 0, 0);
    public static final int DIALOG_BACKGROUND_COLOR = Misc.getColor(230, 70, 38, 83);
}
