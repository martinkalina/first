package cz.jpower8.scheduler.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractConditionContainer implements ICondition {

	private final List<ICondition> children = new ArrayList<ICondition>();
	
	public AbstractConditionContainer(){
	}
	public AbstractConditionContainer(ICondition ... children){
		add(children);
	}
	
	public AbstractConditionContainer add(ICondition ... conditions){
		for (ICondition c : conditions) {
			children.add(c);
		}
		return this;
	};

	public Collection<ICondition> getChildren() {
		return Collections.unmodifiableCollection(children);
	}

	public void setChildren(Collection<ICondition> children) {
		children.clear();
		children.addAll(children);
	}
	

}
