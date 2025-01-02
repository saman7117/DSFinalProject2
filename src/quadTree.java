import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class quadTree {
    int[][] data;
    quadTree topLeft;
    quadTree topRight;
    quadTree bottomLeft;
    quadTree bottomRight;
    boolean isDivided;
    int z1;
    int z2;
    int z3;
    int z4;
    int depth;
    static int MAXdepth = -1;
    public quadTree(int[][] image) {
        this.data = image;
        this.isDivided = true;
        this.z1 = 0;
        this.z2 = 0;
        this.z3 = image.length;
        this.z4 = image[0].length;
    }
    public quadTree(int[][] data, int z1, int z2, int z3, int z4, int depth) {
        this.data = data;
        this.isDivided = true;
        this.z1 = z1;
        this.z2 = z2;
        this.z3 = z3;
        this.z4 = z4;
        this.depth = depth;
    }
    public boolean checkSolid(int[][] arr, int x1, int y1, int x2, int y2) {
        int firstColor = arr[x1][y1];
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (arr[i][j] != firstColor) {
                    return false;
                }
            }
        }
        return true;
    }
    public void Divide(int[][] arr,int d) {
        int height = z3 - z1;
        int width  = z4 - z2;

        if (!checkSolid(arr, z1, z2, z3, z4)) {
            this.isDivided = true;

            int midRow = z1 + height / 2;
            int midCol = z2 + width / 2;

            this.topLeft = new quadTree(data, z1, z2, midRow, midCol,d+1);
            this.topRight = new quadTree(data, midRow, z2, z3, midCol,d+1);
            this.bottomLeft = new quadTree(data, z1, midCol, midRow, z4,d+1);
            this.bottomRight = new quadTree(data, midRow, midCol, z3, z4,d+1);

            this.topLeft.Divide(arr,d+1);
            this.topRight.Divide(arr,d+1);
            this.bottomLeft.Divide(arr,d+1);
            this.bottomRight.Divide(arr,d+1);

        } else {
            this.isDivided = false;
            MAXdepth = Math.max(this.depth,MAXdepth);
        }
    }
    public boolean overlap(int x1, int y1, int x2, int y2) {
        return (this.z1 < x2 && this.z3 > x1 && this.z2 < y2 && this.z4 > y1);
    }
    public void mask1(int x1, int y1, int x2, int y2, int[][] ans) {
        if (this.isDivided) {
            this.topLeft.mask1(x1, y1, x2, y2, ans);
            this.topRight.mask1(x1, y1, x2, y2, ans);
            this.bottomLeft.mask1(x1, y1, x2, y2, ans);
            this.bottomRight.mask1(x1, y1, x2, y2, ans);
        } else {
            if (overlap(x1, y1, x2, y2)) {
                for (int i = z1; i < z3; i++) {
                    for (int j = z2; j < z4; j++) {
                        ans[i][j] = 0x000000;
                    }
                }
            }
        }
    }
    public quadTree mask(int x1, int y1, int x2, int y2) {
        int[][] ans = this.data;
        this.mask1(x1, y1, x2, y2, ans);
        this.data = ans;
        return this;
    }
    public void travis() {
        if (this.isDivided) {
            System.out.println("Subdivided block: " +
                    "[" + z1 + ".." + z3 + ") x [" + z2 + ".." + z4 + ")");
            this.topLeft.travis();
            this.topRight.travis();
            this.bottomLeft.travis();
            this.bottomRight.travis();
        } else {
            System.out.println("Leaf block: " +
                    "[" + z1 + ".." + z3 + ") x [" + z2 + ".." + z4 + ")");
        }
    }
    public int pixelDepth(int px, int py, int depth) {
        if (!this.isDivided) return depth;

        int midRow = (z1 + z3) / 2;
        int midCol = (z2 + z4) / 2;

        if (px < midRow) {
            if (py < midCol) {
                return this.topLeft.pixelDepth(px, py, depth + 1);
            } else {
                return this.bottomLeft.pixelDepth(px, py, depth + 1);
            }
        } else {
            if (py < midCol) {
                return this.topRight.pixelDepth(px, py, depth + 1);
            } else {
                return this.bottomRight.pixelDepth(px, py, depth + 1);
            }
        }
    }
    public BufferedImage convertToImage() {
        int height = data.length;
        int width = data[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = 0xFF000000 | (data[y][x] & 0xFFFFFF);
                image.setRGB(x, y, argb);
            }
        }
        return image;
    }

    public int[] hexToRgb(int hex) {
        int red   = (hex >> 16) & 0xFF;
        int green = (hex >> 8) & 0xFF;
        int blue  = hex & 0xFF;
        return new int[]{red, green, blue};
    }

    public int rgbToHex(int[] rgb) {
        int red   = rgb[0];
        int green = rgb[1];
        int blue  = rgb[2];
        return (red << 16) | (green << 8) | blue;
    }
    public void search1(int x1, int y1, int x2, int y2, int[][] ans, int[][] data) {
        if (this.isDivided) {
            this.topLeft.search1(x1, y1, x2, y2, ans, data);
            this.topRight.search1(x1, y1, x2, y2, ans, data);
            this.bottomLeft.search1(x1, y1, x2, y2, ans, data);
            this.bottomRight.search1(x1, y1, x2, y2, ans, data);
        } else {
            if (overlap(x1, y1, x2, y2)) {
                for (int i = z1; i < z3; i++) {
                    for (int j = z2; j < z4; j++) {
                        ans[i][j] = data[i][j];
                    }
                }
            }
        }
    }
    public quadTree search(int x1, int y1, int x2, int y2) {
        int[][] ans = new int[this.data.length][this.data.length];
        for (int i = 0; i < ans.length; i++) {
            for (int j = 0; j < ans[0].length; j++) {
                ans[i][j] = 0x000000;
            }
        }
        this.search1(x1, y1, x2, y2, ans, this.data);
        this.data = ans;
        return this;
    }

    public quadTree compress(int newSize) {
        int[][] da = new int[newSize][newSize];
        quadTree q = new quadTree(da, 0, 0, newSize, newSize,0);
        int step = this.data.length / newSize;

        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                int rSum = 0, gSum = 0, bSum = 0;
                for (int k = i * step; k < (i + 1) * step; k++) {
                    for (int l = j * step; l < (j + 1) * step; l++) {
                        int[] rgb = hexToRgb(this.data[k][l]);
                        rSum += rgb[0];
                        gSum += rgb[1];
                        bSum += rgb[2];
                    }
                }
                int totalPixels = step * step;
                int rAvg = rSum / totalPixels;
                int gAvg = gSum / totalPixels;
                int bAvg = bSum / totalPixels;
                q.data[i][j] = rgbToHex(new int[]{rAvg, gAvg, bAvg});
            }
        }
        return q;
    }

    public int treeDepth(){
        return MAXdepth;
    }
}
