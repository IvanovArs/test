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
    public Triangle(@JsonProperty("a") Point a,@JsonProperty("b") Point b,@JsonProperty("c") Point c) {
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
                ", a=" + a.getPos() +", b="+b.getPos()+" ,c="+c.getPos()+
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
}
