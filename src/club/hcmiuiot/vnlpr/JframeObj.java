package club.hcmiuiot.vnlpr;

import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JframeObj implements Comparable<JframeObj> {
	private JFrame jFrame;
	private String name;
	private JLabel lbl = new JLabel();
	
	public JFrame getjFrame() {
		return jFrame;
	}
	public void setjFrame(JFrame jFrame) {
		this.jFrame = jFrame;
	}
	public String getName() {
		return name;
	}
	
	public JframeObj(String name) {
		jFrame = new JFrame(name);
		jFrame.setLayout(new FlowLayout());
		//lbl.setIcon(icon);
		jFrame.add(lbl);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.name = name;
	}
	
	public void updateImg(Image img) {
		lbl.setIcon(new ImageIcon(img));
		jFrame.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
		jFrame.setVisible(true);
	}
	
	public JframeObj(JFrame jFrame, String name) {
		this.jFrame = jFrame;
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(JframeObj o) {
		return getName().compareTo(o.getName());
	}
	
}
