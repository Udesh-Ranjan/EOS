package eos;

import  javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Dimension;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import eos.roundBorder.RoundedBorder;

public class WebCamFrame extends JFrame implements ActionListener,WindowListener{
	private JButton capture;
	private JLabel imageHolder;
	private final VideoFeed videoFeed;
	private Image image;
	private Thread thread;
	private MainFrame mainFrame;
	public WebCamFrame(final Dimension size){
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(size);
		init();
		videoFeed=new VideoFeed(this);
		//this.addComponentListener(this);
		this.addWindowListener(this);
		start();
		this.setVisible(true);
	}
	public WebCamFrame(final Dimension size,final MainFrame mainFrame){
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame=mainFrame;
		this.setSize(size);
		init();
		videoFeed=new VideoFeed(this);
		//this.addComponentListener(this);
		this.addWindowListener(this);
		start();
		this.setVisible(true);
	}
	public void windowActivated(WindowEvent event){}
	public void windowClosed(WindowEvent event){
		System.out.println("WebCamFrame window closed");
		//stop();
	}
	public void windowClosing(WindowEvent event){
		System.out.println("WebCamFrame window closing");
		stop();
		this.setVisible(false);
		this.dispose();
		if(mainFrame!=null)
			mainFrame.setCapturedImage(getCapturedImage());
	}
	public void windowDeactivated(WindowEvent event){}
	public void windowDeiconified(WindowEvent event){}
	public void windowIconified(WindowEvent event){}
	public void windowOpened(WindowEvent event){}

	public void start(){
		if(!videoFeed.isRunning()){
			thread=new Thread(videoFeed);
			videoFeed.start();
			thread.start();
		}else System.out.println("error videoFeed instance is already running call stop and then start");
	}
	public void stop(){
		videoFeed.stop();
	}
	private void init(){
		capture=new JButton("Capture");
		//capture.setPreferredSize(new Dimension(30,25));
		capture.setBorder(new RoundedBorder(25));
		capture.setForeground(Color.black);
		//capture.setBackground(Color.RED);
		JPanel panel=new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(capture);
		imageHolder=new JLabel();
		imageHolder.setHorizontalAlignment(JLabel.CENTER);
		capture.addActionListener(this);
		setLayout(new BorderLayout());
		this.add(imageHolder,BorderLayout.CENTER);
		this.add(panel,BorderLayout.SOUTH);
	}
	public void updateImage(final Image image){
		imageHolder.setIcon(new ImageIcon(image));
	}
	public void actionPerformed(ActionEvent event){
		if(event.getSource()==capture){
			System.out.println("capture button pressed");
			image=videoFeed.getImage();
		}
	}
	public Image getCapturedImage(){
		return image;
	}
	private class VideoFeed implements Runnable{
		private volatile boolean running;
		private WebCamFrame frame;
		private Webcam webcam;
		public VideoFeed(WebCamFrame frame){
			this.frame=frame;
			running=false;
		}
		public void start(){
			running=true;
		}
		public void stop(){
			running=false;
			System.out.println("videoFeed running is turned as false");
		}
		public boolean isRunning(){
			return running;
		}
		public Image getImage(){
			return webcam.getImage();
		}
		public void run(){
			webcam=Webcam.getDefault();
			for(Dimension size:webcam.getViewSizes())
				System.out.println("supported size : "+size);
			webcam.setViewSize(WebcamResolution.VGA.getSize());
			webcam.open();
			while(running){
				final Image image=webcam.getImage();
				frame.updateImage(image);
				try{
					Thread.sleep(20);
				}catch(final InterruptedException exception){
					exception.printStackTrace();
				}
				catch(Exception exc){
					exc.printStackTrace();
				}
			}
			webcam.close();
			System.out.println("webcam is closed");
			running=false;
		}
	}
	public static void main(String $[]){
		SwingUtilities.invokeLater(()->{
			WebCamFrame frame=new WebCamFrame(new Dimension(500,500));
		});
	}
}
