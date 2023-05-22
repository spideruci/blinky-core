package org.spideruci.analysis.dynamic.api;

import static org.spideruci.analysis.dynamic.Profiler.REAL_OUT;

import java.util.HashMap;
import java.util.Stack;

import org.spideruci.analysis.trace.EnterExecEvent;
import org.spideruci.analysis.trace.EventType;
import org.spideruci.analysis.trace.InsnExecEvent;
import org.spideruci.analysis.trace.InvokeInsnExecEvent;
import org.spideruci.analysis.trace.MethodDecl;
import org.spideruci.analysis.trace.TraceEvent;

public class CallGraphGenerator extends EmptyProfiler implements IProfiler {
	
	long count = 0;
	long invokeInsnCount = 0; 
	
	HashMap<Long, String> methodLookup = new HashMap<>();
	HashMap<Long, TraceEvent> insnLookup = new HashMap<>();
	HashMap<CallEdge, Long> edgeProfiles = new HashMap<>(); 
	
	HashMap<Long, Stack<MethodFrame>> threadedMethodStacks = new HashMap<>();
	
	Stack<MethodFrame> getMethodStack(long threadId) {
		if (!threadedMethodStacks.containsKey(threadId)) {
			this.threadedMethodStacks.put(threadId, new Stack<MethodFrame>());
		}
		
		return this.threadedMethodStacks.get(threadId);
	}


	@Override
	public String description() {
		return "CallGrapthGenerator";
	}

	@Override
	public void startProfiling(String desc) {
		REAL_OUT.println("Starting tracing");
	}

	@Override
	public void willInstrumentMethod(final MethodDecl e) {
		
		String name = e.owner + e.name;
		long id = e.id;
		
		methodLookup.put(id, name);
	}

	@Override
	public void willInstrumentCode(final TraceEvent e) {
		if (e.getType() != EventType.$invoke$) {
			return;
		}
		
		// DO NOTHING for now.

	}

	@Override
	public void profileMethodEntry(final EnterExecEvent e) {
		long methodId = Long.parseLong(e.dynHostId);
		long eventId = Long.parseLong(e.insnEventId);
		long threadId = Long.parseLong(e.threadId);
		
		Stack<MethodFrame> threadedStack = getMethodStack(threadId);
		
		if (threadedStack.size() > 0) {
			MethodFrame methodFrame = threadedStack.peek();
			long topMethodId = methodFrame.methodId;
			CallEdge edge = new CallEdge(topMethodId, methodId);
			
			synchronized (this.edgeProfiles) {
				long currentCount = edgeProfiles.containsKey(edge) ? edgeProfiles.get(edge) : 0;
				edgeProfiles.put(edge, ++currentCount);	
			}
		}
		
		threadedStack.push(new MethodFrame(methodId, eventId));
	}

	@Override
	public void profileMethodExit(final InsnExecEvent e) {
		if (e.insnEventType != EventType.$exit$.toString()) {
			return;
		}
		
		long methodId = Long.parseLong(e.dynHostId);
		long dynamicId = Long.parseLong(e.calldepth);
		long eventId = e.id;
		long threadId = Long.parseLong(e.threadId);  
		
		Stack<MethodFrame> threadedStack = getMethodStack(threadId);
		
		while(!threadedStack.isEmpty()) {
			MethodFrame topMethodFrame = threadedStack.peek();
			
			long topMethodId = topMethodFrame.methodId;
			long topDynamicId = topMethodFrame.dynamicId;
			
			if (dynamicId == topDynamicId) {
				if (topMethodId != methodId) {  
					// error
				}
				
				threadedStack.pop();
				break;
				
			} else {
				
				long exitingMethodStart = dynamicId;
				long exitingMethodEnd = eventId;
				
				if (topDynamicId >= exitingMethodStart || topDynamicId >= exitingMethodEnd) {
					// the exiting method starts and/or ends, before the top method of the stack even
					// started.
					threadedStack.pop();
					continue;
				}
				// else: we are looking at a method exit that happened _after_ the topMethod started,
				// for a method that started _after_ the topMethod started.
				// nothing else to do here, so break out.
				break;
			}
		}
	}
	
	@Override
	public void profileMethodInvoke(final InvokeInsnExecEvent e) {
		count += 1;
	}

	@Override
	public void endProfiling(String desc) {
		this.threadedMethodStacks.clear();
		
		REAL_OUT.print("invoke insn count:"); 
		REAL_OUT.println(this.invokeInsnCount);
		REAL_OUT.print("invoke count:"); 
		REAL_OUT.println(this.count);
		REAL_OUT.println("endProfiling");
	}
	
	public void emitLogs() {
		REAL_OUT.printf("METHODS (%d)\n", methodLookup.size());
		
		for (var entry : methodLookup.entrySet()) {
			REAL_OUT.printf("%d, %s\n", entry.getKey(), entry.getValue());
		}
		
		REAL_OUT.printf("CALLS (%d)\n", edgeProfiles.size());
		
		for (var entry : edgeProfiles.entrySet()) {
			CallEdge edge = entry.getKey();
			long count = entry.getValue();
			REAL_OUT.printf("%d,%d,%d\n", edge.from, edge.to, count);
		}
	}
  
}

class MethodFrame {
	final public long methodId;
	final public long dynamicId;
	
	public MethodFrame(long id, long dynId) {
		this.methodId = id;
		this.dynamicId = dynId;
	}
	
	@Override
	public int hashCode() {
		return String.format("%d-%d", methodId, dynamicId).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MethodFrame)) {
			return false;
		}
		
		MethodFrame other = (MethodFrame) obj;
		return this.methodId == other.methodId && this.dynamicId == other.dynamicId;
	}
	
}

class CallEdge {
	final public long from;
	final public long to;
	
	public CallEdge(long caller, long callee) {
		this.from = caller;
		this.to = callee;
	}
	
	@Override
	public int hashCode() {
		return String.format("%d-%d", from, to).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CallEdge)) {
			return false;
		}
		
		CallEdge other = (CallEdge) obj;
		return this.from == other.from && this.to == other.to;
	}
}
