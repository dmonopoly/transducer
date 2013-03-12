package driver;

import javax.swing.*;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;

import java.awt.*;
import java.awt.event.*;

public class LabControlPanel extends JPanel implements TReceiver {
	Transducer myTransducer;
	JLabel finalFinishLabel;

	public LabControlPanel(Transducer transducer) {
		myTransducer = transducer;
		myTransducer.register(this, TChannel.CONTROL_PANEL); // NOTE

		this.setPreferredSize(new Dimension(100, 500));

		JLabel tempLabel = new JLabel();
		tempLabel.setPreferredSize(new Dimension(200, 130));
		this.add(tempLabel);

		JButton startButton = new JButton("Start Animations");
		startButton.setPreferredSize(new Dimension(200, 130));
		startButton.addActionListener(new StartButtonActionListener());
		this.add(startButton);

		finalFinishLabel = new JLabel("Final Finish Time: ");
		finalFinishLabel.setPreferredSize(new Dimension(200, 130));
		this.add(finalFinishLabel);
	}

	private class StartButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			Object[] args = new Object[1];

			args[0] = new Long(System.currentTimeMillis());

			myTransducer.fireEvent(TChannel.CONTROL_PANEL, TEvent.START_ANIMATIONS, args);
		}
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		if (event == TEvent.ALL_ANIMATIONS_FINISHED) {
			System.out.println("Control Panel received event " + event + " on channel " + channel + " with argument " + args[0]);
			finalFinishLabel.setText("Final Finish Time: " + args[0]);
		}
	}
}
