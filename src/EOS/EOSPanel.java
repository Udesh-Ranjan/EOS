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
import java.awt.image.RescaleOp;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.BasicStroke;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Dimension;
import javax.swing.JPanel;
import imageUtils.ImageUtils;

public class EOSPanel extends JPanel {
	public BufferedImage img;
	InputStream stream;
	//String path="/home/dev/Downloads/drk_beaver.jpg";
	public String path;
	//LinkedHashSet<Integer>keysPressed;
	double zoom;
	final static double incr=1.2;
	final static int move=5;//pixels
	final static float brightness_change=0.1f;
	boolean fileNoFound=false;
	double rotation;
	int hor,ver;
	float brightness;
	public boolean cropMode;
	public Rectangle rectangle;

	private final MainFrame mainFrame;
	//private JMenu settings;
	//TODO move stuffs to JPanel's child class
	public EOSPanel(final String path,final Dimension size,final MainFrame mainFrame){
		this.path=path;
		this.mainFrame=mainFrame;
		//this.setTitle("EOS");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.addComponentListener(this);
		//this.addKeyListener(this);
		this.setSize(size.getSize());
		//this.setLayout(null);
		//loadImage(path);
		zoom=1;
		rotation=0;
		hor=ver=0;
		brightness=1.0f;
		rectangle=new Rectangle(0,0,0,0);
		if(pictureExists(path))
			loadImage(path);
		else System.out.println(path+"Picture not found");
		//keysPressed=new LinkedHashSet<>();
		//initializeMenuBar();
		//pack();
		this.setVisible(true);
		//this.addKeyListener(this);
		System.out.println("Control : "+KeyEvent.VK_CONTROL);
		System.out.println("Add : "+KeyEvent.VK_ADD);
		//System.out.println("Title Bar height : "+getInsets().top);
	}
	public static boolean pictureExists(final String path){
		File file=new File(path);
		return file.exists() && file.isFile();

	}
	public int getTitleBarHeight(){
		//final Insets insets=getInsets();
		//if(insets!=null)
		//	return insets.top;
		return 0;
	}
	/*public void loadImage(final String path){
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
	*/
	/*public void loadImageMaintainAspectRatio(final String path){
	  synchronized(this){
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
	  }*/

	/*public void loadImage(final String path){
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
	changeBrightness();
	//image.getGraphics().dispose();
	System.out.println("new size : "+new_width+","+new_height);
	//image.dispose();TODO
	}catch(final IOException ioException){
	ioException.printStackTrace();
	}
	repaint();
	}
	*/
	public void loadImage(final String path){
		synchronized(this){
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
				int tb=getTitleBarHeight();
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
				img=ImageUtils.rotateImageByDegrees(scaledImg,rotation);
				changeBrightness();
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
	}
	public void zoomIn(){
		System.out.println("zoomIn");
		zoom=zoom*incr;
		//loadImageMaintainAspectRatioAndZoom(path);
		if(pictureExists(path))
			loadImage(path);
		else System.out.println(path+" not found");

	}
	public void zoomOut(){
		System.out.println("zoomOut");
		zoom=zoom/incr;
		//loadImageMaintainAspectRatioAndZoom(path);
		if(pictureExists(path))
			loadImage(path);
		else System.out.println(path+" not found");
	}
	public void rotate(final double angle){
		System.out.println("rotate");
		hor=ver=0;
		rotation+=angle;
		if(pictureExists(path))
			loadImage(path);
		else System.out.println(path+" not found");
	}
	public void moveImage(final int HORIZONTAL,final int VERTICAL){
		hor+=HORIZONTAL;
		ver+=VERTICAL;
		repaint();
	}
	//@Override
	public void componentHidden(ComponentEvent event){}
	//@Override
	public void componentShown(ComponentEvent event){}
	//@Override
	public void componentResized(ComponentEvent event){
		System.out.println("size : "+getWidth()+","+getHeight());
		hor=ver=0;
		rotation=0;
		cropMode=false;
		//loadImage(path);
		if(pictureExists(path))
			loadImage(path);
		else System.out.println(path+" not found");

	}
	//@Override
	public void componentMoved(ComponentEvent event){}

	public void changeBrightness(){
		if(pictureExists(path)){
			final BufferedImage prev=img;
			final RescaleOp op=new RescaleOp(brightness,0,null);
			img=op.filter(img,null);
		}
		else System.out.println(path+" not found");
	}
	public void increaseBrightness(){
		brightness+=brightness_change;
		loadImage(path);

	}
	public void decreaseBrightness(){
		brightness-=brightness_change;
		loadImage(path);

	}
	public void resetBrightness(){
		brightness=1.0f;
		loadImage(path);
	}
	public void rotateRight(){
		rotate(10);
	}
	public void rotateLeft(){
		rotate(-10);
	}
	public void resetRotation(){
		rotation=0;
		rotate(0);
	}
	public void shiftImageUp(){
		moveImage(0,-move);
	}
	public void shiftImageDown(){
		moveImage(0,move);
	}
	public void shiftImageLeft(){
		moveImage(-move,0);
	}
	public void shiftImageRight(){
		moveImage(move,0);
	}
	public void resetShift(){
		hor=ver=0;
		moveImage(0,0);
	}
	//@Override
	public void keyPressed(final KeyEvent event,final LinkedHashSet<Integer>keysPressed){
		System.out.println("Pressed : "+event.getKeyCode());
		final int KEY=event.getKeyCode();
		//keysPressed.add(KEY);
		//System.out.println(keysPressed);
		boolean exe=false;
		if(cropMode){
			if(KEY==KeyEvent.VK_ENTER){
				//TODO
				//save the select region to new bufferedimage
			}
			if(KEY==KeyEvent.VK_LEFT){
				if(keysPressed.contains(KeyEvent.VK_CONTROL)){
					rectangle.width--;
				}else{
					rectangle.x--;
					rectangle.width++;
				}
				exe=true;
			}
			if(KEY==KeyEvent.VK_RIGHT){
				if(keysPressed.contains(KeyEvent.VK_CONTROL)){
					rectangle.width++;
				}else{
					rectangle.x++;
					rectangle.width--;
				}
				exe=true;
			}
			if(KEY==KeyEvent.VK_UP){
				if(keysPressed.contains(KeyEvent.VK_CONTROL)){
					rectangle.height--;
				}else{
					rectangle.y--;
					rectangle.height++;
				}
				exe=true;
			}
			if(KEY==KeyEvent.VK_DOWN){
				if(keysPressed.contains(KeyEvent.VK_CONTROL)){
					rectangle.height++;
				}else{
					rectangle.y++;
					rectangle.height--;
				}
				exe=true;
			}
			if(exe){
				repaint();
				return;
			}
		}
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
				//				System.out.println("rotate left");
				//				rotate(-10);
				//				exe=true;

			}
		}
		if(KEY==KeyEvent.VK_RIGHT){
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				//				System.out.println("rotate right");
				//				rotate(10);
				//				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_UP){
			if(!exe){
				//moveImage(0,-move);
				//exe=true;
			}
		}
		if(KEY==KeyEvent.VK_DOWN){
			if(!exe){
				//moveImage(0,move);
				//exe=true;
			}
		}
		if(KEY==KeyEvent.VK_LEFT){
			if(!exe){
				//moveImage(-move,0);
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_RIGHT){
			if(!exe){
				//moveImage(move,0);
				exe=true;
			}
		}
		if(KEY==KeyEvent.VK_B)
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				//brightness+=(brightness_change*(keysPressed.contains(KeyEvent.VK_SHIFT)?-1:1));	
				//loadImage(path);
				//changeBrightness(brightness_change*(keysPressed.contains(KeyEvent.VK_SHIFT)?-1:1));
				exe=true;
			}
		if(KEY==KeyEvent.VK_C)
			if(keysPressed.contains(KeyEvent.VK_CONTROL)){
				cropMode=true;
				final int width=50,height=50;
				rectangle.width=width;
				rectangle.height=height;
				rectangle.x=getWidth()/2-width/2;
				rectangle.y=getHeight()/2-height/2;
			}
		if(KEY==KeyEvent.VK_ESCAPE){
			if(cropMode){
				cropMode=false;
				repaint();
			}
		}
		/*if(KEY==KeyEvent.VK_B)
		  if(keysPressed.contains(KeyEvent.VK_CONTROL) && keysPressed.contains(KeyEvent.VK_SHIFT)){
		  changeBrightness(-brightness_change);
		  exe=true;
		  }
		  */
	}

	//@Override
	public void keyTyped(final KeyEvent event,final LinkedHashSet<Integer>keysPressed){}
	//@Override
	public void keyReleased(final KeyEvent event,final LinkedHashSet<Integer>keysPressed){
		System.out.println("Released : "+event.getKeyCode());
		final int KEY=event.getKeyCode();
		//keysPressed.remove(KEY);
		//System.out.println(keysPressed);

	}
	@Override
	public void paintComponent(final Graphics g){
		System.out.println("paintComponent");
		super.paintComponent(g);
		//super.paint(g);
		if(img!=null){
			g.clearRect(0,0,getWidth(),getHeight());
			final int tb=getTitleBarHeight();
			System.out.println("title bar height : "+tb);
			/*g.drawImage(img,getWidth()/2-img.getWidth()/2+hor,getHeight()/2-(img.getHeight()-tb)/2+ver,
			  Math.min(img.getWidth(),getWidth()),Math.min(img.getHeight(),getHeight()),this);
			  */
			/*g.drawImage(img,getWidth()/2-img.getWidth()/2+hor,getHeight()/2-img.getHeight()/2+ver,
			  Math.min(img.getWidth(),getWidth()),Math.min(img.getHeight(),getHeight()),null);
			  */
			g.drawImage(img,getWidth()/2-img.getWidth()/2+hor,getHeight()/2+tb/2-img.getHeight()/2+ver,
					img.getWidth(),img.getHeight(),null);
			//TODO implement cropMode
			if(cropMode && false){
				//	Graphics2D _g=(Graphics2D)img.getGraphics();
				Graphics _g=g;
				Color c=_g.getColor();
				_g.setColor(Color.cyan);
				//Stroke stroke=_g.getStroke();
				//_g.setStroke(new BasicStroke(20));	
				_g.drawRect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
				_g.setColor(c);
				//_g.setStroke(stroke);
			}
		}
		//revalidate();
	}
}
