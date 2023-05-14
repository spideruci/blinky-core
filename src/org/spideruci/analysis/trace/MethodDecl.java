package org.spideruci.analysis.trace;

import org.spideruci.analysis.trace.events.props.DeclPropNames;

public class MethodDecl extends BaseTraceEvent {

	public long id;
	public String /*final*/ name;
	public String /*final*/ owner;
	public String /*final*/ access;

	
	public MethodDecl(long id, String name, String owner, String access) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.access = access;
	}

	public long getId() {
		return id;
	}
	
	public String getDeclName() {
		return name;
	}
	
	public String getDeclOwner() {
		return owner;
	}
	
	public String getDeclAccess() {
		return access;
	}

	public EventType getType() {
		return EventType.$$method$$;
	}

	
	public int getPropCount() {
		return DeclPropNames.values.length;
	}

	public String getProp(int index) {
		if (index < 0 || index >= this.getPropCount()) {
			return null;
		}
		
		DeclPropNames prop = DeclPropNames.values[index];

		switch(prop) {
		case NAME:
			return this.name;

		case OWNER:
			return this.owner;

		case ACCESS:
			return this.access;
		}
		
		return null;
	}

	public String getPropName(int index) {
		if (index < 0 || index >= this.getPropCount()) {
			return null;
		}
		
		DeclPropNames prop = DeclPropNames.values[index];
		return prop.name();
	}

	public void setProp(int index, String propValue) {
		if (index < 0 || index >= this.getPropCount()) {
			return;
		}
		
		DeclPropNames prop = DeclPropNames.values[index];

		switch(prop) {
		case NAME:
			this.name = propValue;

		case OWNER:
			this.owner = propValue;

		case ACCESS:
			this.access = propValue;
		}
		
		return;
	}

}
