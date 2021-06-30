public class MainClass{
	public static void main(String $[]){
		System.out.println("main");
		final StringBuilder path=new StringBuilder("");
		for(final String str:$)
			path.append(str);
		System.out.println("path : "+path);
		MainFrame frame=new MainFrame(path.toString());
	}
}
