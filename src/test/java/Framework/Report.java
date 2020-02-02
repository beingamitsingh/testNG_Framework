package Framework;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;

public class Report {

    private static void pass(String message)   {
       Assert.assertTrue(true, message);
    }
    private static void fail(String message)    { Assert.fail(message); }


    private static String capture(WebDriver driver, String screenShotName) {
        String dest = null;
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File(Config.getProperty("reportFolderPath") +
                    "\\CurrentScreenshot.png"));

        } catch (Exception e) {
            e.getMessage();
            System.out.println(e.getMessage());
        }
        return dest;
    }
}