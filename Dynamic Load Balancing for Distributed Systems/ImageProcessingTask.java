import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcessingTask implements Runnable {
    private String inputImagePath;
    private String outputImagePath;

    public ImageProcessingTask(String inputImagePath, String outputImagePath) {
        this.inputImagePath = inputImagePath;
        this.outputImagePath = outputImagePath;
    }

    @Override
    public void run() {
        System.out.println("Applying Gaussian Blur filter to image: " + inputImagePath);

        try {
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);

            BufferedImage outputImage = applyStrongGaussianBlur(inputImage);

            File outputFile = new File(outputImagePath);
            ImageIO.write(outputImage, "jpg", outputFile);

            System.out.println("Image processing complete. Output saved to: " + outputImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage applyStrongGaussianBlur(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
    
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
        // Define Gaussian Kernel (5x5 for stronger blur)
        double[][] kernel = {
                { 1, 4, 6, 4, 1 },
                { 4, 16, 24, 16, 4 },
                { 6, 24, 36, 24, 6 },
                { 4, 16, 24, 16, 4 },
                { 1, 4, 6, 4, 1 }
        };
    
        int kernelSize = 5;
        int kernelRadius = kernelSize / 2;
    
        // Apply convolution operation
        for (int y = kernelRadius; y < height - kernelRadius; y++) {
            for (int x = kernelRadius; x < width - kernelRadius; x++) {
                double red = 0, green = 0, blue = 0;
    
                for (int i = 0; i < kernelSize; i++) {
                    for (int j = 0; j < kernelSize; j++) {
                        Color color = new Color(inputImage.getRGB(x + j - kernelRadius, y + i - kernelRadius));
                        red += color.getRed() * kernel[i][j];
                        green += color.getGreen() * kernel[i][j];
                        blue += color.getBlue() * kernel[i][j];
                    }
                }
    
                red /= 256.0; // Normalize
                green /= 256.0;
                blue /= 256.0;
    
                int newPixel = (new Color((int) red, (int) green, (int) blue)).getRGB();
                outputImage.setRGB(x, y, newPixel);
            }
        }
    
        return outputImage;
    }
    
}
