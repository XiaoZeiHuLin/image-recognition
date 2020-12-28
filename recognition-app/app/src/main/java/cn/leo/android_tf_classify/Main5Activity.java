package cn.leo.android_tf_classify;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main5Activity extends AppCompatActivity {

	TextView textView;

	String str = "所有数据：\n";

	DBHelper dbHelper;
	SQLiteDatabase sqLiteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main5);

		textView = findViewById(R.id.textView2);

		dbHelper = new DBHelper(this);
		sqLiteDatabase = dbHelper.getWritableDatabase();

		/** 查询并显示所有账户的识别记录 **/
		Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_2,new String[]{DBHelper.time,DBHelper.result_1_name,DBHelper.result_1_rate},null,null,null,null,null);
		int time_index = cursor.getColumnIndex(DBHelper.time);
		int name_index = cursor.getColumnIndex(DBHelper.result_1_name);
		int rate_index = cursor.getColumnIndex(DBHelper.result_1_rate);
		while (cursor.moveToNext()){
			str = str + cursor.getString(time_index) + " " + cursor.getString(name_index) + " " + cursor.getString(rate_index) + "\n";
		}
		textView.setText(str);
		str = "";
	}
}
