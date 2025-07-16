package org.spideruci.analysis.dynamic.api;

import org.spideruci.analysis.dynamic.TraceLogger;
import org.spideruci.analysis.trace.EnterExecEvent;
import org.spideruci.analysis.trace.InsnExecEvent;
import org.spideruci.analysis.trace.InvokeInsnExecEvent;
import org.spideruci.analysis.trace.MethodDecl;
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
  public boolean shouldInstrument(String className) {
    return false;
  }

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

    TraceLogger.printEventlog(e);
  }

  @Override
  public void profileMethodArgument(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileMethodArgumentValue(final Object value, final int argIndex, final int argCount, final String methodName, final String corelId) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void profileMethodInvoke(final InvokeInsnExecEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.

    TraceLogger.printEventlog(e);
  }

  @Override
  public void profileInsn(final InsnExecEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
    TraceLogger.printEventlog(e);
  }

  @Override
  public void profileFieldInsn(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
    TraceLogger.printEventlog(e);
  }

  @Override
  public void profileVarInsn(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
    TraceLogger.printEventlog(e);
  }

  @Override
  public void profileArrayInsn(final TraceEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
    TraceLogger.printEventlog(e);
  }

  @Override
  public void profileMethodExit(final InsnExecEvent e) {
    // Convenience stub. Subclasses should appropriate implementation.
    TraceLogger.printEventlog(e);
  }

  @Override
  public void startProfiling(String desc) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void endProfiling(String desc) {
    // Convenience stub. Subclasses should appropriate implementation.
    TraceLogger.printTraceCount();
  }
  
  @Override
  public void emitLogs(final String traceName) {
    // Convenience stub. Subclasses should appropriate implementation.
  }

  @Override
  public void willInstrumentClass(final String className)  { }

  @Override
  public void willInstrumentMethod(final MethodDecl e)  { }

  @Override
  public void willInstrumentCode(final TraceEvent e) { }

  @Override
  public String getLogConfig() {
    return "0";
  }

  @Override
  public String entryClass() {
    return null;
  }

  @Override
  public String entryMethod() {
    return null;
  }

  @Override
  public boolean stopAppInsn() {
    return false;
  }

  @Override
  public boolean canUseFrames() {
    return false;
  }

  @Override
  public boolean allowRetransform() {
    return false;
  }

  @Override
  public boolean enableControlFlowInstrumentation() {
    return false;
  }

  @Override
  public boolean isSafeMode() {
    return false;
  }

  @Override
  public boolean canRecordCallDepth() {
    return false;
  }

  @Override
  public boolean useSourcefileName() {
    return false;
  }


}
