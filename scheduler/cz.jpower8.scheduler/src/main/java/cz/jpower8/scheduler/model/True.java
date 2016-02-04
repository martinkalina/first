package cz.jpower8.scheduler.model;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class True extends Condition {

	@Override
	public boolean evaluate() {
		return true;
	}
	
}
