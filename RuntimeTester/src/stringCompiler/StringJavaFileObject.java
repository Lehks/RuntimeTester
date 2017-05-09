package stringCompiler;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class StringJavaFileObject extends SimpleJavaFileObject
{
	private static final String URI_PREFIX = "string:///";

	private CharSequence code;

	public StringJavaFileObject(String classname, CharSequence code)
	{
		super(URI.create(URI_PREFIX + classname.replace("/", ".")
				+ Kind.SOURCE.extension), Kind.SOURCE);

		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws IOException
	{
		return code;
	}
}