package com.chekn.service.img;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;


public class RedisDM {
	
	private JedisPool pool;

	private final String url_flow="url_flow";
	private final String url_local="url_local";
	
	public RedisDM(String host, int port, String auth){
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10);
		config.setMaxTotal(30);
		config.setMaxWaitMillis(3*1000);
		
		pool = new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT, auth);
	}
	
	public void init() {
		
	}
	
	
	public synchronized void urlFlowQueue(String url, String reqHash) {
		Jedis client=pool.getResource();
		try {
			String nv=reqHash;
			if( client.hexists(url_flow, url) ) {
				nv= client.hmget(url_flow, url).get(0) + "," + reqHash;
			} 
			client.hset(url_flow, url, nv);
		} finally {
			client.close();
		}
	}
	
	public String urlLocalGet(String url, String reqHash, String dir) throws Exception {

		Jedis client=pool.getResource();
		try {
			if(client.hmget(url_flow, url).get(0).startsWith(reqHash)) {
				//go dl img, and store url_local
				String fn=null;
				fn=dldImg(url, dir);
				client.hset(url_local, url, fn);
				return fn;
			} else {
				int tryCount=0;
				while(true) {
					tryCount++;
					if(client.hexists(url_local, url)) {
						return client.hmget(url_local, url).get(0);
					} else {
						if(tryCount == 10 )
							throw new CustRuntimeException("try 10 times still get none; please try later");
						TimeUnit.MILLISECONDS.sleep(100);
					}
				}
			}
		} finally {
			client.close();
		}
	}
	

	
	public static String dldImg(String ul, String path) throws Exception {
		URL   url   =   new   URL( ul); 
		URLConnection   uc   =   url.openConnection(); 
		String mime=uc.getContentType();
		InputStream   is   =   uc.getInputStream(); 
		FileUtils.forceMkdir(new File(path));
		String uuid=UUID.randomUUID().toString().replace("-", "");
		File   file   =   new   File( path + uuid + "#" + mime.replace("/", "-")); 
		FileOutputStream   out   =   new   FileOutputStream(file); 
		IOUtils.copy(is, out);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(out);
		
		return file.getName();
	}
	
	public static void main(String[] args) {
		RedisDM mc=new RedisDM("127.0.0.1", 6379, "imgservercache");
		Jedis client=mc.pool.getResource();
		
		/*Map<String, String> map2 = new HashMap<String, String>();
		map2.put("123131", "ccs");
		client.hmset("url_local",map2);*/
		client.del("url_flow");
		client.del("url_local");
		System.out.println(client.hgetAll("url_flow"));;
		for(String str: client.hkeys("url_flow")){
			String[] cx=client.hmget("url_flow", str).get(0).split(",");
			for(String x:cx) {
				System.out.println(x);
			}
		}
		System.out.println(client.hgetAll("url_local"));;/**/
		System.out.println(File.pathSeparator);
		
		client.close();
	}
	
}
