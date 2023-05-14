package org.spideruci.analysis.trace;

import org.spideruci.analysis.dynamic.Profiler;
import org.spideruci.analysis.dynamic.TraceLogger;
import org.spideruci.analysis.trace.events.props.ArrayInsnExecPropNames;
import org.spideruci.analysis.trace.events.props.DeclPropNames;
import org.spideruci.analysis.trace.events.props.EnterExecPropNames;
import org.spideruci.analysis.trace.events.props.FieldInsnExecPropNames;
import org.spideruci.analysis.trace.events.props.InsnPropNames;
import org.spideruci.analysis.trace.events.props.InvokeInsnExecPropNames;
import org.spideruci.analysis.trace.events.props.VarInsnExecPropNames;

public class EventBuilder {
 
  private static TraceEvent setupBasicExecProperties(TraceEvent event, long id, 
      String dynamicHostId, String insnId, long[] vitalState, 
      EventType insnType) {
    
    long threadId = vitalState[TraceLogger.THREAD_ID];
    long timestamp = vitalState[TraceLogger.TIMESTAMP];
    long calldepth = vitalState[TraceLogger.CALLDEPTH];
    
    event.setExecInsnDynHost(dynamicHostId);
    event.setExecInsnEventId(insnId);
    event.setExecThreadId(String.valueOf(threadId));
    event.setExecTimestamp(String.valueOf(timestamp));
    event.setExecCalldepth(String.valueOf(calldepth));
    event.setExecInsnType(insnType);
    
    return event;
  }
  
  public static InsnExecEvent buildInsnExecEvent(long id, 
		  String dynamicHostId, 
		  String insnId, 
		  EventType insnType, 
		  long[] vitalState) {
	  
	  long threadId = vitalState[TraceLogger.THREAD_ID];
	  long timestamp = vitalState[TraceLogger.TIMESTAMP];
	  long calldepth = vitalState[TraceLogger.CALLDEPTH];

	  InsnExecEvent event = new InsnExecEvent(id, 
			  String.valueOf(threadId), 
			  String.valueOf(timestamp),
			  String.valueOf(calldepth),
			  dynamicHostId,
			  insnId,
			  insnType.toString());

			  return event;
  }
  
  public static EnterExecEvent buildEnterExecEvent(long id, 
		  String dynamicHostId, 
		  String insnId, 
		  long[] vitalState, 
		  String runtimeSignature) {

	  long threadId = vitalState[TraceLogger.THREAD_ID];
	  long timestamp = vitalState[TraceLogger.TIMESTAMP];
	  long calldepth = vitalState[TraceLogger.CALLDEPTH];
	  
	  EnterExecEvent event = 
			  new EnterExecEvent(id, 
					  String.valueOf(threadId), 
					  String.valueOf(timestamp), 
					  String.valueOf(calldepth), 
					  dynamicHostId, 
					  insnId, 
					  runtimeSignature);
	  return event;
  }
  
  public static InvokeInsnExecEvent buildInvokeInsnExecEvent(long id, 
		  String dynamicHostId, 
		  String insnId, 
		  long[] vitalState, 
		  String runtimeSignature) {

	  long threadId = vitalState[TraceLogger.THREAD_ID];
	  long timestamp = vitalState[TraceLogger.TIMESTAMP];
	  long calldepth = vitalState[TraceLogger.CALLDEPTH];

	  InvokeInsnExecEvent event = 
			  new InvokeInsnExecEvent(id, 
					  String.valueOf(threadId), 
					  String.valueOf(timestamp), 
					  String.valueOf(calldepth), 
					  dynamicHostId, 
					  insnId, 
					  runtimeSignature);

	  return event;
  }
  
  public static TraceEvent buildArrayInsnExecEvent(long id, String dynamicHostId, 
      String insnId, EventType insnType, long[] vitalState,
      int arrayRefId, int index, String arrayElement, int arraylength) {
    TraceEvent event = TraceEvent.createArrayInsnExecEvent(id);
    event = setupBasicExecProperties(event, id, dynamicHostId, insnId, vitalState, insnType);
    
    event.setProp(ArrayInsnExecPropNames.ARRAY_ELEMENT, arrayElement);
    event.setProp(ArrayInsnExecPropNames.ARRAYREF_ID, String.valueOf(arrayRefId));
    event.setProp(ArrayInsnExecPropNames.ELEMENT_INDEX, String.valueOf(index));
    event.setProp(ArrayInsnExecPropNames.ARRAY_LENGTH, String.valueOf(arraylength));
    return event;
  }
  
  public static TraceEvent buildVarInsnExecEvent(long id, String dynamicHostId, 
      String insnId, EventType insnType, long[] vitalState, String varId) {
    TraceEvent event = TraceEvent.createVarInsnExecEvent(id);
    event = setupBasicExecProperties(event, id, dynamicHostId, insnId, vitalState, insnType);
    event.setProp(VarInsnExecPropNames.VAR_ID, varId);
    return event;
  }
  
  public static TraceEvent buildFieldInsnExecEvent(long id, String dynamicHostId, 
      String insnId, EventType insnType, long[] vitalState,
      String fieldId, String fieldOwnerId) {
    TraceEvent event = TraceEvent.createFieldInsnExecEvent(id);
    event = setupBasicExecProperties(event, id, dynamicHostId, insnId, vitalState, insnType);
    event.setProp(FieldInsnExecPropNames.FIELD_ID, fieldId);
    event.setProp(FieldInsnExecPropNames.FIELD_OWNER_ID, fieldOwnerId);
    return event;
  }
  
  public static MethodDecl buildMethodDecl(String className, int access, String name) {
	  MethodDecl methodDecl = new MethodDecl(Count.anotherMethod(), name, className, String.valueOf(access));

    if (TraceLogger.profiler != null) {
      TraceLogger.profiler.willInstrumentMethod(methodDecl);
    }

    return methodDecl;
  }
  
  public static String buildInstructionLog(int byteIndex, int lineNum, EventType type, 
      int opcode, long declHostId) {
    return buildInstructionLog(byteIndex, lineNum, type, opcode, declHostId, null, null);
  }
  
  public static String buildInstructionLog(int byteIndex, int lineNum, EventType type, 
      int opcode, long declHostId, String operand) {
    return buildInstructionLog(byteIndex, lineNum, type, opcode, declHostId, operand, null);
  }
  
  public static String buildInstructionLog(int byteIndex, int lineNum, EventType type,
      int opcode, long declHostId, String op1, String op2) {
    return buildInstructionLog(byteIndex, lineNum, type, opcode, declHostId, op1, op2, null);
  }
  
  public static String buildInstructionLog(int byteIndex, int lineNum, EventType type,
      int opcode, long declHostId, String op1, String op2, String op3) {
    final int insnId = Count.anotherInsn();
    TraceEvent insnEvent = TraceEvent.createInsnEvent(insnId, type);
    insnEvent.setProp(InsnPropNames.DECL_HOST_ID, String.valueOf(declHostId));
    insnEvent.setProp(InsnPropNames.LINE_NUMBER, String.valueOf(lineNum));
    insnEvent.setProp(InsnPropNames.BYTECODE_INDEX, String.valueOf(byteIndex));
    insnEvent.setProp(InsnPropNames.OPCODE, String.valueOf(opcode));
    insnEvent.setProp(InsnPropNames.OPERAND1, op1);
    insnEvent.setProp(InsnPropNames.OPERAND2, op2);
    insnEvent.setProp(InsnPropNames.OPERAND3, op3);

    String instructionLog = insnEvent.getLog();

    if (TraceLogger.profiler != null) {
      TraceLogger.profiler.willInstrumentCode(insnEvent);
    }

    if(Profiler.log) {
      Profiler.REAL_OUT.println(instructionLog);
    }
    
    return String.valueOf(insnId);
  }
}
