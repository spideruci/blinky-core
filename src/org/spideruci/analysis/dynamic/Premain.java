package org.spideruci.analysis.dynamic;

import static org.spideruci.analysis.dynamic.Profiler.REAL_ERR;
import static org.spideruci.analysis.dynamic.Profiler.REAL_OUT;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;

import org.spideruci.analysis.statik.instrumentation.Config;

/**
 * WARNING: DO NOT HAVE ANY NON_STATIC IMPORTS (i.e. DEPENDENCIES) 
 * IN PREMAIN. FOR SOME REASON IT BLOCKS THE INSTRUMENTAION.
 * @author vpalepu
 *
 */
public class Premain {
  
  public static boolean allowRetransform = false;
  public static boolean started = false;
  public static boolean ended = false;
  
  public static void premain(String agentArguments, 
      Instrumentation instrumentation) {
    TraceLogger.premainStartTime = System.currentTimeMillis();
    boolean tempGuard = Profiler.$guard1$; 
    Profiler.$guard1$ = true;
    
    started = true;

    Profiler.initPrintStreams();
    
    REAL_OUT.println("Premain");
    var split = agentArguments.split(";");
    var profilerClass = split[0];
    REAL_OUT.println(profilerClass);

    try {
      Class.forName(profilerClass).getMethod("init").invoke(null);
    } catch (
      IllegalAccessException 
      | IllegalArgumentException 
      | InvocationTargetException 
      | NoSuchMethodException 
      | SecurityException 
      | ClassNotFoundException e
    ) {
      REAL_ERR.println("thrown " + e.getMessage());
      e.printStackTrace();
    }


    if (Config.profiler == null) {
      REAL_OUT.println("Fatal Error: Config.profiler is null");
      REAL_ERR.println("Fatal Error: Config.profiler is null");
      System.exit(1);
    } else {
      REAL_OUT.println("Using Profiler");
      REAL_OUT.println(Config.profiler.description());
    }

    if (split.length >= 2) {
      var instrumentationScope = split[1];
      Config.profiler.setInstrumentationScope(instrumentationScope);
    }
    
    Profiler.initPrintStreams();
    Profiler.initProfilerFlags(Config.profiler);
    Profiler.initLogConfig(Config.profiler.getLogConfig());
    
    instrumentation.addTransformer(new Blinksformer());
    
    if(Premain.allowRetransform && instrumentation.isRetransformClassesSupported()) {
      REAL_ERR.println("retransforming!");
      instrumentation.addTransformer(new RuntimeClassRedefiner(), true);
      
      Class<?>[] loadedClasses = instrumentation.getAllLoadedClasses();
      for(Class<?> loadedClass : loadedClasses) {
        if(!instrumentation.isModifiableClass(loadedClass)) {
          continue;
        }
        // try {
        //   instrumentation.retransformClasses(loadedClass);
        // } catch (UnmodifiableClassException e) {
        //   REAL_ERR.println(loadedClass);
        //   e.printStackTrace(REAL_ERR);
        // }
      }
    } else {
      REAL_ERR.println("FEEDBACK: Class Retransformation is disabled.");
    }
    
    ended = true;
    Profiler.$guard1$ = tempGuard;
    
    synchronized (Profiler.REAL_OUT) {
      Profiler.REAL_OUT.println("Premain:" + Profiler.$guard1$);
    }
    
  }
}
