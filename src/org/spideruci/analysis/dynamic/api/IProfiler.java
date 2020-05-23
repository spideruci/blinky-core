package org.spideruci.analysis.dynamic.api;

import org.spideruci.analysis.trace.TraceEvent;

public interface IProfiler {

  public String description();
  
  public void startProfiling();
  public void endProfiling();
  
  public void willProfile();
  public void profileMethodEntry(final TraceEvent e);

  public void profileMethodArgument(final TraceEvent e);
  public void profileMethodInvoke(final TraceEvent e);
  
  public void profileInsn(final TraceEvent e);
  
  public void profileFieldInsn(final TraceEvent e);
  public void profileVarInsn(final TraceEvent e);
  public void profileArrayInsn(final TraceEvent e);
  
  public void profileMethodExit(final TraceEvent e);
  
  
  public void willInstrumentClass(final String className);
  public void willInstrumentMethod(final TraceEvent e);
  public void willInstrumentCode(final TraceEvent e);

  // TODO:
  // 1. setup didInstrumentXYZ methods
  // 2. have willInstrumentXYZ methods return a bool to let users control which code bits are, or are not instrumented

}


