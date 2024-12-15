import javax.swing.*;
import java.awt.image.BufferedImage;

public class quadTree {
    int color;
    int [][] data;
    quadTree topLeft;
    quadTree topRight;
    quadTree bottomLeft;
    quadTree bottomRight;
    boolean isDivided;

    public quadTree(int [][] image){
        isDivided = true;
        data = image;
    }

    public quadTree(){
        isDivided = false;
    }

    public void Divide(){
        if(!this.checkSolid()){
            int rows = this.data.length;
            int cols = this.data.length;
            int midRow = rows / 2;
            int midCol = cols / 2;

            // Create the result 3D array to hold the 4 subarrays
            int[][][] result = new int[4][][];

            // Top Left
            result[0] = new int[midRow][midCol];
            for (int i = 0; i < midRow; i++) {
                for (int j = 0; j < midCol; j++) {
                    result[0][i][j] = data[i][j];
                }
            }

            // Top Right
            result[1] = new int[midRow][cols - midCol];
            for (int i = 0; i < midRow; i++) {
                for (int j = midCol; j < cols; j++) {
                    result[1][i][j - midCol] = data[i][j];
                }
            }

            // Bottom Left
            result[2] = new int[rows - midRow][midCol];
            for (int i = midRow; i < rows; i++) {
                for (int j = 0; j < midCol; j++) {
                    result[2][i - midRow][j] = data[i][j];
                }
            }

            // Bottom Right
            result[3] = new int[rows - midRow][cols - midCol];
            for (int i = midRow; i < rows; i++) {
                for (int j = midCol; j < cols; j++) {
                    result[3][i - midRow][j - midCol] = data[i][j];
                }
            }

            this.topLeft = new quadTree(result[0]);
            this.topRight = new quadTree(result[1]);
            this.bottomLeft = new quadTree(result[2]);
            this.bottomRight = new quadTree(result[3]);


            this.topRight.Divide();
            this.topLeft.Divide();
            this.bottomRight.Divide();
            this.bottomLeft.Divide();

        }
        else{
            this.topLeft = new quadTree();
            this.topRight = new quadTree();
            this.bottomLeft = new quadTree();
            this.bottomRight = new quadTree();
        }
    }

    public boolean checkSolid() {
        int temp = this.data[0][0];
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[i].length; j++) {
                if (this.data[i][j] != temp) {
                    isDivided = true;
                    return false;
                }
            }
        }
        color = temp;
        return true;
    }

    public void travis(){
        if(this.isDivided){
            for (int i = 0; i < this.data.length; i++) {
                for (int j = 0; j < this.data.length; j++) {
                    System.out.print(this.data[i][j] + " ");
                }
                System.out.println();
            }
            this.topLeft.travis();
            this.topRight.travis();
            this.bottomLeft.travis();
            this.bottomRight.travis();
        }
        else {
            System.out.println("is solid and color = " + this.color);
        }
    }

    public int pixelDepth(int px , int py , int depth){
        if (this.isDivided) {
            if (px <= this.data.length/2) {
                if (py <= this.data.length/2){
                    return this.topLeft.pixelDepth(px , py , depth+1);
                } else {
                    return this.bottomLeft.pixelDepth(px , py , depth + 1);
                }
            }
            else {
                if (py <= this.data.length/2){
                    return this.topRight.pixelDepth(px , py , depth+1);
                } else {
                    return this.bottomRight.pixelDepth(px , py , depth + 1);
                }
            }
        }
        else {
            return depth;
        }
    }

    public BufferedImage convertToImage() {
        int height = this.data.length;
        int width = this.data.length;

        // Create a BufferedImage with the same dimensions as the array
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Loop through the array and set each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, this.data[y][x]);
            }
        }

        return image;
    }

}
