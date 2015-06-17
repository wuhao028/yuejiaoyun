package com.example.activity;

import org.apache.http.conn.ConnectTimeoutException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.OutlineTextView;
import io.vov.vitamio.widget.VideoView;
import com.example.constants.WebInfoConfig;
import com.example.dao.OperateDAO;
import com.example.model.PpListItem;
import com.example.model.PpVodItem;
import com.example.utils.HttpUtil;
import com.example.utils.MD5EncryptUtil;
import com.example.utils.MySysApplication;
import com.example.yuejiaoyun.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class VideoPlayActivity extends Activity implements OnRatingBarChangeListener,OnClickListener{

	private String uriString = "";//"http://219.223.194.119:8080/mp3/MissPuff3DMV.flv";
	private VideoView mVideoView;
    private RatingBar ratingbar;
    
    int count=0;
//    点赞控件
    private Button buttonone;
	private TextView textViewone;
	private android.view.animation.Animation animation;
//  点踩	
	private Button buttontwo;
    private TextView textViewtwo;
    private android.view.animation.Animation cai_anim;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoplay);
		//添加该Activity到MySysApplication对象实例容器中
		MySysApplication.getInstance().addActivity(this);
		
//	 点赞动画
		   animation=AnimationUtils.loadAnimation(VideoPlayActivity.this,R.anim.nn);
	        buttonone=(Button)findViewById(R.id.bt_one);
	        buttonone.setOnClickListener(this);
	        textViewone=(TextView)findViewById(R.id.tv_one);
	        
//	    踩动画
	        cai_anim=AnimationUtils.loadAnimation(VideoPlayActivity.this,R.anim.cai_anim);
	        buttontwo=(Button)findViewById(R.id.bt_two);
	        buttontwo.setOnClickListener(this);
	        textViewtwo=(TextView)findViewById(R.id.tv_two);
	        

	        
		//得到Intent传输过来的数据
		Intent intent = getIntent();
		final PpVodItem wantShowVod = (PpVodItem)intent.getSerializableExtra("wantShowVod");
		//获取xml布局文件中定义的控件
		mVideoView = (VideoView)findViewById(R.id.videoView);
		TextView rootCategoryView = (TextView)findViewById(R.id.rootCategory);
		TextView subCategoryView = (TextView)findViewById(R.id.subCategory);
		TextView vod_nameView = (TextView)findViewById(R.id.vod_name);
		TextView vod_contentView = (TextView)findViewById(R.id.vod_content);
		final TextView vod_up= (TextView)findViewById(R.id.vod_up);
		final TextView vod_down= (TextView)findViewById(R.id.vod_down);




		
		
		//查询数据库，得到“分类”名称,分为“根分类”和“子分类”
		OperateDAO dao = new OperateDAO(this);
		PpListItem subCategoryItem  = dao.getPpListItemByList_ID(wantShowVod.getVod_cid());
		PpListItem rootCategoryItem = null;
		if(!subCategoryItem.getList_pid().equals("0")){
			rootCategoryItem = dao.getPpListItemByList_ID(subCategoryItem.getList_pid());
		}
		//为各个控件设定显示数据
		//如果 根分类 存在,为其设定显示值，如果不存在，将其移除。GONE为设置不显示也不占用空间
		if(rootCategoryItem !=null){
			//设定显示值
			rootCategoryView.setText(rootCategoryItem.getList_name()+" > ");
			rootCategoryView.setTag(rootCategoryItem);
			//设定监听器，跳转到该分类页面下
			rootCategoryView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(VideoPlayActivity.this,ShowVodsInSelectedPpList.class);
					PpListItem listItem =(PpListItem)view.getTag();
					intent.putExtra("list_id", Integer.parseInt(listItem.getList_id()));
					intent.putExtra("list_name", listItem.getList_name());
					startActivity(intent);
				}
			});
		}else{
			rootCategoryView.setVisibility(View.GONE);
		}
		subCategoryView.setText(subCategoryItem.getList_name());
		subCategoryView.setTag(subCategoryItem);
		subCategoryView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(VideoPlayActivity.this,ShowVodsInSelectedPpList.class);
				PpListItem listItem =(PpListItem)view.getTag();
				intent.putExtra("list_id", Integer.parseInt(listItem.getList_id()));
				intent.putExtra("list_name", listItem.getList_name());
				startActivity(intent);
			}
		});
		vod_nameView.setText(wantShowVod.getVod_name());
		vod_contentView.setText("剧情介绍："+wantShowVod.getVod_content());
		
		


		
		//
		String vodsource_id = wantShowVod.getVod_sourceid();
		System.out.println("VideoPlayActivity get vodsource_id-->" + vodsource_id);
		/*
		 * 通过souce_id进行一次请求 ，得到 真正的播放地址
		 * 1、把需要的参数拼接成字符串  【特殊的，有两个默认必填的参数(code需要MD5 32位[小写]加密)：
		 * 		String time=Unix时间戳（精确到秒）; String code=MD5[AccessID.time.do.sourceid]  注意顺序】
		 * 2、拼接成请求的Url地址，get传值各参数
		 * 		接口地址 host+ /service/api/?do= video(或者task？) op=geturl;sourceid;type=http/m3u8
		 * 3、请求  2中 获得的Url
		 * 4、得到结果
		 */
		//1、构造所需参数：
		String unixTimeStamp = new WebInfoConfig().UnixTimeStamp;
		System.out.println("unixTimeStamp--"+unixTimeStamp);
		String doStr = "video";
		String preCode = WebInfoConfig.PKUsz_ACCESS_ID + unixTimeStamp + doStr + vodsource_id;
		System.out.println("preCode--"+preCode);
		String code = MD5EncryptUtil.getMD5(preCode);
		System.out.println("code--"+code);
		//得到请求地址
		String requestUrl = WebInfoConfig.PKUsz_DEMAND_SERVER_API_URL+"service/api/?do="+doStr
				+"&op=geturl&type=http&sourceid="+vodsource_id+"&time="+unixTimeStamp+"&code="+code;
		System.out.println("requestUrl--"+requestUrl);
		//进行http请求
		String result = "";
		try {
			result = HttpUtil.getRequest(requestUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!result.equals("")){
			System.out.println("result--"+result);
			//uriString = parse from result;
			String temp1 = result.substring(result.indexOf("http"), result.indexOf(".flv")+".flv".length());
			//把 结果字符串中的 \/ 替换成 /
			temp1 = temp1.replace("\\", "");
			System.out.println("temp-->"+ temp1);
			uriString = temp1;
		}else System.out.println("http result is nothing");
		//若播放地址非空，则播放之
		if(!uriString.equals("")){
			
			if (!LibsChecker.checkVitamioLibs(this))
				System.out.println("check error");
			
			//mVideoView.setVideoURI(Uri.parse(uriString));
			mVideoView.setVideoPath(uriString);
			mVideoView.setMediaController(new MediaController(this));
			mVideoView.requestFocus();
			//mVideoView.start();
			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
				}
			});
			/*Vitamio ---VideoViewDemo 代码段
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 
			mVideoView.setVideoPath(path);
			mVideoView.setMediaController(new MediaController(this));
			mVideoView.requestFocus();

			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
				}
			});*/
			
			

			
//			 Log.d("被设置点赞的数量为：", wantShowVod.getVod_up());
			
			
			
			//完成播放，则向WebService接口发送 addHits的请求
			String addHitsUrl = WebInfoConfig.ADD_HITS_URL + wantShowVod.getVod_id();
			
			try {
				HttpUtil.getRequest(addHitsUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//同时，本地数据库完成更新
			if(dao.addVodHits(wantShowVod) ==1){
				System.out.println("DB addHits success");
			}else{
				System.out.println("DB addHits fail");
			}
			//完成播放，向play_history表中添加 历史播放信息
			if(dao.insertVodIntoPlayHistory(wantShowVod)>0){
				//如果新插入的Row_id有效的话
				System.out.println("DB play_history insert success");
			}else{
				System.out.println("DB play_history insert fail");
			}
			
		}else{
			System.out.println("VideoPlayActivity uriString error");
		}
		
		

//		查询数据库，得到点赞 点踩的人数；
		String totalzan="赞的人数: "+wantShowVod.getVod_up();
		vod_up.setText(totalzan);
		String totalcai="踩的人数: "+wantShowVod.getVod_down();
		vod_down.setText(totalcai);
		
		
		

		
//	点赞	
	buttonone.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
//点赞服务器接口，将它放在onClick外面就报错，这里能运行的一个条件是wanShowvod被设置为final
				String addLikeUrl= WebInfoConfig.LIKE_HITS_URL+ wantShowVod.getVod_id();
				textViewone.setVisibility(View.VISIBLE);
				textViewone.startAnimation(animation);
//				播放动画			
				new Handler().postDelayed(new Runnable(){
		            public void run() {
		            	textViewone.setVisibility(View.GONE);
		            } 
				}, 1000);
				
				
				try {
					if(count==0){
					HttpUtil.getRequest(addLikeUrl);
//					更新数据
					int temnum= Integer.parseInt(wantShowVod.getVod_up()) +1;
					vod_up.setText("赞的人数："+ temnum);

					Toast.makeText(getBaseContext(), "已经点赞", Toast.LENGTH_SHORT).show();
					
					count++;
					}
					else{
						Toast.makeText(getBaseContext(), "不能重复点击", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();}	
			}	
		});

	
	
//		点踩的操作
	buttontwo.setOnClickListener(new OnClickListener()
		{		
			public void onClick(View v)
			{
//点踩服务器接口，将它放在onClick外面就报错，这里能运行的一个条件是wanShowvod被设置为final
				String addDislikeUrl= WebInfoConfig.DISLIKE_HITS_URL+ wantShowVod.getVod_id();
				textViewtwo.setVisibility(View.VISIBLE);
				textViewtwo.startAnimation(cai_anim);
			//  播放动画
				new Handler().postDelayed(new Runnable(){
		            public void run() {
		            	textViewtwo.setVisibility(View.GONE);
		            } 
				}, 1000);
				
				
				try {
					if(count==0){
					HttpUtil.getRequest(addDislikeUrl);
//					更新数据
					int temnum1= Integer.parseInt(wantShowVod.getVod_down()) +1;
					vod_down.setText("踩的人数："+ temnum1);
					Toast.makeText(getBaseContext(), "已经踩过", Toast.LENGTH_SHORT).show();

					
					count++;}
					else{
						Toast.makeText(getBaseContext(), "不能重复点击", Toast.LENGTH_SHORT).show();
				} 
				}
					catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();}	
			}	
		});

	
	
	//评分  ratingbar
	/* 评分条操作
	   分数更新接口
http://219.223.192.21/testTv/service/goldupdate.php?vodid=95&vodgold=1
输入： vodid=  节目的ID（在数据库中的条目ID）
  vodgold= 用户的评分（1-10之间的一个数字，客户端要把数字控制在这个范围）*/

			final RatingBar rb = (RatingBar)findViewById(R.id.ratingBar1);
			 rb.setOnRatingBarChangeListener( new OnRatingBarChangeListener(){
				public void onRatingChanged(RatingBar arg0,float rating, boolean fromUser)
				{
				rb.setAlpha((int) (rating*255/5) );	
				String score="http://219.223.192.21/testTv/service/goldupdate.php?vodid="+ wantShowVod.getVod_id()
						+ "&vodgold="+ rating;
				 Toast.makeText(getBaseContext(), "感谢您的评分", Toast.LENGTH_SHORT).show();
				try {
					HttpUtil.getRequest(score);	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();}	
				}				
	               });
			 		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
		// TODO Auto-generated method stub
	
	}



	}


