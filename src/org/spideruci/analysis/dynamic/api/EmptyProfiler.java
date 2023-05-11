package org.spideruci.analysis.dynamic.api;

import org.spideruci.analysis.trace.EnterExecEvent;
import org.spideruci.analysis.trace.InsnExecEvent;
import org.spideruci.analysis.trace.InvokeInsnExecEvent;
import org.spideruci.analysis.trace.TraceEvent;

/**
 * A no-op profiler that subclasses can use to inherit from.
 * The stub implementations themselves do not do anything much, but they
 * help avoid setup empty declarations for all methods for an IProfiler
 * implementation.
 *
 * @author vpalepu
 */
public class EmptyProfiler implements IProfiler {

  @Override
  public String description() {
    return "EmptyProfiler";
  }

  @Override
  public void willProfile() {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileMethodEntry(final EnterExecEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileMethodArgument(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileMethodInvoke(final InvokeInsnExecEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileInsn(final InsnExecEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileFieldInsn(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileVarInsn(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileArrayInsn(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileMethodExit(final InsnExecEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void startProfiling() {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void endProfiling() {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void willInstrumentClass(final String className)  { }

  @Override
  public void willInstrumentMethod(final TraceEvent e)  { }

  @Override
  public void willInstrumentCode(final TraceEvent e) { }

}
