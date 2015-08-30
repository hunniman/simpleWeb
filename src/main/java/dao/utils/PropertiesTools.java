package dao.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 资源文件操作工具类
 * @author Administrator
 */
public class PropertiesTools {
	//资源文件加载类
	private static final SafeProperties properties = new SafeProperties();
	//默认保存路径
	private static final String path = CommonUtils.USER_DIR+CommonUtils.SYSTEM_SEPARATE+
			"resource"+CommonUtils.SYSTEM_SEPARATE;
	//默认保存配置文件名字
	private static final String FILE_NAME="game.properties";
	
	/**
	 * 
	 * 从资源文件中获取一个String类型值
	 * @name getProperties
	 * @param key 键值
	 * @return String "" 代表取不到 
	 * @throws Exception 
	 * @author yxh
	 */
	public static String getProperties(String key){
		if(properties.containsKey(key)){
			return properties.getProperty(key);
		}else{
			return "";
		}
	}
	
	
	/**
	 * 
	 * 从资源文件中获取一个int类型值
	 * @name getPropertiesInt
	 * @param key 键值
	 * @return int Integer.MIN_VALUE 代表取不到
	 * @throws Exception 
	 * @author yxh
	 */
	public static int getPropertiesInt(String key) throws Exception{
		String val = "";
		if(properties.containsKey(key)){
			val = properties.getProperty(key);
			if(NumberUtils.isNumber(val)){
				return NumberUtils.toInt(val);
			}
		}else{
			throw new Exception(" getPropertiesInt not find this key key="+key);
		}
		return Integer.MIN_VALUE;
	}
	
	/**
	 * 
	 * 从资源文件中获取一个int类型值
	 * @name getPropertiesLong
	 * @param key 键值
	 * @return  0  代表取不到
	 * @throws Exception 
	 * @author yxh
	 */
	public static long getPropertiesLong(String key) throws Exception{
		String val = "";
		if(properties.containsKey(key)){
			val = properties.getProperty(key);
			if(NumberUtils.isNumber(val)){
				return NumberUtils.toLong(val);
			}
		}else{
			throw new Exception(" getPropertiesInt not find this key key="+key);
		}
		return 0;
	}

	/**
	 * 初始化加载资源文件(可加载src外的包)
	 * @param profile 文件位置
	 * @throws Exception 
	 */
	public static void initPropretiesWithOutFolder(String profile) throws Exception{
		if(StringUtils.isBlank(profile))throw new Exception("fileName is empty init properites fial");
		String filePath = path+profile;
		filePath = URLDecoder.decode(filePath, "utf-8");
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
		properties.load(in);
	}
	
	/**
	 * 添加/修改属性值
	 * @param key
	 * @param value
	 */
	public static void setPropreties(final String key,final String value){
		properties.put(key,value);
	}
	
	/**
	 * 保存修改后的配置文件
	 * @throws Exception 
	 */
	public static void saveProperties(String fileName) throws Exception{
		String name = FILE_NAME;
		if(!StringUtils.isBlank(fileName)){
			name=fileName;
		}
		properties.store(new FileOutputStream(path+name),null);
	}


	public static void main(String[] args) {
		try {
			initPropretiesWithOutFolder("game.properties");
			System.out.println(properties.get("serverOpenTime"));
			properties.put("serverOpenTime", "2013-08-22 10:00:00");
			System.out.println(properties.get("serverOpenTime"));
			properties.store(new FileOutputStream("config"+CommonUtils.SYSTEM_SEPARATE+"game.properties"),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 初始化properties文件
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static SafeProperties initPropreties(String profile) throws Exception{
		if(StringUtils.isBlank(profile))throw new Exception("fileName is empty init properites fial");
		String filePath = profile+FILE_NAME;
		filePath = URLDecoder.decode(filePath, "utf-8");
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
		properties.load(in);
		return properties;
	}
	
	/**
	 * 初始化properties文件
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static SafeProperties initPropreties() throws Exception{
		SafeProperties properties = new SafeProperties();
		String filePath = path+FILE_NAME;
		filePath = URLDecoder.decode(filePath, "utf-8");
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
		properties.load(in);
		return properties;
	}
}