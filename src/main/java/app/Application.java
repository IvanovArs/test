package app;

import Panels.PanelControl;
import Panels.PanelHelp;
import Panels.PanelLog;
import Panels.PanelRendering;
import controls.Label;
import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.RRect;
import io.github.humbleui.skija.Surface;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2i;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

import static app.Colors.APP_BACKGROUND_COLOR;
import static app.Colors.PANEL_BACKGROUND_COLOR;

public class Application implements Consumer<Event> {
    /**
     *
     */
    private final Window window;
    /**
     * радиус скругления элементов
     */
    private final Label label;
    private final Label label2;
    /**
     * Первый заголовок
     */
    private final Label label3;
    private final PanelHelp panelHelp;
    /**
     * панель курсора мыши
     */
    private final PanelControl panelControl;
    /**
     * панель рисования
     */
    private final PanelRendering panelRendering;
    /**
     * панель событий
     */
    private final PanelLog panelLog;
    public static final int C_RAD_IN_PX = 4;
    /**
     * отступы панелей
     */
    public static final int PANEL_PADDING = 5;


    public Application() {

        window = App.makeWindow();

        label = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                4, 4, 1, 1, 1, 1, "Привет, мир!", true, true);
        label2 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                4, 4, 0, 3, 1, 1, "Второй заголовок", true, true);

        label3 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                4, 4, 2, 0, 1, 1, "Это тоже заголовок", true, true);

        panelRendering = new PanelRendering(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 0, 0,
                3, 2
        );
        panelControl = new PanelControl(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 3, 0,
                2, 2
        );
        panelLog = new PanelLog(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 0, 2,
                3, 1
        );
        panelHelp = new PanelHelp(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 3, 2,
                2, 1
        );


        window.setEventListener(this);
        window.setTitle("Java 2D");
        window.setWindowSize(900, 900);
        window.setWindowPosition(100, 100);
        window.setVisible(true);
        switch (Platform.CURRENT) {
            case WINDOWS -> window.setIcon(new File("src/main/resources/windows.ico"));
            case MACOS -> window.setIcon(new File("src/main/resources/macos.icns"));
        }
        String[] layerNames = new String[]{
                "LayerGLSkija", "LayerRasterSkija"
        };

        for (String layerName : layerNames) {
            String className = "io.github.humbleui.jwm.skija." + layerName;
            try {
                Layer layer = (Layer) Class.forName(className).getDeclaredConstructor().newInstance();
                window.setLayer(layer);
                break;
            } catch (Exception e) {
                System.out.println("Ошибка создания слоя " + className);
            }
        }

        if (window._layer == null)
            throw new RuntimeException("Нет доступных слоёв для создания");


    }

    /**
     *
     * @param e the input argument
     */
    @Override
    public void accept(Event e) {
        if (e instanceof EventWindowClose) {
            App.terminate();
        } else if (e instanceof EventWindowCloseRequest) {
            window.close();
        } else if (e instanceof EventFrame) {
            window.requestFrame();
        } else if (e instanceof EventFrameSkija ee) {
            Surface s = ee.getSurface();
            paint(s.getCanvas(), new CoordinateSystem2i(s.getWidth(), s.getHeight()));
        }
        else if (e instanceof EventKey eventKey) {
            // кнопка нажата с Ctrl
            if (eventKey.isPressed()) {
                if (eventKey.isModifierDown(MODIFIER))
                    // разбираем, какую именно кнопку нажали
                    switch (eventKey.getKey()) {
                        case W -> window.close();
                        case H -> window.minimize();
                        case S -> PanelRendering.save();
                        case O -> PanelRendering.load();
                        case DIGIT1 -> {
                            if (maximizedWindow)
                                window.restore();
                            else
                                window.maximize();
                            maximizedWindow = !maximizedWindow;
                        }
                        case DIGIT2 -> window.setOpacity(window.getOpacity() == 1f ? 0.5f : 1f);
                    }
                else
                    switch (eventKey.getKey()) {
                        case ESCAPE -> {
                            window.close();
                            // завершаем обработку, иначе уже разрушенный контекст
                            // будет передан панелям
                            return;

                        }
                    }
            }
        }

        panelControl.accept(e);
        panelRendering.accept(e);
        panelLog.accept(e);
    }

    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        canvas.clear(APP_BACKGROUND_COLOR);
        panelRendering.paint(canvas, windowCS);
        panelControl.paint(canvas, windowCS);
        panelLog.paint(canvas, windowCS);
        panelHelp.paint(canvas, windowCS);
        canvas.restore();
    }
    public static final KeyModifier MODIFIER = Platform.CURRENT == Platform.MACOS ? KeyModifier.MAC_COMMAND : KeyModifier.CONTROL;
    private boolean maximizedWindow;
}
