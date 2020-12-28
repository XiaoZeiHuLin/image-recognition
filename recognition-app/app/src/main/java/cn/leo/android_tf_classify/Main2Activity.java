package cn.leo.android_tf_classify;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

	DBHelper dbHelper;
	SQLiteDatabase sqLiteDatabase;

	EditText account;
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		account = findViewById(R.id.account);
		password = findViewById(R.id.password);
	}

	/** 登录：查询账户和密码 **/
	public void login(View view){
		try {
			dbHelper = new DBHelper(this);
			sqLiteDatabase = dbHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_1,new String[]{DBHelper.account,DBHelper.password},null,null,null,null,null);
			int account_index = cursor.getColumnIndex(DBHelper.account);
			int password_index = cursor.getColumnIndex(DBHelper.password);
			while (cursor.moveToNext()){
				/** 若账户和密码都正确，则跳转到主界面 **/
				if (account.getText().toString().equals(cursor.getString(account_index)) && password.getText().toString().equals(cursor.getString(password_index))){
					Intent intent = new Intent(this,Main3Activity.class);
					intent.putExtra("account",account.getText().toString());
					startActivity(intent);
					return;
				}
			}
			Toast.makeText(this,"账户或密码错误",Toast.LENGTH_SHORT).show();
		}catch (Exception e){
			Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
		}

	}

	/** 注册：向数据库输入账号、密码 **/
	public void signin(View view){
		dbHelper = new DBHelper(this);
		sqLiteDatabase = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBHelper.account, account.getText().toString());
		values.put(DBHelper.password, password.getText().toString());
		sqLiteDatabase.insert(DBHelper.TABLE_NAME_1,null,values);
		Toast.makeText(this,"注册成功！",Toast.LENGTH_SHORT).show();
	}
}