package eos;

import eos.MainFrame;

public class MainClass{
	public static void main(String $[])throws Exception{
		System.out.println("main");
		final StringBuilder path=new StringBuilder("");
		for(final String str:$)
			path.append(str);
		System.out.println("path : "+path);
		javax.swing.SwingUtilities.invokeLater(()->{
			MainFrame frame=new MainFrame(path.toString());
		});
		/*Thread thread=new Thread(()->{
		  while(true){
		  frame.rotate(10);
		  try{
		  Thread.sleep(100);
		  }catch(Exception exception){
		  exception.printStackTrace();
		  }
		  }
		  });
		  thread.start();
		  thread.join();
		  */
	}
}
