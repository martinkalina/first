package cz.jpower8.scheduler.model.condition;

import cz.jpower8.scheduler.model.ICondition;

public class Not implements ICondition {
	
	private ICondition condition;
	
	public Not(ICondition condition) {
		this.condition = condition;
	}

	public Not() {
	}

	@Override
	public boolean evaluate() {
		return !condition.evaluate();
	}
	
	@Override
	public String toString() {
		return "NOT("+condition+")";
	}

}
