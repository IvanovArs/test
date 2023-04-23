package panels;

import app.Primitive;
import io.github.humbleui.jwm.Event;
import io.github.humbleui.jwm.EventKey;
import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.RRect;
import misc.CoordinateSystem2i;
import misc.Misc;
import misc.Vector2i;
import java.util.ArrayList;
/**
 * Панель игры
 */
public class PanelPrimitives extends Panel {
    /**
     * Список примитивов
     */
    private final ArrayList<Primitive> primitives = new ArrayList<>();
    /**
     * Положение текущего примитива
     */
    private int primitivePos;

    /**
     * Конструктор панели
     *
     * @param window          окно
     * @param drawBG          нужно ли рисовать подложку
     * @param backgroundColor цвет фона
     * @param padding         отступы
     */
    public PanelPrimitives(Window window, boolean drawBG, int backgroundColor, int padding) {
        super(window, drawBG, backgroundColor, padding);
        // добавляем точку
        primitives.add(((canvas, windowCS, p) -> canvas.drawRRect(
                RRect.makeXYWH(200, 200, 4, 4, 2), p)
        ));
        primitivePos = 0;
    }

    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // кнопки клавиатуры
        if (e instanceof EventKey eventKey) {
            // кнопка нажата с Ctrl
            if (eventKey.isPressed()) {
                switch (eventKey.getKey()) {
                    // Следующий примитив
                    case LEFT -> primitivePos = (primitivePos - 1 + primitives.size()) % primitives.size();
                    // Предыдущий примитив
                    case RIGHT -> primitivePos = (primitivePos + 1) % primitives.size();
                }
            }
        }
    }
    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        // создаём перо
        Paint p = new Paint();
        // задаём цвет
        p.setColor(Misc.getColor(200, 255, 255, 255));
        // задаём толщину пера
        p.setStrokeWidth(5);
        // рисуем текущий примитив
        primitives.get(primitivePos).render(canvas, windowCS, p);
        primitives.add(canvas, windowCS, p) -> {
            // вершины треугольника
            Vector2i pointA = new Vector2i(400, 200);
            Vector2i pointB = new Vector2i(500, 600);
            Vector2i pointC = new Vector2i(300, 400);
            // рисуем его стороны
            canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, p);
            canvas.drawLine(pointB.x, pointB.y, pointC.x, pointC.y, p);
            canvas.drawLine(pointC.x, pointC.y, pointA.x, pointA.y, p);
        };
    }
}