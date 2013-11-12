package cz.czechGeeks.taskManager.client.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.exception.UnauthorizedException;
import cz.czechGeeks.taskManager.client.android.service.LoginService;

public class SingInActivity extends Activity {

	private EditText userName;
	private EditText pass;
	private Button signIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		userName = (EditText) findViewById(R.id.userNameEditText);
		pass = (EditText) findViewById(R.id.passwordEditText);
		signIn = (Button) findViewById(R.id.signInButton);
		signIn.setEnabled(false);

		binding();
	}

	private void binding() {
		TextWatcher watcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				boolean hasUserName = userName.getEditableText().toString().length() > 0;
				boolean hasPass = pass.getEditableText().toString().length() > 0;
				signIn.setEnabled(hasUserName && hasPass);
			}
		};

		userName.addTextChangedListener(watcher);
		pass.addTextChangedListener(watcher);
		
		signIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				performSingIn(userName.getEditableText().toString(), pass.getEditableText().toString());
			}
		});

	}

	private void performSingIn(String userName, String password) {
		try {
			LoginService.get().signIn(userName, password);
			startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
		} catch (UnauthorizedException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.error_unauthorizedException);
			builder.setPositiveButton("OK", null);
			builder.create().show();
		}
	}

}
