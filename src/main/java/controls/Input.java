package controls;

import panels.GridPanel;
import io.github.humbleui.jwm.*;
import io.github.humbleui.skija.*;
import misc.CoordinateSystem2i;

import static app.Fonts.FONT18;

/**
 * Поле ввода
 */
public class Input extends GridPanel {
    /**
     * Размер поля ввода
     */
    private static final int INPUT_SIZE = 40;
    /**
     * Текст
     */
    String text;
    /**
     * Смещение для красивого отображения текста
     */
    private static final int LOCAL_PADDING = 8;
    /**
     * Флаг, нужно ли выравнивать текст по центру по вертикали
     */
    protected boolean vcentered;
    /**
     * Цвет текста
     */
    private final int textColor;


    /**
     * Панель на сетке
     *
     * @param window          окно
     * @param drawBG          флаг, нужно ли рисовать подложку
     * @param backgroundColor цвет подложки
     * @param padding         отступы
     * @param gridWidth       кол-во ячеек сетки по ширине
     * @param gridHeight      кол-во ячеек сетки по высоте
     * @param gridX           координата в сетке x
     * @param gridY           координата в сетке y
     * @param colspan         кол-во колонок, занимаемых панелью
     * @param rowspan         кол-во строк, занимаемых панелью
     * @param text            начальный текст
     * @param vcentered       флаг, нужно ли выравнивать текст по центру по вертикали
     * @param textColor       цвет текста
     */
    public Input(Window window, boolean drawBG, int backgroundColor, int padding,
                 int gridWidth, int gridHeight, int gridX, int gridY, int colspan,
                 int rowspan, String text, boolean vcentered, int textColor) {
        super(window, drawBG, backgroundColor, padding, gridWidth, gridHeight,
                gridX, gridY, colspan, rowspan);
        this.text = text;
        this.vcentered = vcentered;
        this.textColor = textColor;
    }


    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        try (var paint = new Paint()) {
            canvas.save();
            paint.setColor(backgroundColor);
            FontMetrics metrics = FONT18.getMetrics();
            if (vcentered) {
                canvas.translate(0, (windowCS.getSize().y - INPUT_SIZE) / 2.0f);
            }
            canvas.drawRRect(RRect.makeXYWH(0, 0, windowCS.getSize().x, INPUT_SIZE, 4), paint);
            float y = INPUT_SIZE - LOCAL_PADDING - metrics.getDescent();
            try (TextLine line = TextLine.make(text, FONT18)) {
                canvas.translate(LOCAL_PADDING, y);
                paint.setColor(textColor);
                canvas.drawTextLine(line, 0, 0, paint);
                if (focused && InputFactory.cursorDraw()) {
                    canvas.translate(line.getWidth(), 0);
                    canvas.drawRect(Rect.makeXYWH(0, metrics.getAscent(), 2, metrics.getHeight()), paint);
                }
            }
            canvas.restore();

        }
    }

    /**
     * Обработчик событий
     * При перегрузке обязателен вызов реализации предка
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        super.accept(e);
        if (e instanceof EventTextInput ee) {
            text += ee.getText()
                    .replace((char) 9 + "", "") // Tab
                    .replace((char) 27 + "", ""); // Esc
            window.requestFrame();
        } else if (e instanceof EventKey ee) {
            if (ee.isPressed()) {
                Key key = ee.getKey();
                switch (key) {
                    case BACKSPACE -> {
                        if (!text.isEmpty())
                            text = text.substring(0, text.length() - 1);
                    }
                    case ESCAPE -> {
                    }
                }
            }
        }
    }
    /**
     * Получить вещественное значение из поля ввода
     *
     * @return возвращает значение, если всё ок, в противном случае вернёт 0
     */
    public double doubleValue() {
        try {
            // для правильной конвертации, если нужно, заменяем плавающую запятую
            // на плавающую точку
            return Double.parseDouble(text.replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("ошибка преобразования");
        }
        return 0;
    }

    /**
     * Проверяет, лежит ли в поле ввода правильное вещественное число
     *
     * @return флаг
     */
    public boolean hasValidDoubleValue() {
        try {
            // для правильной конвертации, если нужно, заменяем плавающую запятую
            // на плавающую точку
            Double.parseDouble(text.replace(",", "."));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Получить вещественное значение из поля ввода
     *
     * @return возвращает значение, если всё ок, в противном случае вернёт 0
     */
    public int intValue() {
        try {
            // для правильной конвертации, если нужно, заменяем плавающую запятую
            // на плавающую точку
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println("ошибка преобразования");
        }
        return 0;
    }

    /**
     * Проверяет, лежит ли в поле ввода правильное вещественное число
     *
     * @return флаг
     */
    public boolean hasValidIntValue() {
        try {
            // для правильной конвертации, если нужно, заменяем плавающую запятую
            // на плавающую точку
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Задать текст
     *
     * @param text текст
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Возвращает текст из поля вввода
     *
     * @return текст
     */
    public String getText() {
        return text;
    }
    boolean focused = false;
    public void setFocus() {
        // снимаем фокус со всех полей ввода
        InputFactory.defocusAll();
        // выделяем текущее поле ввода
        this.focused= true;
    }
    public boolean isFocused() {
        return focused;
    }
}