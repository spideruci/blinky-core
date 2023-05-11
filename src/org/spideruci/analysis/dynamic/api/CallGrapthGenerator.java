package org.spideruci.analysis.dynamic.api;

import static org.spideruci.analysis.dynamic.Profiler.REAL_OUT;

import org.spideruci.analysis.trace.EnterExecEvent;
import org.spideruci.analysis.trace.EventType;
import org.spideruci.analysis.trace.InsnExecEvent;
import org.spideruci.analysis.trace.TraceEvent;

public class CallGrapthGenerator extends EmptyProfiler implements IProfiler {


	@Override
	public String description() {
		return "CallGrapthGenerator";
	}

	@Override
	public void startProfiling() {
		REAL_OUT.println("Starting traceing");
	}

	@Override
	public void willInstrumentClass(final String className) {
		REAL_OUT.print("Instrumented Class = ");
		REAL_OUT.println(className);
		REAL_OUT.println();
	}

	@Override
	public void willInstrumentMethod(final TraceEvent e) {
		if (e.getType() != EventType.$$method$$) {
			return;
		}
		String methodName = e.getDeclName();
		String className = e.getDeclOwner();
		long ID = e.getId();
		REAL_OUT.println();
		REAL_OUT.print(e.getType() + "  ");
		REAL_OUT.print(methodName + "  " + className);
		REAL_OUT.println(" ID=" + ID);
	}

	@Override
	public void willInstrumentCode(final TraceEvent e) {
		if (e.getType() == EventType.$enter$ || e.getType() == EventType.$return$) {
			REAL_OUT.println(e.getType() + " " + e.getId() + " " + e.getInsnDeclHostId());
		} else if(e.getType() == EventType.$athrow$) {
			REAL_OUT.println(e.getType() + " " + e.getId() + " " + e.getInsnDeclHostId());
			return;
		}
	}

	@Override
	public void profileMethodEntry(final EnterExecEvent e) {
		REAL_OUT.print(e.getType());
		REAL_OUT.print("  " + e.insnEventType);
		REAL_OUT.print("  EventId=" + e.insnEventId);
		REAL_OUT.print(" Calldepth=" + e.calldepth);
		REAL_OUT.println(" Timestamp=" + e.timestamp);
	}

	@Override
	public void profileMethodExit(final InsnExecEvent e) {
		REAL_OUT.print(e.getType());
		REAL_OUT.print("  " + e.insnEventType);
		REAL_OUT.print("  EventId=" + e.insnEventId);
		REAL_OUT.print(" Calldepth=" + e.calldepth);
		REAL_OUT.println(" Timestamp=" + e.timestamp);
	}

	@Override
	public void endProfiling() {
		REAL_OUT.println("endProfiling");
	}
  
}
