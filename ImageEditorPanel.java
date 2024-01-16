import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ImageEditorPanel extends JPanel implements KeyListener {

    Color[][] pixels;
   
    boolean quit = false;
    boolean isFlipVert = false;
    boolean isGray = false;
    
    public ImageEditorPanel() {
        BufferedImage imageIn = null;
        try {
            // the image should be in the main project folder, not in \src or \bin
            imageIn = ImageIO.read(new File("The-best-nature-photography-collection.jpg"));
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        pixels = makeColorArray(imageIn);
       
        setPreferredSize(new Dimension(pixels[0].length, pixels.length));
        
        setBackground(Color.BLACK);
        addKeyListener(this);
        
    }

    public void paintComponent(Graphics g) {
        // paints the array pixels onto the screen
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col]);
                g.fillRect(col, row, 1, 1);
            }
        }
        
    }

    public void run() {
        // call your image-processing methods here OR call them from keyboard event
        // handling methods
        // write image-processing methods as pure functions - for example: pixels =
        //pixels = flipHorizontal(pixels);
       // pixels = flipVertical(pixels);
        //pixels = grayScale(pixels);
        //pixels = blur(pixels);
        //pixels = colorInvert(pixels);
        //pixels = posterize(pixels);
        //pixels = contrast(pixels);
        repaint();
    }

    public static Color[][] contrast(Color[][] pixels){
        Color[][] newPix = new Color[pixels.length][pixels[0].length];
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                Color temp = pixels[row][col];
                int red = temp.getRed();
                int green = temp.getGreen();
                int blue = temp.getBlue();
                double maxContrastFactor = 1.1;
                double minContrastFactor = .9;

                if(red > 128){
                    red =(int)Math.min(255, red * maxContrastFactor);
                }else{
                    red =(int)Math.max(0, red * minContrastFactor);
                }

                if(green > 128){
                    green =(int)Math.min(255, green * maxContrastFactor);
                }else{
                    green =(int)Math.max(0, green * minContrastFactor);
                }

                if(blue > 128){
                    blue =(int)Math.min(255, blue * maxContrastFactor);
                }else{
                    blue =(int)Math.max(0, blue * minContrastFactor);
                }

                newPix[row][col] = new Color(red, green, blue);
            }
        }
        return newPix;
    }

    public static Color[][] posterize(Color[][] pixels){
         Color[][] newPix = new Color[pixels.length][pixels[0].length];
         Color gray = new Color(52,52,52);
         Color darkBlue = new Color(47, 48, 97);
         Color yellow = new Color(255, 230, 109);
         Color lightBlue = new Color(108, 166, 193);
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                Color temp = pixels[row][col];
                int red = temp.getRed();
                int green = temp.getGreen();
                int blue = temp.getBlue();
                double dist1 = findDist(red, 52, green, 52, blue, 52);
                double dist2 = findDist(red, 47, green, 48, blue, 97);
                double dist3 = findDist(red, 255, green, 230, blue, 109);
                double dist4 = findDist(red, 108, green, 166, blue, 193);

                if(dist1 < dist2 && dist1 < dist3 && dist1 < dist4){
                    newPix[row][col] = new Color(gray.getRed(), gray.getGreen(), gray.getBlue());
                }else if(dist2 < dist3 && dist2 < dist4 && dist2 < dist1){
                    newPix[row][col] = new Color(darkBlue.getRed(), darkBlue.getGreen(), darkBlue.getBlue());
                }else if(dist3 < dist1 && dist3 < dist2 && dist3 < dist4){
                    newPix[row][col] = new Color(yellow.getRed(), yellow.getGreen(), yellow.getBlue());
                }else if (dist4 < dist1 && dist4 < dist2 && dist4 < dist3){
                    newPix[row][col] = new Color(lightBlue.getRed(), lightBlue.getGreen(), lightBlue.getBlue());
                }
            }
        }
        return newPix;
    }

    public static double findDist(int x1,int x2,int y1,int y2,int z1,int z2){
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2));
    }

    public static Color[][] colorInvert(Color[][] pixels){
        Color[][] newPix = new Color[pixels.length][pixels[0].length];
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                Color temp = pixels[row][col];
                int maxRGB = 255;
                int red = temp.getRed();
                int green = temp.getGreen();
                int blue = temp.getBlue();
                newPix[row][col] = new Color(maxRGB - red, maxRGB - green, maxRGB - blue);
            }
        }
        return newPix;
    }

    public static Color[][] blur(Color[][] pixels){
        int neighborSize = 2;
        
        Color[][] newPix = new Color[pixels.length][pixels[0].length];
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                // at target square
                int[] totalArr = {0,0,0};
                int numPixels = 0;
                for (int r = row - neighborSize; r <= row + neighborSize; r++) {
                    for (int c = col - neighborSize; c <= col + neighborSize; c++) {
                        if((r >= 0 && r < pixels.length) && (c >= 0 && c < pixels[row].length)){
                            numPixels++;
                            Color temp = pixels[r][c];
                            totalArr[0] += temp.getRed();
                            totalArr[1] += temp.getGreen();
                            totalArr[2] += temp.getBlue();

                        }
                    }
                    
                }
                newPix[row][col] = new Color(totalArr[0]/numPixels, totalArr[1]/numPixels, totalArr[2]/numPixels);
                
                
            }
        }
        return newPix;
    }

    public static Color[][] grayScale(Color[][] pixels){
        Color[][] newPix = new Color[pixels.length][pixels[0].length];
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                Color temp = pixels[row][col];  
                int gray = (temp.getRed() + temp.getGreen() + temp.getBlue()) / 3;
                newPix[row][col]= new Color(gray,gray,gray);
            }
        }
        return newPix;
    }

    public static Color[][] flipHorizontal(Color[][] pixels){
        Color[][] newPix = new Color[pixels.length][pixels[0].length];
        for (int row = 0; row < newPix.length; row++) {
            for (int col = 0 ; col < newPix[row].length; col++) {
                newPix[row][col] = pixels[row][pixels[row].length - col - 1];
            }
            
        }
        return newPix;
    }
    
    public static Color[][] flipVertical(Color[][] pixels){
         Color[][] newPix = new Color[pixels.length][pixels[0].length];
        for (int row = 0; row < newPix.length; row++) {
            for (int col = 0 ; col < newPix[row].length; col++) {
                newPix[row][col] = pixels[pixels.length - row - 1][col];
            }
            
        }
        return newPix;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
           pixels = flipHorizontal(pixels);
           repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && !isFlipVert){
            pixels = flipVertical(pixels);
            isFlipVert = true;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && isFlipVert){
            pixels = flipVertical(pixels);
            isFlipVert = false;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_G && !isGray){
            pixels = grayScale(pixels);
            isGray = true;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_B){
            pixels = blur(pixels);
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_I){
            pixels = colorInvert(pixels);
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_C){
            pixels = contrast(pixels);
            repaint();
        }
        
       
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // call event handling methods
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // note the difference between getKeyChar and getKeyCode
        if (e.getKeyChar() == 'q')
        quit = true;
    }

    public Color[][] makeColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] result = new Color[height][width];
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        // System.out.println("Loaded image: width: " +width + " height: " + height);
        return result;
    }
}

