package pippin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBarBuilder implements Observer {
	private JMenuItem assemble = new JMenuItem("Assemble Source..."); 
	private JMenuItem load = new JMenuItem("Load Program..."); 
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem go = new JMenuItem("Go");
	private Machine machine;
	
	public MenuBarBuilder(Machine machine) { 
		this.machine = machine;
		machine.addObserver(this);
	}
	
	public JMenu createMenu1() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		
		assemble.setMnemonic(KeyEvent.VK_A); 
		assemble.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menu.addSeparator(); // puts a line across the menu
		assemble.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		            machine.assembleFile();
			}
		});
		menu.add(assemble);
		
		//----------------------------
		//Load
		menu.setMnemonic(KeyEvent.VK_L);
		load.setMnemonic(KeyEvent.VK_A); 
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menu.addSeparator(); // puts a line across the menu
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		            machine.loadFile();
			}
		});
		menu.add(load);
		//---------------------
		//Exit
		menu.setMnemonic(KeyEvent.VK_E);
		exit.setMnemonic(KeyEvent.VK_A); 
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menu.addSeparator(); // puts a line across the menu
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		            machine.exit();
			}
		});
		menu.add(exit);
		return menu;
	}
	public JMenu createMenu2() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_X);
		go.setMnemonic(KeyEvent.VK_A); 
		go.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menu.addSeparator(); // puts a line across the menu
		go.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		            machine.execute();
			}
		});
		menu.add(go);
		return menu;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}