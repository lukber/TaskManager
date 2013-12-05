package cz.czechGeeks.taskManager.client.android.model.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskCategModel;

public class SQLiteServiceTaskCategManager extends AbstractSQLiteTaskManager implements TaskCategManager {

	private Context context;

	public SQLiteServiceTaskCategManager(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void getAll(AsyncTaskCallBack<TaskCategModel[]> callBack) {
		String QUERY = "SELECT " + 
				TASK_CATEG_TABLE_COLUMN_KEY_ID + "," + 
				TASK_CATEG_TABLE_COLUMN_KEY_NAME + "," + 
				"(SELECT count(*) FROM " + TASK_TABLE + " TSK WHERE TSK." + TASK_TABLE_COLUMN_KEY_CATEG_ID + "=CTG." + TASK_CATEG_TABLE_COLUMN_KEY_ID + ")" + 
				" FROM " + TASK_CATEG_TABLE + " CTG";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY, null);
		List<TaskCategModel> modelList = new ArrayList<TaskCategModel>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				TaskCategModel categModel = new TaskCategModel();
				long id = cursor.getLong(0);
				String name = cursor.getString(1);
				long tskCount = cursor.getLong(2);

				categModel.setId(id);
				categModel.setName(name);
				categModel.setUpdatable(tskCount == 0);
				categModel.setDeletable(tskCount == 0);

				modelList.add(categModel);
			} while (cursor.moveToNext());
			cursor.close();
		}

		db.close();
		callBack.onSuccess(modelList.toArray(new TaskCategModel[0]));
	}

	@Override
	public void get(Long id, AsyncTaskCallBack<TaskCategModel> callBack) {
		String QUERY = "SELECT " + 
				TASK_CATEG_TABLE_COLUMN_KEY_ID + "," + 
				TASK_CATEG_TABLE_COLUMN_KEY_NAME + "," + 
				"(SELECT count(*) FROM " + TASK_TABLE + " TSK WHERE TSK." + TASK_TABLE_COLUMN_KEY_CATEG_ID + "=CTG." + TASK_CATEG_TABLE_COLUMN_KEY_ID + ")" + 
				" FROM " + TASK_CATEG_TABLE + " CTG" + 
				" WHERE " + TASK_CATEG_TABLE_COLUMN_KEY_ID + "=?";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY, new String[] { id.toString() });
		if (cursor != null && cursor.moveToFirst()) {
			long categId = cursor.getLong(0);
			String categName = cursor.getString(1);
			long tskCount = cursor.getLong(2);

			TaskCategModel categModel = new TaskCategModel();
			categModel.setId(categId);
			categModel.setName(categName);
			categModel.setUpdatable(tskCount == 0);
			categModel.setDeletable(tskCount == 0);

			cursor.close();
			db.close();
			callBack.onSuccess(categModel);
			return;
		}

		db.close();
		callBack.onError(new ErrorMessage(context.getString(R.string.error_taskCategNotFound)));
	}

	@Override
	public void insert(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TASK_CATEG_TABLE_COLUMN_KEY_NAME, categ.getName());

		long categID = db.insert(TASK_CATEG_TABLE, null, values);
		db.close();

		get(categID, callBack);
	}

	@Override
	public void update(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TASK_CATEG_TABLE_COLUMN_KEY_NAME, categ.getName());

		db.update(TASK_CATEG_TABLE, values, TASK_CATEG_TABLE_COLUMN_KEY_ID + " = ?", new String[] { String.valueOf(categ.getId()) });
		db.close();

		get(categ.getId(), callBack);
	}

	@Override
	public void delete(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TASK_CATEG_TABLE, TASK_CATEG_TABLE_COLUMN_KEY_ID + " = ?", new String[] { String.valueOf(categ.getId()) });
		db.close();

		callBack.onSuccess(categ);
	}

}
