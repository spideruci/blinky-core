package org.spideruci.analysis.trace;

import org.spideruci.analysis.trace.events.props.InvokeInsnExecPropNames;

public class InvokeInsnExecEvent extends InsnExecEvent {

	public /*final*/ String runtimeSignature;
	
	public InvokeInsnExecEvent(long id, 
			String threadId, 
			String timestamp, 
			String calldepth, 
			String dynHostId,
			String insnEventId,
			String runtimeSignature) {
		super(id, threadId, timestamp, calldepth, dynHostId, insnEventId, EventType.$invoke$.toString());
		this.runtimeSignature = runtimeSignature;
	}
	
	@Override
	public int getPropCount() {
		return InvokeInsnExecPropNames.values.length;
	}

	@Override
	public String getProp(int index) {
		if (index < 0 || index >= this.getPropCount()) {
			return null;
		}
		
		// check 
		String propValue = super.getProp(index);
		
		if (propValue != null) {
			return propValue;
		}
		
		InvokeInsnExecPropNames propName = InvokeInsnExecPropNames.values[index];
		
		if (propName == InvokeInsnExecPropNames.RUNTIME_SIGNATURE) {
			return runtimeSignature;
		}
		
		return null;
	}

	@Override
	public String getPropName(int index) {
		if (index < 0 || index >= this.getPropCount()) {
			return null;
		}
		
		InvokeInsnExecPropNames propName = InvokeInsnExecPropNames.values[index];
		return propName.name();
	}

	@Override
	public void setProp(int index, String propValue) {
		if (index < 0 || index >= this.getPropCount()) {
			return;
		}
		
		InvokeInsnExecPropNames propName = InvokeInsnExecPropNames.values[index];
		
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
			
		case RUNTIME_SIGNATURE:
			runtimeSignature = propValue;

		default:
			break;
		
		}
		
	}

}
