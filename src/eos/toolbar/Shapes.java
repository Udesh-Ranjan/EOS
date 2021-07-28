package eos.toolbar;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.net.URL;

public class Shapes extends JPanel implements ActionListener{
	private JButton circle,rectangle,roundedRect;
	public Shapes(){
		initButtons();
	}
	private void initButtons(){
		System.out.println("InitButtons");
		circle=makeButton("","Circle","Clircle");
		add(circle);
		rectangle=makeButton("","Rectangle","Rectangle");
		add(rectangle);
		roundedRect=makeButton("","Rounded Rectangle","Rounded Rectangle");
		add(roundedRect);
	}
	private JButton makeButton(final String imageLocation,
			final String toolTipText,final String altText){
		final URL imageURL=Shapes.class.getResource(imageLocation);
		final JButton button=new JButton();
		button.setToolTipText(toolTipText);
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(30,30));
		if(imageURL!=null)
			button.setIcon(new ImageIcon(imageURL,altText));
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
