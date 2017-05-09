package runtimeTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class RuntimeTesterGUIController
{
	private RuntimeTesterGUI runtimeTesterGui;
	private DirectoryChooser classFileChooser;
	private URI selectedClassFileURI;

	@FXML
	private Button btnRunTest;

	@FXML
	private ChoiceBox<MethodNameBundle> choiceBoxSelectMethod;

	@FXML
	private Label progressLabel;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Spinner<Integer> spinnerIterations;

	@FXML
	private Spinner<Integer> spinnerPrecision;

	@FXML
	private LineChart<Number, Number> resultChart;

	@FXML
	private TableView<EnvironmentVariableStorage.StorageEntry> tableEnvVariables;

	@FXML
	private TableColumn<EnvironmentVariableStorage.StorageEntry, String> tableColumnDatatype;

	@FXML
	private TableColumn<EnvironmentVariableStorage.StorageEntry, String> tableColumnName;

	@FXML
	private TableColumn<EnvironmentVariableStorage.StorageEntry, String> tableColumnValue;

	@FXML
	private TextField txtFieldClassFile;

	@FXML
	private TextField txtFieldClassName;

	@FXML
	private TextArea txtAreaIncrementationPhaseCode;

	@FXML
	private TextArea txtAreaPreRunPhaseCode;

	@FXML
	private VBox vBoxSelectedClassParams;

	@FXML
	private VBox vBoxSelectedMethodParams;

	private ArrayList<MethodParameterSelectorController> methodParamControllers = new ArrayList<>();

	@FXML
	public void onBtnEnvVarAddAction(ActionEvent event)
	{
		addVariable();
	}

	@FXML
	public void onBtnEnvVarDelAction(ActionEvent event)
	{
		delVariable();
	}

	@FXML
	public void onBtnEnvVarModAction(ActionEvent event)
	{
		modVariable();
	}

	@FXML
	public void onTtoolBarBtnAddVarAction(ActionEvent event)
	{
		addVariable();
	}

	@FXML
	public void onTtoolBarBtnDelVarAction(ActionEvent event)
	{
		delVariable();
	}

	@FXML
	public void onTtoolBarBtnModVarAction(ActionEvent event)
	{
		modVariable();
	}

	@FXML
	public void onBtnRunTestAction(ActionEvent event)
	{
		runTest();
	}

	@FXML
	public void onBtnSelectNewClassAction(ActionEvent event)
	{
		loadClass();
	}

	@FXML
	public void onMenuItemExport(ActionEvent event)
	{
		exportResults();
	}

	@FXML
	public void onMenuItemAddVarAction(ActionEvent event)
	{
		addVariable();
	}

	@FXML
	public void onMenuItemDelVarAction(ActionEvent event)
	{
		delVariable();
	}

	@FXML
	public void onMenuItemModVarAction(ActionEvent event)
	{
		modVariable();
	}

	@FXML
	public void onTxtFieldClassNameAction(ActionEvent event)
	{
		loadMethods();
	}

	@FXML
	public void onTableKeyReleased(KeyEvent event)
	{
		if (event.getCode() == KeyCode.DELETE)
		{
			delVariable();
		}
	}

	@FXML
	public void initialize()
	{

		classFileChooser = new DirectoryChooser();
		classFileChooser.setInitialDirectory(new File("."));

		spinnerIterations.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
						Integer.MAX_VALUE, 1));
		spinnerPrecision.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
						Integer.MAX_VALUE, 1));

		resultChart.getXAxis().setLabel(Constants.RUNTIME_TESTER_GUI_CONTROLLER_CHART_AXIS_X_LABEL);
		resultChart.getYAxis().setLabel(Constants.RUNTIME_TESTER_GUI_CONTROLLER_CHART_AXIS_Y_LABEL);

		choiceBoxSelectMethod.valueProperty()
				.addListener(new ChangeListener<MethodNameBundle>()
				{

					@Override
					public void changed(
							ObservableValue<? extends MethodNameBundle> observable,
							MethodNameBundle oldValue,
							MethodNameBundle newValue)
					{
						vBoxSelectedMethodParams.getChildren().clear();

						for (Parameter param : newValue.method.getParameters())
						{
							addParameterSelection(param);
							runtimeTesterGui.setLoadedMethod(newValue.method);
						}
					}
				});
	}

	public void initTable()
	{
		tableEnvVariables.getSelectionModel()
				.setSelectionMode(SelectionMode.SINGLE);
		tableEnvVariables.setItems(runtimeTesterGui
				.getEnvironmentVariableStorage().entriesProperty());
		tableColumnDatatype
				.setCellValueFactory(new PropertyValueFactory<>("type"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnValue
				.setCellValueFactory(new PropertyValueFactory<>("data"));

		tableColumnValue.setEditable(true);
	}

	private void addVariable()
	{
		try
		{
			AddEnvVariableDialog dialog = new AddEnvVariableDialog(
					runtimeTesterGui.getPrimaryStage(),
					runtimeTesterGui.getEnvironmentVariableStorage());

			dialog.showDialog();

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private void delVariable()
	{
		EnvironmentVariableStorage.StorageEntry entry = getSelectedEntry();
		if (entry != null)
		{
			runtimeTesterGui.getEnvironmentVariableStorage()
					.remove(entry.getName());
		}
	}

	private void modVariable()
	{
		EnvironmentVariableStorage.StorageEntry entry = getSelectedEntry();

		if (entry != null)
		{
			try
			{
				ModEnvVariableDialog dialog = new ModEnvVariableDialog(
						runtimeTesterGui.getPrimaryStage(),
						runtimeTesterGui.getEnvironmentVariableStorage(),
						entry);

				dialog.showDilalog();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

		}
	}

	private EnvironmentVariableStorage.StorageEntry getSelectedEntry()
	{
		return tableEnvVariables.getSelectionModel().getSelectedItem();
	}

	private void runTest()
	{
		updateProgressBar(-1.0f);

		ArrayList<String> parameters = new ArrayList<>();

		for (MethodParameterSelectorController controller : methodParamControllers)
		{
			parameters.add(controller.getTextFieldValue());
		}

		runtimeTesterGui.updateRuntimeTesterParameters(parameters);

		runtimeTesterGui.updatePreRunCode(txtAreaPreRunPhaseCode.getText());
		runtimeTesterGui.updateIncrementationCode(
				txtAreaIncrementationPhaseCode.getText());

		updateProgressBar(0.0f);

		runtimeTesterGui.run(spinnerIterations.getValue(),
				spinnerPrecision.getValue());

		updateChart(runtimeTesterGui.getRuntimeTester().getResult());
	}

	private void loadClass()
	{
		File openedFile = classFileChooser
				.showDialog(runtimeTesterGui.getPrimaryStage());

		if (openedFile != null)
		{
			txtFieldClassFile.setText(openedFile.getPath());
			selectedClassFileURI = openedFile.toURI();
		}
	}

	private void exportResults()
	{
		FileChooser fileChooser = new FileChooser();

		File file = fileChooser
				.showOpenDialog(runtimeTesterGui.getPrimaryStage());

		try
		{
			FileWriter writer = new FileWriter(file);
			writer.write(
					runtimeTesterGui.getRuntimeTester().getResult().toString());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void loadMethods()
	{
		try
		{
			URL[] urls = new URL[]
			{
					selectedClassFileURI.toURL()
			};
			URLClassLoader classLoader = new URLClassLoader(urls);

			Class<?> newClass = classLoader
					.loadClass(txtFieldClassName.getText());

			classLoader.close();

			runtimeTesterGui.setLoadedClass(newClass);

			loadMethods(newClass);

			btnRunTest.setDisable(false);

		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void updateChart(Result result)
	{
		resultChart.getData().clear();

		XYChart.Series<Number, Number> resultAverage = new Series<>();

		resultAverage.setName(Constants.RUNTIME_TESTER_GUI_CONTROLLER_CHART_GRAPH_AVERAGE_NAME);

		for (int i = 0; i < result.getIterationResults().size(); i++)
		{
			Number n1 = i;
			Number n2 = result.getIterationResults().get(i).getAverage();

			XYChart.Data<Number, Number> newData = new XYChart.Data<>(n1, n2);

			resultAverage.getData().add(newData);
		}

		resultChart.getData().add(resultAverage);

		for (XYChart.Series<Number, Number> chart : resultChart.getData())
		{
			for (XYChart.Data<Number, Number> data : chart.getData())
			{
				Tooltip tooltip = new Tooltip(
						String.format(Constants.RUNTIME_TESTER_GUI_CONTROLLER_CHART_DATA_TOOLTIP_TEXT, data.getYValue()));

				Tooltip.install(data.getNode(), tooltip);
			}
		}
	}

	private void updateProgressBar(float progress)
	{
		progressBar.setProgress(progress);

		if (progress < 0)
			progressLabel.setText("~");
		else
			progressLabel.setText(String.format("%.1f", progress * 100));
	}

	private void loadMethods(Class<?> cls)
	{
		vBoxSelectedClassParams.setDisable(false);

		for (Method m : cls.getMethods())
		{
			choiceBoxSelectMethod.getItems().add(new MethodNameBundle(m));
		}

		choiceBoxSelectMethod.getSelectionModel().selectFirst();
	}

	private void addParameterSelection(Parameter parameter)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass()
					.getResource(Constants.METHOD_PARAMETER_SELECTION_FXML));
			HBox node = loader.load();

			MethodParameterSelectorController controller = loader
					.getController();

			controller.setParameter(parameter);
			methodParamControllers.add(controller);

			vBoxSelectedMethodParams.getChildren().add(node);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setRuntimeTesterGui(RuntimeTesterGUI runtimeTesterGui)
	{
		this.runtimeTesterGui = runtimeTesterGui;

		runtimeTesterGui.getRuntimeTester().getProgress()
				.addListener(new ChangeListener<Number>()
				{ // TODO keep progress bar in sync
					@Override
					public void changed(
							ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue)
					{
						updateProgressBar((Float) newValue);
					}
				});

	}

	public void updateTable(EnvironmentVariableStorage.StorageEntry newEntry)
	{
		tableEnvVariables.getItems().add(newEntry);
	}

	private class MethodNameBundle
	{
		private Method method;
		private String name;

		public MethodNameBundle(Method method)
		{
			this.method = method;

			StringBuilder nameBuilder = new StringBuilder();

			nameBuilder.append(method.getReturnType()).append(" ")
					.append(method.getName()).append("(");

			for (int i = 0; i < method.getParameters().length; i++)
			{
				if (i != 0)
					nameBuilder.append(", ");

				nameBuilder.append(method.getParameters()[i]);
			}

			nameBuilder.append(")");

			this.name = nameBuilder.toString();
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
