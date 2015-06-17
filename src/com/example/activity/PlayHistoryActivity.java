package com.example.activity;

import java.util.List;

import com.example.constants.DBInfoConfig;
import com.example.dao.OperateDAO;
import com.example.model.PlayHistoryItem;
import com.example.model.PpVodItem;
import com.example.utils.PlayHistoryItemAdapter;
import com.example.yuejiaoyun.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PlayHistoryActivity extends Activity {

	//显示 历史记录Vods的个数
	private final int NUMBER = DBInfoConfig.NUM_OF_GET_HISTORY_VOD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playhistory);
		ListView historyLV = (ListView)findViewById(R.id.historyLV);
		OperateDAO dao = new OperateDAO(this);
		List<PlayHistoryItem> historyItems = dao.getPlayHistory(NUMBER);
		if(!historyItems.isEmpty()){
			PlayHistoryItemAdapter adapter = new PlayHistoryItemAdapter(this, historyItems);
			historyLV.setAdapter(adapter);
			historyLV.setOnItemClickListener(new OnItemClickListener() {
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
			System.out.println("PlayHistoryActivity从数据库中查询出的结果集为空");
		}
	}
}
