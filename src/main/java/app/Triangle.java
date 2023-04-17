package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import misc.Misc;

import java.util.Objects;

public class Triangle {
    @Getter
    Point a;
    @Getter
    Point b;
    @Getter
    Point c;


    @JsonCreator
    public Triangle(@JsonProperty("a") Point a, @JsonProperty("b") Point b, @JsonProperty("c") Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }


    /**
     * Получить цвет точки по её множеству
     *
     * @return цвет точки
     */
    @JsonIgnore
    public int getColor() {
        return Misc.getColor(0xCC, 0x00, 0x00, 0xFF);
    }


    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Point{" +
                "pointSetType=" +
                ", a=" + a.getPos() + ", b=" + b.getPos() + " ,c=" + c.getPos() +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Objects.equals(a, triangle.a) && Objects.equals(b, triangle.b) && Objects.equals(c, triangle.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    public boolean isEquilateral() {
        double dx1 = a.pos.x - b.pos.x;
        double dy1 = a.pos.y - b.pos.y;

        double dx2 = b.pos.x - c.pos.x;
        double dy2 = b.pos.y - c.pos.y;

        double dx3 = c.pos.x - a.pos.x;
        double dy3 = c.pos.y - a.pos.y;

        System.out.println(dx1 * dx1 + dy1 * dy1);
        System.out.println(dx2 * dx2 + dy2 * dy2);
        System.out.println(dx3 * dx3 + dy3 * dy3);

        return Math.abs((dx1 * dx1 + dy1 * dy1) - (dx2 * dx2 + dy2 * dy2)) < 0.01 &&
                Math.abs((dx2 * dx2 + dy2 * dy2) - (dx3 * dx3 + dy3 * dy3)) < 0.01 &&
                Math.abs((dx3 * dx3 + dy3 * dy3) - (dx1 * dx1 + dy1 * dy1)) < 0.01;
    }
    public boolean isNotEquilateral() {
        double dx1 = a.pos.x - b.pos.x;
        double dy1 = a.pos.y - b.pos.y;

        double dx2 = b.pos.x - c.pos.x;
        double dy2 = b.pos.y - c.pos.y;

        double dx3 = c.pos.x - a.pos.x;
        double dy3 = c.pos.y - a.pos.y;

        System.out.println(dx1 * dx1 + dy1 * dy1);
        System.out.println(dx2 * dx2 + dy2 * dy2);
        System.out.println(dx3 * dx3 + dy3 * dy3);

        return Math.abs((dx1 * dx1 + dy1 * dy1) - (dx2 * dx2 + dy2 * dy2)) > 0.01 ||
                Math.abs((dx2 * dx2 + dy2 * dy2) - (dx3 * dx3 + dy3 * dy3)) > 0.01 ||
                Math.abs((dx3 * dx3 + dy3 * dy3) - (dx1 * dx1 + dy1 * dy1)) > 0.01;
    }
}
