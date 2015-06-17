package com.example.contentprovider;

import java.util.HashMap;

import com.example.constants.DBInfoConfig;
import com.example.dao.OperateDAO;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 用于向SearchView提供 定制的建议项数据
 */
public class CustomSuggestionsProvider extends ContentProvider {

	/**
	 * 以下为 CustomSuggestionsProvider 设定 需要用到的各项 属性值
	 */
	//AUTHORITY必须要与该provider在manifest配置文件中的AUTHORITY属性值相一致
	public static String AUTHORITY = "com.example.contentprovider.CustomSuggestionsProvider";
	//CONTENT_URI 为 取得这个provider所需要提供对应的URI值：
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/pp_vod");
	//数据库名 yjy.db
	public static final String DATABASE_NAME = DBInfoConfig.DB_NAME;
	//表名
	public static final String TABLE_NAME = "pp_vod";
	/*MIME types used for searching vods or looking up a single definition
		VODS_MIME_TYPE 为取得所有vods的 数据类型 ，DEFINITION_MIME_TYPE为某一条vod数据的类型*/
    public static final String VODS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                                                  "/vnd.example.android.searchablevod";
    public static final String VOD_DEFINITION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                                                       "/vnd.example.android.searchablevod";
    //The columns we'll include in the pp_vod table
    public static final String KEY_VOD_NAME = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String KEY_VOD_DEFINITION = SearchManager.SUGGEST_COLUMN_TEXT_2;
    //要使用的 操作数据库 的dao类，在onCreate回调函数中被实例化
    private OperateDAO dao;
    
    //UriMatcher stuff  一般用一个整数 与 UriMatcher中的某个字段对应，以便于 匹配
    private static final int SEARCH_SUGGEST = 0;
    private static final UriMatcher sURIMatcher = buildUriMatcher();
    /**
     * 该buildUriMatcher()用于初始化上面定义的UriMatcher全局变量。
     * 在其中定义了很多键值对，用于在query()中对提交过来的Uri进行 匹配判别
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        // to get suggestions...  '/*' 的意思是可以匹配所有text， 而 '/#'的意思是只能匹配数字
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        return matcher;
    }
    
    private static final HashMap<String,String> mColumnMap = buildColumnMap();
    /**
     * 真正执行查询的时候 会用到 SQLiteQueryBuilder，而全局变量 mColumnMap 正是为其提供参数
     * mColumnMap对应的buildColumnMap()创建了一个列名和别名的映射关系，
     * 即 虚拟的“建议表”中字段和实际的“pp_vod”中字段 之间的映射
     */
    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        //CustomSuggestionProvider需要用到的虚拟“建议表”中的_ID
        map.put(BaseColumns._ID, "vod_id AS " +BaseColumns._ID);
        //建议表中的   suggest_text_1 字段，即每一条建议项的 首行文本
        map.put(KEY_VOD_NAME, "vod_name AS "+KEY_VOD_NAME);
        //建议表中的   suggest_text_2 字段，即每一条建议项的 第二行文本
        map.put(KEY_VOD_DEFINITION, "vod_content AS "+KEY_VOD_DEFINITION);
        /* 建议表中 存储的 “非可见内容”，用于为每一条建议项记录附加一个INTENT_DATA，
         * 当 用户点击某个建议项引发Intent跳转时，该字段为“接收该intent的SearchableActivity”提供data */
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA, "vod_id AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA);
        /*查询快捷方式，目前为止还用不上这样的功能
         * map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);*/
        return map;
    }
    
    
    //======================================================
	@Override
	public boolean onCreate() {
		dao = new OperateDAO(getContext());
		return false;
	}

	/**
	 * 用户开始在搜索对话框或搜索widget里输入文本时，每键入一个字符，系统都将调用一次query()，
	 * 并在content provider中检索建议项。
	 * 在query()中，必须实现对content provider建议项数据的检索，并返回一个指向最佳建议项数据行的Cursor。
	 * 所以 query()相当于provider的核心
	 * @param 
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
		// Use the UriMatcher to see what kind of query we have and format the db query accordingly
		//根据 传过来的 uri进行Match判断，以此决定将执行怎样的操作
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
            	if (selectionArgs == null) {
                    throw new IllegalArgumentException(
                        "selectionArgs must be provided for the Uri: " + uri);
                  }
                  return getSuggestions(selectionArgs[0]);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
	}
    /**
     * 用Cursor向系统返回建议项时，每一行数据的列格式都是系统规定的。
     * 因此，无论是要把建议项数据存储在本地或Web服务器的SQLite数据库中，还是要以本地或web的其它格式存储，
     * 都必须把建议项格式化为表的一行数据，并用Cursor来表示。
     * 系统可以识别多个列，但有两列是必需的：其余是可选的
     * 	1、整数类型的行ID，唯一标识建议项。在ListView中显示建议项时，系统会用到该值
     * 	2、SUGGEST_COLUMN_TEXT_1 ，  代表建议项的字符串。
     */
	private Cursor getSuggestions(String query) {
		String[] columns = new String[] {
		          BaseColumns._ID,
		          KEY_VOD_NAME,
		          KEY_VOD_DEFINITION,
		          SearchManager.SUGGEST_COLUMN_INTENT_DATA
		};//得在此增加 intent_data  参考 dictionaryProvider
		String selection = KEY_VOD_NAME + " LIKE ? OR "+ KEY_VOD_DEFINITION + " LIKE ?";
        String[] selectionArgs = new String[] {query+"%","%"+query+"%"};
        //用SQLiteQueryBuilder来构建查询语句
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        //设定查询哪个表：
        builder.setTables(TABLE_NAME);
        // 这里设定ColumnMap，可以按谷歌API的要求  实现 列名的映射，即加入 “vod_id AS” 语句
        builder.setProjectionMap(mColumnMap);

       /*特殊的，下面这段查询语句为 简化 的测试方法，回头得优化，还是 使用谷歌API推荐的SQLiteQueryBuilder来查询为好
        String sql = "select vod_id AS _id,vod_name AS suggest_text_1,vod_content AS suggest_text_2 from pp_vod where vod_name like ? or vod_content like ?";
		String likeStr = "%"+ query +"%";
		String bindArgs[] = new String[]{likeStr,likeStr};
		Cursor cursor = dao.getReadableDB().rawQuery(sql, bindArgs);
        */
        //执行查询 此处 groupBy、having、sortOrder暂时为空
        Cursor cursor = builder.query(dao.getReadableDB(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
