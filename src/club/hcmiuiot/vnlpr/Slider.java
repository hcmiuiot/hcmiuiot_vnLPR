package club.hcmiuiot.vnlpr;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Slider extends JFrame implements Runnable {

	private JPanel contentPane;
	private JSlider slider = new JSlider();
	private JLabel label = new JLabel("1");
    static public Integer mem;
	private Slider popup;


	/**
	 * Create the frame.
	 */
	public Slider(Integer valAdrress) {
		popup = this;
		mem = valAdrress;
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 111);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		slider.setSnapToTicks(true);
		slider.setMinorTickSpacing(1);
		slider.setLabelTable(slider.createStandardLabels(10));
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				label.setText(""+slider.getValue());
				mem = slider.getValue();
				LPRengine.setVal1(slider.getValue());
			}
		});
		slider.setPaintLabels(true);
		slider.setValue(3);
		slider.setPaintTicks(true);
		contentPane.add(slider, BorderLayout.CENTER);
		
		label.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(label, BorderLayout.SOUTH);
	}

	@Override
	public void run() {
		try {
			Slider frame = popup;
			frame.setVisible(true);
		} catch (Exception e) {
		e.printStackTrace();
		}
		
	}

}
