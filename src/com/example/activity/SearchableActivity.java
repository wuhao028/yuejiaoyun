package com.example.activity;

import java.util.List;

import com.example.contentprovider.RecentSuggestionsProvider;
import com.example.dao.OperateDAO;
import com.example.model.PpVodItem;
import com.example.utils.ActivityVodAdapter;
import com.example.utils.MySysApplication;
import com.example.yuejiaoyun.R;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

public class SearchableActivity extends Activity {

	String queriedStr = null;
	ListView searchResultLV = null;
	SearchView searchView = null;
	List<PpVodItem> vodItems = null;
	OperateDAO dao = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		//添加该Activity到MySysApplication对象实例容器中
		MySysApplication.getInstance().addActivity(this);
		//初始化全局变量、数据、控件等等
		searchResultLV = (ListView)findViewById(R.id.searchResultLV);
		dao = new OperateDAO(this);
		handleIntent(getIntent());  
	}
	
	//与该activity在manifest配置文件中的属性 launchMode =singTop 相对应 
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
		
	}
	//www.cnblogs.com/over140/archive/2012/01/06/2314114.html
	//处理 onCreate 和onNewIntent 提供的intent Search 请求
	private void handleIntent(Intent intent) {
		// 当用户在搜索框内回车确定搜索某个内容的时候，将通过Intent.ACTION_SEARCH渠道到此SearchableActivity
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		      String query = intent.getStringExtra(SearchManager.QUERY);
		      //判断 此次查询和上次查询是否重复
		      if(!query.equals(queriedStr)){
		    	  //去完成 真正的 search 工作
		    	  doEnterSearch(query);
		    	  //备份query字符串为 查询过的queriedStr字符串
		    	  queriedStr = query;
		    	  //创建一个SearchRecentSuggestions实例，调用saveRecentQuery方法将收到的查询关键词进行保存
		    	  SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
		    			  RecentSuggestionsProvider.AUTHORITY, RecentSuggestionsProvider.MODE); 
		          suggestions.saveRecentQuery(query, null); 
		      }else return;
		    }else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
		        // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
		    	//当用户 选中了 建议项 中的某一项时，将通过Intent.ACTION_VIEW渠道跳转到此
		    	//在此 获取 选中的 建议项 的data数据，完成“搜索结果的显示”或者“跳转”
		    	System.out.println("跳转到了SearchableActivity的ACTION_VIEW分支：");
		        Uri data = intent.getData();
		        System.out.println("Uri Str--"+ data.toString());
		        String vod_id = data.getLastPathSegment();
		        doPointSearch(vod_id);
		    }
	}
	
	/**
	 * 完成用Enter键入的搜索动作，将搜索得到的结果赋值到全局变量 List<PpVodItem> vodItems中
	 * @param query--所要搜索的字符串
	 */
	private void doEnterSearch(String query) {
		// TODO Auto-generated method stub
		System.out.println("doMySearch's query--"+query);
		vodItems = dao.searchVodLikeStr(query);
		showSearchedVodsResult();
	}

	/**
	 * 完成从建议项中选中的某条数据的显示，其实也需要完成一次搜索，只不过这次搜索已经通过“选中的建议项”知道了那个确定的PpVodItem
	 * @param vod_id  那个指定的建议项所提供的vod_id
	 */
	private void doPointSearch(String vod_id) {
		System.out.println("SearchableActivity doPointSearch()获得的vod_id--"+vod_id);
		vodItems = dao.searchVodByVod_id(vod_id);
		showSearchedVodsResult();
	}
	
	/**
	 * 如果不为空的话，显示全局变量List<PpVodItem> vodItems中的数据
	 */
	private void showSearchedVodsResult() {
		
		if(!vodItems.isEmpty()){
			//如果查询到的结果非空
			ActivityVodAdapter adapter = new ActivityVodAdapter(this, vodItems);
			searchResultLV.setAdapter(adapter);
			searchResultLV.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					// TODO Auto-generated method stub 
					System.out.println("data from tag--->"+view.getTag().toString());

					Intent intent = new Intent(view.getContext(), VideoPlayActivity.class);
					intent.putExtra("wantShowVod", (PpVodItem)view.getTag());
					startActivity(intent);
				}
			});
		}else{
			System.out.println("doMySearch's vodItems isEmpty");
			Toast.makeText(this, "没有找到匹配的视频", 2000 );
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_search_activity, menu);
	    
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
	    
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //设置搜索框是否收起，此处false即打开搜索框
	    searchView.setIconifiedByDefault(false);
	    //里面Hint默认的值
	    searchView.setQueryHint(queriedStr);
	    /*searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				//Toast.makeText(this, "您的选择是:" + query,Toast.LENGTH_SHORT).show();
				System.out.println("query--"+query);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEARCH);
				intent.putExtra(SearchManager.QUERY, query);
				startActivity(intent);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(newText)){
					System.out.println("newText--"+newText);
				}
				return true;
			}
		});*/
	    return true;
	}
	
}
