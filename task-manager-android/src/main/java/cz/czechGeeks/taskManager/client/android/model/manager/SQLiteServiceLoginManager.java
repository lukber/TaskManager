package cz.czechGeeks.taskManager.client.android.model.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class SQLiteServiceLoginManager extends AbstractSQLiteTaskManager implements LoginManager {

	private Context context;

	public SQLiteServiceLoginManager(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void signIn(String userName, String password, AsyncTaskCallBack<LoginModel> callBack) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(LOGIN_TABLE);
		Cursor cursor = builder.query(db, new String[] { LOGIN_TABLE_COLUMN_KEY_ID, LOGIN_TABLE_COLUMN_KEY_NAME }, LOGIN_TABLE_COLUMN_KEY_USR_NAME + "=? AND " + LOGIN_TABLE_COLUMN_KEY_USR_PASS + "=?", new String[] { userName, password }, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				LoginModel login = new LoginModel(Long.parseLong(cursor.getString(0)), cursor.getString(1));
				callBack.onSuccess(login);
				cursor.close();
				db.close();
				return;
			}
		}
		callBack.onError(new ErrorMessage(context.getString(R.string.error_noValidUserNameOrPass)));
		db.close();
	}

	@Override
	public void get(long userId, AsyncTaskCallBack<LoginModel> callBack) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(LOGIN_TABLE);

		Cursor cursor = builder.query(db, new String[] { LOGIN_TABLE_COLUMN_KEY_ID, LOGIN_TABLE_COLUMN_KEY_NAME }, LOGIN_TABLE_COLUMN_KEY_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				LoginModel login = new LoginModel(Long.parseLong(cursor.getString(0)), cursor.getString(1));
				callBack.onSuccess(login);
				cursor.close();
				db.close();
				return;
			}
		}
		callBack.onError(new ErrorMessage(context.getString(R.string.error_loginNotFound)));
		db.close();
	}

	@Override
	public void getAll(AsyncTaskCallBack<LoginModel[]> callBack) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(LOGIN_TABLE);

		Cursor cursor = builder.query(db, new String[] { LOGIN_TABLE_COLUMN_KEY_ID, LOGIN_TABLE_COLUMN_KEY_NAME }, null, null, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				List<LoginModel> modelList = new ArrayList<LoginModel>();
				do {
					LoginModel loginModel = new LoginModel(Long.parseLong(cursor.getString(0)), cursor.getString(1));
					modelList.add(loginModel);
				} while (cursor.moveToNext());
				callBack.onSuccess(modelList.toArray(new LoginModel[0]));
				cursor.close();
				db.close();
				return;
			}
		}

		callBack.onSuccess(null);
		db.close();
	}

}
