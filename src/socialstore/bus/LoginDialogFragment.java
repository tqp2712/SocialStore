package socialstore.bus;


import socialstore.bus.R;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginDialogFragment extends DialogFragment {
	int style;
	static LoginDialogFragment newInstance() {
		LoginDialogFragment f = new LoginDialogFragment();
        return f;
    }
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);
    }
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder builder = new Builder(getActivity());
		builder.setIcon(null);
		builder.setTitle("");
		View layout = getActivity().getLayoutInflater().inflate(R.layout.login_screen, null);
		final EditText edt_username = (EditText) layout.findViewById(R.id.edt_username);
		final EditText edt_password = (EditText) layout.findViewById(R.id.edt_password);
		final TextView txt_error = (TextView) layout.findViewById(R.id.txt_error);
		Button btn_save = (Button) layout.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = edt_username.getText().toString();
				String password = edt_password.getText().toString();
				if(username.equals("") || password.equals("")) {
					txt_error.setText(getResources().getString(R.string.error_login_fail));
					txt_error.setVisibility(TextView.VISIBLE);
					return;	
				} else {
					txt_error.setText(getResources().getString(R.string.error_login_fail));
					txt_error.setVisibility(TextView.VISIBLE);
				}
			}
		});
		builder.setView(layout);
		return builder.create();
	}
}
