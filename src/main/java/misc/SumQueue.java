package misc;

/**
 * Класс суммирующей очереди
 */
public class SumQueue {
    /**
     * Предел суммирования
     */
    int SUM_LIMIT = 1000;
    /**
     * Начальная длина очереди
     */
    int QUEUE_INIT_LENGTH = 180;
    /**
     * Сколько записей мы уже получили
     */
    int dataLength;
    /**
     * положение курсора
     */
    int cursor;
    /**
     * Значения
     */
    public float[] values = new float[QUEUE_INIT_LENGTH];

    /**
     * Добавить элемент
     *
     * @param a значение элемента
     */
    public void add(float a) {
        values[cursor] = a;
        cursor = (cursor + 1) % values.length;
        if (dataLength < values.length)
            dataLength++;
    }

    /**
     * Получить среднее значение очереди
     *
     * @return среднее значение очереди
     */
    public double getMean() {
        double res = 0;
        int cnt = 0;
        for (int i = 0; i < Math.min(dataLength, values.length); i++) {
            var realPos = (cursor - i + values.length) % values.length;
            if (values[realPos] > 0) {
                res += values[realPos];
                cnt++;
            }
            if (res > SUM_LIMIT)
                break;
        }
        return res / cnt;
    }

    /**
     * Получить длину очереди
     *
     * @return длина очереди
     */
    public int getLength() {
        return values.length;
    }

    /**
     * Задать длину очереди
     *
     * @param len новая длина
     */
    public void setLength(int len) {
        cursor = 0;
        values = new float[len];
        dataLength = 0;
    }

    /**
     * Получить элемент очереди ппо индексу
     *
     * @param i индекс
     * @return элемент очереди
     */
    public float get(int i) {
        var idx = (cursor + i) % values.length;
        return values[idx];
    }
}
