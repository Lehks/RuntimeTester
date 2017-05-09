package runtimeTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;

import exception.CompilationErrorException;
import stringCompiler.StringCompiler;

public class UserInputCodeProcessor
{
	private String hull;
	private String className;
	private String processedCode;

	private File outputDirectory;

	public UserInputCodeProcessor(String hull, String className)
			throws FileNotFoundException
	{
		this(hull, className, new File("."));
	}

	public UserInputCodeProcessor(String hull, String className,
			File outputDirectory) throws FileNotFoundException
	{
		this.hull = hull;
		this.className = className;
		this.outputDirectory = outputDirectory;

		if (!outputDirectory.exists())
		{
			outputDirectory.mkdirs();
		}
		else
		{
			if (!outputDirectory.isDirectory())
			{
				throw new FileNotFoundException(Constants.ERROR_FILE_IS_NOT_DIRECTORY);
			}
		}
	}

	public Class<?> process(String userInput)
			throws CompilationErrorException, FileNotFoundException
	{
		Matcher matcher = Constants.USER_INPUT_PROCESSOR_CLASS_NAME_INJECTION_IDENTIFIER_PATTERN.matcher(hull);

		if (matcher.find())
			processedCode = matcher.replaceAll(className);

		matcher = Constants.USER_INPUT_PROCESSOR_CODE_INJECTION_IDENTIFIER_PATTERN.matcher(processedCode);

		if (matcher.find())
			processedCode = matcher.replaceAll(processUserInput(userInput));

		return compile();
	}

	private String processUserInput(String userInput)
	{
		Matcher matcher = Constants.USER_INPUT_PROCESSOR_ENVIRONMENTAL_GET_VARIABLE_PATTERN.matcher(userInput);

		while (matcher.find())
		{
			String variableName = matcher.group(1).trim();
			userInput = userInput.replaceFirst(
					Constants.USER_INPUT_PROCESSOR_ENVIRONMENTAL_GET_VARIABLE_PATTERN.pattern(),
					String.format(Constants.USER_INPUT_PROCESSOR_ENVIRONMENTAL_VARIABLE_STORAGE_CALL_GET,
							variableName));
		}

		matcher = Constants.USER_INPUT_PROCESSOR_ENVIRONMENTAL_SET_VARIABLE_PATTERN.matcher(userInput);

		while (matcher.find())
		{
			String variableName = matcher.group(1).trim();
			String newValue = matcher.group(2).trim();

			userInput = userInput.replaceFirst(
					Constants.USER_INPUT_PROCESSOR_ENVIRONMENTAL_SET_VARIABLE_PATTERN.pattern(),
					String.format(Constants.USER_INPUT_PROCESSOR_ENVIRONMENTAL_VARIABLE_STORAGE_CALL_SET,
							variableName, newValue));
		}

		return userInput;
	}

	private Class<?> compile()
			throws CompilationErrorException, FileNotFoundException
	{
		Class<?> ret = StringCompiler.compile(className, processedCode,
				outputDirectory);

		return ret;
	}

	public void setOutputDirectory(File outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}

	public File getOutputDirectory()
	{
		return outputDirectory;
	}
}
