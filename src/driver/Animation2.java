package driver;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;

public class Animation2 extends JLabel implements TReceiver, ActionListener {
	private Transducer myTransducer;

	private boolean animationInProgress = false;
	private int stage = 1;
	private ImageIcon[] icons;
	private int MAX_STAGE;

	public Animation2(Transducer transducer) {
		this.setPreferredSize(new Dimension(50, 50));
		this.setSize(42, 42);

		MAX_STAGE = 10;
		icons = new ImageIcon[MAX_STAGE + 1];

		for (int i = 1; i <= MAX_STAGE; i++) {
			String path = "imageicons" + File.separator + "anim2_" + i + ".png";
			icons[i] = new ImageIcon(path);
		}

		this.setIcon(icons[1]);
		myTransducer = transducer;
		transducer.register(this, TChannel.GUI); // NOTE
	}

	private void startAnimation() {
		if (animationInProgress) {
			throw new IllegalStateException("Animation already in progress!");
		} else {
			animationInProgress = true;
		}
	}

	@Override
	public void eventFired(TChannel channel, TEvent event, Object[] args) {
		// System.out.println("---Animation2 received event fired");
		if (event == TEvent.START_ANIMATION2) {
			System.out.println("Animation2: Starting Animation");
			this.startAnimation();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (animationInProgress) {
			stage++;

			if (stage <= MAX_STAGE) {
				this.setIcon(icons[stage]);
			} else {
				animationInProgress = false;
				Object[] args = new Object[1];
				args[0] = new Long(System.currentTimeMillis());
				myTransducer.fireEvent(TChannel.GUI, TEvent.ANIMATION2_FINISHED, args);
			}
		}
	}
}
