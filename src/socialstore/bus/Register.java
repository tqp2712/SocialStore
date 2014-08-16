package socialstore.bus;

import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends Activity {
	private EditText edt_fullname, edt_username, edt_pass, edt_confirm_pass, edt_email;
	private TextView txt_error;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		edt_fullname = (EditText) findViewById(R.id.edt_fullname);
		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_pass = (EditText) findViewById(R.id.edt_password);
		edt_confirm_pass = (EditText) findViewById(R.id.edt_confirm_password);
		edt_email = (EditText) findViewById(R.id.edt_email);
		txt_error = (TextView) findViewById(R.id.txt_error);
	}
	public void register(View v) {
		if (validate()) {
			
		}
	}
	public boolean validate() {
		Pattern email_pattern = Pattern.compile("[a-zA-Z]+[a-zA-Z0-9]-_*@[a-zA-Z0-9]+.[a-zA-Z0-9]+");
		Pattern password_pattern = Pattern.compile("[a-z]");
		String fullname = edt_fullname.getText().toString();
		String username = edt_username.getText().toString();
		String password = edt_pass.getText().toString();
		String confirm_pass = edt_confirm_pass.getText().toString();
		String email = edt_email.getText().toString();
		if(fullname.equals("") || username.equals("") || password.equals("") 
				|| confirm_pass.equals("") || email.equals(""))
			txt_error.setText("");
		else if(checkExistUser(username))
			txt_error.setText("");
		else if(!password_pattern.matcher(password).matches())
			txt_error.setText("");
		else if(!password.equalsIgnoreCase(confirm_pass))
			txt_error.setText("");
		else if(!email_pattern.matcher(email).matches())
			txt_error.setText("");
		else
			return true;
		return false;
	}
	public boolean checkExistUser(String username) {
		return false;
	}
}
