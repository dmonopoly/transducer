package driver;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import transducer.Transducer;

public class LabDisplayPanel extends JPanel {
	Transducer myTransducer;

	public LabDisplayPanel(Transducer transducer) {
		Animation1 anim1 = new Animation1(transducer);
		Animation2 anim2 = new Animation2(transducer);

		Timer myTimer = new Timer(500, null);
		myTimer.addActionListener(anim1);
		myTimer.addActionListener(anim2);
		myTimer.start();

		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(new GridLayout(2, 3));
		this.add(new JLabel());
		this.add(anim1);
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(anim2);
		this.add(new JLabel());
	}
}
