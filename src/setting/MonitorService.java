package setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import org.apache.http.util.EncodingUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MonitorService extends Service {
	private Handler objHandler = new Handler();  
    private int intCounter=0;  
    private int mHour;   
    private int mMinute;   
    private int mYear;   
    private int mMonth;   
    private int mDay;  
    private String mdate;  
      
      
    final public String DEV_FILE = "/proc/self/net/dev";//绯荤粺娴侀噺鏂囦欢  
    String[] ethdata={"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};  
    String[] gprsdata={"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};  
    String[] wifidata={"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};  
    String data="0,0,0,0,0,0,0,0,0,0,0,0";//瀵瑰簲on.txt閲岄潰鐨勬牸寮� 
    final String ETHLINE="eth0";//浠ュお缃戜俊鎭墍鍦ㄨ  
    final String GPRSLINE="rmnet0";  
    final String WIFILINE="wlan0";  
      
    final String TEXT_ENCODING = "UTF-8";  
       
    final public String ONPATH = "/mnt/sdcard/network_monitor.txt";  
    final public String LOGPATH = "/mnt/sdcard/network_log.txt";  
      
    private Runnable mTasks = new Runnable()   
    {  
     
        public void run()//杩愯璇ユ湇鍔℃墽琛屾鍑芥暟  
        {  
          refresh();  
          
          intCounter++;  
        // DisplayToast("Counter:"+Integer.toString(intCounter));  
         objHandler.postDelayed(mTasks, 1000);//姣�000姣鎵ц涓�  
       }   
    };  
    
  @Override  
  public void onStart(Intent intent, int startId)  
  {  
    // TODO Auto-generated method stub  
    //writefile("0,0,0,0,0,0,0,0,0,0,0,0",ONPATH);//姣忔鍚姩鏈嶅姟 鍒濆鍖杘npath  
      
    objHandler.postDelayed(mTasks, 0);  
    super.onStart(intent, startId);  
  }  
  @Override  
  public void onCreate()  
  {  
    // TODO Auto-generated method stub  
        
    super.onCreate();  
  }  
    
  @Override  
  public IBinder onBind(Intent intent)  
  {  
    // TODO Auto-generated method stub  
      
      
    return null;  
  }  
  @Override  
  public void onDestroy()  
  {  
    // TODO Auto-generated method stub  
      
    /*  */  
    objHandler.removeCallbacks(mTasks);  
    super.onDestroy();  
  }    
  public void DisplayToast(String str)  
  {  
    Toast.makeText(this,str,Toast.LENGTH_SHORT).show();  
  }    
  public void readdev()  
  {  
	  log("readdev()");
	  
      FileReader fstream = null;  
      try {  
          fstream = new FileReader(DEV_FILE);  
            
          }   
        catch (FileNotFoundException e) {  
            DisplayToast("Could not read " + DEV_FILE);  
            
        }  
       BufferedReader in = new BufferedReader(fstream, 500);  
       String line;  
       String[] segs;  
       String[] netdata;  
         
       int count=0;  
       int k;  
       int j;  
       try {  
          while ((line = in.readLine()) != null) {  
              segs = line.trim().split(":");  
              if(line.contains(ETHLINE))  
                {  
                    
                  netdata=segs[1].trim().split(" ");  
                  for(k=0,j=0;k<netdata.length;k++)  
                  {  
                     if(netdata[k].length()>0)   
                     {   
                          ethdata[j]=netdata[k];  
                          j++;  
                     }  
                  }  
              }  
              else if(line.contains(GPRSLINE))  
              {  
            	  netdata=segs[1].trim().split(" ");  
	              for(k=0,j=0;k<netdata.length;k++)  
	              {  
	                 if(netdata[k].length()>0)   
	                 {   
	                      gprsdata[j]=netdata[k];  
	                      j++;  
	                 }  
	              }  
              }  
            else if(line.contains(WIFILINE))  
            {  
            	netdata=segs[1].trim().split(" ");  
                for(k=0,j=0;k<netdata.length;k++)  
                {  
                    if(netdata[k].length()>0)   
                    {   
                      
                      wifidata[j]=netdata[k];  
                      j++;  
                    }  
                }  
          }  
          count++;  
          }  
          
          fstream.close();  
        }   
        catch (IOException e) {  
          DisplayToast(e.toString());  
        }  
       
       log("************************** from file *********************");
       log("eth0");
       for (int i = 0; i < ethdata.length; ++i)
       {
    	   Log.v("test", ethdata[i] + "  ");
       }
       
       log("gprs");
       for (int i = 0; i < gprsdata.length; ++i)
       {
    	   Log.v("test", gprsdata[i] + "  ");
       }
       
       log("wifi");
       for (int i = 0; i < wifidata.length; ++i)
       {
    	   Log.v("test", wifidata[i] + "  ");
       }
  }  
  public String getinfo(String path)  
  {  
    File file;  
    String str="";   
    FileInputStream in;  
   try{  
    //鎵撳紑鏂囦欢file鐨処nputStream  
     file = new File(path);  
       in = new FileInputStream(file);  
       //灏嗘枃浠跺唴瀹瑰叏閮ㄨ鍏ュ埌byte鏁扮粍  
       int length = (int)file.length();  
       byte[] temp = new byte[length];  
       in.read(temp, 0, length);  
       //灏哹yte鏁扮粍鐢║TF-8缂栫爜骞跺瓨鍏isplay瀛楃涓蹭腑  
       str =  EncodingUtils.getString(temp,TEXT_ENCODING);  
       //鍏抽棴鏂囦欢file鐨処nputStream  
       in.close();  
   }  
   catch (IOException e) {  
         
      DisplayToast(e.toString());  
         
   }  
    return str;  
  }  
  public void writefile(String str,String path )  
  {  
	  log("writefile(). str  = " + str + ",  path = " + path);
	  
    File file;  
    FileOutputStream out;  
     try {  
           //鍒涘缓鏂囦欢  
         file = new File(path);  
           file.createNewFile();  
           //鎵撳紑鏂囦欢file鐨凮utputStream  
           out = new FileOutputStream(file);  
           String infoToWrite = str;  
           //灏嗗瓧绗︿覆杞崲鎴恇yte鏁扮粍鍐欏叆鏂囦欢  
           out.write(infoToWrite.getBytes());  
           //鍏抽棴鏂囦欢file鐨凮utputStream  
           out.close();   
       } catch (IOException e) {  
           //灏嗗嚭閿欎俊鎭墦鍗板埌Logcat  
          DisplayToast(e.toString());  
             
       }  
  }  
  public void refresh()  
  {  
	  log("refresh()");
        
      readdev();//璇诲彇鏈寮�満涔嬪悗鐩村埌褰撳墠绯荤粺鐨勬�娴侀噺  
        
       data=ethdata[0]+","+ethdata[1]+","+ethdata[8]+","+ethdata[9]+","  
           +gprsdata[0]+","+gprsdata[1]+","+gprsdata[8]+","+gprsdata[9]+","  
           +wifidata[0]+","+wifidata[1]+","+wifidata[8]+","+wifidata[9];  
       
       log("data = " + data);
       
      String onstr=getinfo(ONPATH);//璇诲彇on.txt璁板綍鍒皁nstr閲� 
       String ondata[]=onstr.split(",");//灏唎nstr鍚勯」鍒嗙 鏀惧埌ondata閲� 
//       log("鏂囦欢涓殑涓婃璁板綍锛�);
       for (int i = 0; i< ondata.length; ++i)
       {
    	   Log.v("test", ondata[i] + "  ");
       }
       
      //璁＄畻澧為噺  
      int [] delta=new int [12];  
        
      delta[0]=Integer.parseInt(ethdata[0])-Integer.parseInt(ondata[0]);  
      delta[1]=Integer.parseInt(ethdata[1])-Integer.parseInt(ondata[1]);  
      delta[2]=Integer.parseInt(ethdata[8])-Integer.parseInt(ondata[2]);  
      delta[3]=Integer.parseInt(ethdata[9])-Integer.parseInt(ondata[3]);  
      delta[4]=Integer.parseInt(gprsdata[0])-Integer.parseInt(ondata[4]);  
      delta[5]=Integer.parseInt(gprsdata[1])-Integer.parseInt(ondata[5]);  
      delta[6]=Integer.parseInt(gprsdata[8])-Integer.parseInt(ondata[6]);  
      delta[7]=Integer.parseInt(gprsdata[9])-Integer.parseInt(ondata[7]);  
      delta[8]=Integer.parseInt(wifidata[0])-Integer.parseInt(ondata[8]);  
      delta[9]=Integer.parseInt(wifidata[1])-Integer.parseInt(ondata[9]);  
      delta[10]=Integer.parseInt(wifidata[8])-Integer.parseInt(ondata[10]);  
      delta[11]=Integer.parseInt(wifidata[9])-Integer.parseInt(ondata[11]);  
        
//      log("澧為噺锛�);
      for (int i = 0; i< delta.length; ++i)
      {
    	  Log.v("test", delta[i] + "  ");
      }
        
      //璇诲彇log.txt  
    //鑾峰彇褰撳墠鏃堕棿  
       final Calendar c = Calendar.getInstance();   
      mYear = c.get(Calendar.YEAR); //鑾峰彇褰撳墠骞翠唤   
      mMonth = c.get(Calendar.MONTH)+1;//鑾峰彇褰撳墠鏈堜唤   
      mDay = c.get(Calendar.DAY_OF_MONTH);//鑾峰彇褰撳墠鏈堜唤鐨勬棩鏈熷彿鐮�  
      mHour = c.get(Calendar.HOUR_OF_DAY);//鑾峰彇褰撳墠鐨勫皬鏃舵暟   
      mMinute = c.get(Calendar.MINUTE);//鑾峰彇褰撳墠鐨勫垎閽熸暟     
      mdate=mYear+"-"+mMonth+"-"+mDay;  
        
      String text=getinfo(LOGPATH);//灏唋og.txt鐨勫唴瀹硅鍒皌ext瀛楃涓蹭腑  
      log("text = " + text);
      
      String [] line=text.split("/n");   
       
      String today=line[line.length-1];//鑾峰緱浠婃棩宸茶褰曟祦閲� 
      String [] beToday=today.split(",");   
     //妫�煡鏂囦欢鏈�悗涓�鏄惁涓轰粖澶╃殑娴侀噺璁板綍淇℃伅  
      if(!beToday[0].equals(mdate))//  
        //鍒ゆ柇浠婃棩娴侀噺鏄惁宸茬粡璁板綍,濡傛灉浠婃棩娴侀噺娌℃湁璁板綍  
      {  
       
          text=text+mdate+",0,0,0,0,0,0,0,0,0,0,0,0/n";  
          writefile(text,LOGPATH);  
            
          line=text.split("/n");  
          today=line[line.length-1];//鑾峰緱浠婃棩宸茶褰曟祦閲� 
           
          beToday=today.split(",");   
      }  
      int i;  
     //澶勭悊浠婃棩娴侀噺  
      int [] newTodaydata=new int [12];//琛ㄧず浠婃棩娴侀噺  
      String newtoday=mdate;  
      for(i=0;i<=11;i++)//鏇存柊浠婃棩娴侀噺  
      {  
          newTodaydata[i]=Integer.parseInt(beToday[i+1])+delta[i];  
          newtoday=newtoday+","+newTodaydata[i];  
      }  
      newtoday=newtoday+"/n";  
      log("newtoday = " + newtoday);
        
        
      String [] beTotal=line[0].split(",");  
      int [] newTotaldata=new int [12];//琛ㄧず鎬绘祦閲忔暟鍊� 
      //鏇存柊绗竴琛� 
      String newtotal="total";  
      for(i=0;i<=11;i++)//鏇存柊浠婃棩娴侀噺鍜屾�娴侀噺  
      {    
          newTotaldata[i]=Integer.parseInt(beTotal[i+1])+delta[i];//鎬绘祦閲忔暟鍊�delta[i]鏇存柊  
          newtotal=newtotal+","+newTotaldata[i];  
      }  
      newtotal= newtotal+"/n";  
      log("newtotal = " + newtotal);
      
      //澶勭悊涓棿涓嶅彉鐨勯儴鍒� 
      String before="";//before涓轰箣鍓嶇殑浠庣1琛屽埌鏄ㄥぉ鐨勬祦閲忚褰� 
        
      for(i=1;i<=line.length-2;i++)  
        before=before+line[i]+"/n";//浠ｈ〃涓棿涓嶅彉鐨勯儴鍒� 
        
      log("before = " + before);
      
      String newlog=newtotal+before+newtoday;  
      log("newlog = " + newlog);
      
      writefile(data,ONPATH);//鏇存柊娴侀噺璁板綍  
      writefile(newlog,LOGPATH);//鏇存柊log*/  
      
      // 鏇存柊鐣岄潰
      Handler h = trafficstats.getUiHanlder();
      if (null != h)
      {
    	  Message msg = h.obtainMessage();
    	  msg.what = 1;
    	  
    	  Bundle b = new Bundle();
    	  b.putInt("real_recv", delta[0] + delta[4] + delta[8]);
    	  b.putInt("total_recv", Integer.parseInt(ethdata[0]) +  Integer.parseInt(gprsdata[0]) +  Integer.parseInt(wifidata[0]));
    	  
    	  b.putInt("real_send", delta[2] + delta[6] + delta[10]);
    	  b.putInt("total_send",  Integer.parseInt(ethdata[8]) +  Integer.parseInt(gprsdata[8]) +  Integer.parseInt(wifidata[8]));
    	  
    	  msg.obj = b;
    	  
    	  msg.sendToTarget();
      }
  }  
  
  private void log(String msg)
  {
  	Log.v("MonitorService", "============> " + msg);
  }
  
}
