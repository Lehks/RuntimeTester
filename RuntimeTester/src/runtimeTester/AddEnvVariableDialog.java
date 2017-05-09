package runtimeTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import exception.CompilationErrorException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Utils;

public class AddEnvVariableDialog extends Stage
{
	private EnvironmentVariableStorage environmentVariableStorage;
	private UserInputCodeProcessor variableValueProcessor;
	private EnvironmentVariableModificationResult result = EnvironmentVariableModificationResult.CANCELED;

	public AddEnvVariableDialog(Stage owner,
			EnvironmentVariableStorage environmentVariableStorage)
			throws FileNotFoundException
	{
		this.environmentVariableStorage = environmentVariableStorage;

		variableValueProcessor = new UserInputCodeProcessor(
				Utils.fileToString(new File(
						"ClassTemplates/ParameterSetterCode.template")),
				"EnvVariableAddValue",  RuntimeTesterGUI.USER_INPUT_PROCESSOR_OUTPUT_DIR);

		initModality(Modality.WINDOW_MODAL);
		initOwner(owner);

		setTitle("Add new Environment Variable");

		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("AddEnvVariable.fxml"));

		try
		{
			Parent parent = loader.load();

			AddEnvVariableDialogController controller = loader.getController();

			controller.setDialog(this);

			Scene scene = new Scene(parent);

			setScene(scene);
		}
		catch (IOException e) // TODO error handling
		{
			e.printStackTrace();
		}
	}

	public EnvironmentVariableModificationResult showDialog()
	{
		showAndWait();

		return result;
	}

	public void addVariable(String name, String value)
	{
		try// TODO error handling
		{
			Class<?> valueObjGetterClass = variableValueProcessor.process(value);
			
			IParameterSetter setter = (IParameterSetter) valueObjGetterClass.getConstructor().newInstance();
			
			boolean addResult = environmentVariableStorage.add(name, setter.getParameter(environmentVariableStorage));

			result = (addResult ? EnvironmentVariableModificationResult.SUCCESS : EnvironmentVariableModificationResult.ALREADY_PRESENT);
		}
		catch (FileNotFoundException | CompilationErrorException e)
		{
			if(e instanceof CompilationErrorException)
			{
				CompilationErrorException e1 = (CompilationErrorException) e;
				
				for(Diagnostic<? extends JavaFileObject> d: e1.getErrors())
				{
					System.out.println(d);
				}
					
			}
			
			result = EnvironmentVariableModificationResult.COMPILE_ERROR;
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}
}
