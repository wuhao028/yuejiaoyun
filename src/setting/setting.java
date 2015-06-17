package setting;


import java.io.File;
import android.content.Context;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.activity.MainActivity;
import com.example.activity.webpageloader;
import com.example.yuejiaoyun.R;
import setting.ReportOval;

public class setting extends Activity {
	
    private Button cachebutton =null;
//	private Button updatebutton = null;
	private Button cleansetting=null;
	private Button traffic=null;
    private Button about=null;
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		 
		
	
		//设置监听器
		      cachebutton=(Button)findViewById(R.id.cache);
			  cachebutton.setOnClickListener(new ButtonListener());
		 
//			  updatebutton=(Button)findViewById(R.id.update);
//				设置监听器
//		      updatebutton.setOnClickListener(new MyButtonListener());	
		      
		      cleansetting=(Button)findViewById(R.id.cleansetting);
		      cleansetting.setOnClickListener(new cleanListener());
		      
		      traffic=(Button)findViewById(R.id.traffic);
		      traffic.setOnClickListener(new trafficListener());
		      
		      about=(Button)findViewById(R.id.about);
		      about.setOnClickListener(new aboutListener());
		      
		      final ActivityManager activityManager = (ActivityManager)   getSystemService(ACTIVITY_SERVICE);    
		      
		        ActivityManager.MemoryInfo   info = new ActivityManager.MemoryInfo();   
		   
		          activityManager.getMemoryInfo(info);    
		   
//		          Log.i(ACCESSIBILITY_SERVICE,"系统剩余内存:"+(info.availMem >> 10)+"k");    
//		          Log.i(ACCESSIBILITY_SERVICE,"系统是否处于低内存运行："+info.lowMemory);
//		          Log.i(ACCESSIBILITY_SERVICE,"当系统剩余内存低于"+info.threshold+"时就看成低内存运行");
		          
		          long lowmm=info.availMem >>20;
		          long allmm=info.totalMem>>20;
		          
		    ReportOval myOval = (ReportOval) findViewById(R.id.oval);
		  	myOval.setWeights(new float[]{lowmm,1000-lowmm});
		  	myOval.setValues(new String[]{"可用内存"+lowmm+"M","全部内存"+allmm+"M"});
		  	myOval.setColors(new int[]{Color.YELLOW,Color.GREEN});
		  	myOval.setTopOvalColor(Color.WHITE);
//		  	myOval.setLineWidth(0);
		  	
	}

	class ButtonListener extends DataCleanManager implements OnClickListener{
    	
		//生成该类的对象，并将其注册到控件上。如果该控件被用户按下，就会执行onClick方法 
		@Override
		public void onClick(View v) {
			
			  cleanInternalCache(getBaseContext());
			  cleanExternalCache(getBaseContext());
		      cleanFiles(getBaseContext());
		       
		      
		      Toast toast=Toast.makeText(getApplicationContext(),"缓存已清除",Toast.LENGTH_SHORT);
		      toast.show();
				}
		}
	
	
	
	class MyButtonListener implements OnClickListener{
    	//生成该类的对象，并将其注册到控件上。如果该控件被用户按下，就会执行onClick方法 
		@Override
		public void onClick(View v) {
//检测更新
			  Toast toast=Toast.makeText(getApplicationContext(),"已是最新版",Toast.LENGTH_SHORT);
		      toast.show();
		}
	}
	
	
	
	class trafficListener implements OnClickListener{
		
		public void onClick(View v) {
//流量统计
		   Intent intent001=new Intent();		
		   intent001.setClass(setting.this, trafficstats.class);
		   setting.this.startActivity(intent001);
					}
		
	}
	
	
//	关于页面监听器
   class aboutListener implements OnClickListener{
		
		public void onClick(View v) {
//流量统计
		   Intent intent0011=new Intent();		
		   intent0011.setClass(setting.this, about.class);
		   setting.this.startActivity(intent0011);
					}
		
	}
	
	
	
	
	class cleanListener extends DataCleanManager implements OnClickListener{
    	//生成该类的对象，并将其注册到控件上。如果该控件被用户按下，就会执行onClick方法 
		@Override
		public void onClick(View v) {
			
		      cleanDatabases(getBaseContext());
		      cleanSharedPreference(getBaseContext());
		      
		      Toast toast=Toast.makeText(getApplicationContext(),"收藏和设置已清除",Toast.LENGTH_SHORT);
		      toast.show();
			
		}
	}
	

	
	
	
	public class DataCleanManager {
	    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
	    public void cleanInternalCache(Context context) {
	        deleteFilesByDirectory(context.getCacheDir());
	    }

	    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
	    public void cleanDatabases(Context context) {
	        deleteFilesByDirectory(new File("/data/data/"
	                + context.getPackageName() + "/databases"));
	    }

	    /**
	     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
	     * context
	     */
	    public void cleanSharedPreference(Context context) {
	        deleteFilesByDirectory(new File("/data/data/"
	                + context.getPackageName() + "/shared_prefs"));
	    }

	    /** * 按名字清除本应用数据库 * * @param context * @param dbName */
	    public void cleanDatabaseByName(Context context, String dbName) {
	        context.deleteDatabase(dbName);
	    }

	    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
	    public void cleanFiles(Context context) {
	        deleteFilesByDirectory(context.getFilesDir());
	    }

	    /**
	     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
	     * context
	     */
	    public void cleanExternalCache(Context context) {
	        if (Environment.getExternalStorageState().equals(
	                Environment.MEDIA_MOUNTED)) {
	            deleteFilesByDirectory(context.getExternalCacheDir());
	        }
	    }

	    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
	    public void cleanCustomCache(String filePath) {
	        deleteFilesByDirectory(new File(filePath));
	    }

	    /** * 清除本应用所有的数据 * * @param context * @param filepath */
	    public void cleanApplicationData(Context context, String... filepath) {
	        cleanInternalCache(context);
	        cleanExternalCache(context);
	        cleanDatabases(context);
	        cleanSharedPreference(context);
	        cleanFiles(context);
	        
	        for (String filePath : filepath) {
	            cleanCustomCache(filePath);
	        }
	    }

	    
	    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	    public void deleteFilesByDirectory(File directory) {
	        if (directory != null && directory.exists() && directory.isDirectory()) {
	            for (File item : directory.listFiles()) {
	                item.delete();
	            }
	        }
	    }
	}
	

}

