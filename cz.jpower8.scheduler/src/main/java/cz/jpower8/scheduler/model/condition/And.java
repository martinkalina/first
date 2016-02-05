package cz.jpower8.scheduler.model.condition;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import cz.jpower8.scheduler.model.AbstractConditionContainer;
import cz.jpower8.scheduler.model.ICondition;

public class And extends AbstractConditionContainer implements ICondition {

	@Override
	public boolean evaluate() {
		Collection<ICondition> children = getChildren();
		for (ICondition c : children) {
			if (!c.evaluate()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "(" + StringUtils.join(getChildren(), " AND ") + ")";
	}
}
