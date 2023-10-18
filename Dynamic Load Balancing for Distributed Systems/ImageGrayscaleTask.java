import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageGrayscaleTask implements Runnable {
    private String inputImagePath;
    private String outputImagePath;

    public ImageGrayscaleTask(String inputImagePath, String outputImagePath) {
        this.inputImagePath = inputImagePath;
        this.outputImagePath = outputImagePath;
    }

    @Override
    public void run() {
        System.out.println("Converting image to grayscale: " + inputImagePath);
        try {
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);

            if (inputImage == null) {
                System.err.println("Error reading image: " + inputImagePath);
                return;
            }

            BufferedImage outputImage = convertToGrayscale(inputImage);

            File outputFile = new File(outputImagePath);
            ImageIO.write(outputImage, "jpg", outputFile);

            System.out.println("Image grayscale conversion complete. Output saved to: " + outputImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage convertToGrayscale(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(inputImage.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int grayValue = (red + green + blue) / 3;
                Color grayColor = new Color(grayValue, grayValue, grayValue);
                outputImage.setRGB(x, y, grayColor.getRGB());
            }
        }

        return outputImage;
    }
}
