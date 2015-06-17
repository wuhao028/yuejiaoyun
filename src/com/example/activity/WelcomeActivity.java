package com.example.activity;


import java.util.Timer;
import java.util.TimerTask;

import com.example.utils.MySysApplication;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.yuejiaoyun.R;
import com.igexin.sdk.PushManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	//获取到“粤教云”背景图片
	ImageView welcome_background = null;
	//设定 Alpha渐现动画持续时间（亦即 页面跳转时间）
	private final static long DurationMillis = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//从 http://blog.csdn.net/wangjinyu501/article/details/8140588 上学到
		//隐藏 菜单栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		
		
		network();
		
		
		
		PushManager.getInstance().initialize(this.getApplicationContext());
		//添加该Activity到MySysApplication对象实例容器中
		MySysApplication.getInstance().addActivity(this);
		
		//使全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//System.out.println("welcomeActivity begin-->"+new WebInfoConfig().UnixTimeStamp);;
		/*
		 * 启动Service完成数据更新
		 */
		System.out.println("welcomeactivity--service--begin");
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.example.service.LOAD_PPLIST");
     	startService(serviceIntent);
		System.out.println("welcomeactivity--service--end");
		/**
		 * ==============功能1，完成Alpha动画==================
		
		welcome_background = (ImageView)findViewById(R.id.welcome_background);
		//用代码设定 Alpha动画，新建一个动画集合，用于盛放AlphaAnimation
		AnimationSet animationSet = new AnimationSet(true);
		//设定变化过程，从可见度为0.5 变成 完全可见 1  //两个参数分别表示初始透明度和目标透明度，1表示不透明，0表示完全透明
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1);
		//设置动画持续时间
		alphaAnimation.setDuration(DurationMillis);
		//加入到动画集合中
		animationSet.addAnimation(alphaAnimation);
		//开始动画效果
		welcome_background.startAnimation(animationSet);
		//==============功能1  结束==================
		
		
		*/
		/**
		 * 在显示Alpha动画的同时，完成程序的自检：
		 * 1、检查版本号
		 * 2、若没有找到必须的数据库，就需要创建并更新数据
		 */
		//待完成，需要用到数据库操作 和  使用 Apache HttpClient 请求WebService获得版本号
		
		
		/*测试是否具备SD卡权限
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			System.out.println("具备访问SD卡权限");
		}*/
		
		
		/**
		 * 自检数据库 OK  
		 * 但是程序报错：The application may be doing too much work on its main thread.
		 * 导致后面的动画效果没了，所以打算在此 新开线程完成自检
		 * 2014-3-28  16:53
		 
		DBOpenHelper dbOpenHelper = new DBOpenHelper(this, "yjy.db", null, 1);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into pp_list values(1,0,2,1,'zhongxue','pp_vodchannel','movie',null,null,null)");
		System.out.println("插入数据成功");
		*/
		
		/**
		 * 尝试用LoadDBAsyncTask开启异步任务来完成自检
		 */
		//LoadDBAsyncTask loadDBAsyncTask = new LoadDBAsyncTask();

		//loadDBAsyncTask.execute();


		
	
		//System.out.println("welcomeActivity end-->"+new WebInfoConfig().UnixTimeStamp);;
		System.out.println("beigin timerTask");
		/**
		 * =====创建Timer延时任务 来完成跳转=====
	
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mainActivityIntent = new Intent();
				mainActivityIntent.setClass(WelcomeActivity.this, MainActivity.class);
				startActivity(mainActivityIntent);
				
			}
		}, DurationMillis*2);*/
		
	
	  new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// 完成跳转
				Intent mainActivityIntent = new Intent();
				mainActivityIntent.setClass(WelcomeActivity.this, MainActivity.class);
				startActivity(mainActivityIntent);
				//关掉当前Activity
				finish();
			}
		}, DurationMillis*2);

		//=====创建Timer延时任务 来完成跳转  over===== 
	}
	

	protected void network() {
		ConnectivityManager manager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			Toast.makeText(WelcomeActivity.this, "网络连接连接", 0).show();
		} else {
			AlertDialog.Builder builder = new Builder(WelcomeActivity.this);
			builder.setTitle("开启网络服务");
			builder.setMessage("网络没有连接，请到设置进行网络设置！");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (android.os.Build.VERSION.SDK_INT > 10) {
								// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
								startActivity(new Intent(
										android.provider.Settings.ACTION_SETTINGS));
							} else {
								startActivity(new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							}
							dialog.cancel();
						}
					});

			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.show();
		}
		super.onStart();
	}
	

	
}
