package org.spideruci.analysis.trace;

public abstract class BaseTraceEvent implements ITraceEvent {
	
	private static final String SEP = ",";
	private static final String IPD_SEP = "|";

	
	public abstract long getId();

	public abstract EventType getType();

	public String getLog() {
	    StringBuffer buffer = new StringBuffer();
	    EventType type = this.getType();
	    
	    buffer.append(type == null ? "" : type).append(SEP);
	    buffer.append(this.getId()).append(SEP);
	    
	    int lastIndex = this.getPropCount() - 1;
	    for(int i = 0; i < lastIndex; i += 1) {
	      String value = this.getProp(i);
	      buffer.append(value == null ? "" : value).append(SEP);
	    }
	    
	    if (lastIndex >= 0) {
	    	buffer.append(this.getProp(lastIndex));
	    }
	    
	    return buffer.toString();
	}

	// General-purpose Property Access/Setting
	
	public abstract int getPropCount();

	public abstract String getProp(int index);

	public abstract String getPropName(int index);

	public abstract void setProp(int index, String propValue);
	
	
	// QUICKFIX: Park empty implementation for IPD
	
	public void setIpd(String ipd) {
		// do nothing for now.
	}

}
