package org.spideruci.analysis.trace;

public class EnterExecEvent extends InvokeInsnExecEvent {

	public EnterExecEvent(long id, 
			String threadId, 
			String timestamp, 
			String calldepth, 
			String dynHostId,
			String insnEventId,
			String runtimeSignature) {
		super(id, threadId, timestamp, calldepth, dynHostId, insnEventId, runtimeSignature);
		this.insnEventType = EventType.$enter$.toString();
	}
}