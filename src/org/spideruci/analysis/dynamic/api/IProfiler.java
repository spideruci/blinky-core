package org.spideruci.analysis.dynamic.api;

import org.spideruci.analysis.trace.EnterExecEvent;
import org.spideruci.analysis.trace.InsnExecEvent;
import org.spideruci.analysis.trace.InvokeInsnExecEvent;
import org.spideruci.analysis.trace.MethodDecl;
import org.spideruci.analysis.trace.TraceEvent;

public interface IProfiler {

  public String description();
  
  public void startProfiling(String description);
  public void endProfiling(String description);
  
  public void willProfile();
  public void profileMethodEntry(final EnterExecEvent e);

  public void profileMethodArgument(final TraceEvent e);
  public void profileMethodInvoke(final InvokeInsnExecEvent e);
  
  public void profileInsn(final InsnExecEvent e);
  
  public void profileFieldInsn(final TraceEvent e);
  public void profileVarInsn(final TraceEvent e);
  public void profileArrayInsn(final TraceEvent e);
  
  public void profileMethodExit(final InsnExecEvent e);
  
  
  public void willInstrumentClass(final String className);
  public void willInstrumentMethod(final MethodDecl e);
  public void willInstrumentCode(final TraceEvent e);

  // TODO:
  // 1. setup didInstrumentXYZ methods
  // 2. have willInstrumentXYZ methods return a bool to let users control which code bits are, or are not instrumented

}


