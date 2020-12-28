package cn.leo.android_tf_classify;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

	TextView textView;

	String account;

	DBHelper dbHelper;
	SQLiteDatabase sqLiteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main3);
		textView = findViewById(R.id.apptitle);

		/** 获取当前登录进来的登录者的账户 **/
		Intent intent = getIntent();
		account = intent.getStringExtra("account");

		textView.setText("欢迎您，" + account);
	}

	/** 跳转到图像识别界面 **/
	public void recognition(View view){
		Intent intent = new Intent(this,MainActivity.class);
		intent.putExtra("account",account);
		startActivity(intent);
	}

	/** 跳转到查询界面 **/
	public void query(View view){
		Intent intent = new Intent(this,Main4Activity.class);
		intent.putExtra("account",account);
		startActivity(intent);
	}

	/** 跳转到分析界面 **/
	public void analyze(View view){
		Intent intent = new Intent(this,Main5Activity.class);
		startActivity(intent);
	}

	/** 跳转到设置界面 **/
	public void setting(View view){
		Intent intent = new Intent(this,Main6Activity.class);
		intent.putExtra("account",account);
		startActivity(intent);
	}

	public void test(View view){
		dbHelper = new DBHelper(this);
		sqLiteDatabase = dbHelper.getWritableDatabase();
		Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_2,new String[]{DBHelper.account,DBHelper.time,DBHelper.result_1_name,DBHelper.result_1_rate},null,null,null,null,null);
		int account_index = cursor.getColumnIndex(DBHelper.account);
		int time_index = cursor.getColumnIndex(DBHelper.time);
		int name_index = cursor.getColumnIndex(DBHelper.result_1_name);
		int rate_index = cursor.getColumnIndex(DBHelper.result_1_rate);
		while (cursor.moveToNext()){
			Toast.makeText(this,cursor.getString(account_index) + "\n" + cursor.getString(time_index) + "\n" + cursor.getString(name_index) + "\n" + cursor.getString(rate_index), Toast.LENGTH_LONG).show();
		}
	}

	public void clear(View view){
		dbHelper = new DBHelper(this);
		sqLiteDatabase = dbHelper.getWritableDatabase();
		int count = sqLiteDatabase.delete(DBHelper.TABLE_NAME_2,null,null);
		sqLiteDatabase.close();
		Toast.makeText(this,"delete " + count + "data(s)",Toast.LENGTH_SHORT).show();
	}
}
