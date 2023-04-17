import app.Point;
import app.Task;
import app.Triangle;
import com.beust.ah.A;
import misc.CoordinateSystem2d;
import misc.Vector2d;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UnitTest {
    // ToDo: тест на равносторонность треугольников (и равносторонние, и не равносторонние) - хотя бы два
    // ToDo: тест на дваждытреугольность множеств точек (и дваждытреугольные, и недваждытреугольые)
    // Причём несколько тестов: 1 - нет треугольников, 2 - однотреугольное, 3 - частично двухтреугольные
    // 4 - полность двухтрегуольное множество
    @Test
    public void test1() {
        Triangle triangle = new Triangle(
                new Point(new Vector2d(0, 0)),
                new Point(new Vector2d(2.829, 0)),
                new Point(new Vector2d(1.415, 2.449))
        );
        assert triangle.isEquilateral();
    }

    @Test
    public void test2() {
        CoordinateSystem2d cs = new CoordinateSystem2d(-10, -10, 20, 20);
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(new Vector2d(0, 0)));
        points.add(new Point(new Vector2d(2.829, 0)));
        points.add(new Point(new Vector2d(1.415, 2.449)));
        Task t = new Task(cs, points);
    }


    @Test
    public void test3() {
        Triangle triangle = new Triangle(
                new Point(new Vector2d(0, 0)),
                new Point(new Vector2d(2.829, 0)),
                new Point(new Vector2d(1.732, 2.449))
        );
        assert triangle.isNotEquilateral();
    }

    @Test
    public void test4() {
        CoordinateSystem2d cs = new CoordinateSystem2d(-10, -10, 20, 20);
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(new Vector2d(0, 2.449)));
        points.add(new Point(new Vector2d(2.829, 2.449)));
        points.add(new Point(new Vector2d(1.415, 4.898)));
        points.add(new Point(new Vector2d(1.415, 0)));
        Task t = new Task(cs, points);
        assert t.check1();
    }

    @Test
    public void test5() {
        CoordinateSystem2d cs = new CoordinateSystem2d(-10, -10, 20, 20);
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(new Vector2d(0, 0)));
        points.add(new Point(new Vector2d(2.829, 0)));
        points.add(new Point(new Vector2d(1.415, 2.449)));
        points.add(new Point(new Vector2d(0.707, 1.225)));
        points.add(new Point(new Vector2d(1.415, 0)));
        points.add(new Point(new Vector2d(2.121, 1.225)));
        Task t = new Task(cs, points);
        assert t.check2();
    }
}