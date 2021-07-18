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

public class MainFrame extends JFrame implements KeyListener,ComponentListener,ActionListener{
	String path;
	final LinkedHashSet<Integer>keysPressed;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newMenuItem,openMenuItem,saveMenuItem,saveAsMenuItem,exitMenuItem;
	private JMenu editMenu;
	private JMenuItem brightnessMenuItem,increaseBrightnessMenuItem,decreaseBrightnessMenuItem;
	private JMenuItem rotateMenuItem,rotateLeftMenuItem,rotateRightMenuItem;
	private JMenuItem shiftMenuItem,shiftLeftMenuItem,shiftRightMenuItem,shiftUpMenuItem,shiftDownMenuItem;
	private JMenuItem zoomMenuItem,zoomInMenuItem,zoomOutMenuItem;

	private JMenu toolsMenu;
	private JMenuItem cameraMenuItem;
	private EOSPanel eosPanel;
	//private JMenu settings;
	final private JTabbedPane tabbedPane;
	private JFileChooser fileChooser;
	public MainFrame(final String path){
		this.path=path;
		this.setTitle("EOS");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		keysPressed=new LinkedHashSet<>();
		tabbedPane=new JTabbedPane();
		add(tabbedPane);
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
		fileMenu.add(exitMenuItem);
		//fileMenu.setPreferredSize(new Dimension(100,50));
		menuBar.add(fileMenu);
	}
	private void initializeEditMenu(){
	}
	private void initializeToolsMenu(){
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
