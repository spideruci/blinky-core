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

public class CallGrapthGenerator extends EmptyProfiler implements IProfiler {
	
	long count = 0;
	long invokeInsnCount = 0; 
	
	HashMap<Long, String> methodLookup = new HashMap<>();
	HashMap<Long, TraceEvent> insnLookup = new HashMap<>();
	HashMap<CallEdge, Long> edgeProfiles = new HashMap<>(); 
	
	HashMap<Long, Stack<Long>> threadedMethodStacks = new HashMap<>();
	
	Stack<Long> getMethodStack(long threadId) {
		if (!threadedMethodStacks.containsKey(threadId)) {
			this.threadedMethodStacks.put(threadId, new Stack<Long>());
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

//	@Override
//	public void willInstrumentCode(final TraceEvent e) {
//		if (e.getType() != EventType.$invoke$) {
//			return;
//		}
//		
//		e.get
//
//	}

	@Override
	public void profileMethodEntry(final EnterExecEvent e) {
		long methodId = Long.parseLong(e.dynHostId);
		long eventId = Long.parseLong(e.insnEventId);
		long threadId = Long.parseLong(e.threadId);
		
		Stack<Long> threadedStack = getMethodStack(threadId);
		
		if (threadedStack.size() > 0) {
			long topMethodId = threadedStack.peek();
			CallEdge edge = new CallEdge(topMethodId, methodId);
			
			synchronized (this.edgeProfiles) {
				long count = edgeProfiles.containsKey(edge) ? edgeProfiles.get(edge) : 1;
				edgeProfiles.put(edge, count);	
			}
		}
		
		threadedStack.push(methodId);
	}

	@Override
	public void profileMethodExit(final InsnExecEvent e) {
		if (e.insnEventType != EventType.$exit$.toString()) {
			return;
		}
		
		long methodId = Long.parseLong(e.dynHostId);
		long threadId = Long.parseLong(e.threadId);  
		
		Stack<Long> threadedStack = getMethodStack(threadId);
		
		// what if the threadedStack was newly created?
		if (threadedStack.isEmpty()) {
			throw new RuntimeException();
		}
		
		
		
	}
	
	@Override
	public void profileMethodInvoke(final InvokeInsnExecEvent e) {
		count += 1;
	}

	@Override
	public void endProfiling(String desc) {
		REAL_OUT.print("invoke insn count:"); 
		REAL_OUT.println(this.invokeInsnCount);
		REAL_OUT.print("invoke count:"); 
		REAL_OUT.println(this.count);
		REAL_OUT.println("endProfiling");
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
		return String.format("%ld-%ld", from, to).hashCode();
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
