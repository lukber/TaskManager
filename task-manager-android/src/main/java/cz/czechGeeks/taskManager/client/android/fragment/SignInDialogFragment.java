package cz.czechGeeks.taskManager.client.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.util.PreferencesUtils;
import cz.czechGeeks.taskManager.client.android.util.PreferencesUtils.ConnectionItems;

/**
 * Prihlasovaci dialog. Data pro zobrazeni bere z preferences. Po kliku na Prihlasit hodnoty ulozi zpet do preferences
 * 
 * @author lukasb
 * 
 */
public class SignInDialogFragment extends DialogFragment {

	public interface SignInDialogFragmentCallBack {
		/**
		 * Bylo stisknuto tlacitko prihlasit. V preferences jsou ulozeny nove hodnoty
		 */
		void onSignInDialogResultOk();

		/**
		 * Bylo stisknuto tlacitko cancel.
		 */
		void onSignInDialogResulCancel();
	}

	private SignInDialogFragmentCallBack callBack;
	private EditText baseUrl;
	private EditText userName;
	private EditText password;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(false);

		FragmentActivity activity = getActivity();

		// Klice pro preferences

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = activity.getLayoutInflater();

		View rootView = inflater.inflate(R.layout.dialog_signin, null);
		this.baseUrl = (EditText) rootView.findViewById(R.id.signIn_url);
		this.userName = (EditText) rootView.findViewById(R.id.signIn_userName);
		this.password = (EditText) rootView.findViewById(R.id.signIn_password);

		// Nacteni hodnot z preferences
		ConnectionItems connectionItems = PreferencesUtils.getConnectionItems(activity);
		String BASE_URL = connectionItems.BASE_URL;
		String LOGIN_NAME = connectionItems.USER_NAME;
		String PASSWORD = connectionItems.PASSWORD;

		// Nastaveni hodnot z preferences do poli
		baseUrl.setText(BASE_URL);
		userName.setText(LOGIN_NAME);
		password.setText(PASSWORD);

		Builder view = builder.setView(rootView);
		view.setPositiveButton(R.string.signIn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Nastaveni novych hodnot do preferences
				ConnectionItems connectionItems = new ConnectionItems();
				connectionItems.BASE_URL = baseUrl.getEditableText().toString();
				connectionItems.USER_NAME = userName.getEditableText().toString();
				connectionItems.PASSWORD = password.getEditableText().toString();
				PreferencesUtils.saveConnectionItems(connectionItems, getActivity());

				// volani callback
				callBack.onSignInDialogResultOk();
			}
		}).setNegativeButton(R.string.storno, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				callBack.onSignInDialogResulCancel();
			}
		});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			callBack = (SignInDialogFragmentCallBack) activity;
		} catch (Exception e) {
			throw new ClassCastException("Aktivity musi implementovat rozhrani " + SignInDialogFragmentCallBack.class);
		}
	}

}
