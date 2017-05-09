package stringCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import exception.CompilationErrorException;
import runtimeTester.Constants;

public class StringCompiler
{
	private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	
	private StringCompiler()
	{}
	
	public static Class<?> compile(String className, CharSequence code, File outputDirectory) throws CompilationErrorException, FileNotFoundException
	{
		if(compiler == null)
			throw new NullPointerException("Compiler is not present. This has yet to be fixed."); //TODO
		
		DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
		
		if(!outputDirectory.isDirectory())
			throw new FileNotFoundException(Constants.ERROR_FILE_IS_NOT_DIRECTORY);

		if(!outputDirectory.exists())
			outputDirectory.mkdirs();
		
		try
		{
			StringJavaFileObject codeFileObject = new StringJavaFileObject(className, code);
			Iterable<? extends JavaFileObject> units = Arrays.asList(codeFileObject);
			Iterable<String> arguments = Arrays.asList("-d", outputDirectory.getPath());
			
			CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, arguments, null, units);
			Boolean result = task.call();

			if(!result)
				throw new CompilationErrorException(diagnosticCollector.getDiagnostics());
			
			URLClassLoader classLoader = new URLClassLoader(new URL[]{outputDirectory.toURI().toURL()});
			
			Class<?> cls = classLoader.loadClass(className);
			
			classLoader.close();
			
			return cls;
		}
		catch (IOException e) //Should never happen, b/c it will be caused by the class loader when the class file is inaccessible
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) //Should never happen, b/c the class should always exist since it was compiled before
		{
			e.printStackTrace();
		}
		catch(RuntimeException e) //Should never happen, b/c it is thrown by compiler.getTast() when illegal arguments are passed (which should not happen)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fileManager.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
