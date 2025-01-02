import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

public class Main {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\Sazgar\\IdeaProjects\\DSFinalProject2\\src\\image4_RGB.csv";
        BufferedImage bfi = csvToImage(path,"C:\\Users\\Sazgar\\IdeaProjects\\DSFinalProject2\\src\\image4_RGB.png");
        String videoPath = "C:\\Users\\Sazgar\\IdeaProjects\\DSFinalProject2\\src\\vid1.mov";
//        videoToImage(videoPath);
//        processImages();
        imageToVideo();
        int [][] array = imageToHexArray("C:\\Users\\Sazgar\\IdeaProjects\\DSFinalProject2\\src\\image4_RGB.png" , 256 , 256);
        int[][] array2 = new int[][]{
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2, 0xf58d42, 0xf58d42, 0x36c4cf, 0xcf36c2},
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xFFFFFF, 0x8823b0, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF},
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xcf36c2, 0xFFFFFF, 0x8823b0, 0x8823b0, 0x36c4cf, 0xf58d42, 0x8823b0, 0x36c4cf},
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xFFFFFF, 0x8823b0, 0xcf36c2, 0xf58d42, 0x36c4cf, 0x8823b0, 0x0000FF},
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2},
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xcf36c2, 0xFFFFFF, 0x8823b0, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2},
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xFFFFFF, 0x8823b0, 0x8823b0, 0x36c4cf, 0xf58d42, 0x36c4cf, 0x0000FF},
                {0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xf58d42, 0xFFFFFF, 0x8823b0, 0xcf36c2, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF},
                {0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2, 0xf58d42, 0xf58d42, 0x36c4cf, 0xcf36c2, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2, 0xf58d42, 0xf58d42, 0x36c4cf, 0xcf36c2},
                {0xf58d42, 0xf58d42, 0xFFFFFF, 0x8823b0, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF, 0xf58d42, 0xf58d42, 0xFFFFFF, 0x8823b0, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF},
                {0xcf36c2, 0xFFFFFF, 0x8823b0, 0x8823b0, 0x36c4cf, 0xf58d42, 0x8823b0, 0x36c4cf, 0xcf36c2, 0xFFFFFF, 0x8823b0, 0x8823b0, 0x36c4cf, 0xf58d42, 0x8823b0, 0x36c4cf},
                {0xf58d42, 0xFFFFFF, 0x8823b0, 0xcf36c2, 0xf58d42, 0x36c4cf, 0x8823b0, 0x0000FF, 0xf58d42, 0xFFFFFF, 0x8823b0, 0xcf36c2, 0xf58d42, 0x36c4cf, 0x8823b0, 0x0000FF},
                {0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2},
                {0xf58d42, 0xcf36c2, 0xFFFFFF, 0x8823b0, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2, 0xf58d42, 0xcf36c2, 0xFFFFFF, 0x8823b0, 0xf58d42, 0xf58d42, 0x8823b0, 0xcf36c2},
                {0xf58d42, 0xFFFFFF, 0x8823b0, 0x8823b0, 0x36c4cf, 0xf58d42, 0x36c4cf, 0x0000FF, 0xf58d42, 0xFFFFFF, 0x8823b0, 0x8823b0, 0x36c4cf, 0xf58d42, 0x36c4cf, 0x0000FF},
                {0xf58d42, 0xFFFFFF, 0x8823b0, 0xcf36c2, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF, 0xf58d42, 0xFFFFFF, 0x8823b0, 0xcf36c2, 0xf58d42, 0xf58d42, 0x8823b0, 0x0000FF}
        };

        quadTree q = new quadTree(array);
//        q.Divide(array,0);
//        System.out.println(q.treeDepth());
        int[][] ans = q.data;
//        q.travis();
//        q.mask(0,2,100,125);
//        q.search(100,100,128,128);
//        quadTree temp = q.compress(1);
        BufferedImage image = q.convertToImage();
        try {
            ImageIO.write(image, "PNG", new File("output_image.png"));
            System.out.println("Image saved successfully!");
        } catch (IOException var6) {
            System.out.println("Error saving image: " + var6.getMessage());
        }
    }
        public static int[][] imageToHexArray(String imagePath, int width, int height) throws IOException {
            BufferedImage img = ImageIO.read(new File(imagePath));
            BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            resizedImg.getGraphics().drawImage(img, 0, 0, width, height, null);
            int[][] hexArray = new int[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(resizedImg.getRGB(x, y));
                    int hexColor = (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
                    hexArray[y][x] = hexColor;
                }
            }
            return hexArray;
        }

    public static BufferedImage csvToImage(String path, String output){
        String csvFilePath = path;
        String outputImagePath = output;
        int width = 256;
        int height = 256;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine();
            String line;
            int row = 0;
            int yy = 0;
            line = br.readLine();
            while (row < height) {
                String[] rgbValues = line.split(",");
                for (int col = 0; col < width*3; col+=3) {
                    String[] redd = rgbValues[yy].trim().split(" ");
                    String[] bluee = rgbValues[yy+1].trim().split(" ");
                    String[] greenn = rgbValues[yy+2].trim().split(" ");
                    yy+=3;

                    int red = Integer.parseInt(redd[0].replaceAll("\"", ""));
                    int green = Integer.parseInt(bluee[0].replaceAll("\"", ""));
                    int blue = Integer.parseInt(greenn[0].replaceAll("\"", ""));

                    int pixelValue = (red << 16) | (green << 8) | blue;
                    image.setRGB(col/3, row, pixelValue);
                }
                row++;
            }
            ImageIO.write(image, "png", new File(outputImagePath));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void Video(String videoPath) throws IOException {
        // Path to the input video file

        // Directory to save the compressed frames

        // Path to save the compressed video
        String outputVideoPath = "compressed_video.mp4";

        // Open the video file
        VideoCapture videoCapture = new VideoCapture(videoPath);

        if (!videoCapture.isOpened()) {
            System.out.println("Error: Could not open video file.");
            return;
        }

        int frameWidth = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        int fps = (int) videoCapture.get(Videoio.CAP_PROP_FPS);

        // Create a VideoWriter to save the output video
        VideoWriter videoWriter = new VideoWriter(outputVideoPath, VideoWriter.fourcc('M', 'J', 'P', 'G'), fps, new Size(frameWidth, frameHeight));

        Mat frame = new Mat();
        int frameNumber = 0;

        while (videoCapture.read(frame)) {
            // Path to the compressed PNG frame
            String compressedFramePath = "compressed_frame_" + frameNumber + ".png";
            Mat compressedFrame = Imgcodecs.imread(compressedFramePath);

            int [][] videoArray = imageToHexArray(compressedFramePath , 1024 , 1024);
            quadTree videoQuad = new quadTree(videoArray);
            quadTree compressedQuad = videoQuad.compress(256);
            BufferedImage compressedImage = compressedQuad.convertToImage();
            // Read the compressed frame\

//            Mat compressedFrame = bufferedImageToMat(compressedImage);

            // Write the compressed frame to the video
            videoWriter.write(compressedFrame);

            System.out.println("Frame " + frameNumber + " processed and added to video.");
            frameNumber++;
        }

        // Release the video capture and writer objects
        videoCapture.release();
        videoWriter.release();
        System.out.println("Video processing and compression complete.");
    }

    private static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data); return mat;
    }

    public static void videoToImage(String videoPath) {
        // Path to the directory to save frames
        String frameDirectory = "images/";

        // Create the directory if it doesn't exist
        File directory = new File(frameDirectory);
        if (!directory.exists()) {
            directory.mkdir();
            System.out.println("Directory created: " + frameDirectory);
        }

        // Path to save frames
        String framePath = frameDirectory + "output_frame_";

        // Open the video file
        VideoCapture videoCapture = new VideoCapture(videoPath);

        if (!videoCapture.isOpened()) {
            System.out.println("Error: Could not open video file.");
            return;
        }

        Mat frame = new Mat();
        int frameNumber = 0;

        while (videoCapture.read(frame)) {
            // Save the frame as an image
            String fileName = framePath + frameNumber + ".png";
            Imgcodecs.imwrite(fileName, frame);
            System.out.println("Saved: " + fileName);
            frameNumber++;
        }

        // Release the video capture object
        videoCapture.release();
        System.out.println("Video processing complete.");
    }

    public static void processImages() {
        // Directory containing original frames
        String frameDirectory = "images/";
        // Directory to save compressed frames
        String compressedFrameDirectory = "compressedImages/";

        // Create the directory if it doesn't exist
        File compressedDirectory = new File(compressedFrameDirectory);
        if (!compressedDirectory.exists()) {
            compressedDirectory.mkdir();
            System.out.println("Directory created: " + compressedFrameDirectory);
        }

        // Process each image in the images directory
        File directory = new File(frameDirectory);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files == null) {
            System.out.println("No images found in the directory.");
            return;
        }

        for (File file : files) {
            try {
                String compressedFilePath = compressedFrameDirectory + file.getName();
                // Apply compression
                int [][] array = imageToHexArray(file.getAbsolutePath() , 1024 , 1024);
                quadTree q = new quadTree(array);
                quadTree temp = q.compress(256);
                BufferedImage bufferedImage = temp.convertToImage();

                String compressedFileName = compressedFrameDirectory + file.getName();
                ImageIO.write(bufferedImage, "png", new File(compressedFileName));
                System.out.println("Compressed and saved: " + compressedFilePath);
            } catch (IOException e) {
                System.out.println("Error processing file: " + file.getName());
                e.printStackTrace();
            }
        }

        System.out.println("Image compression complete.");
    }

    public static void imageToVideo(){
        // Directory containing compressed frames
        String compressedFrameDirectory = "compressedImages/";
        // Path to save the output video
        String outputVideoPath = "compressed_video.mov";

        // Initialize video properties (you may need to adjust width, height, and fps)
        int frameWidth = 256;  // Adjust based on your frame size
        int frameHeight = 256; // Adjust based on your frame size
        int fps = 30;           // Adjust based on your video fps

        // Create a VideoWriter to save the output video
        VideoWriter videoWriter = new VideoWriter(outputVideoPath, VideoWriter.fourcc('m', 'p', '4', 'v'), fps, new Size(frameWidth, frameHeight));

        if (!videoWriter.isOpened()) {
            System.out.println("Error: Could not open video writer.");
            return;
        }

        int frameNumber = 0;
        while (true) {
            // Path to the compressed PNG frame
            String compressedFramePath = compressedFrameDirectory + "output_frame_" + frameNumber + ".png";
            File file = new File(compressedFramePath);

            if (!file.exists()) {
                System.out.println("No more frames to read. Stopping.");
                break;
            }

            // Read the compressed frame
            Mat compressedFrame = Imgcodecs.imread(compressedFramePath);
            if (compressedFrame.empty()) {
                System.out.println("Error: Could not read image from " + compressedFramePath);
                break;
            }

            // Debugging: Check if the frame is correctly read
            System.out.println("Reading frame: " + compressedFramePath + " Size: " + compressedFrame.size());

            // Write the compressed frame to the video
            videoWriter.write(compressedFrame);

            // Debugging: Confirm frame is added to video
            System.out.println("Frame " + frameNumber + " added to video.");
            frameNumber++;
        }

        // Release the video writer object
        videoWriter.release();
        System.out.println("Video creation complete.");
    }
}
