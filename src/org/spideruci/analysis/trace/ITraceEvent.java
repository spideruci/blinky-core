package org.spideruci.analysis.trace;

public interface ITraceEvent {
	public long getId();
	public EventType getType();
	public String getLog();
	
	public int getPropCount();
	public String getProp(int index);
	public String getPropName(int index);
	public void setProp(int index, String propValue);
	
	public void setIpd(String ipd);
}
