package org.spideruci.analysis.dynamic;

import static org.spideruci.analysis.dynamic.Profiler.REAL_OUT;

import java.util.HashMap;

import org.spideruci.analysis.trace.EventType;
import org.spideruci.analysis.trace.TraceEvent;

public class CoverageTracker extends EmptyProfiler implements IProfiler {
  
  HashMap<String, Integer> coverageMap;
  HashMap<Long, String> parentMap = new HashMap<>();
  
  @Override
  public void startProfiling() {
    coverageMap = new HashMap<>();
  }
  
  @Override
  public void willInstrumentClass(final String className)  { 
    REAL_OUT.println("Starting coverage tracker");
  }
  
  @Override
  public void willInstrumentMethod(final TraceEvent e)  { 
    if (e.getType() != EventType.$$method$$) {
      return;
    }
    
    String methodName = e.getDeclName();
    String className = e.getDeclOwner();
    long id = e.getId();
    
    parentMap.put(id, className + "." + methodName);
  }
  
  @Override
  public void profileInsn(final TraceEvent e) {
    if (e.getExecInsnType() != EventType.$line$) {
      return;
    }
    
    int lineNumber = e.getInsnLine();
    int lineParent = e.getInsnDeclHostId(); // the host method of the line
    
    String lineParentName = parentMap.containsKey(lineParent) 
                                  ? parentMap.get(lineParent) 
                                      : lineParent + " " + lineNumber;
    
    if (coverageMap.containsKey(lineParentName)) {
      int count = coverageMap.get(lineParentName);
      coverageMap.put(lineParentName, count + 1);
    } else {
      coverageMap.put(lineParentName, 1);
    }
  }
  
  @Override
  public void endProfiling() {
    for (String line : this.coverageMap.keySet()) {
      if (line == null || line.isEmpty()) {
        continue;
      }
      
      int counter = coverageMap.get(line);
      REAL_OUT.println("LINE: " + line + ":: Coverage Counter:" + counter);
    }
  }
}
