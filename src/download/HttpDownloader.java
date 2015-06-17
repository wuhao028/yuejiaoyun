package download;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class HttpDownloader {

private URL url = null;
	
	/**
	 * 根据URL下载文件，前提是这个文件是文本的，函数的返回值就是文件当中的内容
	 * 1.创建一个URL对象
	 * 2.通过URL对象，创建一个HttpURLConnection对象
	 * 3.得到InputStream
	 * 4.从InputStream得到数据
	 */
	public String download(String urlStr){
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try{
			//创建一个url连接
			url = new URL(urlStr);
			//创建一个http连接
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			//使用IO读取数据
			buffer  = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while((line = buffer.readLine()) != null){
				sb.append(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * 该函数返回整形，-1代表下载文件出错，0代表下载文件成功，1代表文件已经存在
	 */
	public int downFile(String urlStr, String path,String fileName){
		InputStream inputStream = null;
		try{
			FileUtils fu = new FileUtils();
			
			if(fu.isFileExist(fileName,path)){
				return 1;
			}else{
				
				inputStream = getInputStreamFromUrl(urlStr);
				System.out.println("test");
				File resultFile = fu.write2SDFromInput(path, fileName, inputStream);
				if(resultFile == null){
					return -1;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 根据URL得到输入流
	 */
	public InputStream getInputStreamFromUrl(String urlStr) throws MalformedURLException, IOException{
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}
}
