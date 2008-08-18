package org.webinit.gwt.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.webinit.gwt.rebind.ann.EmptyImplementation;

public class KeyValueCodingObserving_WIProxyImpl implements KeyValueCodingObserving {

	private Map<String, Object> _values = new HashMap<String, Object>();
	private Map<String, Object> _undefined = new HashMap<String, Object>();
	private Map<String, Set<KVCObserver>> observers_ = new HashMap<String, Set<KVCObserver>>();
	
	private static class KVCObserver {
		
		private KeyValueCodingObserving observer_;
		private Set<KeyValueObservingOption> options_;
		private Object context_;
		
		private KVCObserver(KeyValueCodingObserving observer, Set<KeyValueObservingOption> options, Object context) {
			observer_ = observer;
			options_ = options;
			context_ = context;
		}
		
		private KeyValueCodingObserving getObserver() {
			return observer_;
		}
		
		private Set<KeyValueObservingOption> getOptions() {
			return options_;
		}
		
		private Object getContext() {
			return context_;
		}
	}
	
	private void triggerWillChangeValueForKey(String key) {
		Set<KVCObserver> observers = getObservers(key);
		for (KVCObserver theObserver: observers) {
			theObserver.getObserver().willChangeValueForKey(key);
		}
	}
	
	private void triggerDidChangeValueForKey(String key) {
		Set<KVCObserver> observers = getObservers(key);
		for (KVCObserver theObserver: observers) {
			theObserver.getObserver().didChangeValueForKey(key);
		}
	}
	
	private void triggerPriorObserveValueForKey(String key) {
		Set<KVCObserver> observers = getObservers(key);
		
		for (KVCObserver theObserver: observers) {
			KeyValueCodingObserving object = theObserver.getObserver();
			Set<KeyValueObservingOption> options = theObserver.getOptions();
			// fill the change dictionary
			Map<KeyValueChangeKey, Object> change = new HashMap<KeyValueChangeKey, Object>();
			
			if (options.contains(KeyValueObservingOption.Prior)) {
				change.put(KeyValueChangeKey.NotificationIsPrior, true);
			}

			object.observeValueForKey(key, this, change, theObserver.getContext());
		}
	}
	
	private void triggerObserveValueForKey(String key, Object oldValue, Object newValue) {
		Set<KVCObserver> observers = getObservers(key);
		for (KVCObserver theObserver: observers) {
			KeyValueCodingObserving object = theObserver.getObserver();
			Set<KeyValueObservingOption> options = theObserver.getOptions();
			// fill the change dictionary
			Map<KeyValueChangeKey, Object> change = new HashMap<KeyValueChangeKey, Object>();
			
			if (options.contains(KeyValueObservingOption.Old)) {
				change.put(KeyValueChangeKey.Old, oldValue);
			}
			if (options.contains(KeyValueObservingOption.New)) {
				change.put(KeyValueChangeKey.New, newValue);
			}
			
			object.observeValueForKey(key, this, change, theObserver.getContext());
		}
	}
	
	public void setValueForKey(String key, Object value) {
		triggerPriorObserveValueForKey(key);
		triggerWillChangeValueForKey(key);
		Object oldValue = _values.get(key);
		_values.put(key, value);
		triggerDidChangeValueForKey(key);
		triggerObserveValueForKey(key, oldValue, value);
	}

	public void setValueForUndefinedKey(String key, Object value) {
		_undefined.put(key, value);
	}

	public boolean validateValueForKey(String key, Object value) {
		return true;
	}

	public Object valueForKey(String key) {
		if (_values.containsKey(key))
			return _values.get(key);
		else
			return valueForUndefinedKey(key);
	}

	public Object valueForUndefinedKey(String key) {
		return _undefined.get(key);
	}

	private Set<KVCObserver> getObservers(String key) {
		if (observers_.containsKey(key)) {
			return observers_.get(key);
		}
		else {
			Set<KVCObserver> observersForKey = new HashSet<KVCObserver>();
			observers_.put(key, observersForKey);
			return observersForKey;
		}
	}

	public void addObserver(KeyValueCodingObserving object, String key,
			Set<KeyValueObservingOption> options, Object context) {
		Set<KVCObserver> observers = getObservers(key);
		KVCObserver theObserver = new KVCObserver(object, options, context);
		observers.add(theObserver);
		// trigger initial event
		if (options.contains(KeyValueObservingOption.Initial)) {
			// fill the change dictionary
			Map<KeyValueChangeKey, Object> change = new HashMap<KeyValueChangeKey, Object>();

			if (options.contains(KeyValueObservingOption.New)) {
				change.put(KeyValueChangeKey.New, valueForKey(key));
			}
			
			object.observeValueForKey(key, this, change, context);
		}
	}

	public boolean removeObserver(KeyValueCodingObserving object, String key) {
		Set<KVCObserver> observers = getObservers(key);
		for (KVCObserver theObserver: observers) {
			if (theObserver.getObserver().equals(object)) {
				return observers.remove(theObserver);
			}
		}
		return false;
	}

	@EmptyImplementation
	public void didChangeValueForKey(String key) {
	}

	@EmptyImplementation
	public void observeValueForKey(String key, KeyValueCodingObserving object,
			Map<KeyValueChangeKey, Object> change, Object context) {
	}

	@EmptyImplementation
	public void willChangeValueForKey(String key) {
	}

}
