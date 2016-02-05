package cz.jpower8.scheduler.model;

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
