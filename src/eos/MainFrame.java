package eos;

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import java.awt.event.InputEvent;
import javax.swing.KeyStroke;
import java.io.FileWriter;
import java.util.Random;
import java.awt.AWTException;
import java.awt.Robot;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.BorderLayout;

import eos.imageUtils.ImageUtils;
import eos.toolbar.ToolBar;

public class MainFrame extends JFrame implements KeyListener,ComponentListener,ActionListener{
	String path;
	final LinkedHashSet<Integer>keysPressed;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newMenuItem,openMenuItem,saveMenuItem,saveAsMenuItem,exitMenuItem;

	private JMenu editMenu;

	private JMenu brightnessMenu;
	private JMenuItem increaseBrightnessMenuItem,decreaseBrightnessMenuItem,resetBrightnessMenuItem;

	private JMenu rotateMenu;
	private JMenuItem rotateLeftMenuItem,rotateRightMenuItem,resetRotateMenuItem;

	private JMenu shiftMenu;
	private JMenuItem shiftLeftMenuItem,shiftRightMenuItem,shiftUpMenuItem,shiftDownMenuItem,resetShiftMenuItem;

	private JMenu zoomMenu;
	private JMenuItem zoomInMenuItem,zoomOutMenuItem,resetZoomMenuItem;

	private JMenu opacityMenu;
	private JMenuItem increaseOpacityMenuItem,decreaseOpacityMenuItem,resetOpacityMenuItem;

	private JMenu toolsMenu;
	private JMenuItem cameraMenuItem;
	private JMenuItem screenShotMenuItem;
	private EOSPanel eosPanel;
	//private JMenu settings;
	final private JTabbedPane tabbedPane;
	private JFileChooser fileChooser;
	private WebCamFrame webCamFrame;
	final private ToolBar toolBar;
	public MainFrame(final String path){
		this.path=path;
		this.setTitle("EOS");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		keysPressed=new LinkedHashSet<>();
		tabbedPane=new JTabbedPane();
		this.setLayout(new BorderLayout());
		toolBar=new ToolBar();
		add(tabbedPane,BorderLayout.CENTER);
		add(toolBar,BorderLayout.NORTH);
		this.setSize(500,500);
		initializeMenuBar();

		this.addComponentListener(this);
		this.addKeyListener(this);
		requestFocus();
		//setFocusable is important to register the keys
		setFocusable(true);
		if(pictureExists(path)){
			/*File file=new File(path);
			  eosPanel=new EOSPanel(path,getSize(),this);
			  tabbedPane.add(file.getName(),eosPanel);
			  tabbedPane.setTabComponentAt(tabbedPane.getSelectedIndex(),new ButtonTabComponent(tabbedPane));
			  */
			addToTabbedPane(path);
		}
		else System.out.println(path+" Picture not found in the path");
		//pack();
		//setUndecorated(!false);
		this.setVisible(true);
		System.out.println("Control : "+KeyEvent.VK_CONTROL);
		System.out.println("Add : "+KeyEvent.VK_ADD);
		System.out.println("Present working directory : "+System.getProperty("user.dir"));
		//System.out.println("Title Bar height : "+getInsets().top);
	}
	private void addToTabbedPane(final String path){
		File file=new File(path);
		eosPanel=new EOSPanel(path,getSize(),this);
		tabbedPane.add(file.getName(),eosPanel);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1,new ButtonTabComponent(tabbedPane));
		tabbedPane.setSelectedComponent(eosPanel);
	}
	private void initializeMenuBar(){
		menuBar=new JMenuBar();
		initializeFileMenu();
		initializeEditMenu();
		initializeToolsMenu();
		this.setJMenuBar(menuBar);
		//menuBar.setVisible(true);
		//menuBar.revalidate();
	}
	private void initializeFileMenu(){
		fileMenu=new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newMenuItem=new JMenuItem("New");
		newMenuItem.addActionListener(this);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));

		openMenuItem=new JMenuItem("Open");
		openMenuItem.addActionListener(this);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK));

		saveMenuItem=new JMenuItem("Save");
		saveMenuItem.addActionListener(this);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK));

		saveAsMenuItem=new JMenuItem("SaveAs");
		saveAsMenuItem.addActionListener(this);
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));

		exitMenuItem=new JMenuItem("Exit");
		exitMenuItem.addActionListener(this);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,InputEvent.ALT_DOWN_MASK));

		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		//fileMenu.setPreferredSize(new Dimension(100,50));
		menuBar.add(fileMenu);
	}
	private void initializeEditMenu(){
		/*private JMenu editMenu;
		  private JMenuItem brightnessMenuItem,increaseBrightnessMenuItem,decreaseBrightnessMenuItem,resetBrightnessMenuItem;
		  private JMenuItem rotateMenuItem,rotateLeftMenuItem,rotateRightMenuItem,resetRotateMenuItem;
		  private JMenuItem shiftMenuItem,shiftLeftMenuItem,shiftRightMenuItem,shiftUpMenuItem,shiftDownMenuItem,resetShiftMenuItem;
		  private JMenuItem zoomMenuItem,zoomInMenuItem,zoomOutMenuItem,resetZoomMenuItem;*/
		editMenu=new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		initializeBrightnessMenu();
		initializeRotateMenu();
		initializeShiftMenu();
		initializeZoomMenu();
		initializeOpacityMenu();
		menuBar.add(editMenu);
	}
	private void initializeBrightnessMenu(){
		brightnessMenu=new JMenu("Brightness");

		increaseBrightnessMenuItem=new JMenuItem("Increase Brightness");
		increaseBrightnessMenuItem.addActionListener(this);
		increaseBrightnessMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,InputEvent.CTRL_DOWN_MASK));

		decreaseBrightnessMenuItem=new JMenuItem("Decrease Brightness");
		decreaseBrightnessMenuItem.addActionListener(this);
		decreaseBrightnessMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));

		resetBrightnessMenuItem=new JMenuItem("Reset Brightness");
		resetBrightnessMenuItem.addActionListener(this);
		//resetBrightnessMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R+KeyEvent.VK_B,InputEvent.CTRL_DOWN_MASK));

		brightnessMenu.add(increaseBrightnessMenuItem);
		brightnessMenu.add(decreaseBrightnessMenuItem);
		brightnessMenu.add(resetBrightnessMenuItem);

		editMenu.add(brightnessMenu);
	}
	private void initializeRotateMenu(){

		/*private JMenu rotateMenu;
		  private JMenuItem rotateLeftMenuItem,rotateRightMenuItem,resetRotateMenuItem;*/
		rotateMenu=new JMenu("Rotate");
		rotateLeftMenuItem=new JMenuItem("Rotate Left");
		rotateLeftMenuItem.addActionListener(this);
		rotateLeftMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,InputEvent.CTRL_DOWN_MASK));

		rotateRightMenuItem=new JMenuItem("Rotate Right");
		rotateRightMenuItem.addActionListener(this);
		rotateRightMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,InputEvent.CTRL_DOWN_MASK));

		resetRotateMenuItem=new JMenuItem("Reset Rotation");
		resetRotateMenuItem.addActionListener(this);

		rotateMenu.add(rotateLeftMenuItem);
		rotateMenu.add(rotateRightMenuItem);
		rotateMenu.add(resetRotateMenuItem);

		editMenu.add(rotateMenu);
	}
	private void initializeShiftMenu(){
		//		private JMenu shiftMenu;
		//		private JMenuItem shiftLeftMenuItem,shiftRightMenuItem,shiftUpMenuItem,shiftDownMenuItem,resetShiftMenuItem;
		shiftMenu=new JMenu("Shift Image");

		shiftLeftMenuItem=new JMenuItem("Sift Left");
		shiftLeftMenuItem.addActionListener(this);
		shiftLeftMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,InputEvent.SHIFT_DOWN_MASK));

		shiftRightMenuItem=new JMenuItem("Shift Right");
		shiftRightMenuItem.addActionListener(this);
		shiftRightMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,InputEvent.SHIFT_DOWN_MASK));

		shiftUpMenuItem=new JMenuItem("Shift Up");
		shiftUpMenuItem.addActionListener(this);
		shiftUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,InputEvent.SHIFT_DOWN_MASK));

		shiftDownMenuItem=new JMenuItem("Shift Down");
		shiftDownMenuItem.addActionListener(this);
		shiftDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,InputEvent.SHIFT_DOWN_MASK));

		resetShiftMenuItem=new JMenuItem("Reset Shift");
		resetShiftMenuItem.addActionListener(this);

		shiftMenu.add(shiftLeftMenuItem);
		shiftMenu.add(shiftRightMenuItem);
		shiftMenu.add(shiftUpMenuItem);
		shiftMenu.add(shiftDownMenuItem);
		shiftMenu.add(resetShiftMenuItem);

		editMenu.add(shiftMenu);
	}
	//TODO setAccelerator
	private void initializeZoomMenu(){

		//		private JMenu zoomMenu;
		//		private JMenuItem zoomInMenuItem,zoomOutMenuItem,resetZoomMenuItem;
		zoomMenu=new JMenu("Zoom Image");

		zoomInMenuItem=new JMenuItem("Zoom In");
		zoomInMenuItem.addActionListener(this);
		zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD,InputEvent.CTRL_DOWN_MASK));

		zoomOutMenuItem=new JMenuItem("Zoom Out");
		zoomOutMenuItem.addActionListener(this);
		zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT,InputEvent.CTRL_DOWN_MASK));

		resetZoomMenuItem=new JMenuItem("Reset Zoom");
		resetZoomMenuItem.addActionListener(this);

		zoomMenu.add(zoomInMenuItem);
		zoomMenu.add(zoomOutMenuItem);
		zoomMenu.add(resetZoomMenuItem);

		editMenu.add(zoomMenu);
	}
	private void initializeOpacityMenu(){
		opacityMenu=new JMenu("Opacity");

		increaseOpacityMenuItem=new JMenuItem("Increase Opacity");
		increaseOpacityMenuItem.addActionListener(this);
		increaseOpacityMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK+InputEvent.ALT_DOWN_MASK));

		decreaseOpacityMenuItem=new JMenuItem("Decrease Opacity");
		decreaseOpacityMenuItem.addActionListener(this);
		decreaseOpacityMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));

		resetOpacityMenuItem=new JMenuItem("Reset Opacity");
		resetOpacityMenuItem.addActionListener(this);
		//resetOpacityMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MENU));

		opacityMenu.add(increaseOpacityMenuItem);
		opacityMenu.add(decreaseOpacityMenuItem);
		opacityMenu.add(resetOpacityMenuItem);

		editMenu.add(opacityMenu);
	}
	private void initializeToolsMenu(){
		toolsMenu=new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);

		screenShotMenuItem=new JMenuItem("ScreenShot");
		screenShotMenuItem.addActionListener(this);
		screenShotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));

		cameraMenuItem=new JMenuItem("Camera");
		cameraMenuItem.addActionListener(this);
		cameraMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_DOWN_MASK));

		toolsMenu.add(screenShotMenuItem);
		toolsMenu.add(cameraMenuItem);

		menuBar.add(toolsMenu);
	}
	private void initializeSettingsMenu(){
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
	//TODO implement it
	@Override
	public void actionPerformed(final ActionEvent event){
		if(event.getSource()==newMenuItem){
			System.out.println("newMenuItem");
			JFileChooser fileChooser=new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
			int retValue=fileChooser.showOpenDialog(this);
			if(retValue==JFileChooser.APPROVE_OPTION){
				System.out.println("approved");
				File file=fileChooser.getSelectedFile();
				if(file.exists() && !file.isDirectory() && file.isFile()){
					addToTabbedPane(file.getAbsolutePath());
				}
			}

		}
		if(event.getSource()==openMenuItem){
			System.out.println("OpenMenuItem");
			JFileChooser fileChooser=new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
			int retValue=fileChooser.showOpenDialog(this);
			if(retValue==JFileChooser.APPROVE_OPTION){
				System.out.println("approved");
				File file=fileChooser.getSelectedFile();
				if(file.exists() && !file.isDirectory() && file.isFile()){
					addToTabbedPane(file.getAbsolutePath());
				}
			}

		}
		if(event.getSource()==saveMenuItem){
			System.out.println("SaveMenuItem");
			save();
		}
		if(event.getSource()==saveAsMenuItem){
			System.out.println("SaveAs");
			saveAs();
		}
		if(event.getSource()==exitMenuItem){
			System.out.println("exiting");
			System.exit(0);
		}
		if(event.getSource()==screenShotMenuItem){
			System.out.println("screenShot");
			final String str=generateRandomString(20);
			System.out.println("RandomString generated : "+str);
			final String path=System.getProperty("user.dir")+"/"+str+".png";
			final BufferedImage image=captureScreen();
			try{
				ImageIO.write(image,"png",new File(path));
				addToTabbedPane(path);
			}catch(IOException ioException){
				ioException.printStackTrace();
				System.out.println("cannot add to the tabbed pane");
			}
		}
		if(event.getSource()==cameraMenuItem){
			System.out.println("WebCam");
			captureImage();
		}
		if(event.getSource()==increaseBrightnessMenuItem){
			System.out.println("Increase Brightness");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.increaseBrightness();
			}
		}
		if(event.getSource()==decreaseBrightnessMenuItem){
			System.out.println("Decrease Brightness");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.decreaseBrightness();
			}
		}
		if(event.getSource()==resetBrightnessMenuItem){
			System.out.println("ResetBrightness");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.resetBrightness();
			}
		}
		if(event.getSource()==rotateLeftMenuItem){
			System.out.println("Rotate Left");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.rotateLeft();
			}
		}
		if(event.getSource()==rotateRightMenuItem){
			System.out.println("Rotate Right");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.rotateRight();
			}
		}
		if(event.getSource()==resetRotateMenuItem){
			System.out.println("Reset Rotate");
			EOSPanel EOSPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.resetRotation();
			}
		}
		if(event.getSource()==shiftLeftMenuItem){
			System.out.println("Shift Left");
			EOSPanel EOSPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.shiftImageLeft();
			}
		}
		if(event.getSource()==shiftRightMenuItem){
			System.out.println("Shift Right");
			EOSPanel EOSPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.shiftImageRight();
			}
		}
		if(event.getSource()==shiftUpMenuItem){
			System.out.println("Shift UP");
			EOSPanel EOSPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.shiftImageUp();
			}
		}
		if(event.getSource()==shiftDownMenuItem){
			System.out.println("Shift Down");
			EOSPanel EOSPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.shiftImageDown();
			}
		}
		if(event.getSource()==resetShiftMenuItem){
			System.out.println("Reset Shift");
			EOSPanel EOSPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.resetShift();
			}
		}
		if(event.getSource()==zoomInMenuItem){
			System.out.println("Zoom In");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.zoomIn();
			}
		}
		if(event.getSource()==zoomOutMenuItem){
			System.out.println("Zoom Out");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.zoomOut();
			}
		}
		if(event.getSource()==resetZoomMenuItem){
			System.out.println("Reset zoom");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.resetZoom();
			}
		}
		if(event.getSource()==increaseOpacityMenuItem){
			System.out.println("Increase Opacity");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.increaseOpacity();
			}
		}
		if(event.getSource()==decreaseOpacityMenuItem){
			System.out.println("Decrease Opacity");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.decreaseOpacity();
			}
		}
		if(event.getSource()==resetOpacityMenuItem){
			System.out.println("Reset Opacity");
			EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
			if(eosPanel!=null){
				eosPanel.resetOpacity();
			}
		}

	}
	/*private void captureImage(){
	  try{
	  final Webcam webcam=Webcam.getDefault();
	  for(Dimension size:webcam.getViewSizes())
	  System.out.println("supported size : "+size);
	  webcam.setViewSize(WebcamResolution.VGA.getSize());
	  webcam.open();
	  final BufferedImage image=webcam.getImage();
	  webcam.close();
	  final String str=generateRandomString(20);
	  System.out.println("RandomString generated : "+str);
	  final String path=System.getProperty("user.dir")+"/"+str+".png";
	  ImageIO.write(image,"PNG",new File(path));
	  addToTabbedPane(path);
	  }catch(IOException ioException){
	  ioException.printStackTrace();
	  System.out.println("cannot add to the tabbed pane");
	  }

	  }*/
	private void captureImage(){
		/*final webcam webcam=webcam.getdefault();
		  for(dimension size:webcam.getviewsizes())
		  system.out.println("supported size : "+size);
		  webcam.setviewsize(webcamresolution.vga.getsize());
		  webcam.open();
		  final bufferedimage image=webcam.getimage();
		  webcam.close();
		  final string str=generaterandomstring(20);
		  system.out.println("randomstring generated : "+str);
		  final string path=system.getproperty("user.dir")+"/"+str+".png";
		  imageio.write(image,"png",new file(path));
		  addtotabbedpane(path);
		  */
		webCamFrame=new WebCamFrame(new Dimension(500,500),this);
		this.setVisible(false);

	}
	private void save(){
		final EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
		if(eosPanel==null){
			System.out.println("no component selected");
		}
		else{
			final JFileChooser fileChooser=new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
			final int retValue=fileChooser.showSaveDialog(this);
			if(retValue==JFileChooser.APPROVE_OPTION){
				System.out.println("approved");
				final File file=fileChooser.getSelectedFile();
				final String path=file.getAbsolutePath();
				System.out.println(path);
				String extension=null;
				for(int i=path.length()-1;i>=0;i--)
					if(path.charAt(i)=='.'){
						extension=path.substring(i+1,path.length());
						break;
					}
				System.out.println("extension:"+extension);
				if(extension!=null && extension.length() > 0){
					//addToTabbedPane(file.getAbsolutePath());
					try{
						//final FileWriter fileWriter=new FileWriter(file);
						//fileWriter.write();
						//fileWriter.close();
						ImageIO.write(eosPanel.img,extension,file);
						System.out.println("Image is saved");
					}catch(final IOException ioException){
						ioException.printStackTrace();
					}
				}else{
					System.out.println("Please enter a valid extension for file");
				}
			}
		}
	}
	private void saveAs(){
		save();
	}
	private BufferedImage captureScreen(){
		Robot robot=null;
		this.setVisible(false);
		BufferedImage image=null;
		try{
			//we are delaying it for some time so the frame is not visible
			Thread.sleep(500);
			robot=new Robot();
			image=robot.createScreenCapture(new Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
		}catch(final InterruptedException | AWTException exception){
			exception.printStackTrace();
		}
		this.setVisible(true);
		return image;
	}
	private String generateRandomString(int size){
		final Random random=new Random();
		final StringBuilder str=new StringBuilder();
		for(int i=0;i<size;i++){
			int val;
			if((random.nextInt()&1)==0){
				val=Math.abs(random.nextInt()%26)+65;
			}else{
				val=Math.abs(random.nextInt()%26)+97;
			}
			str.append((char)val);
		}
		return str.toString();
	}

	public void setCapturedImage(final Image image){
		try{
			if(image!=null){
				final String str=generateRandomString(20);
				System.out.println("randomstring generated : "+str);
				final String path=System.getProperty("user.dir")+"/"+str+".png";
				ImageIO.write(ImageUtils.toBufferedImage(image),"png",new File(path));
				addToTabbedPane(path);
				webCamFrame=null;
			}else System.out.println("please press capture button to capture image");
		}catch(final Exception exception){
			exception.printStackTrace();
		}
		setVisible(true);
	}
	@Override
	public void componentHidden(ComponentEvent event){}
	@Override
	public void componentShown(ComponentEvent event){}
	@Override
	public void componentResized(ComponentEvent event){
		/*System.out.println("size : "+getWidth()+","+getHeight());
		  hor=ver=0;
		  rotation=0;
		  cropMode=false;
		//loadImage(path);
		if(pictureExists(path))
		loadImage(path);
		else System.out.println(path+" not found");
		*/
		System.out.println("component resized");
		final int tabCount=tabbedPane.getTabCount();
		for(int i=0;i<tabCount;i++){
			final EOSPanel eosPanel=(EOSPanel)tabbedPane.getComponentAt(i);
			eosPanel.componentResized(event);
		}
	}
	@Override
	public void componentMoved(ComponentEvent event){}
	@Override
	public void keyPressed(KeyEvent event){
		System.out.println("Keys Pressed");
		final int KEY=event.getKeyCode();
		keysPressed.add(KEY);
		final EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
		if(eosPanel!=null){
			if(true)
				return;//TODO REMOVE THIS
			eosPanel.keyPressed(event,keysPressed);
		}
		/*System.out.println("Pressed : "+event.getKeyCode());
		  final int KEY=event.getKeyCode();
		  keysPressed.add(KEY);
		  System.out.println(keysPressed);
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
	if(KEY==KeyEvent.VK_B)
		if(keysPressed.contains(KeyEvent.VK_CONTROL)){
			brightness+=(brightness_change*(keysPressed.contains(KeyEvent.VK_SHIFT)?-1:1));	
			loadImage(path);
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
	//if(KEY==KeyEvent.VK_B)
	//  if(keysPressed.contains(KeyEvent.VK_CONTROL) && keysPressed.contains(KeyEvent.VK_SHIFT)){
	//  changeBrightness(-brightness_change);
	//  exe=true;
	//  }

	*/
	}

	@Override
	public void keyTyped(KeyEvent event){
		final int KEY=event.getKeyCode();
		keysPressed.add(KEY);
	}
	@Override
	public void keyReleased(KeyEvent event){
		final int KEY=event.getKeyCode();
		final EOSPanel eosPanel=(EOSPanel)tabbedPane.getSelectedComponent();
		if(eosPanel!=null){
			eosPanel.keyReleased(event,keysPressed);
		}
		keysPressed.remove(KEY);
		/*System.out.println("Released : "+event.getKeyCode());
		  final int KEY=event.getKeyCode();
		  keysPressed.remove(KEY);
		  System.out.println(keysPressed);
		  */

	}
}

