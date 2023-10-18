import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageRotationTask implements Runnable {
    private String inputImagePath;
    private String outputImagePath;
    private double angle;

    public ImageRotationTask(String inputImagePath, String outputImagePath, double angle) {
        this.inputImagePath = inputImagePath;
        this.outputImagePath = outputImagePath;
        this.angle = angle;
    }

    @Override
    public void run() {
        System.out.println("Rotating image: " + inputImagePath);
        try {
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);

            if (inputImage == null) {
                System.err.println("Error reading image: " + inputImagePath);
                return;
            }

            BufferedImage outputImage = rotateImage(inputImage, angle);

            File outputFile = new File(outputImagePath);
            ImageIO.write(outputImage, "jpg", outputFile);

            System.out.println("Image rotation complete. Output saved to: " + outputImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage rotateImage(BufferedImage inputImage, double angle) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        Graphics2D graphics = outputImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);
        graphics.setTransform(transform);
        graphics.drawImage(inputImage, 0, 0, null);
        graphics.dispose();

        return outputImage;
    }
}
