//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        int[][] array = new int[][]{
                {0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF},
                {0xf58d42, 0xf58d42, 0xFFFFFF, 0x8823b0},
                {0xf58d42, 0xFFFFFF, 0x8823b0, 0x8823b0},
                {0xf58d42, 0xFFFFFF, 0x8823b0, 0x8823b0}};
        quadTree q = new quadTree(array);
        q.Divide();

        quadTree temp = q.compress(2);

        BufferedImage image = temp.convertToImage();

        try {
            ImageIO.write(image, "PNG", new File("output_image.png"));
            System.out.println("Image saved successfully!");
        } catch (IOException var6) {
            System.out.println("Error saving image: " + var6.getMessage());
        }

    }
}
