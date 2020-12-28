package cn.leo.android_tf_classify;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.otaliastudios.cameraview.AspectRatio;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.SizeSelector;
import com.otaliastudios.cameraview.SizeSelectors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

	DBHelper dbHelper;
	SQLiteDatabase sqLiteDatabase;

	String account;

    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128;
    private static final String INPUT_NAME = "Mul";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/output_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/output_labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private Button btnDetectObject;
    private ImageView imageViewResult;
    private CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    dbHelper = new DBHelper(this);
	    sqLiteDatabase = dbHelper.getWritableDatabase();

	    Intent intent = getIntent();
	    account = intent.getStringExtra("account");

	    cameraView = (CameraView) findViewById(R.id.cameraView);
	    imageViewResult = (ImageView) findViewById(R.id.imageViewResult);
	    textViewResult = (TextView) findViewById(R.id.textViewResult);
	    textViewResult.setMovementMethod(new ScrollingMovementMethod());

	    btnDetectObject = (Button) findViewById(R.id.btnDetectObject);

	    cameraView.addCameraListener(new CameraListener() {
		    @Override
		    /** （4）当获取到一张照片时 **/
		    public void onPictureTaken(byte[] picture) {
			    try {
				    Bitmap  bitmap =BitmapFactory.decodeByteArray(picture,0,picture.length);
				    bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

				    imageViewResult.setImageBitmap(bitmap);

				    /** （5）使用分类器对照片进行识别并给出结果 **/
				    final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

				    textViewResult.setText(results.get(0).toString().split(" ")[1] + results.get(0).toString().split(" ")[2].split("%")[0].substring(1));

				    /** （6）将识别结果与账户、时间等信息一同存入数据库 **/
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH mm SS");
				    Date date = new Date(System.currentTimeMillis());
				    ContentValues values = new ContentValues();
				    values.put(DBHelper.account, account);
				    values.put(DBHelper.time, simpleDateFormat.format(date));
				    values.put(DBHelper.result_1_name, results.get(0).toString().split(" ")[1]);
				    values.put(DBHelper.result_1_rate, results.get(0).toString().split(" ")[2].split("%")[0].substring(1));
				    sqLiteDatabase.insert(DBHelper.TABLE_NAME_2,null,values);
				    Toast.makeText(getApplicationContext(),"存入成功!",Toast.LENGTH_LONG).show();
			    }catch (Exception e){
				    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
			    }
		    }
	    });
	    SizeSelector width = SizeSelectors.maxWidth(224);
	    SizeSelector height = SizeSelectors.maxHeight(224);
	    SizeSelector dimensions = SizeSelectors.and(width, height); // Matches sizes bigger than 1000x2000.
	    SizeSelector ratio = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0); // Matches 1:1 sizes.
	    SizeSelector result = SizeSelectors.or(
			    SizeSelectors.and(ratio, dimensions), // Try to match both constraints
			    ratio, // If none is found, at least try to match the aspect ratio
			    SizeSelectors.biggest() // If none is found, take the biggest
	    );
	    cameraView.setPictureSize(result);

	    btnDetectObject.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	/** （3）拍照 **/
			    cameraView.capturePicture();
		    }
	    });


	    executor.execute(new Runnable() {
		    @Override
		    public void run() {
			    try {
			    	/** （1）依据训练结果与分类结果等创建一个基于Tensorflow的图像分类器 **/
				    classifier = TensorFlowImageClassifier.create(getAssets(), MODEL_FILE, LABEL_FILE, INPUT_SIZE, IMAGE_MEAN, IMAGE_STD, INPUT_NAME, OUTPUT_NAME);
				    runOnUiThread(new Runnable() {
					    @Override
					    public void run() {
					    	/** （2）使拍照按键显示在界面上 **/
						    btnDetectObject.setVisibility(View.VISIBLE);
					    }
				    });
			    } catch (final Exception e) {
				    throw new RuntimeException("Error initializing TensorFlow!", e);
			    }
		    }
	    });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            cameraView.start();
        }catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }
}
