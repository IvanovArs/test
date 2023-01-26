package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import lombok.Getter;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static app.Colors.CROSSED_COLOR;
import static app.Colors.SUBTRACTED_COLOR;
import static app.Point.POINT_SIZE;
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            Заданы два множества точек в вещественном
            пространстве. Требуется построить пересечение
            и разность этих множеств""";
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    @Getter
    private final ArrayList<Point> points;
    private CoordinateSystem2i lastWindowCS = null;

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
        solved = false;
        Point newPoint = new Point(pos, pointSet);
        points.add(newPoint);
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
                if (a.pos.equals(b.pos) && !a.pointSet.equals(b.pointSet)) {
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
}
