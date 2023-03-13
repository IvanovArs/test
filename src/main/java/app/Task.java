package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.*;
import lombok.Getter;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static app.Colors.*;
import static app.Point.POINT_SIZE;
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            Множество точек на плоскости назовем 
            дваждытреугольным, если каждая точка этого 
            множества является вершиной хотя бы двух 
            правильных треугольников, построенных по 
            точкам множества. Определить, удовлетворяет ли 
            заданное множество точек этому свойству.""";
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    private static final int DELIMITER_ORDER = 10;
    @Getter
    private final ArrayList<Point> points;
    private CoordinateSystem2i lastWindowCS = null;
    private static final float WHEEL_SENSITIVE = 0.001f;

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("points") ArrayList<Point> points
    ) {
        this.ownCS = ownCS;
        this.points = points;
        this.crossed = new ArrayList<>();
        this.single = new ArrayList<>();
    }

    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        lastWindowCS = windowCS;
        renderGrid(canvas, lastWindowCS);
        renderTask(canvas, windowCS);
    }

    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos);
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {
            addPoint(taskPos);
        }
    }

    public void addPoint(Vector2d pos) {
        solved = false;
        Point newPoint = new Point(pos);
        points.add(newPoint);
        PanelLog.info("точка " +newPoint + " добавлена");
    }

    public void addRandomPoints(int cnt) {
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);
        for (int i = 0; i < cnt; i++) {
            Vector2i gridPos = addGrid.getRandomCoords();
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            if (ThreadLocalRandom.current().nextBoolean())
                addPoint(pos);
            else
                addPoint(pos);
        }
    }
    private boolean solved;
    public void clear() {
        points.clear();
        solved = false;
    }
    public void solve() {
        crossed.clear();
        single.clear();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point a = points.get(i);
                Point b = points.get(j);
                if (a.pos.equals(b.pos) && !a.equals(b)) {
                    if (!crossed.contains(a)){
                        crossed.add(a);
                        crossed.add(b);
                    }
                }
            }
        }
        for (Point point : points)
            if (!crossed.contains(point))
                single.add(point);
        solved = true;
    }
    /**
     * Отмена решения задачи
     */
    public void cancel() {
        solved = false;
    }
    public boolean isSolved() {
        return solved;
    }
    @Getter
    @JsonIgnore
    private final ArrayList<Point> crossed;
    /**
     * Список точек в разности
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> single;
    public void renderGrid(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        float strokeWidth = 0.03f / (float) ownCS.getSimilarity(windowCS).y + 0.5f;
        try (var paint = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(strokeWidth).setColor(TASK_GRID_COLOR)) {
            for (int i = (int) (ownCS.getMin().x); i <= (int) (ownCS.getMax().x); i++) {
                Vector2i windowPos = windowCS.getCoords(i, 0, ownCS);
                float strokeHeight = i % DELIMITER_ORDER == 0 ? 5 : 2;
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y + strokeHeight, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y - strokeHeight, paint);
            }
            for (int i = (int) (ownCS.getMin().y); i <= (int) (ownCS.getMax().y); i++) {
                Vector2i windowPos = windowCS.getCoords(0, i, ownCS);
                float strokeHeight = i % 10 == 0 ? 5 : 2;
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x + strokeHeight, windowPos.y, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x - strokeHeight, windowPos.y, paint);
            }
        }
        canvas.restore();
    }
    private void renderTask(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        try (var paint = new Paint()) {
            for (Point p : points) {
                if (!solved) {
                    paint.setColor(p.getColor());
                } else {
                    if (crossed.contains(p))
                        paint.setColor(CROSSED_COLOR);
                    else
                        paint.setColor(SUBTRACTED_COLOR);
                }
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }
        }
        canvas.restore();
    }
    public void scale(float delta, Vector2i center) {
        if (lastWindowCS == null) return;
        Vector2d realCenter = ownCS.getCoords(center, lastWindowCS);
        ownCS.scale(1 + delta * WHEEL_SENSITIVE, realCenter);
    }
    @JsonIgnore
    public Vector2d getRealPos(int x, int y, CoordinateSystem2i windowCS) {
        return ownCS.getCoords(x, y, windowCS);
    }
    public void paintMouse(Canvas canvas, CoordinateSystem2i windowCS, Font font, Vector2i pos) {
        // создаём перо
        try (var paint = new Paint().setColor(TASK_GRID_COLOR)) {
            canvas.save();
            canvas.drawRect(Rect.makeXYWH(0, pos.y - 1, windowCS.getSize().x, 2), paint);
            canvas.drawRect(Rect.makeXYWH(pos.x - 1, 0, 2, windowCS.getSize().y), paint);
            canvas.translate(pos.x + 3, pos.y - 5);
            Vector2d realPos = getRealPos(pos.x, pos.y, lastWindowCS);
            canvas.drawString(realPos.toString(), 0, 0, font, paint);
            canvas.restore();
        }
    }
}
