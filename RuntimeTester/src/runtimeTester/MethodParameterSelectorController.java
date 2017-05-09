package runtimeTester;

import java.lang.reflect.Parameter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MethodParameterSelectorController
{
	@FXML
	private Label lblDatatype;
	
	@FXML
	private TextField txtFieldValue;
	
	public String getDatatype()
	{
		return lblDatatype.getText();
	}
	
	public void setDatatype(String text)
	{
		lblDatatype.setText(text);
	}
	
	public String getTextFieldValue()
	{
		return txtFieldValue.getText();
	}

	public void setParameter(Parameter parameter)
	{
		lblDatatype.setText(String.format(Constants.METHOD_PARAMETER_SELECTION_LABEL_DATATYPE_TEXT, parameter.getType().toString(), parameter.getName()));
	}
}
