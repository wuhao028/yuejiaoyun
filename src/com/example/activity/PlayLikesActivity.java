package com.example.activity;

import java.util.List;

import com.example.constants.DBInfoConfig;
import com.example.dao.DBOpenHelper;
import com.example.dao.OperateDAO;
import com.example.model.PlayHistoryItem;
import com.example.model.PlayLikesItem;
import com.example.model.PpVodItem;
import com.example.utils.MySysApplication;
import com.example.utils.PlayHistoryItemAdapter;
import com.example.utils.PlayLikesItemAdapter;
import com.example.yuejiaoyun.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog;

public class PlayLikesActivity extends Activity {

	//显示 历史记录Vods的个数
	private final int NUMBER = DBInfoConfig.NUM_OF_GET_LIKES_VOD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlikes);
		ListView likesLV = (ListView)findViewById(R.id.likesLV);
		OperateDAO dao = new OperateDAO(this);
		List<PlayLikesItem> likesItems = dao.getPlayLikes(NUMBER);
		if(!likesItems.isEmpty()){
			PlayLikesItemAdapter adapter = new PlayLikesItemAdapter(this, likesItems);
			likesLV.setAdapter(adapter);
		
//			点击播放
	likesLV.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					System.out.println("data from tag--->"+view.getTag());
					Intent intent = new Intent(view.getContext(), VideoPlayActivity.class);
					intent.putExtra("wantShowVod", (PpVodItem)view.getTag());
					startActivity(intent);
				}
			});
		}else{
			System.out.println("PlayLikesActivity从数据库中查询出的结果集为空");
		}
	}
}
