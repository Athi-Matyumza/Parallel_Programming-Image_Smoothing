import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MeanFilterParallel extends RecursiveAction {
    int[][] sliding_window; // 2D array for the sliding window

    BufferedImage image; // Input image
    int[][] pixels; // Pixel data of the image
    int low, hi, sliding_width; // Parameters for parallel processing

    final static int SEQUENTIAL_CUT_OFF = 50; // Threshold for switching to sequential processing

    public MeanFilterParallel(int[][] pixels, BufferedImage image, int sliding_width, int low, int hi) {
        this.pixels = pixels;
        this.low = low;
        this.hi = hi;
        this.sliding_width = sliding_width;
        this.image = image;
    }

    protected void compute() {
        if ((hi - low) < SEQUENTIAL_CUT_OFF) {
            // Creating Sliding window
            sliding_window = new int[sliding_width][sliding_width];

            // Sliding window extraction point & calculation of mean
            for (int y = low; y < (hi - sliding_window.length + 1); y++) {
                for (int x = 0; x < pixels[y].length - sliding_window.length + 1; x++) {
                    // slices the rows of the 2d array to window height size
                    sliding_window = Arrays.copyOfRange(pixels, y, y + sliding_width);

                    for (int i = 0; i < sliding_window.length; i++) {
                        // limits the columns of the rows sliced to window width size
                        sliding_window[i] = Arrays.copyOfRange(sliding_window[i], x, x + sliding_width);
                    }

                    // Set the pixel at the center of the window to the mean value
                    image.setRGB(x + sliding_width/2, y + sliding_width/2, mean(sliding_window));
                }
            }
        } else {
            // Split the task into two sub-tasks
            MeanFilterParallel left = new MeanFilterParallel(pixels, image, sliding_width, low, (hi + low) / 2);
            MeanFilterParallel right = new MeanFilterParallel(pixels, image, sliding_width, (hi + low) / 2, hi);

            left.fork();
            right.compute();
            left.join();
        }
    }

    public static void main(String[] args) {
        // User Input
        int sliding_width = Integer.parseInt(args[2]);

        // Reading image
        URL file_loc = MeanFilterParallel.class.getResource(args[0]);
        File f = null;

        if ((sliding_width%2 == 0) && (sliding_width >= 3)){
            System.out.println("The program only accepts odd numbers >= 3 for sliding window width. Please fix the mistake and try again.");
            System.exit(0);
        }

        if (file_loc != null) {
            f = new File(file_loc.getPath());
        } else {
            System.out.println("The file (" + args[0] + ") does not exist.");
            System.exit(0);
        }

        BufferedImage img;
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Creating pixel array
        int[][] pixels = new int[img.getHeight()][img.getWidth()];

        // Pixel Array Population point
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[row].length; col++) {
                pixels[row][col] = img.getRGB(col, row);
            }
        }

        long start_time = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new MeanFilterParallel(pixels, img, sliding_width, 0, pixels.length));
        long end_time = System.currentTimeMillis() - start_time;

        System.out.println("This program takes: " + end_time/1000.0f + " seconds.");
        
        // Writing to output image
        f = new File(args[1]);
        try {
            ImageIO.write(img, "jpeg", f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int mean(int[][] window) {
        int red = 0, green = 0, blue = 0;
        int div = window.length * window.length;

        // Calculate the mean for each color component
        for (int[] ints : window) {
            for (int x = 0; x < window.length; x++) {
                red += (ints[x] >> 16) & 0xff;
                green += (ints[x] >> 8) & 0xff;
                blue += ints[x] & 0xff;
            }
        }

        red /= div;
        green /= div;
        blue /= div;

        // Combine color components and return the result
        return ((red << 16) | (green << 8) | blue);
    }
}
