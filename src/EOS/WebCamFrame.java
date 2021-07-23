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

public class WebCamFrame extends JFrame implements ActionListener,ComponentListener{
	JButton capture;
	JLabel imageHolder;
	VideoFeed videoFeed;
	Image image;
	public WebCamFrame(final Dimension size){
		this.setSize(size);
		init();
		videoFeed=new VideoFeed(this);
		videoFeed.start();
		(new Thread(videoFeed)).start();
		this.addComponentListener(this);
		this.setVisible(true);
	}
	public void componentHidden(ComponentEvent event){
	}
	public void componentShown(ComponentEvent event){
	}
	public void componentClosed(ComponentEvent event){
	}
	public void componentMoved(ComponentEvent event){
	}
	public void componentResized(ComponentEvent event){
	}
	private void init(){
		capture=new JButton("Capture");
		//capture.setPreferredSize(new Dimension(30,25));
		capture.setBorder(new roundBorder.RoundedBorder(25));
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
	public static void main(String $[]){
		SwingUtilities.invokeLater(()->{
			WebCamFrame frame=new WebCamFrame(new Dimension(500,500));
		});
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
	public class VideoFeed implements Runnable{
		private boolean running;
		WebCamFrame frame;
		Webcam webcam;;
		public VideoFeed(WebCamFrame frame){
			this.frame=frame;
			running=false;
		}
		public void start(){
			running=true;
		}
		public void stop(){
			running=false;
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
			}
			webcam.close();
			running=false;
		}
	}
}
