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

public class TaskCategEditDialogFragment extends DialogFragment {

	public interface TaskCategEditDialogFragmentCallBack {
		void onTaskCategSave(int position, String newCategName);
	}

	public static final String TASK_CATEG_POSITION = "cz.czechGeeks.taskManager.client.android.fragment.TaskCateg.Position";
	public static final String TASK_CATEG_NAME = "cz.czechGeeks.taskManager.client.android.fragment.TaskCateg.Model";

	private EditText categNameEditText;

	private TaskCategEditDialogFragmentCallBack callBack;
	private int position;
	private String categName;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(true);

		position = getArguments().getInt(TASK_CATEG_POSITION);
		categName = getArguments().getString(TASK_CATEG_NAME);

		FragmentActivity activity = getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = activity.getLayoutInflater();

		View rootView = inflater.inflate(R.layout.dialog_task_categ, null);
		categNameEditText = (EditText) rootView.findViewById(R.id.taskCateg);
		categNameEditText.setText(categName);

		Builder view = builder.setView(rootView);
		view.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				callBack.onTaskCategSave(position, categNameEditText.getEditableText().toString());
			}
		});
		view.setNegativeButton(R.string.storno, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				TaskCategEditDialogFragment.this.getDialog().cancel();
			}
		});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			callBack = (TaskCategEditDialogFragmentCallBack) getTargetFragment();
		} catch (Exception e) {
			throw new ClassCastException("Aktivity musi implementovat rozhrani " + TaskCategEditDialogFragmentCallBack.class);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callBack = null;
	}
}
