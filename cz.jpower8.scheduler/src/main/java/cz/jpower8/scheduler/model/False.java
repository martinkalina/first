package cz.jpower8.scheduler.model;

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
