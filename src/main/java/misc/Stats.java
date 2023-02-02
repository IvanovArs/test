package misc;

import io.github.humbleui.skija.*;
import misc.CoordinateSystem2i;
import misc.SumQueue;

import static app.Colors.*;

/**
 * Cтатистика
 */
public class Stats {

    /**
     * Время старта
     */
    public long prevTime = System.nanoTime();
    /**
     * Очередь временных меток
     */
    private final SumQueue deltaTimes = new SumQueue();


    /**
     * Рисование
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     * @param font     шрифт
     * @param padding  отступ
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS, Font font, int padding) {
        try (var paint = new Paint()) {
            canvas.save();
            canvas.translate(padding, windowCS.getSize().y - padding - 32);
            paint.setColor(STATS_BACKGROUND_COLOR);
            canvas.drawRRect(RRect.makeXYWH(
                    0, 0, windowCS.getSize().x - padding * 2, 32,
                    4, 4, 0, 0), paint);
            paint.setColor(STATS_COLOR);
            for (int i = 0; i < deltaTimes.getLength(); i++) {
                float currentDelta = deltaTimes.get(i);
                canvas.drawRect(Rect.makeXYWH(i, Math.min(windowCS.getSize().y, 32 - currentDelta), 1, currentDelta), paint);
            }
            int len = windowCS.getSize().x - padding * 2;
            if (len > 0 && deltaTimes.getLength() != len) {
                deltaTimes.setLength(len);
            }
            long now = System.nanoTime();
            deltaTimes.add((now - prevTime) / 1000000.0f);
            prevTime = now;
            canvas.restore();
            canvas.save();
            paint.setColor(STATS_TEXT_COLOR);
            canvas.translate(windowCS.getSize().x - padding - 50, padding + 15);
            String fps = String.format("%.01f", (1 / deltaTimes.getMean() * 1000));
            canvas.drawString("FPS: " + fps, 0, 0, font, paint);
            canvas.restore();
        }
    }
}