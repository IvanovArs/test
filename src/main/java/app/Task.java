package app;

import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static app.Point.POINT_SIZE;

public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            Заданы два множества точек в вещественном
            пространстве. Требуется построить пересечение
            и разность этих множеств""";
    private final CoordinateSystem2d ownCS;
    private CoordinateSystem2i lastWindowCS = null;
    /**
     * Список точек
     */
    private final ArrayList<Point> points;

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    public Task(CoordinateSystem2d ownCS, ArrayList<Point> points) {
        this.ownCS = ownCS;
        this.points = points;
    }

    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        try (var paint = new Paint()) {
            for (Point p : points) {

                paint.setColor(p.getColor());
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }
        }
        canvas.restore();
        lastWindowCS = windowCS;
    }

    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos, Point.PointSet.FIRST_SET);
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {
            addPoint(taskPos, Point.PointSet.SECOND_SET);
        }
    }

    public void addPoint(Vector2d pos, Point.PointSet pointSet) {
        Point newPoint = new Point(pos, pointSet);
        points.add(newPoint);
        // Добавляем в лог запись информации
        PanelLog.info("точка " + newPoint + " добавлена в " + newPoint.getSetName());
    }
    public void addRandomPoints(int cnt) {
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);
        for (int i = 0; i < cnt; i++) {
            Vector2i gridPos = addGrid.getRandomCoords();
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            if (ThreadLocalRandom.current().nextBoolean())
                addPoint(pos, Point.PointSet.FIRST_SET);
            else
                addPoint(pos, Point.PointSet.SECOND_SET);
        }
    }
    public void clear() {
        points.clear();
    }
    public void solve() {
        PanelLog.warning("Вызван метод solve()\n Пока что решения нет");
    }
    /**
     * Отмена решения задачи
     */
    public void cancel() {

    }
}
