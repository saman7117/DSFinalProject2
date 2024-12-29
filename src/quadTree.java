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
    // Bounding box (half-open intervals):
    // z1,z2 inclusive, z3,z4 exclusive in the sense [z1..z3) × [z2..z4)
    int z1;
    int z2;
    int z3;
    int z4;
    int depth;
    static int MAXdepth = -1;

    // Constructor for the top-level quadtree
    public quadTree(int[][] image) {
        this.data = image;
        this.isDivided = true;
        // If square, assume image.length == image[0].length
        this.z1 = 0;
        this.z2 = 0;
        this.z3 = image.length;       // half-open => last row index is z3-1
        this.z4 = image[0].length;// last col index is z4-1
    }

    // Constructor for children, specifying bounding box
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
                    // Not uniform
                    return false;
                }
            }
        }
        return true;
    }
    public void Divide(int[][] arr,int d) {
        int height = z3 - z1;  // row span
        int width  = z4 - z2;  // col span

        // If not uniform, subdivide
        if (!checkSolid(arr, z1, z2, z3, z4)) {
            // We set isDivided = true and create children
            this.isDivided = true;

            int midRow = z1 + height / 2; // midpoint in rows
            int midCol = z2 + width / 2;  // midpoint in cols

            // topLeft -> [z1..midRow) × [z2..midCol)
            this.topLeft = new quadTree(data, z1, z2, midRow, midCol,d+1);
            // topRight -> [midRow..z3) × [z2..midCol)
            this.topRight = new quadTree(data, midRow, z2, z3, midCol,d+1);
            // bottomLeft -> [z1..midRow) × [midCol..z4)
            this.bottomLeft = new quadTree(data, z1, midCol, midRow, z4,d+1);
            // bottomRight -> [midRow..z3) × [midCol..z4)
            this.bottomRight = new quadTree(data, midRow, midCol, z3, z4,d+1);

            // Recurse
            this.topLeft.Divide(arr,d+1);
            this.topRight.Divide(arr,d+1);
            this.bottomLeft.Divide(arr,d+1);
            this.bottomRight.Divide(arr,d+1);

        } else {
            // It's uniform => single color block
            this.isDivided = false;
            MAXdepth = Math.max(this.depth,MAXdepth);
        }
    }
    public boolean overlap(int x1, int y1, int x2, int y2) {
        return (this.z1 < x2 && this.z3 > x1 && this.z2 < y2 && this.z4 > y1);
    }
    public void mask1(int x1, int y1, int x2, int y2, int[][] ans) {
        if (this.isDivided) {
            // Subdivided => pass to children
            this.topLeft.mask1(x1, y1, x2, y2, ans);
            this.topRight.mask1(x1, y1, x2, y2, ans);
            this.bottomLeft.mask1(x1, y1, x2, y2, ans);
            this.bottomRight.mask1(x1, y1, x2, y2, ans);
        } else {
            // Leaf node => if ANY overlap, color entire bounding box black
            if (overlap(x1, y1, x2, y2)) {
                for (int i = z1; i < z3; i++) {
                    for (int j = z2; j < z4; j++) {
                        ans[i][j] = 0x000000; // black
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
        // If not subdivided, we are at a leaf => return depth
        if (!this.isDivided) return depth;

        // If subdivided, figure out which child covers (px,py)
        int midRow = (z1 + z3) / 2; // halfway in rows
        int midCol = (z2 + z4) / 2; // halfway in cols

        // Check if px < midRow => top side, else bottom side
        // and if py < midCol => left side, else right side
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
                // Force alpha=255
                int argb = 0xFF000000 | (data[y][x] & 0xFFFFFF);
                image.setRGB(x, y, argb);
            }
        }
        return image;
    }

    // Helper to convert 0xRRGGBB int to [R,G,B]
    public int[] hexToRgb(int hex) {
        int red   = (hex >> 16) & 0xFF;
        int green = (hex >> 8) & 0xFF;
        int blue  = hex & 0xFF;
        return new int[]{red, green, blue};
    }

    // Helper to combine [R,G,B] into 0xRRGGBB
    public int rgbToHex(int[] rgb) {
        int red   = rgb[0];
        int green = rgb[1];
        int blue  = rgb[2];
        return (red << 16) | (green << 8) | blue;
    }
    public void search1(int x1, int y1, int x2, int y2, int[][] ans, int[][] data) {
        if (this.isDivided) {
            // Subdivided => pass to children
            this.topLeft.search1(x1, y1, x2, y2, ans, data);
            this.topRight.search1(x1, y1, x2, y2, ans, data);
            this.bottomLeft.search1(x1, y1, x2, y2, ans, data);
            this.bottomRight.search1(x1, y1, x2, y2, ans, data);
        } else {
            // Leaf node => if ANY overlap, color entire bounding box black
            if (overlap(x1, y1, x2, y2)) {
                for (int i = z1; i < z3; i++) {
                    for (int j = z2; j < z4; j++) {
                        ans[i][j] = data[i][j]; // black
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
        int step = this.data.length / newSize;  // assume perfect division

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
