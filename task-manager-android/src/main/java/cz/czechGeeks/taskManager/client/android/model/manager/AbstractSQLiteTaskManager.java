package cz.czechGeeks.taskManager.client.android.model.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AbstractSQLiteTaskManager extends SQLiteOpenHelper {
	
	private static final String LOG_TAG = "SQLiteTaskManager";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "TaskManager";

	protected static final String TASK_CATEG_TABLE = "TSK_CATEG";
	protected static final String TASK_TABLE = "TSK";
	protected static final String LOGIN_TABLE = "LOGIN";

	// Sloupce tabulky LOGIN
	protected static final String LOGIN_TABLE_COLUMN_KEY_ID = "LOGIN_ID";
	protected static final String LOGIN_TABLE_COLUMN_KEY_NAME = "LOGIN_NAME";
	protected static final String LOGIN_TABLE_COLUMN_KEY_USR_NAME = "LOGIN_USR_NAME";
	protected static final String LOGIN_TABLE_COLUMN_KEY_USR_PASS = "LOGIN_USR_PASS";

	// Sloupce tabulky TSK_CATEG
	protected static final String TASK_CATEG_TABLE_COLUMN_KEY_ID = "TSK_CATEG_ID";
	protected static final String TASK_CATEG_TABLE_COLUMN_KEY_NAME = "TSK_CATEG_NAME";
	
	// Sloupce tabulky TSK
	protected static final String TASK_TABLE_COLUMN_KEY_ID = "TSK_ID";
	protected static final String TASK_TABLE_COLUMN_KEY_CATEG_ID = "TSK_CATEG_ID";
	protected static final String TASK_TABLE_COLUMN_KEY_NAME = "TSK_NAME";
	protected static final String TASK_TABLE_COLUMN_KEY_DESC = "TSK_DESK";
	protected static final String TASK_TABLE_COLUMN_KEY_FINISH_TO_DATE = "TSK_FINISH_TO_DATE";
	protected static final String TASK_TABLE_COLUMN_KEY_FINISHED_DATE = "TSK_FINISHED_DATE";
	protected static final String TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR = "TSK_LOGIN_ID_EXECUTOR";
	protected static final String TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS = "TSK_LOGIN_ID_INS";
	protected static final String TASK_TABLE_COLUMN_KEY_INS_DATE = "TSK_INS_DATE";
	protected static final String TASK_TABLE_COLUMN_KEY_UPD_DATE = "TSK_UPD_DATE";
	protected static final String TASK_TABLE_COLUMN_KEY_UNREAD = "TSK_UNREAD";

	public AbstractSQLiteTaskManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOG_TAG, "Zalozeni tabulky LOGIN");
		String CREATE_LOGIN_TABLE =	"CREATE TABLE " + LOGIN_TABLE + "(" + 
				LOGIN_TABLE_COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + 
				LOGIN_TABLE_COLUMN_KEY_NAME + " CHAR(50) NOT NULL," + 
				LOGIN_TABLE_COLUMN_KEY_USR_NAME + " CHAR(50) NOT NULL," + 
				LOGIN_TABLE_COLUMN_KEY_USR_PASS + " CHAR(50) NOT NULL)";

		// zalozeni tabulky pro LOGIN
		db.execSQL(CREATE_LOGIN_TABLE);

		// Pridani defaultnich hodnot do LOGIN
		Log.i(LOG_TAG, "Pridani defaultnich uzivatelu");
		ContentValues loginValues = new ContentValues();
		loginValues.put(LOGIN_TABLE_COLUMN_KEY_NAME, "Petr");
		loginValues.put(LOGIN_TABLE_COLUMN_KEY_USR_NAME, "petr");
		loginValues.put(LOGIN_TABLE_COLUMN_KEY_USR_PASS, "taskmanager");
		db.insert(LOGIN_TABLE, null, loginValues);// Zalozeni uzivatele PETR
		loginValues.put(LOGIN_TABLE_COLUMN_KEY_NAME, "Martin");
		loginValues.put(LOGIN_TABLE_COLUMN_KEY_USR_NAME, "martin");
		db.insert(LOGIN_TABLE, null, loginValues);// Zalozeni uzivatele MARTIN
		loginValues.put(LOGIN_TABLE_COLUMN_KEY_NAME, "Tomáš");
		loginValues.put(LOGIN_TABLE_COLUMN_KEY_USR_NAME, "tomas");
		db.insert(LOGIN_TABLE, null, loginValues);// Zalozeni uzivatele TOMAS
		
		// Zalozeni tabulky kategorii
		Log.i(LOG_TAG, "Zalozeni tabulky TSK_CATEG");
		String CREATE_TASK_CATEG_TABLE = "CREATE TABLE " + TASK_CATEG_TABLE + "(" + 
				TASK_CATEG_TABLE_COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + 
				TASK_CATEG_TABLE_COLUMN_KEY_NAME + " CHAR(50) NOT NULL)";

		//zalozeni tabulky pro KATEGORIE
		db.execSQL(CREATE_TASK_CATEG_TABLE);

		// pridani defaultnich hodnot do TSK_CATEG
		Log.i(LOG_TAG, "Pridani defaultnich kategorii");
		ContentValues categValues = new ContentValues();
		categValues.put(TASK_CATEG_TABLE_COLUMN_KEY_NAME, "Pracovní");
		db.insert(TASK_CATEG_TABLE, null, categValues);// Zalozeni kategorie Pracovni
		categValues.put(TASK_CATEG_TABLE_COLUMN_KEY_NAME, "Domácí");
		db.insert(TASK_CATEG_TABLE, null, categValues);// Zalozeni kategorie Domaci

		//Zalozeni tabulky TSK
		Log.i(LOG_TAG, "Zalozeni tabulky TSK");
		String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE + "(" + 
				TASK_TABLE_COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + 
				TASK_TABLE_COLUMN_KEY_CATEG_ID + " INTEGER NOT NULL," + 
				TASK_TABLE_COLUMN_KEY_NAME + " CHAR(50) NOT NULL," + 
				TASK_TABLE_COLUMN_KEY_DESC + " TEXT NULL," + 
				TASK_TABLE_COLUMN_KEY_FINISH_TO_DATE + " INTEGER NULL," + 
				TASK_TABLE_COLUMN_KEY_FINISHED_DATE + " INTEGER NULL," + 
				TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + " INTEGER NOT NULL," + 
				TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + " INTEGER NOT NULL," + 
				TASK_TABLE_COLUMN_KEY_INS_DATE + " INTEGER NOT NULL," + 
				TASK_TABLE_COLUMN_KEY_UPD_DATE + " INTEGER NOT NULL," + 
				TASK_TABLE_COLUMN_KEY_UNREAD + " INTEGER NOT NULL DEFAULT (1)," +
				
				//cizi klice
				"FOREIGN KEY(" + TASK_TABLE_COLUMN_KEY_CATEG_ID + ") REFERENCES " + TASK_CATEG_TABLE + "(" + TASK_CATEG_TABLE_COLUMN_KEY_ID + ")," +
				"FOREIGN KEY(" + TASK_TABLE_COLUMN_KEY_LOGIN_ID_EXECUTOR + ") REFERENCES " + LOGIN_TABLE + "(" + LOGIN_TABLE_COLUMN_KEY_ID + ")," +
				"FOREIGN KEY(" + TASK_TABLE_COLUMN_KEY_LOGIN_ID_INS + ") REFERENCES " + LOGIN_TABLE + "(" + LOGIN_TABLE_COLUMN_KEY_ID + ")" +
				")";
		
		db.execSQL(CREATE_TASK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TASK_CATEG_TABLE);

		// Create tables again
		onCreate(db);
	}

}
