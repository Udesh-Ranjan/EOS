package eos.toolbar;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;


public class ToolBar extends JPanel implements ActionListener{
	JToolBar toolBar=new JToolBar("EOS ToolBar");
	Shapes shapes;
	JScrollPane scrollShapes;
	public ToolBar(){
		toolBar=new JToolBar("EOS ToolBar");
		shapes=new Shapes();
		shapes.setPreferredSize(new Dimension(100,50));
		scrollShapes=new JScrollPane(shapes);
		add(scrollShapes);
	}
	@Override
	public void actionPerformed(final ActionEvent event){

	}
}
