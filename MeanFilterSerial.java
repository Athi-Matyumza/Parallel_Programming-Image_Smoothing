import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class MeanFilterSerial {
    public static void main(String[] args) {
        // User Input
        int sliding_width = Integer.parseInt(args[2]);

        // Reading image
        URL file_loc = MeanFilterSerial.class.getResource(args[0]);
        File f = null;


        if ((sliding_width%2 == 0) && (sliding_width >= 3)){
            System.out.println("The program only accepts odd numbers >= 3 for sliding window width. Please fix the mistake and try again.");
            System.exit(0);
        }

        if (file_loc != null) {
            f = new File(file_loc.getPath());
        }else {
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
        for (int row = 0; row < img.getHeight(); row++) {
            for (int col = 0; col < img.getWidth(); col++) {
                pixels[row][col] = img.getRGB(col, row);
            }
        }

        long start_time = System.currentTimeMillis();
        // Creating window array
        int[][] sliding_window;

        // Sliding window extraction point & calculation of mean
        for (int y = 0; y < pixels.length - sliding_width + 1; y++) {
            for (int x = 0; x < pixels[y].length - sliding_width + 1; x++) {
                // slices the rows of the 2d array to window height size
                sliding_window = Arrays.copyOfRange(pixels, y, y + sliding_width);

                for (int i = 0; i < sliding_width; i++) {
                    // limits the columns of the rows sliced to window width size
                    sliding_window[i] = Arrays.copyOfRange(sliding_window[i], x, x + sliding_width);
                }

                img.setRGB(x + sliding_width/2, y + sliding_width/2, mean(sliding_window));
            }
        }

        long end_time = System.currentTimeMillis() - start_time;
        System.out.println("This program takes: " + end_time/1000.0f + " seconds.");
        
        // Writing to output image
        f = new File(args[1]);
        try {
            ImageIO.write(img, "jpeg", f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }// Ending of main function
    
    public static int mean(int[][] window) {
        int red = 0, green = 0, blue = 0;
        int div = window.length*window.length;

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

        return ((red<<16) | (green<<8) | blue);
    }//Ending of mean function
}