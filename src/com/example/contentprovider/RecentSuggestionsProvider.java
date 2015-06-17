package com.example.contentprovider;

import android.content.SearchRecentSuggestionsProvider;

public class RecentSuggestionsProvider extends SearchRecentSuggestionsProvider {
	//搜索authority可以是任何唯一的字符串，但最好是用content provider的完全限定名称--包名加provider类名
	public final static String AUTHORITY = "com.example.contentprovider.RecentSuggestionsProvider";
	/*数据库模式必须包括DATABASE_MODE_QUERIES，另一个可选项DATABASE_MODE_2LINES会在建议项列表中添加一列，使得每个建议项能提供两条文本。
		public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;*/
    public final static int MODE = DATABASE_MODE_QUERIES;
    
	public RecentSuggestionsProvider() {
		//调用参数为搜索authority和数据库模式
		setupSuggestions(AUTHORITY, MODE);
	}
}
