package runtimeTester;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import exception.CompilationErrorException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import utils.Utils;

public class RuntimeTesterGUI extends Application
{
	private RuntimeTester runtimeTester;
	private EnvironmentVariableStorage environmentVariableStorage;
	private Stage primaryStage;
	private Class<?> loadedClass;
	private Method loadedMethod;

	private UserInputCodeProcessor preRunCodeProcessor;
	private UserInputCodeProcessor incrementationCodeProcessor;
	private UserInputCodeProcessor parameterSetterProcessor;

	private IPreRunPhase preRunCode;
	private IIncrementationPhase incrementationCode;

	private ArrayList<IParameterSetter> runtimeTesterParameter;

	public RuntimeTesterGUI() throws FileNotFoundException
	{
		runtimeTesterParameter = new ArrayList<>();
		environmentVariableStorage = new EnvironmentVariableStorage();

		// TODO manage absence of *.template files
		preRunCodeProcessor = new UserInputCodeProcessor(
				Utils.fileToString(Constants.PRE_RUN_CODE_TEMPLATE_FILE),
				Constants.PRE_RUN_CODE_CLASS_NAME,
				Constants.USER_INPUT_PROCESSOR_OUTPUT_DIR);
		incrementationCodeProcessor = new UserInputCodeProcessor(
				Utils.fileToString(Constants.INCREMENTATION_CODE_TEMPLATE_FILE),
				Constants.INCREMENTATION_CODE_CLASS_NAME,
				Constants.USER_INPUT_PROCESSOR_OUTPUT_DIR);
		parameterSetterProcessor = new UserInputCodeProcessor(
				Utils.fileToString(
						Constants.PARAMETER_SETTER_CODE_TEMPLATE_FILE),
				Constants.PARAMETER_SETTER_CODE_CLASS_NAME,
				Constants.USER_INPUT_PROCESSOR_OUTPUT_DIR);

		runtimeTester = new RuntimeTester(20, 15, incrementationCode,
				preRunCode);
	}

	@Override
	public void start(Stage primaryStage)
	{
		this.primaryStage = primaryStage;

		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource(Constants.RUNTIME_TESTER_GUI_FXML));

			Parent root = fxmlLoader.load();

			RuntimeTesterGUIController controller = fxmlLoader.getController();

			controller.setRuntimeTesterGui(this);
			controller.initTable();

			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void run(int iterations, int precision)
	{
		try
		{
			runtimeTester.setIterations(iterations);
			runtimeTester.setPrecision(precision);

			runtimeTester.setIncrementationRule(incrementationCode);
			runtimeTester.setPreRunPhase(preRunCode);

			Object instance = loadedClass.getConstructors()[0].newInstance();

			Object[] params = new Object[runtimeTesterParameter.size()];

			for (int i = 0; i < params.length; i++)
			{
				params[i] = runtimeTesterParameter.get(i)
						.getParameter(environmentVariableStorage);
			}

			runtimeTester.run(instance, environmentVariableStorage,
					environmentVariableStorage, loadedMethod, params);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public RuntimeTester getRuntimeTester()
	{
		return runtimeTester;
	}

	public Stage getPrimaryStage()
	{
		return primaryStage;
	}

	public boolean updatePreRunCode(String userInput)
	{
		try
		{
			Class<?> cls = preRunCodeProcessor.process(userInput);

			if (cls.getConstructors().length == 1)
			{
				preRunCode = (IPreRunPhase) cls.getConstructors()[0]
						.newInstance();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (CompilationErrorException e)
		{
			showCompilationError(e.getErrors());
			return false;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean updateIncrementationCode(String userInput)
	{
		try
		{
			Class<?> cls = incrementationCodeProcessor.process(userInput);

			if (cls.getConstructors().length == 1)
			{
				incrementationCode = (IIncrementationPhase) cls
						.getConstructors()[0].newInstance();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (CompilationErrorException e)
		{
			showCompilationError(e.getErrors());
			return false;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean updateRuntimeTesterParameters(ArrayList<String> parameters)
	{
		runtimeTesterParameter.clear();

		for (String parameter : parameters)
		{
			try
			{
				Class<?> cls = parameterSetterProcessor.process(parameter);

				if (cls.getConstructors().length == 1)
				{
					IParameterSetter parameterSetter = (IParameterSetter) cls
							.getConstructors()[0].newInstance();

					runtimeTesterParameter.add(parameterSetter);
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (CompilationErrorException e)
			{
				showCompilationError(e.getErrors());
				return false;
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public EnvironmentVariableStorage getEnvironmentVariableStorage()
	{
		return environmentVariableStorage;
	}

	public Class<?> getLoadedClass()
	{
		return loadedClass;
	}

	public void setLoadedClass(Class<?> loadedClass)
	{
		this.loadedClass = loadedClass;
	}

	public Method getLoadedMethod()
	{
		return loadedMethod;
	}

	public void setLoadedMethod(Method loadedMethod)
	{
		this.loadedMethod = loadedMethod;
	}

	public void showCompilationError(List<Diagnostic<? extends JavaFileObject>> errorList)
	{
		Alert alert = new Alert(AlertType.ERROR);
		
		alert.setTitle("Compilation Error");
		alert.setHeaderText("A critical compilation error has occured.");
		
		StringBuilder errorTextBuilder = new StringBuilder();
		
		for(Diagnostic<? extends JavaFileObject> diagnostic: errorList)
		{
			errorTextBuilder.append(diagnostic.getMessage(Locale.getDefault()));
			errorTextBuilder.append("\n\n");
		}
		
		alert.setContentText(errorTextBuilder.toString());
		
		alert.showAndWait();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
