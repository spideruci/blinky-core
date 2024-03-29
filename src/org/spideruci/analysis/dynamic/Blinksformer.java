package org.spideruci.analysis.dynamic;

import static org.spideruci.analysis.dynamic.Profiler.REAL_ERR;
import static org.spideruci.analysis.logging.ErrorLogManager.FAILD;
import static org.spideruci.analysis.logging.ErrorLogManager.SKIPD;
import static org.spideruci.analysis.logging.ErrorLogManager.SUXES;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.spideruci.analysis.dynamic.RuntimeClassRedefiner.RedefinitionTargets;
import org.spideruci.analysis.logging.ErrorLogManager;
import org.spideruci.analysis.statik.instrumentation.ClassInstrumenter;
import org.spideruci.analysis.statik.instrumentation.Config;
import org.spideruci.analysis.statik.instrumentation.OfflineInstrumenter;
import org.spideruci.analysis.util.ByteCodePrinter;
import org.spideruci.analysis.util.Constants;

public class Blinksformer implements ClassFileTransformer {

	static final boolean logErrors = false;

	static {
		// System.out.println(System.getProperty("java.class.path"));
	}

	public Blinksformer() {
		super();
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classBytes) throws IllegalClassFormatException {

		File blinkyErrorLogPath = new File(ByteCodePrinter.bytecodePrintPath);

		if(!blinkyErrorLogPath.exists() || !blinkyErrorLogPath.isDirectory()) {
			blinkyErrorLogPath.mkdirs();
		}

		boolean isRetransformTarget = Premain.allowRetransform && RedefinitionTargets.isTarget(className);

		if(!shouldInstrument(className) && !isRetransformTarget) {
			
			if (logErrors) {
				ErrorLogManager.logClassTxStatus(className, false, SKIPD);
			}
			
			return classBytes;
		}

		byte[] instrumentedBytes = instrumentClass(className, classBytes, false /*isRuntime*/);

		return instrumentedBytes;
	}

	/**
	 * @param className
	 * @param classBytes
	 * @return
	 */
	static byte[] instrumentClass(String className, byte[] classBytes, boolean isRuntime) {
		
		byte[] instrumentedBytes = null;

		try {
			OfflineInstrumenter.isActive = isRuntime;
			ClassInstrumenter ins = new ClassInstrumenter();
			instrumentedBytes = ins.instrument(className, classBytes, null);
			OfflineInstrumenter.isActive = false;

			ErrorLogManager.logClassTxStatus(className, isRuntime, SUXES);

			if (logErrors) {
				ByteCodePrinter.printToFile(className, classBytes, instrumentedBytes);
			}

		} catch(Exception ex) {

			instrumentedBytes = classBytes;

			ErrorLogManager.logClassTxStatus(className, isRuntime, FAILD);
			REAL_ERR.println(ex.getMessage());

			if (logErrors) {
				ByteCodePrinter.printToFile(className, classBytes, instrumentedBytes);
				ex.printStackTrace(REAL_ERR);
			}
		}

		return instrumentedBytes;
	}

	public static boolean neverInstrument(String className) {
		boolean isTroublesomeClass =
				className.startsWith("java/lang")
				|| className.startsWith("java/nio/Buffer")
				|| className.startsWith("java/nio/ByteBuffer")
				|| className.startsWith("java/nio/CharBuffer")
				|| className.startsWith("java/nio/HeapCharBuffer")
				|| className.startsWith("java/nio/charset")

				|| className.startsWith("java/io/BufferedOutputStream")
				|| className.startsWith("java/io/BufferedWriter")
				|| className.startsWith("java/io/FileOutputStream")
				|| className.startsWith("java/io/OutputStream")
				|| className.startsWith("java/io/PrintStream")
				|| className.startsWith("java/io/Writer")

				|| className.endsWith("$py")
				|| className.startsWith("org/python/pycode/_pyx")
				|| className.startsWith("org/apache/batik/xml/XMLCharacters")
				;
		
		if (isTroublesomeClass) {
			return true;
		}
		
		final boolean classUnderSpideruciNamespace = 
				className.startsWith(Constants.SPIDER_NAMESPACE)
				|| className.startsWith(Constants.SPIDER_NAMESPACE2);
		
		if (classUnderSpideruciNamespace) {
			boolean isTestSubject = className.contains("test") || className.contains("subject");
			
			if (!isTestSubject) {
				return true;
			}
		}
		
		if(className.contains("Test")
				|| className.contains("Mockito") 
				|| className.contains("Mock")) {
			return true;
		}
		
		return false;
	}

	
	private boolean shouldInstrument(String className) {
		final boolean shouldInstrument = true;
		
		if(Config.checkInclusionList) {
			for(String item : Config.inclusionList) {
				if(logErrors) { REAL_ERR.println("shouldInstrument::checking if starts with: " + item); }
				if(className.startsWith(item)) {
					return shouldInstrument;
				}
			}
		}

		if(logErrors) { REAL_ERR.println("shouldInstrument::checking if starts with: " + Profiler.entryClass); }

		if (Profiler.entryClass != null && className.startsWith(Profiler.entryClass)) {
			return shouldInstrument;
		}  
		
		if (Config.forceCheckInclusionList) {
			return !shouldInstrument;
		}

		if(logErrors) { REAL_ERR.println("Entering shouldInstrument for: " + className); }

		if(neverInstrument(className)) {
			return !shouldInstrument;
		}

		if(logErrors) { REAL_ERR.println("shouldInstrument::finished test namespace checks"); }

		if(RedefinitionTargets.isException(className)) { // TODO abstract this away?
			return !shouldInstrument;
		}

		if(logErrors) { REAL_ERR.println("shouldInstrument::finished redefn isException check"); }

		for(String item : Config.exclusionList) {
			if(className.startsWith(item)) {
				return !shouldInstrument;
			}
		}

		return shouldInstrument;
	}

}
