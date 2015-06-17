package com.example.activity;

import info.info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.constants.DBInfoConfig;
import com.example.dao.DBOpenHelper;
import com.example.dao.OperateDAO;
import com.example.model.PpVodItem;
import com.example.yuejiaoyun.R;
import com.example.utils.ActivityVodAdapter;
import com.example.utils.MySysApplication;
import com.example.dao.OperateDAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class ShowVodsInSelectedPpList extends Activity {
	//放置异步加载的Drawable 对象，key为vod_pic
	Map<String,Drawable> drawableMap = new HashMap<String, Drawable>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showvods_in_selectedpplist);
		//添加该Activity到MySysApplication对象实例容器中
		MySysApplication.getInstance().addActivity(this);
		
		//获得跳转过来的Intent对象，可以直接从Intent中取得其携带的默认bundle数据包
		Intent intent = getIntent();
		int list_id = intent.getIntExtra("list_id", -1);
		//if(list_id == -1) error
		String list_name = intent.getStringExtra("list_name");
		//数据库操作，按list_id查出该分类下的所有vod
		OperateDAO dao = new OperateDAO(this);
		
		List<PpVodItem> vodItems = dao.getVodsByPpList(String.valueOf(list_id));
		if(!vodItems.isEmpty()){//如果结果集非空
			//打印一下结果先
			/*for (PpVodItem ppVodItem : vodItems) {
				System.out.println(ppVodItem.toString());
			}*/
			
			/*注释掉 ----》获得每个vod的Drawalbe图像
			for (PpVodItem ppVodItem : vodItems) {
				String vod_id = ppVodItem.getVod_id();
				String vod_pic = ppVodItem.getVod_pic();
				AsyncImageLoader loader = new AsyncImageLoader();
				//loader.loadDrawable(vod_pic, this);
				Drawable tempDrawable  = loader.loadImageFromUrl(vod_pic);
				drawableMap.put(vod_pic, tempDrawable);
			}*/
			/*遍历Map，查看下 是否在下面 使用adapter之前就已经把drawableMap填充好了
			 Set<String> key = drawableMap.keySet();
		        for (Iterator it = key.iterator(); it.hasNext();) {
		            String s = (String) it.next();
		            System.out.println("mapKey--"+s);
		            //System.out.println("drawableMap-->"+s+"---"+drawableMap.get(s).toString());
		        }*/
			System.out.println("为ListView配置数据");
			ListView vodsList = (ListView)findViewById(R.id.vodsList);
			/* 用adapter配置数据之前，需要把数据源包装成List<Map>类型
			List<Map<String,Object>> adapterData = new ArrayList<Map<String,Object>>();
			for (PpVodItem ppVodItem : vodItems) {
				Map<String,Object> map = new HashMap<String, Object>();
				//vod表结构 vod_id integer primary key,vod_cid,vod_name,vod_sourceid,vod_pic
				map.put("vod_id", ppVodItem.getVod_id());
				map.put("vod_cid", ppVodItem.getVod_cid());
				map.put("vod_name", ppVodItem.getVod_name());
				map.put("vod_sourceid", ppVodItem.getVod_sourceid());
				map.put("vod_pic", ppVodItem.getVod_pic());
				adapterData.add(map);
			}
			
			尝试发现simpleadapter功能不足以解决图片的异步加载问题，故继承BaseAdapter来定义自己的adapter
			SimpleAdapter adapter = new SimpleAdapter(this, adapterData, R.layout.simple_showvods_info_item,
					new String[]{"vod_id","vod_cid","vod_name","vod_sourceid","vod_pic"}, 
					new int[]{R.id.vod_id,R.id.vod_cid,R.id.vod_name,R.id.vod_sourceid,R.id.vod_pic});
					*/
			ActivityVodAdapter activityVodAdapter = new ActivityVodAdapter(this, vodItems);
			vodsList.setAdapter(activityVodAdapter);
			
			
//长按选择收藏
			vodsList.setOnItemLongClickListener(new OnItemLongClickListener(){  
				
			    public boolean onItemLongClick(AdapterView<?> arg0, View view,  
			            int arg2, long arg3) {  
			        // TODO Auto-generated method stub  
			    	System.out.print("收藏栏目");
			        islikes("确定要收藏吗？");
			        
// 进行收藏操作
			    	PpVodItem selectedlikes=(PpVodItem)view.getTag();  
			    	OperateDAO newdao =new OperateDAO(getBaseContext());
  				    newdao.insertVodIntoPlayLikes(selectedlikes);
  				  System.out.print("收藏已栏目");
			        return true;  
			    }  
			  });
				
//	短按点击播放		
			vodsList.setOnItemClickListener(new OnItemClickListener() {
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
		}else System.out.println("搞了半天啥都没有");		

		TextView textView = (TextView)findViewById(R.id.ppListInfo);
		textView.setText(list_name + list_id);
		
	}

	/*实现 接口中定义的方法, 目前该代码不再需要被使用
	@Override
	public void addLoadedImage(String vod_pic,Drawable imageDrawable) {
		// TODO Auto-generated method stub
		drawableMap.put(vod_pic, imageDrawable);
	}*/

	
//	长按要调用的函数
  private void islikes(String msg) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(msg).setCancelable(false)
              .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface arg0, int arg1) {
                      // TODO Auto-generated method stub
                	  
                	  
/*                      加入收藏表，本应在这里进行，但代码在这里出错，所以移至上面
                	  PpVodItem selectedlikes=(PpVodItem)view.getTag();  
  			    	OperateDAO newdao =new OperateDAO(getBaseContext());
    				    newdao.insertVodIntoPlayLikes(selectedlikes);
    				  System.out.print("收藏已栏目");
             */        

                       return;
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
