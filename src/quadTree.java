//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class quadTree {
    int color;
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

    public quadTree(int[][] image) {
        this.isDivided = true;
        this.data = image;
    }

    public quadTree(int[][] data, int z1, int z2, int z3, int z4) {
        this.data = data;
        this.z1 = z1;
        this.z2 = z2;
        this.z3 = z3;
        this.z4 = z4;
        this.isDivided = true;
    }

    public void Divide() {
        int rows = this.data.length;
        int cols = this.data.length;
        int midRow = rows / 2;
        int midCol = cols / 2;
        int[][][] result = new int[4][][];
        result[0] = new int[midRow][midCol];

        int i;
        int j;
        for(i = 0; i < midRow; ++i) {
            for(j = 0; j < midCol; ++j) {
                result[0][i][j] = this.data[i][j];
            }
        }

        result[1] = new int[midRow][cols - midCol];

        for(i = 0; i < midRow; ++i) {
            for(j = midCol; j < cols; ++j) {
                result[1][i][j - midCol] = this.data[i][j];
            }
        }

        result[2] = new int[rows - midRow][midCol];

        for(i = midRow; i < rows; ++i) {
            for(j = 0; j < midCol; ++j) {
                result[2][i - midRow][j] = this.data[i][j];
            }
        }

        result[3] = new int[rows - midRow][cols - midCol];

        for(i = midRow; i < rows; ++i) {
            for(j = midCol; j < cols; ++j) {
                result[3][i - midRow][j - midCol] = this.data[i][j];
            }
        }

        this.topLeft = new quadTree(result[0], 0, 0, midRow, midCol);
        this.topRight = new quadTree(result[1], midCol, 0, midRow, rows);
        this.bottomLeft = new quadTree(result[2], 0, midRow, cols, midCol);
        this.bottomRight = new quadTree(result[3], midRow, midCol, rows, cols);
        if (!this.checkSolid()) {
            this.topRight.Divide();
            this.topLeft.Divide();
            this.bottomRight.Divide();
            this.bottomLeft.Divide();
        } else {
            this.isDivided = false;
            this.topLeft.isDivided = false;
            this.topRight.isDivided = false;
            this.bottomLeft.isDivided = false;
            this.bottomRight.isDivided = false;
        }

    }

    public boolean checkSolid() {
        int temp = this.data[0][0];

        for(int i = 0; i < this.data.length; ++i) {
            for(int j = 0; j < this.data[i].length; ++j) {
                if (this.data[i][j] != temp) {
                    this.isDivided = true;
                    return false;
                }
            }
        }

        this.color = temp;
        return true;
    }

    public void travis() {
        if (this.isDivided) {
            for(int i = 0; i < this.data.length; ++i) {
                for(int j = 0; j < this.data.length; ++j) {
                    if (this.data[i][j] == 16092482) {
                        System.out.print("O ");
                    }

                    if (this.data[i][j] == 16777215) {
                        System.out.print("W ");
                    }

                    if (this.data[i][j] == 8922032) {
                        System.out.print("P ");
                    }

                    if (this.data[i][j] == 255) {
                        System.out.print("B ");
                    }
                }

                System.out.println();
            }

            this.topLeft.travis();
            this.topRight.travis();
            this.bottomLeft.travis();
            this.bottomRight.travis();
        }

    }

    public int pixelDepth(int px, int py, int depth) {
        if (this.isDivided) {
            if (px <= this.data.length / 2) {
                return py <= this.data.length / 2 ? this.topLeft.pixelDepth(px, py, depth + 1) : this.bottomLeft.pixelDepth(px, py, depth + 1);
            } else {
                return py <= this.data.length / 2 ? this.topRight.pixelDepth(px, py, depth + 1) : this.bottomRight.pixelDepth(px, py, depth + 1);
            }
        } else {
            return depth;
        }
    }

    public BufferedImage convertToImage() {
        int height = this.data.length;
        int width = this.data.length;
        BufferedImage image = new BufferedImage(width, height, 1);

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                image.setRGB(x, y, this.data[y][x]);
            }
        }

        return image;
    }

    public int[] hexToRgb(int hex){
        int red = (hex >> 16) & 0xFF;   // Extract red
        int green = (hex >> 8) & 0xFF;  // Extract green
        int blue = hex & 0xFF;
        int [] rgb = new int[]{red , green , blue};
        return rgb;
    }

    public int rgbToHex (int [] rgb){
        int red = rgb[0];
        int green = rgb[1];
        int blue = rgb[2];
        int hex = (red << 16) | (green << 8) | blue;
        return hex;
    }

    public void mask1(int x1, int y1, int x2, int y2, int[][] ans) {
        if (this.isDivided) {
            this.topLeft.mask1(x1, y1, x2, y2, ans);
            this.topRight.mask1(x1, y1, x2, y2, ans);
            this.bottomLeft.mask1(x1, y1, x2, y2, ans);
            this.bottomRight.mask1(x1, y1, x2, y2, ans);
        } else if (this.z1 >= x1 && this.z1 <= x2 && this.z2 >= y1 && this.z1 <= y2 || this.z3 >= x1 && this.z3 <= x2 && this.z4 >= y1 && this.z4 <= y2) {
            for(int i = this.z1; i < this.z3; ++i) {
                for(int j = this.z2; j < this.z4; ++j) {
                    ans[i][j] = 0;
                }
            }
        }

    }

    public void mask2(int x1, int y1, int x2, int y2, int[][] ans) {
        for(int i = 0; i < ans.length; ++i) {
            for(int j = 0; j < ans.length; ++j) {
                if (i >= x1 && i <= x2 && j >= y1 && j <= y2) {
                    ans[i][j] = 0;
                }
            }
        }
    }

    public quadTree mask(int x1 , int y1 , int x2 , int y2){
        int [][] ans = this.data;
        mask1(x1 , y1 , x2 , y2 , ans);
        mask2(x1 , y1 , x2 , y2 , ans);
        this.data = ans;
        return this;
    }

    public void search1(int x1, int y1, int x2, int y2, int[][] ans, int[][] data) {
        if (this.isDivided) {
            this.topLeft.search1(x1, y1, x2, y2, ans, data);
            this.topRight.search1(x1, y1, x2, y2, ans, data);
            this.bottomLeft.search1(x1, y1, x2, y2, ans, data);
            this.bottomRight.search1(x1, y1, x2, y2, ans, data);
        } else if (this.z1 >= x1 && this.z1 <= x2 && this.z2 >= y1 && this.z1 <= y2 || this.z3 >= x1 && this.z3 <= x2 && this.z4 >= y1 && this.z4 <= y2) {
            for(int i = this.z1; i < this.z3; ++i) {
                for(int j = this.z2; j < this.z4; ++j) {
                    ans[i][j] = data[i][j];
                }
            }
        }

    }

    public void search2(int x1, int y1, int x2, int y2, int[][] ans) {
        for(int i = 0; i < ans.length; ++i) {
            for(int j = 0; j < ans.length; ++j) {
                if (i >= x1 && i <= x2 && j >= y1 && j <= y2) {
                    ans[i][j] = this.data[i][j];
                }
            }
        }
    }

    public quadTree searchSubSpacewithRange(int x1 , int y1 , int x2 , int y2){
        int [][] ans = new int[this.data.length][this.data[0].length];

        search1(x1 , y1 , x2 , y2 , ans , this.data);
        search2(x1 , y1 , x2 , y2 , ans);

        this.data = ans;
        return this;
    }

    public quadTree compress(int newSize) {
        int[][] da = new int[newSize][newSize];
        quadTree q = new quadTree(da);
        int step = this.data.length / newSize;
        for (int i = 0; i < step; i++) {
            for (int j = 0; j < step; j++) {
                int rSum = 0, gSum = 0, bSum = 0;
                for (int k = i * step; k < (i + 1) * step; k++) {
                    for (int l = j * step; l < (j + 1) * step; l++) {
                        int[] rgb = hexToRgb(this.data[k][l]);
                        System.out.println(rgb[0] + " " + rgb[1] + " " + rgb[2]);
                        rSum += rgb[0];
                        gSum += rgb[1];
                        bSum += rgb[2];
                    }
                }
                int rAvg = rSum / (newSize * newSize);
                int gAvg = gSum / (newSize * newSize);
                int bAvg = bSum / (newSize * newSize);
                int [] temp = new int[]{rAvg , gAvg , bAvg};
                q.data[i][j] = rgbToHex(temp);
            }
        }
        return q;
    }
}
