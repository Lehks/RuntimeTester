package runtimeTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import exception.CompilationErrorException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Utils;

public class ModEnvVariableDialog extends Stage
{
	private EnvironmentVariableStorage environmentVariableStorage;
	private EnvironmentVariableStorage.StorageEntry entry;
	private UserInputCodeProcessor variableValueProcessor;
	private EnvironmentVariableModificationResult result = EnvironmentVariableModificationResult.CANCELED;

	public ModEnvVariableDialog(Stage owner,
			EnvironmentVariableStorage environmentVariableStorage,
			EnvironmentVariableStorage.StorageEntry entry)
			throws FileNotFoundException
	{
		this.environmentVariableStorage = environmentVariableStorage;
		this.entry = entry;
		this.variableValueProcessor = new UserInputCodeProcessor(
				Utils.fileToString(new File(
						"ClassTemplates/ParameterSetterCode.template")),
				"EnvVariableModValue",
				RuntimeTesterGUI.USER_INPUT_PROCESSOR_OUTPUT_DIR);

		initOwner(owner);
		initModality(Modality.WINDOW_MODAL);

		setTitle("Modify Environmental Variable");

		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("ModEnvVariable.fxml.fxml"));

		try
		{
			Parent parent = loader.load();

			ModEnvVariableDialogController controller = loader.getController();

			controller.setup(entry.getData(), this);

			Scene scene = new Scene(parent);

			setScene(scene);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public EnvironmentVariableModificationResult showDilalog()
	{
		showAndWait();

		return result;
	}

	public void modify(String newValue)
	{
		try
		{
			Class<?> valueObjGetterClass = variableValueProcessor
					.process(newValue);

			IParameterSetter setter = (IParameterSetter) valueObjGetterClass
					.getConstructor().newInstance();

			entry.setData(setter.getParameter(environmentVariableStorage));

			result = EnvironmentVariableModificationResult.SUCCESS;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			result = EnvironmentVariableModificationResult.COMPILE_ERROR;
		}
		catch (CompilationErrorException e)
		{
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
