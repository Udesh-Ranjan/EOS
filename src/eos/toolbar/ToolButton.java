package eos.toolbar;

import javax.swing.JButton;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
/**
 * Component to be used as button in ToolBox
 * @author : Dev Parzival
 * @date : 29th March 2021
 */
public class ToolButton extends JButton {
	BufferedImage image;	
	Dimension size;

	final String label;
	public ToolButton(final String pathToImage,final String label,final Dimension size,
			final String toolTipText,final ActionListener actionListener){
		this.label=label;
		this.size=size;
		setPreferredSize(size);
		setToolTipText(toolTipText);
		//Make the button took same
		setUI(new BasicButtonUI());
		//make it transparent
		setContentAreaFilled(true);
		//setBorder(BorderFactory.createEtchedBorder());
		//setBorderPainted(true);
		//setRolloverEnabled(true);
		addActionListener(actionListener);
		File file=new File(pathToImage);
		if(file.exists() && file.isFile()){
			//instantiate the buffered image
			//resize the image to the size of buffered image
			BufferedImage img=null;
			try{
				img=ImageIO.read(file);
				Image _image=img.getScaledInstance((int)size.getWidth(),(int)size.getHeight(),Image.SCALE_SMOOTH);
				image=new BufferedImage((int)size.getWidth(),(int)size.getHeight(),BufferedImage.TYPE_INT_ARGB);
				final Graphics2D g=(Graphics2D)image.createGraphics();
				g.drawImage(_image,0,0,this);
				g.dispose();
			}catch (final IOException exception){
				exception.printStackTrace();
				setText(label);	
			}
		}else{
			System.err.println("path to the image is not found : "+pathToImage);
			setText(label);	
		}
		setVisible(true);
		setFocusable(true);
	}
	//we dont want to update UI
	public void updateUI(){}
	//paint the image
	protected void paintComponent(Graphics g){
		System.out.println("paintComponent button ");
		super.paintComponent(g);
		if(image!=null){
			g.drawImage(image,0,0,null);
		}
		else System.err.println("image is null can't paint : "+label);
	}
}	
