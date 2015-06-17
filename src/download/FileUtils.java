package download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import info.info;

import android.os.Environment;

public class FileUtils {

	private String SDCardRoot;

	public FileUtils(){
		//得到当前外部储存设备的目录,File.separator是文件分隔符，比如在window下是"\"
		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
	}
	
	/**
	 * 在SD卡上创建文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName,String dir) throws IOException{
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		System.out.println("file-->"+file);
		file.createNewFile();
		return file;
	} 
	
	/**
	 * 在SD卡上创建目录
	 */
	public File createSDDir(String dir){
		File dirFile  = new File(SDCardRoot + dir + File.separator);
		System.out.println("create dir " + dirFile.mkdir());
		return dirFile;
	}
	
	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName,String path){
		File file = new File(SDCardRoot + path +File.separator + fileName);
		return file.exists();
	}
	
	/**
	 * 将一个InputSream里面的数据写入到SD卡上
	 */
	public File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try{
			createSDDir(path);
			file = createSDFile(fileName , path );
			output = new FileOutputStream(file);
			byte buffer[] = new byte[10 * 1024];
			int temp;
			while((temp = (input.read(buffer))) != -1){
				output.write(buffer ,0 ,temp);
			}
			output.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				output.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	/**
	 * 读取目录中mp3文件的名字和大小+lrc文件
	 */
	public List<info> getMp3Files(String path){
		List<info> mp3Infos = new ArrayList<info>();
		File file = new File(SDCardRoot + path+ File.separator );
		File[] files = file.listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].getName().endsWith("mp3")){
				info mp3Info = new info();
				mp3Info.setVideoName(files[i].getName());
				mp3Info.setVideoURL(files[i].length() + "");
				String lrcname[]=files[i].getName().split("\\.");
				String lrc=lrcname[0]+".lrc";
				if(isFileExist(lrc, "/mp3")){
					
				}				
				mp3Infos.add(mp3Info);			
			}
		}
		return mp3Infos;
	}
}
