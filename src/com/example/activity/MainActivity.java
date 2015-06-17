package com.example.activity;

import java.lang.reflect.Method;
import com.example.fragment.ChosenVodFragment;
import com.example.fragment.LatestVodFragment;
import com.example.fragment.SpeciesVodFragment;
import com.example.utils.MySysApplication;
import com.example.yuejiaoyun.R;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.ActionBar.Tab;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import com.igexin.sdk.PushManager;
import setting.setting;

public class MainActivity extends FragmentActivity 
	implements ActionBar.TabListener{
	
	ViewPager viewPager;
	ActionBar actionBar;
	
	//用于判断“两次按返回键后退出”的当前时间
	private long mExitTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
     	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PushManager.getInstance().initialize(this.getApplicationContext());
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		
		//根据控件ID得到控件
		//添加该Activity到MySysApplication对象实例容器中
		MySysApplication.getInstance().addActivity(this);
		
		// 获取ActionBar对象
		actionBar = getActionBar();
		// 获取ViewPager
		viewPager = (ViewPager) findViewById(R.id.pager);
		// 创建一个FragmentPagerAdapter对象，该对象负责为ViewPager提供多个Fragment
		FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(
				getSupportFragmentManager())
		{
			// 获取第position位置的Fragment
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				switch(position)
				{//顺序依次是 “分类”、“精选”、“最新”
					case 0:
						SpeciesVodFragment speciesVodFragment = new SpeciesVodFragment();
						return speciesVodFragment;
					case 1:
						ChosenVodFragment chosenVodFragment = new ChosenVodFragment();
						return chosenVodFragment;
					case 2:
						LatestVodFragment latestVodFragment = new LatestVodFragment();
						return latestVodFragment;
				}
				return null;
		}

			// 该方法的返回值i表明该Adapter总共包括多少个Fragment
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 3;
			}
		};
		// 设置ActionBar使用Tab导航方式
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// 为每个Fragment对应创建Tab标签
		Tab tab = actionBar.newTab().setText("分类").setTabListener(this);
		actionBar.addTab(tab);
		tab = actionBar.newTab().setText("精选").setTabListener(this);
		actionBar.addTab(tab);
		tab = actionBar.newTab().setText("最新").setTabListener(this);
		actionBar.addTab(tab);
		// 为ViewPager组件设置FragmentPagerAdapter
		viewPager.setAdapter(pagerAdapter);
		// 为ViewPager组件绑定事件监听器
		viewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener()
				{
					// 当ViewPager显示的Fragment发生改变时激发该方法
					@Override
					public void onPageSelected(int position)
					{
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		
		
		Log.d("个推推送", "initializing sdk...");
     	PushManager.getInstance().initialize(this.getApplicationContext());
		
		

	}//onCreate()结束
	
	
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	// 当指定Tab被选中时激发该方法
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
 
	//复写onKeyDown方法 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//捕获 BACK按键事件处理
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
            	MySysApplication.getInstance().exit();
            }
            return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//用reflect反射机制 设置 menu item 可以显示出Icon
		setIconEnable(menu, true);
		getMenuInflater().inflate(R.menu.menu_main_activity, menu);
		
		// Get the SearchView and set the searchable configuration  searchButton
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_view).getActionView();
	    
	    // Assumes current activity is the searchable activity 
	    //为SearchView设定searchable.xml中定义的各属性
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //设定 搜索框 一开始 是 作为图标 收起的
	    searchView.setIconifiedByDefault(true);
		/*
		//获取到SearchView
		SearchView searchView = (SearchView)menu.findItem(R.id.search_menu).getActionView();
		//设置 该SearchView 内默认显示的提示文本
		searchView.setQueryHint("搜索");
		//设置该SearchView显示 搜索 按钮
		searchView.setSubmitButtonEnabled(true);
		
		
		View mainView = getCurrentFocus();
		//为该SearchView组件设置事件监听器
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			View searchContentView = getLayoutInflater().inflate(R.layout.activity_showvods_in_selectedpplist, null);
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				System.out.println("submit--"+ query);
				//actionBar.setDisplayShowCustomEnabled(true);
				//actionBar.setCustomView(searchContentView);
				//searchContentView.setAlpha(0.5f);
				//setContentView(searchContentView);
				LayoutParams params = searchContentView.getLayoutParams();
				params.
				searchContentView.setLayoutParams(params);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(newText)){
					System.out.println("newText--"+newText);
				}

				return false;
			}
		});*/
		return true;
	}
	//菜单被单击后的回调方法
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		/*很好的菜单栏选项响应方法示例 http://blog.sina.com.cn/s/blog_798c1d510101d5ln.html*/
		switch(item.getItemId()){
			/*case R.id.menu_search_view:
				Intent intent = new Intent(this,SearchableActivity.class);
				startActivity(intent);
				break; */
			case R.id.menu_exit:
				exitAlert("真的要退出吗？");
				break;
			case R.id.menu_playHistory: //跳转到PlayHistoryActivity
				Intent intent = new Intent(this,PlayHistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.menu_share:
				 Intent intent001=new Intent(Intent.ACTION_SEND);
			      
				 intent001.setType("text/plain");
			      intent001.putExtra(Intent.EXTRA_SUBJECT, "分享");
			      intent001.putExtra(Intent.EXTRA_TEXT, "小伙伴们，还不快用粤教云客户端！有了它，妈妈再也不用担心我的学习");
			      startActivity(Intent.createChooser(intent001, getTitle()));
			      break;
			      
			case R.id.menu_search:
				 Intent intent4=new Intent();
				   intent4.setClass(MainActivity.this, search.class);
				   MainActivity.this.startActivity(intent4);
					break;
			case R.id.menu_test:
				 Intent intent003=new Intent();
				   intent003.setClass(MainActivity.this, webpageloader.class);
				   MainActivity.this.startActivity(intent003);
					break;
					
			case R.id.menu_likes:
				 Intent intent004=new Intent();
				   intent004.setClass(MainActivity.this, PlayLikesActivity.class);
				   MainActivity.this.startActivity(intent004);
		          break;
		          
		          
			case R.id.menu_setting:
				 Intent intent005=new Intent();
				   intent005.setClass(MainActivity.this, setting.class);
				   MainActivity.this.startActivity(intent005);
					break;
		}
		//super.onOptionsItemSelected(item)
		return super.onOptionsItemSelected(item);
	}
	//enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
	/**
	 * 解决android4.0系统中菜单(Menu)添加Icon无效问题，参考了  http://blog.csdn.net/stevenhu_223/article/details/9705173
	 * 在Android4.0系统中，创建菜单Menu，通过setIcon方法给菜单添加图标是无效的 ,因为 菜单的源码类 MenuBuilder做了改变，默认private boolean mOptionalIconsVisible = false;  
	 * 考虑用反射了，在代码运行创建菜单的时候通过反射调用setOptionalIconsVisible方法设置mOptionalIconsVisible为true，
	 * 然后在给菜单添加Icon，这样就可以在菜单中显示添加的图标了
	 * @param menu
	 * @param enable
	 */
    private void setIconEnable(Menu menu, boolean enable)
    {
    	try 
    	{
			Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
			m.setAccessible(true);
			
			//MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
			m.invoke(menu, enable);
    		
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
    }
    
    private void exitAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        ShareSDK.stopSDK(this);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                	
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        //finish(); 经过测试后发现搞不定额
                        //System.exit(0);  经过测试后发现搞不定额
                    	MySysApplication.getInstance().exit();
                    	
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        return;
                    }
                }).create().show();
    }
}