package cz.jpower8.scheduler.model;

import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class Condition {

	abstract boolean evaluate();
	
}
