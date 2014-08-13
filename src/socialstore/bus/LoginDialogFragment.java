package socialstore.bus;


import socialstore.bus.R;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.login_screen, container, false);
		final TextView txt_error = (TextView)v.findViewById(R.id.txt_error);
		final TextView txt_username = (TextView)v.findViewById(R.id.txt_username);
		final TextView txt_password = (TextView)v.findViewById(R.id.txt_password);
		ImageButton btn_save = (ImageButton)v.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = txt_username.getText().toString();
				String password = txt_password.getText().toString();
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
		ImageButton btn_cancel = (ImageButton) v.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		return v;
	}
}
