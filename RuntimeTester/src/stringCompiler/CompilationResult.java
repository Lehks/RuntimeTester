package stringCompiler;

import java.io.File;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompilationResult
{
	private Class<?> compiledClass;
	private String errorMessage;
	private File outputFile;
	private ErrorType type;
	private List<Diagnostic<? extends JavaFileObject>> externalErrors;

	public CompilationResult(Class<?> compiledClass, String errorMessage, File outputFile, ErrorType type)
	{
		this.compiledClass = compiledClass;
		this.errorMessage = errorMessage;
		this.outputFile = outputFile;
		this.type = type;
	}
	
	public boolean wasSuccesful()
	{
		return compiledClass != null;
	}
	
	public Class<?> getCompiledClass()
	{
		return compiledClass;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public File getOutputFile()
	{
		return outputFile;
	}
	
	public ErrorType getType()
	{
		return type;
	}
	
	public List<Diagnostic<? extends JavaFileObject>> getExternalErrors()
	{
		return externalErrors;
	}
	
	public enum ErrorType
	{
		INTERNAL,
		EXTERNAL
	}
}
