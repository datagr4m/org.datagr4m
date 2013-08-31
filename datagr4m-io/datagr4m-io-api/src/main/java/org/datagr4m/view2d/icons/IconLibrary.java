package org.datagr4m.view2d.icons;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/** A class providing an easy way to read icons from a folder
 * Expected location of images is [root]/data/images/, where
 * default root is ""
 * 
 */
public class IconLibrary {
    protected static Map<String, ImageIcon> iconStore = new HashMap<String, ImageIcon>();
    protected String root = "";
    protected static double SCALING = 0.5;
    protected static double SCALING_LOW = 0.1;
    
    public static IconLibrary lib = new IconLibrary();
        
    /** Load an icon, scale it, and store it in an icon cache. */
    public static ImageIcon getIcon(String url) {
    	return lib.loadIcon(url);
    }
    
	public static void setRoot(String root) {
		lib.setTheRoot(root);
	}


    public static ImageIcon createImageIconAsFile(String path) {
        return lib.createImageIconAsAFile(path);
    }
    
    public static ImageIcon createImageIconAsResource(String path){
		return lib.createImageIconAsAResource(path);
    }
    
    public ImageIcon loadIcon(String url) {
    	String path = root + url;
        if( iconStore.containsKey(path) ){ 
        	return iconStore.get(path);
        } else{
        	//Logger.getLogger(IconLibrary.class).info(url);
	        Image image = new ImageIcon(path).getImage();
	        if (image == null)
	            throw new RuntimeException("missing image " + path);
	        else{
	            try{
        	        BufferedImage bufferedImage = toBufferedImage(image);
        	        BufferedImageOp op = new AffineTransformOp(AffineTransform.getScaleInstance(SCALING, SCALING), new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
                    BufferedImage resize = op.filter(bufferedImage, null);
                    ImageIcon icon = new ImageIcon(resize);
                    //LayeredIcon icon = new LayeredIcon(resize);
                    iconStore.put(path, icon);
                    return icon;
	            }
	            catch(IllegalArgumentException e){
	                File f = new File(".");
	                throw new RuntimeException("Image buffer has wrong arguments, maybe file does not exists: " + path + "\n Currently executing in :" + f.getAbsolutePath());
	            }
	        }
        }
    }
        
    /* PATH TO IMAGES */
    
    /** Return the current root of the icon library.*/
    public String getRoot() {
		return root;
	}

	public void setTheRoot(String root) {
		this.root = root;
	}
    
	public void printCurrentDir() {
        try {
            System.out.println("Current dir : " + new File(".").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /* CONVERSIONS AND LOADING */

    // This method returns a buffered image with the contents of an image
    public BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = true;// hasAlpha(image);

        // Create a buffered image with a format that's compatible with the
        // screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public ImageIcon createImageIconAsAFile(String path) {
        return new ImageIcon(path);
    }
    
    public ImageIcon createImageIconAsAResource(String path){
		return new ImageIcon(IconLibrary.class.getClass().getResource(path));
    }
}
