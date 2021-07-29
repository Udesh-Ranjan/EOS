package eos.toolbar;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

public class Shapes extends JPanel implements ActionListener{
	private JButton circle,rectangle,roundedRect;
	public Shapes(){
		initButtons();
	}
	private void initButtons(){
		System.out.println("InitButtons");
		System.out.println("present working dir : "+System.getProperty("user.dir"));
		circle=makeButton("icons/circle.png","Circle","Clircle");
		add(circle);
		rectangle=makeButton("icons/rectangle.png","Rectangle","Rectangle");
		add(rectangle);
		roundedRect=makeButton("icons/rounded-rectangle.png","Rounded Rectangle","Rounded Rectangle");
		add(roundedRect);
	}
	private JButton makeButton(final String imageLocation,
			final String toolTipText,final String altText){
		File file=new File(imageLocation);
		URL imageURL=null;
		//	try{
				imageURL=eos.MainClass.class.getResource(imageLocation);
		//}catch(MalformedURLException exception){
		//		exception.printStackTrace();
		//	}
		
	//	InputStream in=getClass().getResourceAsStream(imageLocation);
		//if(in==null){
	//		System.out.println("path not found : "+imageLocation);
	//	}
		final JButton button=new JButton();
		button.setToolTipText(toolTipText);
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(100,100));
		if(imageURL!=null){
			//try{
				button.setIcon(new ImageIcon(imageURL,altText));
			//}catch(IOException exception){
		//		exception.printStackTrace();
				//button.setText(altText);
			}
		else {
			button.setText(altText);
			System.err.println("Resource not found : "+imageLocation);
		}
		return button;
	}
	public void actionPerformed(final ActionEvent event){
		final Object src=event.getSource();
		if(src==circle){
			drawCircle();
		}
		if(src==rectangle){
			drawRectangle();
		}
		if(src==roundedRect){
			drawRoundedRect();
		}
	}
	public void drawCircle(){
		System.out.println("draw Circle");
	}
	public void drawRectangle(){
		System.out.println("draw Rectangle");
	}
	public void drawRoundedRect(){
		System.out.println("draw RoundedRect");
	}

}
