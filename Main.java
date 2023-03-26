import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.*;

public class Main extends Application {

    public static GraphicsContext g;

    public static final long TIMESTEP = 1;
    public static int w = 200, h = 200;
    public Methods m;

    @Override
    public void start(Stage stage) throws Exception{
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(bp);
        Canvas canvas = new Canvas(w, h);
        bp.setCenter(canvas);
        g = canvas.getGraphicsContext2D();
        m = new Methods();

        scene.setRoot(bp);
        stage.setScene(scene);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                switch (key) {
                    case R:
                        m = new Methods();
                        break;
                    case O:
                        break;
                    case P:
                        break;
                    case X:
                        System.exit(0);
                        break;
                }
            }
        });

//        stage.setFullScreen(true);
        stage.show();

        ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(this::tick, 0, TIMESTEP, TimeUnit.MILLISECONDS);
    }

    public void tick() {
        CountDownLatch drawLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            g.clearRect(0, 0, w, h);
            m.draw(g);
            drawLatch.countDown();
        });
        try {
            drawLatch.await();
        } catch (InterruptedException ignore) {}
    }

    @Override
    public void stop() throws Exception{
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}