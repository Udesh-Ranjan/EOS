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
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.Color;

public class MainFrame extends JFrame implements ComponentListener ,KeyListener{
	BufferedImage img;
	InputStream stream;
	//String path="/home/dev/Downloads/drk_beaver.jpg";
	String path;
	LinkedHashSet<Integer>keysPressed;
	double zoom;
	final double incr=1.2;
	boolean fileNoFound=false;
	double rotation;
	int move=5;//pixels
	int hor,ver;
	public MainFrame(final String path){
		this.path=path;
		this.setTitle("EOS");
		this.setSize(500,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentListener(this);
		this.addKeyListener(this);
		//loadImage(path);
		zoom=1;
		rotation=0;
		hor=ver=0;
		if(pictureExists(path))
			loadImageMaintainAspectRatio(path);
		else System.out.println(path+" not found");
		keysPressed=new LinkedHashSet<>();
		this.setVisible(true);
		System.out.println("Control : "+KeyEvent.VK_CONTROL);
		System.out.println("Add : "+KeyEvent.VK_ADD);
		//System.out.println("Title Bar height : "+getInsets().top);
	}
	public static boolean pictureExists(final String path){
		File file=new File(path);
		return file.exists() && file.isFile();

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
			if(img!=null){
				img.getGraphics().dispose();
				stream.close();
				img=null;
			}
			stream=new FileInputStream(path);
			img=ImageIO.read(stream);
			final float width=img.getWidth(),height=img.getHeight();
			//img.getGraphics().dispose();
			//stream.close();
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
			int new_width=Math.min(new_width1,new_width2);
			int new_height=Math.min(new_height1,new_height2);
			final Image image=img.getScaledInstance(new_width,new_height,Image.SCALE_SMOOTH);
			img.getGraphics().dispose();
			stream.close();
			img.flush();
			img=new BufferedImage(new_width,new_height,BufferedImage.TYPE_4BYTE_ABGR);
			img.getGraphics().drawImage(image,0,0,this);
			System.out.println("new size : "+new_width+","+new_height);
			//image.dispose();TODO
		}catch(final IOException ioException){
			ioException.printStackTrace();
		}
		repaint();
	}

	public void loadImageMaintainAspectRatioAndZoom(final String path){
		try{
			if(img!=null){
				img.getGraphics().dispose();
				stream.close();
				img.flush();
				img=null;
			}
			stream=new FileInputStream(path);
			final BufferedImage _img=ImageIO.read(stream);
			final float width=_img.getWidth(),height=_img.getHeight();
			//img.getGraphics().dispose();
			//stream.close();
			//img.flush();
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
			int new_width=Math.min(new_width1,new_width2);
			int new_height=Math.min(new_height1,new_height2);
			new_width=(int)(zoom*new_width);
			new_height=(int)(zoom*new_height);
			if(new_width <=0 || new_height<=0){
				System.out.println("Cannot zoomOut image is too small");
				_img.getGraphics().dispose();
				stream.close();
				_img.flush();
				return;
			}
			final Image image=_img.getScaledInstance(new_width,new_height,Image.SCALE_SMOOTH);
			_img.getGraphics().dispose();
			stream.close();
			_img.flush();
			img=new BufferedImage(new_width,new_height,BufferedImage.TYPE_4BYTE_ABGR);
			img.getGraphics().drawImage(image,0,0,this);
			//image.getGraphics().dispose();
			System.out.println("new size : "+new_width+","+new_height);
			//image.dispose();TODO
		}catch(final IOException ioException){
			ioException.printStackTrace();
		}
		repaint();
	}
	public void loadImageMaintainAspectRatioZoomAndRotate(final String path){
		try{
			if(img!=null){
				img.getGraphics().dispose();
				stream.close();
				img.flush();
				img=null;
			}
			stream=new FileInputStream(path);
			final BufferedImage _img=ImageIO.read(stream);
			final float width=_img.getWidth(),height=_img.getHeight();
			//img.getGraphics().dispose();
			//stream.close();
			//img.flush();
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
			int new_width=Math.min(new_width1,new_width2);
			int new_height=Math.min(new_height1,new_height2);
			new_width=(int)(zoom*new_width);
			new_height=(int)(zoom*new_height);
			if(new_width <=0 || new_height<=0){
				System.out.println("Cannot zoomOut image is too small");
				_img.getGraphics().dispose();
				stream.close();
				_img.flush();
				return;
			}
			final Image image=_img.getScaledInstance(new_width,new_height,Image.SCALE_SMOOTH);
			_img.getGraphics().dispose();
			stream.close();
			_img.flush();
			final BufferedImage scaledImg=new BufferedImage(new_width,new_height,BufferedImage.TYPE_4BYTE_ABGR);
			scaledImg.getGraphics().drawImage(image,0,0,this);
			img=rotateImageByDegrees(scaledImg,rotation);
			scaledImg.getGraphics().dispose();
			scaledImg.flush();
			//image.getGraphics().dispose();
			System.out.println("new size : "+new_width+","+new_height);
			//image.dispose();TODO
		}catch(final IOException ioException){
			ioException.printStackTrace();
		}
		repaint();
	}
	public BufferedImage rotateImageByDegrees(final BufferedImage img,final double angle) {
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
		g2d.drawImage(img, 0, 0, this);
		//g2d.setColor(Color.RED);
		//g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
		g2d.dispose();

		return rotated;
	}
	public void zoomIn(){
		System.out.println("zoomIn");
		zoom=zoom*incr;
		loadImageMaintainAspectRatioAndZoom(path);
		if(pictureExists(path))
			loadImageMaintainAspectRatioZoomAndRotate(path);
		else System.out.println(path+" not found");

	}
	public void zoomOut(){
		System.out.println("zoomOut");
		zoom=zoom/incr;
		//loadImageMaintainAspectRatioAndZoom(path);
		if(pictureExists(path))
			loadImageMaintainAspectRatioZoomAndRotate(path);
		else System.out.println(path+" not found");
	}
	public void rotate(final double angle){
		System.out.println("rotate");
		rotation+=angle;
		if(pictureExists(path))
			loadImageMaintainAspectRatioZoomAndRotate(path);
		else System.out.println(path+" not found");
	}
	public void moveImage(final int HORIZONTAL,final int VERTICAL){
		hor+=HORIZONTAL;
		ver+=VERTICAL;
		repaint();
	}

	@Override
	public void paint(Graphics g){
		if(img!=null){
			g.clearRect(0,0,getWidth(),getHeight());
			final int tb=getTitleBarHeight();
			g.drawImage(img,getWidth()/2-img.getWidth()/2+hor,getHeight()/2-(img.getHeight()-tb)/2+ver,
					img.getWidth(),img.getHeight(),this);
		}
	}
	@Override
	public void componentHidden(ComponentEvent event){}
	@Override
	public void componentShown(ComponentEvent event){}
	@Override
	public void componentResized(ComponentEvent event){
		System.out.println("size : "+getWidth()+","+getHeight());
		hor=ver=0;
		//loadImage(path);
		if(pictureExists(path))
			loadImageMaintainAspectRatio(path);
		else System.out.println(path+" not found");

	}
	@Override
	public void componentMoved(ComponentEvent event){}
	@Override
	public void keyPressed(KeyEvent event){
		System.out.println("Pressed : "+event.getKeyCode());
		final int KEY=event.getKeyCode();
		keysPressed.add(KEY);
		System.out.println(keysPressed);
		boolean exe=false;
		if(KEY==KeyEvent.VK_ADD){
			System.out.println("Add");
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				System.out.println("found");
				zoomIn();
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_SUBTRACT){
			System.out.println("Minus");
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				System.out.println("found");
				zoomOut();
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_LEFT){
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				System.out.println("rotate left");
				rotate(-10);
				exe=true;

			}
		}
		if(KEY==KeyEvent.VK_RIGHT){
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				System.out.println("rotate right");
				rotate(10);
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_UP){
			if(!exe){
				moveImage(0,-move);
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_DOWN){
			if(!exe){
				moveImage(0,move);
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_LEFT){
			if(!exe){
				moveImage(-move,0);
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_RIGHT){
			if(!exe){
				moveImage(move,0);
				exe=true;
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
