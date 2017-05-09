package exception;

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompilationErrorException extends Exception
{
	private static final long serialVersionUID = -6865259205655851776L;

	private List<Diagnostic<? extends JavaFileObject>> errors;
	
	public CompilationErrorException(List<Diagnostic<? extends JavaFileObject>> errors)
	{
		this.errors = errors;
	}

	public CompilationErrorException(String message, List<Diagnostic<? extends JavaFileObject>> errors)
	{
		super(message);
		this.errors = errors;
	}

	public CompilationErrorException(Throwable cause, List<Diagnostic<? extends JavaFileObject>> errors)
	{
		super(cause);
		this.errors = errors;
	}

	public CompilationErrorException(String message, Throwable cause, List<Diagnostic<? extends JavaFileObject>> errors)
	{
		super(message, cause);
		this.errors = errors;
	}

	public CompilationErrorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace, List<Diagnostic<? extends JavaFileObject>> errors)
	{
		super(message, cause, enableSuppression, writableStackTrace);
		this.errors = errors;
	}

	public List<Diagnostic<? extends JavaFileObject>> getErrors()
	{
		return errors;
	}
}
