import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Insets;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.LinkedHashSet;

public class MainFrame extends JFrame implements ComponentListener ,KeyListener{
	BufferedImage img;
	String path="/home/dev/Downloads/drk_beaver.jpg";
	LinkedHashSet<Integer>keysPressed;
	public MainFrame(){
		this.setTitle("EOS");
		this.setSize(500,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentListener(this);
		this.addKeyListener(this);
		//loadImage(path);
		loadImageMaintainAspectRatio(path);
		keysPressed=new LinkedHashSet<>();
		this.setVisible(true);
		System.out.println("Control : "+KeyEvent.VK_CONTROL);
		System.out.println("Add : "+KeyEvent.VK_ADD);
		//System.out.println("Title Bar height : "+getInsets().top);
	}
	public int getTitleBarHeight(){
		final Insets insets=getInsets();
		if(insets!=null)
			return insets.top;
		return 0;
	}
	public void loadImage(final String path){
		try{
			img=ImageIO.read(new File(path));
			final Image image=img.getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH);
			img=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
			img.getGraphics().drawImage(image,0,0,this);
		}catch(final IOException ioException){
			ioException.printStackTrace();
		}
		repaint();
	}
	public void loadImageMaintainAspectRatio(final String path){
		try{
			img=ImageIO.read(new File(path));
			final float width=img.getWidth(),height=img.getHeight();
			final float ac=width/height;
			System.out.println("ar: "+ac);
			final int tb=getTitleBarHeight();
			int new_width1=Math.min((int)width,getWidth());
			int new_height1=Math.min((int)height,getHeight()-tb);
			new_width1=(int)(new_height1*ac);
			new_height1=(int)(new_width1*(1/ac));
			int new_width2=Math.min((int)width,getWidth());
			int new_height2=Math.min((int)height,getHeight()-tb);
			new_height2=(int)(new_width2*(1/ac));
			new_width2=(int)(new_height2*ac);
			final int new_width=Math.min(new_width1,new_width2);
			final int new_height=Math.min(new_height1,new_height2);
			final Image image=img.getScaledInstance(new_width,new_height,Image.SCALE_SMOOTH);
			img=new BufferedImage(new_width,new_height,BufferedImage.TYPE_4BYTE_ABGR);
			img.getGraphics().drawImage(image,0,0,this);
			System.out.println("new size : "+new_width+","+new_height);
			//image.dispose();TODO
		}catch(final IOException ioException){
			ioException.printStackTrace();
		}
		repaint();
	}
	public void zoomIn(){
		System.out.println("zoomIn");
	}
	@Override
	public void paint(Graphics g){
		if(img!=null){
			g.clearRect(0,0,getWidth(),getHeight());
			final int tb=getTitleBarHeight();
			g.drawImage(img,getWidth()/2-img.getWidth()/2,getHeight()/2-(img.getHeight()-tb)/2,img.getWidth(),img.getHeight(),this);
		}
	}
	@Override
	public void componentHidden(ComponentEvent event){}
	@Override
	public void componentShown(ComponentEvent event){}
	@Override
	public void componentResized(ComponentEvent event){
		System.out.println("size : "+getWidth()+","+getHeight());
		//loadImage(path);
		loadImageMaintainAspectRatio(path);
	}
	@Override
	public void componentMoved(ComponentEvent event){}
	@Override
	public void keyPressed(KeyEvent event){
		System.out.println("Pressed : "+event.getKeyCode());
		final int KEY=event.getKeyCode();
		keysPressed.add(KEY);
		System.out.println(keysPressed);
		if(KEY==KeyEvent.VK_ADD){
			System.out.println("Add");
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				System.out.println("found");
				zoomIn();
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent event){}
	@Override
	public void keyReleased(KeyEvent event){
		System.out.println("Released : "+event.getKeyCode());
		final int KEY=event.getKeyCode();
		keysPressed.remove(KEY);
		System.out.println(keysPressed);

	}
}
