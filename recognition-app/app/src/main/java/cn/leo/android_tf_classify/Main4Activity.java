package cn.leo.android_tf_classify;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {

	TextView textView;

	String str = "";

	DBHelper dbHelper;
	SQLiteDatabase sqLiteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main4);

		textView = findViewById(R.id.textView);

		dbHelper = new DBHelper(this);
		sqLiteDatabase = dbHelper.getWritableDatabase();


		/** 只查询并显示本账户的识别记录 **/
		Intent intent = getIntent();
		str = intent.getStringExtra("account") + "的数据：\n";
		String[] args = {String.valueOf(intent.getStringExtra("account"))};
		Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_2,new String[]{DBHelper.time,DBHelper.result_1_name,DBHelper.result_1_rate},"account=?",args,null,null,null);
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
