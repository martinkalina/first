package cz.jpower8.scheduler.model.condition;

import cz.jpower8.scheduler.model.ICondition;

public class True implements ICondition {

	@Override
	public boolean evaluate() {
		return true;
	}
	@Override
	public String toString() {
		return Boolean.TRUE.toString();
	}
	
}
