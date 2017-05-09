package runtimeTester;

import java.io.File;
import java.util.regex.Pattern;

public abstract class Constants
{
	public static final String PARAMETER_SETTER_CODE_TEMPLATE = "ClassTemplates/ParameterSetterCode.template";
	public static final String INCREMENTATION_CODE_TEMPLATE = "ClassTemplates/IncrementationCodeTemplate.template";
	public static final String PRE_RUN_CODE_TEMPLATE = "ClassTemplates/PreRunCodeTemplate.template";

	public static final File PARAMETER_SETTER_CODE_TEMPLATE_FILE = new File(Constants.PARAMETER_SETTER_CODE_TEMPLATE);
	public static final File INCREMENTATION_CODE_TEMPLATE_FILE = new File(Constants.INCREMENTATION_CODE_TEMPLATE);
	public static final File PRE_RUN_CODE_TEMPLATE_FILE = new File(Constants.PRE_RUN_CODE_TEMPLATE);

	public static final String PARAMETER_SETTER_CODE_CLASS_NAME = "ParameterSetter";
	public static final String INCREMENTATION_CODE_CLASS_NAME = "IncrementationCode";
	public static final String PRE_RUN_CODE_CLASS_NAME = "PreRunCode";
	
	public static final String ADD_ENV_VAR_DIALOG_SETTER_CLASS_NAME = "EnvVariableAddValue";
	public static final String ADD_ENV_VAR_DIALOG_WINDOW_TITLE = "Add new Environment Variable";
	public static final String ADD_ENV_VAR_DIALOG_FXML_FILENAME = "AddEnvVariable.fxml";

	public static final String MOD_ENV_VAR_DIALOG_SETTER_CLASS_NAME = "EnvVariableModValue";
	public static final String MOD_ENV_VAR_DIALOG_WINDOW_TITLE = "Modify Environmental Variable";
	public static final String MOD_ENV_VAR_DIALOG_FXML_FILENAME = "ModEnvVariable.fxml.fxml";

	public static final String RUNTIME_TESTER_GUI_FXML = "RuntimeTester.fxml";

	public static final File USER_INPUT_PROCESSOR_OUTPUT_DIR = new File("Classes/");

	public static final String METHOD_PARAMETER_SELECTION_LABEL_DATATYPE_TEXT = "%s %s =";
	public static final String METHOD_PARAMETER_SELECTION_FXML = "MethodParameterSelection.fxml";

	public static final String RUNTIME_TESTER_GUI_CONTROLLER_CHART_GRAPH_AVERAGE_NAME = "Average";
	public static final String RUNTIME_TESTER_GUI_CONTROLLER_CHART_DATA_TOOLTIP_TEXT = "Time: %dns";
	public static final String RUNTIME_TESTER_GUI_CONTROLLER_CHART_AXIS_X_LABEL = "Iterations";
	public static final String RUNTIME_TESTER_GUI_CONTROLLER_CHART_AXIS_Y_LABEL = "Time";

	static final String USER_INPUT_PROCESSOR_ENVIRONMENTAL_VARIABLE_STORAGE_CALL_GET = "environmentVariableStorage.getData(%s)";
	static final Pattern USER_INPUT_PROCESSOR_ENVIRONMENTAL_GET_VARIABLE_PATTERN = Pattern
			.compile("get\\((\".*\")\\)");
	static final Pattern USER_INPUT_PROCESSOR_ENVIRONMENTAL_SET_VARIABLE_PATTERN = Pattern
			.compile("set\\((\".*\"),(.*)\\)");
	static final Pattern USER_INPUT_PROCESSOR_CODE_INJECTION_IDENTIFIER_PATTERN = Pattern
			.compile("__CODE___HERE__");
	static final Pattern USER_INPUT_PROCESSOR_CLASS_NAME_INJECTION_IDENTIFIER_PATTERN = Pattern
			.compile("__CLASS__NAME__HERE__");
	static final String USER_INPUT_PROCESSOR_ENVIRONMENTAL_VARIABLE_STORAGE_CALL_SET = "environmentVariableStorage.setData(%s, %s)";
	
	public static final String ERROR_FILE_IS_NOT_DIRECTORY = "File is not a directory";
}
