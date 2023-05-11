package org.spideruci.analysis.dynamic;

import static org.spideruci.analysis.dynamic.Profiler.REAL_OUT;

import org.spideruci.analysis.dynamic.api.IProfiler;
import org.spideruci.analysis.statik.instrumentation.Config;
import org.spideruci.analysis.trace.EnterExecEvent;
import org.spideruci.analysis.trace.EventBuilder;
import org.spideruci.analysis.trace.EventType;
import org.spideruci.analysis.trace.InsnExecEvent;
import org.spideruci.analysis.trace.InvokeInsnExecEvent;
import org.spideruci.analysis.trace.TraceEvent;

public class TraceLogger {

  public static final int THREAD_ID = 0;
  public static final int TIMESTAMP = 1;
  public static final int CALLDEPTH = 2;
  
  public static IProfiler profiler = Config.profiler;
  
  private static long count = 0;
  
  
//  synchronized static public void outprintln(final Object object) {
//    outprintln(String.valueOf(object));
//  }
//  
//  synchronized static public void outprintln(final String string) {
//    outprintln(string + "\n");
//  }
//  
//  synchronized static public void outprint(final String string) {
//    synchronized (REAL_OUT) {
//      REAL_OUT.println(string);
//    }
//    
//  }

  synchronized static public void printTraceCount() {
    long ct = System.currentTimeMillis();
    REAL_OUT.println("Trace Size:" + count);
     
    long t = ct - TraceLogger.time;
    long t2 = ct - TraceLogger.premainStartTime;
    REAL_OUT.println("Time Taken:" + t);
    REAL_OUT.println("Time Taken since premain:" + t2);
    REAL_OUT.println("Time Spent in instrumenting classes: " + classInstrumentationTime);
    REAL_OUT.println("Time Spent in instrumenting classes after unsetguard: " + classIntrumentaitonTimeAfterTimeStart);
    TraceLogger.time = System.currentTimeMillis();
    TraceLogger.premainStartTime = TraceLogger.time;
  }
  
  synchronized static public void printTraceCount(long startTime, String comment) {
    long ct = System.currentTimeMillis();
    long t = ct - startTime;
    REAL_OUT.println(comment);
    REAL_OUT.println("Trace Size:" + count);
    REAL_OUT.println("Time Taken:" + t);
    classInstrumentationTime += t;
    if (TraceLogger.time > 0) {
      classIntrumentaitonTimeAfterTimeStart += t;
    }
  }
  
  /**
   * @return new long[] {time-stamp, current-thread-id, calldepth}
   */
  synchronized private static long[] getVitalExecState() {
    Thread currentThread = Thread.currentThread();

    long time = System.currentTimeMillis() - TraceLogger.time;
    long threadId = currentThread.getId();

    long calldepth = -1;
    if(Profiler.callDepth) {
      StackTraceElement[] x = currentThread.getStackTrace();
      /**
       * A subtraction of 4 from the stack trace length is done to 
       * account for the following 4 methods in the call stack:
       * - getStackTrace
       * - getVitalExecState
       * - handleXLog
       * - printlnXLog
       */
      calldepth = x.length - 4;
    }

    return new long[] { threadId, time, calldepth };
  }

  public static String PROBE_WILL_START = "probleWillStart";
  public static String PROBE_WILL_START_DESC = "()V";
  synchronized static public void probleWillStart() {
    profiler.willProfile();
  }
  
  synchronized static public void handleEnterLog(String hostInsnId, String tag) {
    long[] vitalState = getVitalExecState();

    final String runtimeSignature = RuntimeTypeProfiler.getEnterRuntimeSignature(null);
    EnterExecEvent event = EventBuilder.buildEnterExecEvent(++count, tag, hostInsnId, vitalState, runtimeSignature);

    if(profiler == null) {
      printEventlog(event);
      return;
    }

    profiler.profileMethodEntry(event);
  }

	synchronized static public void handleInvokeLog(String insnId, String tag) {
    long[] vitalState = getVitalExecState();

    final String runtimeSignature = RuntimeTypeProfiler.getInvokeRuntimeSignature();
    InvokeInsnExecEvent event = EventBuilder.buildInvokeInsnExecEvent(++count, tag, insnId, vitalState, runtimeSignature);

    if(profiler == null) {
      printEventlog(event);
      return;
    }

    profiler.profileMethodInvoke(event);
  }

  synchronized static public void handleLog(String insnId, String tag, EventType insnType) {
    long[] vitalState = getVitalExecState();

    TraceEvent event = EventBuilder.buildInsnExecEvent(++count, tag, insnId, 
        insnType, vitalState);

    if(profiler == null) {
      printEventlog(event);
      return;
    }
    
    if (insnType == EventType.$exit$) {
      profiler.profileMethodExit(event);
    } else {
      profiler.profileInsn(event);
    }
  }

  synchronized static public void handleArrayLog(String insnId, String tag, EventType insnType, int arrayrefId, int index, String elementId, int length) {
    long[] vitalState = getVitalExecState();

    TraceEvent event = EventBuilder.buildArrayInsnExecEvent(++count, tag, 
        insnId, insnType, vitalState, arrayrefId, index, elementId, length);

    if(profiler == null) {
      printEventlog(event);
      return;
    }

    profiler.profileArrayInsn(event);
  }

  synchronized static public void handleVarLog(String insnId, String tag, String varId) {
    long[] vitalState = getVitalExecState();

    TraceEvent event = EventBuilder.buildVarInsnExecEvent(++count, tag, 
        insnId, EventType.$var$, vitalState, varId);

    if(profiler == null) {
      printEventlog(event);
      return;
    }

    profiler.profileVarInsn(event);
  }

  synchronized static public void handleFieldLog(String insnId, String tag, String fieldId, String fieldOwnerId) {
    long[] vitalState = getVitalExecState();

    TraceEvent event = EventBuilder.buildFieldInsnExecEvent(++count, tag, 
        insnId, EventType.$field$, vitalState, fieldId, fieldOwnerId);

    if(profiler == null) {
      printEventlog(event);
      return;
    }

    profiler.profileFieldInsn(event);
  }

  synchronized static public void handleArgLog(String argType, String index, EventType type, boolean isFirst, boolean isLast) {
    

//     if(isFirst) {
//       long[] vitalState = getVitalExecState();
//       long threadId = vitalState[THREAD_ID];
//       long timestamp = vitalState[TIMESTAMP];
//       long calldepth = vitalState[CALLDEPTH];
//       REAL_OUT.print("$$$," + ++count + "," + threadId + "," + timestamp + "," + calldepth + ",");
//     }

//    REAL_OUT.print(argType + "," + index + "," + type.toString() + 
//        (isLast? "\n" : ","));
  }

  synchronized static private void printEventlog(TraceEvent event) {
    int insnId = Integer.parseInt(event.getExecInsnEventId());
    if(Profiler.stopAppInsn && insnId >= 0) {
      return;
    }
    
    // TODO
    // REAL_OUT.println(event.getLog());
  }
  
  synchronized static private void printEventlog(InsnExecEvent event) {
	  int insnId = Integer.parseInt(event.insnEventId);
	  if(Profiler.stopAppInsn && insnId >= 0) {
		  return;
	  }

	  // REAL_OUT.println(event.getLog());
  }

  public static long premainStartTime = 0;
  public static long time = 0;
  public static long classInstrumentationTime = 0;
  public static long classIntrumentaitonTimeAfterTimeStart = 0;

}
