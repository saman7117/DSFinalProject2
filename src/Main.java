import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int[][] array = {
                {0x000000, 0x000000, 0x000000, 0x0000FF},
                {0x000000, 0x000000, 0xFFFFFF, 0x000000},
                {0x000000, 0xFFFFFF, 0x000000, 0x000000},
                {0x000000, 0xFFFFFF, 0x000000, 0x000000}
        };
        quadTree q = new quadTree(array);
        q.Divide();
        q.travis();
        System.out.println(q.pixelDepth(4,3,0));
        BufferedImage image = q.convertToImage();

        // Save the image to a file
        try {
            ImageIO.write(image, "PNG", new File("output_image.png"));
            System.out.println("Image saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
}