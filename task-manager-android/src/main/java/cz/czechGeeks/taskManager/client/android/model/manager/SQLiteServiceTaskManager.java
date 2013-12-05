package cz.czechGeeks.taskManager.client.android.model.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.util.LoginUtils;

public class SQLiteServiceTaskManager extends AbstractSQLiteTaskManager implements TaskManager {
	
	private static final String BASE_SELECT_QUERY = "SELECT " + 
			"tsk." + TASK_TABLE_COLUMN_KEY_ID + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_CATEG_ID + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_NAME + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_DESC + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_FINISH_TO_DATE + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_FINISHED_DATE + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_INS_DATE + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_UPD_DATE + "," + 
			"tsk." + TASK_TABLE_COLUMN_KEY_UNREAD + "," + 
			"ctg." + TASK_CATEG_TABLE_COLUMN_KEY_NAME + "," + 
			"lgnExec." + LOGIN_TABLE_COLUMN_KEY_NAME + "," + 
			"lgnInse." + LOGIN_TABLE_COLUMN_KEY_NAME +  
		" FROM " + TASK_TABLE + " tsk" +
		" LEFT JOIN " + TASK_CATEG_TABLE + " ctg ON(ctg." + TASK_CATEG_TABLE_COLUMN_KEY_ID + "=tsk." + TASK_TABLE_COLUMN_KEY_CATEG_ID + ")" +
		" LEFT JOIN " + LOGIN_TABLE + " lgnExec ON(lgnExec." + LOGIN_TABLE_COLUMN_KEY_ID + "=tsk." + TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + ")" +
		" LEFT JOIN " + LOGIN_TABLE + " lgnInse ON(lgnInse." + LOGIN_TABLE_COLUMN_KEY_ID + "=tsk." + TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + ")";
	
	private Context context; 

	public SQLiteServiceTaskManager(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void getAllMain(AsyncTaskCallBack<TaskModel[]> callBack) {
		String QUERY = BASE_SELECT_QUERY + 
				" WHERE " + 
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + "=? AND " +
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + "=?";

		Long loggedUserId = LoginUtils.get().getLoggedUserId();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY, new String[] { loggedUserId.toString(), loggedUserId.toString() });
		List<TaskModel> modelList = new ArrayList<TaskModel>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				TaskModel taskModel = createModel(loggedUserId, cursor);
				modelList.add(taskModel);
			} while (cursor.moveToNext());
			cursor.close();
		}

		db.close();
		callBack.onSuccess(modelList.toArray(new TaskModel[0]));
	}

	@Override
	public void getAllDelegatedToMe(AsyncTaskCallBack<TaskModel[]> callBack) {
		String QUERY = BASE_SELECT_QUERY + 
				" WHERE " + 
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + "=? AND " +
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + "<>?";

		Long loggedUserId = LoginUtils.get().getLoggedUserId();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY, new String[] { loggedUserId.toString(), loggedUserId.toString() });
		List<TaskModel> modelList = new ArrayList<TaskModel>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				TaskModel taskModel = createModel(loggedUserId, cursor);
				modelList.add(taskModel);
			} while (cursor.moveToNext());
			cursor.close();
		}

		db.close();
		callBack.onSuccess(modelList.toArray(new TaskModel[0]));
	}

	@Override
	public void getAllDelegatedToMe(Long fromTaskId, AsyncTaskCallBack<TaskModel[]> callBack) {
		String QUERY = BASE_SELECT_QUERY + 
				" WHERE " + 
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + "=? AND " +
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + "<>? AND " +
					TASK_TABLE_COLUMN_KEY_ID + ">?";

		Long loggedUserId = LoginUtils.get().getLoggedUserId();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY, new String[] { loggedUserId.toString(), loggedUserId.toString(), fromTaskId == null ? "0" : fromTaskId.toString() });
		List<TaskModel> modelList = new ArrayList<TaskModel>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				TaskModel taskModel = createModel(loggedUserId, cursor);
				modelList.add(taskModel);
			} while (cursor.moveToNext());
			cursor.close();
		}

		db.close();
		callBack.onSuccess(modelList.toArray(new TaskModel[0]));
	}

	@Override
	public void getAllDelegatedToOthers(AsyncTaskCallBack<TaskModel[]> callBack) {
		String QUERY = BASE_SELECT_QUERY + 
				" WHERE " + 
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + "<>? AND " +
					TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + "=?";

		Long loggedUserId = LoginUtils.get().getLoggedUserId();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY, new String[] { loggedUserId.toString(), loggedUserId.toString() });
		List<TaskModel> modelList = new ArrayList<TaskModel>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				TaskModel taskModel = createModel(loggedUserId, cursor);
				modelList.add(taskModel);
			} while (cursor.moveToNext());
			cursor.close();
		}

		db.close();
		callBack.onSuccess(modelList.toArray(new TaskModel[0]));
	}

	@Override
	public void get(Long id, AsyncTaskCallBack<TaskModel> callBack) {
		String QUERY = BASE_SELECT_QUERY + 
				" WHERE " + 
					TASK_TABLE_COLUMN_KEY_ID + "=?";

		Long loggedUserId = LoginUtils.get().getLoggedUserId();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY, new String[] { id.toString() });
		if (cursor != null && cursor.moveToFirst()) {
			TaskModel taskModel = createModel(loggedUserId, cursor);
			cursor.close();
			db.close();
			callBack.onSuccess(taskModel);
			return;
		}

		db.close();
		callBack.onError(new ErrorMessage(context.getString(R.string.error_taskNotFound)));		
	}
	
	private TaskModel createModel(Long loggedUserId, Cursor cursor) {
		int columnIndex = 0;
		TaskModel taskModel = new TaskModel();
		
		taskModel.setId(cursor.getLong(columnIndex++));
		taskModel.setCategId(cursor.getLong(columnIndex++));
		
		taskModel.setName(cursor.getString(columnIndex++));
		taskModel.setDesc(cursor.getString(columnIndex++));
		
		taskModel.setFinishToDate(convertToTimeStamp(cursor.getString(columnIndex++)));
		taskModel.setFinishedDate(convertToTimeStamp(cursor.getString(columnIndex++)));
		
		taskModel.setExecutorId(cursor.getLong(columnIndex++));
		taskModel.setInserterId(cursor.getLong(columnIndex++));
		
		taskModel.setInsDate(convertToTimeStamp(cursor.getString(columnIndex++)));
		taskModel.setUpdDate(convertToTimeStamp(cursor.getString(columnIndex++)));
		
		taskModel.setUnread(cursor.getInt(columnIndex++) == 0 ? false : true);
		
		taskModel.setCategName(cursor.getString(columnIndex++));
		taskModel.setExecutorName(cursor.getString(columnIndex++));
		taskModel.setInserterName(cursor.getString(columnIndex++));
		
		taskModel.setUpdatable((loggedUserId.equals(taskModel.getInserterId()) || loggedUserId.equals(taskModel.getExecutorId())) && !taskModel.isClosed());
		taskModel.setCloseable((loggedUserId.equals(taskModel.getInserterId()) || loggedUserId.equals(taskModel.getExecutorId())) && !taskModel.isClosed());
		taskModel.setDeletable(loggedUserId.equals(taskModel.getInserterId()));
		return taskModel;
	}

	private Timestamp convertToTimeStamp(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}

		return new Timestamp(Long.valueOf(value));
	}

	@Override
	public void delete(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TASK_TABLE, TASK_TABLE_COLUMN_KEY_ID + " = ?", new String[] { String.valueOf(task.getId()) });
		db.close();

		callBack.onSuccess(task);
	}

	@Override
	public void update(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TASK_TABLE_COLUMN_KEY_CATEG_ID, task.getCategId());
		values.put(TASK_TABLE_COLUMN_KEY_NAME, task.getName());
		values.put(TASK_TABLE_COLUMN_KEY_DESC, task.getDesc());
		values.put(TASK_TABLE_COLUMN_KEY_FINISH_TO_DATE, task.getFinishToDate() != null ? task.getFinishToDate().getTime() : null);
		values.put(TASK_TABLE_COLUMN_KEY_FINISHED_DATE, task.getFinishedDate() != null ? task.getFinishedDate().getTime() : null);
		values.put(TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR, task.getExecutorId());
		
		values.put(TASK_TABLE_COLUMN_KEY_UPD_DATE, new Date().getTime());

		db.update(TASK_TABLE, values, TASK_TABLE_COLUMN_KEY_ID + "=?", new String[] { String.valueOf(task.getId()) });
		db.close();

		get(task.getId(), callBack);
	}

	@Override
	public void insert(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();
		Long inserterId = LoginUtils.get().getLoggedUserId();
		Long executorId = task.getExecutorId() != null ? task.getExecutorId() : inserterId;

		ContentValues values = new ContentValues();
		values.put(TASK_TABLE_COLUMN_KEY_CATEG_ID, task.getCategId());
		values.put(TASK_TABLE_COLUMN_KEY_NAME, task.getName());
		values.put(TASK_TABLE_COLUMN_KEY_DESC, task.getDesc());
		values.put(TASK_TABLE_COLUMN_KEY_FINISH_TO_DATE, task.getFinishToDate() != null ? task.getFinishToDate().getTime() : null);
		values.put(TASK_TABLE_COLUMN_KEY_FINISHED_DATE, task.getFinishedDate() != null ? task.getFinishedDate().getTime() : null);
		values.put(TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR, executorId);
		values.put(TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS, inserterId);
		
		values.put(TASK_TABLE_COLUMN_KEY_UNREAD, executorId.compareTo(inserterId) != 0);
		
		values.put(TASK_TABLE_COLUMN_KEY_INS_DATE, new Date().getTime());
		values.put(TASK_TABLE_COLUMN_KEY_UPD_DATE, new Date().getTime());

		long taskId = db.insert(TASK_TABLE, null, values);
		db.close();

		get(taskId, callBack);
	}

	@Override
	public void markAsReaded(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TASK_TABLE_COLUMN_KEY_UNREAD, 0);

		db.update(TASK_TABLE, values, TASK_TABLE_COLUMN_KEY_ID + "=?", new String[] { task.getId().toString() });
		db.close();

		get(task.getId(), callBack);
	}

	@Override
	public void close(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TASK_TABLE_COLUMN_KEY_FINISHED_DATE, new Date().getTime());

		db.update(TASK_TABLE, values, TASK_TABLE_COLUMN_KEY_ID + "=?", new String[] { task.getId().toString() });
		db.close();

		get(task.getId(), callBack);
	}

}
