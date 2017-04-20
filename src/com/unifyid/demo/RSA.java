package com.unifyid.demo;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RSA {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	static private BigInteger p;
	static private BigInteger q;
	static private BigInteger N;
	static private BigInteger phi;
	static private BigInteger e;
	static private BigInteger d;
    //private int        bitlength = 1024;
    private Random     r;
 
    public RSA() throws IOException
    {
        int values[] = getPrimeNumber();
		BigInteger p = new BigInteger(""+values[0]);
	    BigInteger q = new BigInteger(""+values[1]);
		e = new BigInteger(""+values[2]);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(phi);
    }
 
    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }
 
    // Encrypt message
    public byte[] encrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }
 
    // Decrypt message
    public byte[] decrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
    
    private static int[] getPrimeNumber() throws IOException{
		int res[]=new int[3];
		while(true){
			int values [] = getProbablePrimeNumber();
			if(isPrime(values[0]) && isPrime(values[1]) && isPrime(values[2]) && values[0]!=values[1]){
				BigInteger ph = new BigInteger(""+values[0]).subtract(BigInteger.ONE).multiply(new BigInteger(""+values[1]).subtract(BigInteger.ONE));
				if(new BigInteger(""+values[2]).gcd(ph).intValue()==1){
					System.arraycopy(values,0,res,0,values.length);
					break;
				}
			}
		}
		return res;
	}
	
	private static int[] getProbablePrimeNumber() throws IOException{
		System.out.println("Calling... getProbablePrimeNumber");
		int res[] = new int[3];
		URL obj = new URL("https://www.random.org/integers/?num=100&min=7&max=999&col=2&base=10&format=plain&rnd=new");
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
				if(isPrime(i)){
					res[index]=i;
					index++;
				}
				if(index==3){
					break;
				}
			}
			System.out.println("Prime numbers: "+res[0]+", "+res[1]);
		} else {
			System.out.println("GET request not worked");
		}
		return res;
	}
	
	static boolean isPrime(int n) {
		// check if n is a multiple of 2
		if (n % 2 == 0)
			return false;
		// if not, then just check the odds
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}
	@SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException
    {
		RSA rsa = new RSA();
        String str = "Johnsy Here";
        System.out.println("Message: "+str);
        String ecrypted="";
        String decrypted="";
        for(int i=0;i<str.length();i++){
        	BigInteger m = new BigInteger(""+(int)str.charAt(i));
            BigInteger c =  m.modPow(e, N);
            ecrypted = ecrypted+(char)c.intValue();
            BigInteger pl = c.modPow(d, N);
            decrypted = decrypted + (char)pl.intValue();
        }
        
        System.out.println("Encrypted: "+ecrypted);
        System.out.println("Decrypted: "+decrypted);
        

    }
 
}