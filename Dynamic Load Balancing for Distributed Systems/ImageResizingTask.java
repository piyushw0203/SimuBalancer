import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageResizingTask implements Runnable {
    private String inputImagePath;
    private String outputImagePath;
    private int newWidth;
    private int newHeight;

    public ImageResizingTask(String inputImagePath, String outputImagePath, int newWidth, int newHeight) {
        this.inputImagePath = inputImagePath;
        this.outputImagePath = outputImagePath;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void run() {
        System.out.println("Resizing image: " + inputImagePath);
        try {
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);

            if (inputImage == null) {
                System.err.println("Error reading image: " + inputImagePath);
                return;
            }

            BufferedImage outputImage = resizeImage(inputImage, newWidth, newHeight);

            File outputFile = new File(outputImagePath);
            ImageIO.write(outputImage, "jpg", outputFile);

            System.out.println("Image resizing complete. Output saved to: " + outputImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resizeImage(BufferedImage inputImage, int newWidth, int newHeight) {
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return outputImage;
    }
}
