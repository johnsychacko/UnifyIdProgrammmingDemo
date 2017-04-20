package com.unifyid.demo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ImageExample {
private static final String USER_AGENT = "Mozilla/5.0";
	
	
	public static void main(String args[])throws IOException{
	     //image dimension
		ImageExample obj= new ImageExample();
	     int width = 640;
	     int height = 320;
	     //create buffered image object img
	     BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	     //file object
	     File f = null;
	     //create random image pixel by pixel
	     int values[]= obj.getRandomNumber();
	     for(int y = 0; y < height; y++){
	       for(int x = 0; x < width; x++){
	    	   for(int val :values){
	    		   int a = val*256;
	    		   int r = val*256; //red
	    	       int g = val*256; //green
	    	       int b = val*256; 
	    	       int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
	    	         img.setRGB(x, y, p);
	    	   }
	       }
	     }
	     //write image
	     try{
	       f = new File(System.currentTimeMillis()+"_Output.png");
	       ImageIO.write(img, "png", f);
	     }catch(IOException e){
	       System.out.println("Error: " + e);
	     }
	}
	
	private static int[] getRandomNumber() throws IOException{
		System.out.println("Calling... getRandomNumber");
		int res[] = new int[3];
		URL obj = new URL("https://www.random.org/integers/?num=3&min=0&max=255&col=2&base=10&format=plain&rnd=new");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStream is = con.getInputStream();
			byte data[]=new byte[1024*10];
			int s = 0;
			List <Integer>values = new ArrayList<Integer>();
			while((s = is.read(data))>0){
				String str = new String(data,0,s);
				//System.out.println("Response: "+str);
				String val = "";
				for(int i=0;i<str.length();i++){
					char ch = str.charAt(i);
					if(ch>47 && ch< 58){
						val = val + ch;
					}else{
						if(!val.isEmpty()){
							values.add(Integer.parseInt(val));
							val = "";
						}
					}
				}
			}	
			System.out.println("Response in number: values: "+values);
			int index = 0;
			for(Integer i : values){
				res[index]=i;
				index++;
				if(index==3){
					break;
				}
			}
			System.out.println("RGB values: "+res[0]+", "+res[1]+", "+res[2]);
		} else {
			System.out.println("GET request not worked");
		}
		return res;
	}


}
