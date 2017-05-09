package runtimeTester;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddEnvVariableDialogController
{
	private AddEnvVariableDialog dialog;

	@FXML
	private TextField txtFieldName;

	@FXML
	private TextField txtFieldValue;

	@FXML
	void onBtnCancelAction(ActionEvent event)
	{
		exitCancel();
	}

	@FXML
	void onBtnOkAction(ActionEvent event)
	{
		exitOk();
	}

	private void exitCancel()
	{
		dialog.close();
	}
	
	private void exitOk()
	{
		if(!txtFieldName.getText().isEmpty() && !txtFieldValue.getText().isEmpty())
		{
			dialog.addVariable(txtFieldName.getText(), txtFieldValue.getText());
		}
		
		dialog.close();
	}

	public void setDialog(AddEnvVariableDialog dialog)
	{
		this.dialog = dialog;
	}
}
