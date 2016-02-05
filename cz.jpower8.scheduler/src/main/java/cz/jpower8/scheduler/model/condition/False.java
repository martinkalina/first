package cz.jpower8.scheduler.model.condition;

import cz.jpower8.scheduler.model.ICondition;

public class False implements ICondition {

	@Override
	public boolean evaluate() {
		return false;
	}
	@Override
	public String toString() {
		return Boolean.FALSE.toString();
	}

}
