package lab;

import transducer.TChannel;
import transducer.TEvent;
import transducer.TReceiver;
import transducer.Transducer;

public class LabAgent implements TReceiver {
	private boolean an1Finished = false;
	private boolean an2Finished = false;
	private Transducer transducer;

	private long startTime;

	// Times are grabbed, but not really used...
	private long an1FinishTime;
	private long an2FinishTime;

	// the Agent needs to be able to activate the animations in order.
	// perhaps it requires some extra data in order to know what to do next?

	public LabAgent(Transducer t) {
		transducer = t;
		// remember that you only need to register with channels you want to LISTEN to.
		// you can fire events on channels even if you are not registered on them!

		// this means that the labagent listens to that channel
		transducer.register(this, TChannel.CONTROL_PANEL); // to listen to LabControlPanel's start button
		transducer.register(this, TChannel.GUI); // to respond to Animation1 and Animation2
	}

	@Override
	public synchronized void eventFired(TChannel channel, TEvent event, Object[] args) {
		System.out.println("LabAgent received event " + event + " on channel " + channel);
		// Expecting event firing from LabControlPanel on CONTROL_PANEL channel
		if (channel == TChannel.CONTROL_PANEL) {
			if (event == TEvent.START_ANIMATIONS) {
				// Grab start time
				startTime = (Long) args[0];

				System.out.println("Starting Animations!");
				transducer.fireEvent(TChannel.GUI, TEvent.START_ANIMATION1, null);
				// transducer.fireEvent(TChannel.GUI, TEvent.START_ANIMATION2, null);
			}
		}

		// Expecting event firing from Animation1 and Animation2 on GUI channel
		if (channel == TChannel.GUI) {
			if (event == TEvent.ANIMATION1_FINISHED) {
				an1Finished = true;

				// Grab finish time (might as well since it is sent)
				an1FinishTime = (Long) args[0];

				System.out.println("LabAgent knows animation 1 is finished; launching Animation2");
				transducer.fireEvent(TChannel.GUI, TEvent.START_ANIMATION2, null);
			}
			if (event == TEvent.ANIMATION2_FINISHED) {
				an2Finished = true;

				// Grab finish time (might as well since it is sent)
				an2FinishTime = (Long) args[0];

				System.out.println("LabAgent knows animation 2 is finished");
			}
			
			if (an1Finished && an2Finished) {
				System.out.println("LabAgent realizes both animations are done.");
				an1Finished = false; // make sure to set this before the event is fired!
				an2Finished = false;

				// Prepare parameters to pass
				Object[] argsToPass = new Object[1];
				long endTime = new Long(System.currentTimeMillis());
				argsToPass[0] = new Long(endTime - startTime);

				transducer.fireEvent(TChannel.CONTROL_PANEL, TEvent.ALL_ANIMATIONS_FINISHED, argsToPass);
			}
		}
	}
}
