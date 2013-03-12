package driver;

import java.awt.*;
import javax.swing.*;

import lab.LabAgent;

import transducer.Transducer;

public class LabFrame extends JFrame {
	LabAgent myAgent;

	public LabFrame() {
		Transducer transducer = new Transducer();

		myAgent = new LabAgent(transducer);

		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1, 2));
		this.add(new LabDisplayPanel(transducer));
		this.add(new LabControlPanel(transducer));
	}
}
