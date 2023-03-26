import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by douwz on 12/12/2016.
 */
public class Methods2 {

    private double[][] a, b, aNext, bNext;
    private Random rnd;
    private int w = Main.w, h = Main.h;

    public Methods2() {
        a = new double[w][h];
        b = new double[w][h];
        aNext = new double[w][h];
        bNext = new double[w][h];
        rnd = new Random();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                a[i][j] = 1D;
            }
        }
//        addOval(5, w/2, h/2, 1.5, 1.0);
//        addCircle(25, w/2, h/2);
        addCircle(5, w/2, h/2);
//        addCircle(2, w/4, h/4);
//        addCircle(2, w-w/4, h-h/4);
    }

    public void addCircle(int rad, int x, int y) {
        for (int i = 0; i < 360; i++) {
            b[(int)(rad * Math.cos(Math.toRadians(i))) + x][(int)(rad * Math.sin(Math.toRadians(i))) + y] = 1D;
        }
    }
    public void addOval(int rad, int x, int y, double xStretch, double yStretch) {
        for (int i = 0; i < 360; i++) {
            b[(int)(rad * Math.cos(Math.toRadians(i)) * xStretch) + x][(int)(rad * Math.sin(Math.toRadians(i)) * yStretch) + y] = 1D;
        }
    }

    int ensureRange(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public void draw(GraphicsContext g) {
        int c;
        update();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                c = (int)Math.floor((aNext[i][j] - bNext[i][j])*255);
                c = ensureRange(c, 0, 255);
                Color col = Color.rgb(c, c, c);
                g.getPixelWriter().setColor(i, j, col);
            }
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                a[i][j] = aNext[i][j];
                b[i][j] = bNext[i][j];
            }
        }
    }

    /* Constants
    Normal:  f=.055, k=.062
    Mitosis: f=.0367, k=.0649
    Coral:   f=.0545, k=.062
    Vary k and f along axis: f along x between .045 to .07
                             k along y between .01 to .1
     */
    private double valA, valB, dA = 1.0, dB = 0.5, feed = 0.055, k = 0.062;
    public void update() {
        Arrays.stream(a).parallel().forEach(x -> Arrays.stream(a).parallel().forEach(z -> {

        }));
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
//                feed = 0.045 - (0.025 * (Math.sqrt(i * i + j * j) / Math.sqrt(w * w + h * h)));
//                feed = 0.045 - (0.04 * (Math.sin(i + j) / ((w + h) / 128.0)));
//                k = 0.01 + (0.09 * ((double)j / (a.length - 1)));
                valA = a[i][j];
                valB = b[i][j];
                aNext[i][j] = valA + ((dA * laplace(a, i, j)) - (valA * valB * valB) + (feed * (1.0 - valA)));
                bNext[i][j] = valB + ((dB * laplace(b, i, j)) + (valA * valB * valB) - ((k + feed) * valB));
            }
        }
    }
    public double laplace(double[][]m, int x, int y) {
        double sum = 0;
        sum += m[x][y] * -1.0;
        if (x != 0)
            sum += m[x-1][y] * 0.2;
        if (x != w - 1)
            sum += m[x+1][y] * 0.2;
        if (y != 0)
            sum += m[x][y-1] * 0.2;
        if (y != h - 1)
            sum += m[x][y+1] * 0.2;
        if (x != 0 && y != 0)
            sum += m[x-1][y-1] * 0.05;
        if (x != 0 && y != h - 1)
            sum += m[x-1][y+1] * 0.05;
        if (x != w - 1 && y != 0)
            sum += m[x+1][y-1] * 0.05;
        if (x != w - 1 && y != h - 1)
            sum += m[x+1][y+1] * 0.05;
        return sum;
    }
}