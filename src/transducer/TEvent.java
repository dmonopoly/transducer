package transducer;

/**
 * Events that the transducer can fire
 * 
 * @author Jon Sun
 */
public enum TEvent {
	// from Agent to Gui
	START_ANIMATION1, START_ANIMATION2,

	// from Gui to Agent
	ANIMATION1_FINISHED, ANIMATION2_FINISHED,

	// from ControlPanel to Agent
	START_ANIMATIONS,

	// from Agent to ControlPanel
	ALL_ANIMATIONS_FINISHED,
}
