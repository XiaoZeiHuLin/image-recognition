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

public class Main6Activity extends AppCompatActivity {

	DBHelper dbHelper;
	SQLiteDatabase sqLiteDatabase;

	Intent intent;

	EditText editText_primary;
	EditText editText_new;
	EditText editText_re;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main6);

		intent = getIntent();

		editText_primary = findViewById(R.id.primary_password);
		editText_new = findViewById(R.id.new_password);
		editText_re = findViewById(R.id.re_password);
	}

	public void confirm(View view){
		dbHelper = new DBHelper(this);
		sqLiteDatabase = dbHelper.getWritableDatabase();
		String[] args = {String.valueOf(intent.getStringExtra("account"))};
		Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_1,new String[]{DBHelper.password},"account=?",args,null,null,null);
		int password_index = cursor.getColumnIndex(DBHelper.password);
		while (cursor.moveToNext()){
			if (editText_primary.getText().toString().equals(cursor.getString(password_index))){
				Toast.makeText(this,"找到了！",Toast.LENGTH_SHORT).show();
				if (editText_new.getText().toString().equals(editText_re.getText().toString())){
					ContentValues values = new ContentValues();
					values.put("password",editText_new.getText().toString());
					String[] a = {String.valueOf(intent.getStringExtra("account"))};
					sqLiteDatabase.update(DBHelper.TABLE_NAME_1,values,"account=?",a); // 修改数据库中存储的本账户的密码
					Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
			}
		}
	}
}
