package org.webinit.gwt.client.locks;

import com.google.gwt.user.client.Timer;



public class ReentrantLock implements Lock {
	
	private static int RESOLUTION = 100;
	private Object currentOwner_;
	private Object currentMessage_;
	private int holdCount_;
	
	private static class Pair {
		private Object owner_;
		private Object message_;
		private Pair(Object owner, Object message) {
			owner_ = owner;
			message_ = message;
		}
		protected Object owner() {
			return owner_;
		}
		protected Object message() {
			return message_;
		}
	}
	
//	private Queue<Pair> holdingQueue_;
	
	public ReentrantLock() {
		currentOwner_ = null;
		currentMessage_ = null;
		holdCount_ = 0;
//		holdingQueue_ = new LinkedList<Pair>();
	}
	
	private void queueUntilUnlocked(Pair pair) {
//		holdingQueue_.offer(pair);
		waitUntilUnlocked();
	}

	private void waitUntilUnlocked() {
		Timer timer = new Timer() {
			@Override
			public void run() {
				// this is an empty command
			}
		};
		
		// loop until unlocked
		while (holdCount_ > 0 || currentOwner_ != null || currentMessage_ != null)
			timer.schedule(RESOLUTION);
		
		// eject the holding queue when next done.
//		holdingQueue_.poll();
	}
	
	public void lock(Object owner, Object message) {
		assert owner != null && message != null;
		// BEGIN atomic
		// if currently not locked.
		if (null == currentOwner_ && null == currentMessage_) {
			currentOwner_ = owner;
			currentMessage_ = message;
			holdCount_ ++;
		}
		// add lock counts
		else if (owner == currentOwner_ && message == currentMessage_) {
			holdCount_ ++;
		}
		// queue until unlocked if locked
		else {
			Pair pair = new Pair(owner, message);
			queueUntilUnlocked(pair);
		}
		// END atomic
	}

	public void unlock(Object owner, Object message) {
		assert owner != null && message != null;
		// BEGIN atomic
		if (owner == currentOwner_ && message == currentMessage_) {
			holdCount_ --;
			// release the lock if hold count is about to be zero.
			if (holdCount_ == 0) {
				currentOwner_ = null;
				currentMessage_ = null;
			}
		}
		else {
			throw new IllegalStateException("Invalid Unlock operation by a different owner.");
		}
		// END atomic
	}

}
