package misc;

import java.util.ArrayList;
import java.util.List;

public class Misc {

    /**
     * Получить цвет по компонентам
     *
     * @param a прозрачность
     * @param r красная компонента
     * @param g зелёная компонента
     * @param b синяя компонента
     * @return целове число с цветом
     */
    public static int getColor(int a, int r, int g, int b) {
        return ((a * 256 + r) * 256 + g) * 256 + b;
    }
    public static List<String> limit(String line, int maxLength) {
        List<String> lst = new ArrayList<>();
        if (line.length() < maxLength) {
            lst.add(line);
            return lst;
        }
        int spacePos;
        do {
            spacePos = line.substring(0, maxLength).lastIndexOf(' ');
            if (spacePos > 0) {
                lst.add(line.substring(0, spacePos));
                line = line.substring(spacePos + 1);
            }
        } while (spacePos > 0 && line.length() > maxLength);
        lst.add(line);
        return lst;
    }

    /**
     * Запрещаем вызов конструктора
     */
    private Misc() {
        throw new AssertionError("Вызов этого конструктора запрещён");
    }

}
