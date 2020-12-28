package cn.leo.android_tf_classify;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "my_database";
	public static final String TABLE_NAME_1 = "user";
	public static final String TABLE_NAME_2 = "data";
	public static final int DB_VERSION = 1;
	public static final String account = "account";
	public static final String password = "password";
	public static final String time = "time";
	public static final String result_1_name = "result_1_name";
	public static final String result_1_rate = "result_1_rate";


	public DBHelper(Context context){
		super(context,DB_NAME,null,DB_VERSION);
	}

	/** 创建SQLite数据库 **/
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql_user = "create table " + TABLE_NAME_1 + "(_id integer primary key autoincrement, " + account + " varchar, " + password + " varchar)";
		String sql_data = "create table " + TABLE_NAME_2 + "(_id integer primary key autoincrement, " + account + " varchar, " + time + " varchar, " + result_1_name + " varchar, " + result_1_rate + " varchar)";
		db.execSQL(sql_user);
		db.execSQL(sql_data);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
