package runtimeTester;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ModEnvVariableDialogController
{

	private ModEnvVariableDialog dialog;

	@FXML
	private TextField txtFieldOldValue;

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
		if (!txtFieldValue.getText().isEmpty())
		{
			dialog.modify(txtFieldValue.getText());
		}

		dialog.close();
	}

	public void setup(Object oldValue, ModEnvVariableDialog dialog)
	{
		txtFieldOldValue.setText(oldValue.toString());
		this.dialog = dialog;
	}
}
