package setting;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.example.yuejiaoyun.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class trafficstats extends Activity {

	final public String ONPATH = "/mnt/sdcard/network_monitor.txt";  
	private static Handler mUiHandler = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic);
        
        mUiHandler = new UiHandler();
        startMonitorService();
        
    }


    
    private void startMonitorService()
    {
    	log("startMonitorService()");
    	
    	writefile("0,0,0,0,0,0,0,0,0,0,0,0", ONPATH); 
    	
    	Intent bootActivityIntent=new Intent(this,MonitorService.class);//启动服务  
        bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        writefile("0,0,0,0,0,0,0,0,0,0,0,0", ONPATH);  
        startService(bootActivityIntent); 
    }
    
    public void writefile(String str,String path )  
    {  
       File file;  
       try {  
          //创建文件  
         file = new File(path);  
          file.createNewFile();  
            
          //打开文件file的OutputStream  
          FileOutputStream out = new FileOutputStream(file);  
          String infoToWrite = str;  
          //将字符串转换成byte数组写入文件  
          out.write(infoToWrite.getBytes());  
          //关闭文件file的OutputStream  
          out.close();  
       }
       catch (IOException e) {  
          //将出错信息打印到Logcat
    	   e.printStackTrace();
       }  
    }
    
    private void handleUpdateUi(Message msg)
    {
    	log("handleUpdateUi");
    	
    	Bundle b = (Bundle) msg.obj;
    	int real_recv = b.getInt("real_recv", 0);
    	int total_recv = b.getInt("total_recv", 0);
    	int real_send = b.getInt("real_send", 0);
    	int total_send = b.getInt("total_send", 0);
    	
    	TextView tv = null;
    	tv = (TextView) findViewById(R.id.real_rece);
    	tv.setText(real_recv / 1024 + "  Kb/s");
  	  
    	tv = (TextView) findViewById(R.id.total_rece);
    	tv.setText(total_recv / 1024 + "  Kb");
    	
    	tv = (TextView) findViewById(R.id.real_send);
    	tv.setText(real_send / 1024 + "  Kb/s");
    	
    	tv = (TextView) findViewById(R.id.total_send);
    	tv.setText(total_send / 1024 + "  Kb");
    }
    
    public static Handler getUiHanlder()
    {
    	return mUiHandler;
    }
    
    class UiHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			case 1:
				handleUpdateUi(msg);
				break;
				
			default:
				break;
			}
		}
	}
    
    private void log(String msg)
    {
    	Log.v("TestNetworkMonitor", "============> " + msg);
    }
}
