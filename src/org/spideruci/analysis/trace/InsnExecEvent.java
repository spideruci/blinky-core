package org.spideruci.analysis.trace;

import org.spideruci.analysis.trace.events.props.InsnExecPropNames;

public class InsnExecEvent extends BaseTraceEvent {
	
	public final long id;
	public /*final*/ String threadId;
	public /*final*/ String timestamp;
	public /*final*/ String calldepth;
	public /*final*/ String dynHostId;
	public /*final*/ String insnEventId;
	public /*final*/ String insnEventType;
	
	public InsnExecEvent(long id, 
			String threadId,
			String timestamp,
			String calldepth,
			String dynHostId,
			String insnEventId,
			String insnEventType) {
		this.id = id;
		this.threadId = threadId;
		this.timestamp = timestamp;
		this.calldepth = calldepth;
		this.dynHostId = dynHostId;
		this.insnEventId = insnEventId;
		this.insnEventType = insnEventType;
	}
	
	
	// ITraceEvent methods

	@Override
	public long getId() {
		return id;
	}

	@Override
	public EventType getType() {
		return EventType.$$$;
	}

	@Override
	public int getPropCount() {
		return InsnExecPropNames.values.length;
	}

	@Override
	public String getProp(int index) {
		if (index < 0 || index >= this.getPropCount()) {
			return null;
		}
		
		InsnExecPropNames propName = InsnExecPropNames.values[index];
		
		switch (propName) {
		case CALLDEPTH:
			return calldepth;

		case DYN_HOST_ID:
			return dynHostId;
			
		case INSN_EVENT_ID:
			return insnEventId;

		case INSN_EVENT_TYPE:
			return insnEventType;
			
		case THREAD_ID:
			return threadId;

		case TIMESTAMP:
			return timestamp;

		default:
			break;
		}
		
		return null;
	}

	@Override
	public String getPropName(int index) {
		if (index < 0 || index >= this.getPropCount()) {
			return null;
		}
		
		InsnExecPropNames propName = InsnExecPropNames.values[index];
		return propName.name();
	}

	@Override
	public void setProp(int index, String propValue) {
		if (index < 0 || index >= this.getPropCount()) {
			return;
		}
		
		InsnExecPropNames propName = InsnExecPropNames.values[index];
		
		switch (propName) {
		case CALLDEPTH:
			calldepth = propValue;

		case DYN_HOST_ID:
			dynHostId = propValue;
			
		case INSN_EVENT_ID:
			insnEventId = propValue;

		case INSN_EVENT_TYPE:
			insnEventType = propValue;
			
		case THREAD_ID:
			threadId = propValue;

		case TIMESTAMP:
			timestamp = propValue;

		default:
			break;
		
		}
		
	}

}
