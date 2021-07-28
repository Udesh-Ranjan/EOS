package eos.imageUtils;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;

public class ImageUtils{
	public static BufferedImage toBufferedImage(final Image image){
		if(image instanceof BufferedImage)
			return (BufferedImage)image;
		BufferedImage bimage=new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=(Graphics2D)bimage.createGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
		return bimage;
	}
	public static BufferedImage rotateImageByDegrees(final BufferedImage img,final double angle) {
		final double rads = Math.toRadians(angle);
		final double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		final int w = img.getWidth();
		final int h = img.getHeight();
		final int newWidth = (int) Math.floor(w * cos + h * sin);
		final int newHeight = (int) Math.floor(h * cos + w * sin);

		final BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2d = rotated.createGraphics();
		final AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		final int x = w / 2;
		final int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, null);
		//g2d.setColor(Color.RED);
		//g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
		g2d.dispose();

		return rotated;
	}
}
