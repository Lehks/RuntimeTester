package runtimeTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.CompilationErrorException;
import stringCompiler.StringCompiler;

public class UserInputCodeProcessor
{
	private static final Pattern ENVIRONMENTAL_GET_VARIABLE_PATTERN = Pattern
			.compile("get\\((\".*\")\\)");
	private static final Pattern ENVIRONMENTAL_SET_VARIABLE_PATTERN = Pattern
			.compile("set\\((\".*\"),(.*)\\)");

	private static final Pattern CODE_INJECTION_IDENTIFIER_PATTERN = Pattern
			.compile("__CODE___HERE__");
	private static final Pattern CLASS_NAME_INJECTION_IDENTIFIER_PATTERN = Pattern
			.compile("__CLASS__NAME__HERE__");

	private static final String ENVIRONMENTAL_VARIABLE_STORAGE_CALL_GET = "environmentVariableStorage.getData(%s)";
	private static final String ENVIRONMENTAL_VARIABLE_STORAGE_CALL_SET = "environmentVariableStorage.setData(%s, %s)";

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
				throw new FileNotFoundException("File is not a directory.");
			}
		}
	}

	public Class<?> process(String userInput)
			throws CompilationErrorException, FileNotFoundException
	{
		Matcher matcher = CLASS_NAME_INJECTION_IDENTIFIER_PATTERN.matcher(hull);

		if (matcher.find())
			processedCode = matcher.replaceAll(className);

		matcher = CODE_INJECTION_IDENTIFIER_PATTERN.matcher(processedCode);

		if (matcher.find())
			processedCode = matcher.replaceAll(processUserInput(userInput));

		return compile();
	}

	private String processUserInput(String userInput)
	{
		Matcher matcher = ENVIRONMENTAL_GET_VARIABLE_PATTERN.matcher(userInput);

		while (matcher.find())
		{
			String variableName = matcher.group(1).trim();
			userInput = userInput.replaceFirst(
					ENVIRONMENTAL_GET_VARIABLE_PATTERN.pattern(),
					String.format(ENVIRONMENTAL_VARIABLE_STORAGE_CALL_GET,
							variableName));
		}

		matcher = ENVIRONMENTAL_SET_VARIABLE_PATTERN.matcher(userInput);

		while (matcher.find())
		{
			String variableName = matcher.group(1).trim();
			String newValue = matcher.group(2).trim();

			userInput = userInput.replaceFirst(
					ENVIRONMENTAL_SET_VARIABLE_PATTERN.pattern(),
					String.format(ENVIRONMENTAL_VARIABLE_STORAGE_CALL_SET,
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
