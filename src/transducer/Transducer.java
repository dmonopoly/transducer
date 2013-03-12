package transducer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This class is the fundamental support for front/back end communication. 
 * Any class implementing the TReceiver interface can use the Transducer to listen to events on any channel.
 * 
 * @author Jon Sun
 */
public class Transducer {
	/**
	 * A mapping of all the properties and Agents that are registered
	 */
	private Map<TChannel, Set<TReceiver>> diverter;

	/**
	 * Default constructor creates an empty map
	 */
	public Transducer() {
		diverter = Collections.synchronizedMap(new HashMap<TChannel, Set<TReceiver>>());
	}

	/**
	 * Registers a Receiver with the Transducer so that the receiver can receive messages. 
	 * The Receiver will receive messages every time an event with the corresponding property is activated.
	 * 
	 * @param toRegister the Receiver to register
	 * @param channel the channel to listen to
	 * @return true if the Receiver was successfully registered, false if they were already registered
	 */
	public synchronized boolean register(TReceiver toRegister, TChannel channel) {
		System.out.println("Transducer: " + "Registering " + toRegister + " on channel " + channel + "...");

		if (toRegister == null) {
			throw new NullPointerException("Cannot register null receivers!");
		} else if (channel == null) {
			throw new NullPointerException("Cannot register null channels!");
		}

		Set<TReceiver> prevRegistered = diverter.get(channel);
		boolean success = false;

		// No Receiver registered to property, create a new entry
		if (prevRegistered == null) {
			System.out.println("Transducer: " + "Creating new channel entry " + channel + " for " + toRegister);

			// create new Receiver set and add it to diverter
			prevRegistered = new CopyOnWriteArraySet<TReceiver>();
			prevRegistered.add(toRegister);
			diverter.put(channel, prevRegistered);
			success = true;
		}
		// Other Agents registered to event, attempt to add new Receiver
		else {
			System.out.println("Transducer: " + "Attempting to add " + toRegister + " to channel entry " + channel);

			// add Receiver to existing set in diverter
			success = prevRegistered.add(toRegister);
		}

		if (success) {
			System.out.println("Transducer: " + toRegister + " successfully registered on channel " + channel);
		}

		return success;
	}

	/**
	 * Unregisters a Receiver with the Transducer so that they stop receiving messages for a specific property.
	 * 
	 * @param toUnregister the Receiver to unregister
	 * @param channel the channel to stop listening to
	 * @return true if the Receiver was successfully unregistered, false if they were never registered
	 */
	public synchronized boolean unregister(TReceiver toUnregister, TChannel channel) {
		System.out.println("Transducer: " + "Registering " + toUnregister + " on channel " + channel + "...");

		if (toUnregister == null) {
			throw new NullPointerException("Cannot unregister null receivers!");
		} else if (channel == null) {
			throw new NullPointerException("Cannot unregister null channels!");
		}

		Set<TReceiver> prevRegistered = diverter.get(channel);
		boolean success;

		// No Receiver registered to property
		if (prevRegistered == null) {
			success = false;
		}
		// Agents registered to property, attempt removal of current Receiver
		else {
			success = prevRegistered.remove(toUnregister);
		}

		if (success) {
			System.out.println("Transducer: " + toUnregister + " successfully unregistered from channel " + channel);
		}

		return success;
	}

	/**
	 * Fires an event on the Transducer. All Receivers listening to the property will be notified. It is the Receiver's responsibility to parse the eventMessage in a meaningful way.
	 * 
	 * @param channel the channel to fire the event on
	 * @param eventMessage the message to send to all listening Receivers
	 * @return the number of Receivers that were notified
	 */
	public synchronized int fireEvent(TChannel channel, TEvent event, Object[] args) {
		System.out.println("Transducer: " + "Firing event " + event + " on channel " + channel);

		if (channel == null) {
			throw new NullPointerException("Cannot fire events on null channels!");
		} else if (event == null) {
			throw new NullPointerException("Cannot fire null events!");
		}

		// look for Receivers listening to the desired channel
		Set<TReceiver> toNotify = diverter.get(channel);
		int numNotified = 0;

		if (toNotify == null) {
			System.out.println("Transducer: " + "Channel " + channel + " has never been registered!");
		} else {
			// fire events to all listening Receivers, if any exist
			if (!toNotify.isEmpty()) {
				for (TReceiver r : toNotify) {
					r.eventFired(channel, event, args);
				}
			}
		}

		return numNotified;
	}
}
