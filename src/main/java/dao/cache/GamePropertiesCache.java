package dao.cache;

import java.text.ParseException;
import java.util.Arrays;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import dao.utils.PropertiesTools;
import dao.utils.TimeDateUtil;

/**
 * 游戏配置缓存
 * @author yxh
 *
 */
public class GamePropertiesCache {
	/**
	 * 中央服务器ip
	 */
	public static String globalIp = null;
	/**
	 * 中央服务器端口
	 */
	public static int globalPort = 0;
	
	/**
	 * 区号 数据合服后会自动修改区号,以下划线区分:1_2_3
	 */
	public static int[] zoneidSet = null;
	/**
	 * 服务器域名
	 */
	public static String serverAddress = "";
	/**
	 * 游戏主端口
	 */
	public static int serverPort = 0;
	/**
	 * 后台端口
	 */
	public static int bkPort = 0;
	/**
	 * 充值端口
	 */
	public static int rechargePort=0;
	/**
	 * 时区
	 */
	public static TimeZone timeZone= TimeZone.getDefault();
	
	/**
	 * 开服时间 使用2013-07-13 12:00:00这种形式，此值在isServerOpen=0的时候可以通过后台更改
	 * 这里用时间戳来记录
	 */
	public static int serverOpenTime;
	/**
	 * GM是否开启
	 */
	public static boolean gmFlag;
	/**
	 * 下线清除玩家信息间隔时间
	 */
	public static int CLEARGAP ;
	/**
	 * 表自增起始id
	 */
	public static long autoId;
	/**
	 * 将领表自启id
	 */
	public static long generalAutoId;
	
	/**
	 * buffer100%触发开关
	 */
	public static boolean bufferOn;
	/**
	 * 技能100%触发开关
	 */
	public static boolean skillOn;
	
	/**
	 * 启动是否显示UI界面
	 */
	public static boolean showUI;
	/**
	 * 是否测试服
	 */
	public static boolean isTest;
	
	/**
	 * 防沉迷标识
	 */
	public static boolean antiFlag;
	
	/**
	 * openAPI地址
	 */
	public static String openApiAddress ="";
	
	/**
	 * 联运标识
	 */
	public static String transid="";
	
	/**
	 * 战斗播放器版本
	 */
	public static int vcr_version=2;
	/**
	 * 合服标识
	 */
	public static boolean isHeQu=false;
	/**
	 * 是否执行合服程序
	 */
	public static boolean hefuFlag=false;
	/**
	 * 初始化游戏配置
	 * @throws Exception 
	 */
	public static void initGameProperties() throws Exception{
		try {
			//区号
			initZoneId(PropertiesTools.getProperties("zoneid"));
			//服务器域名
			serverAddress = PropertiesTools.getProperties("serverAddress");
			//游戏主端口
			serverPort = PropertiesTools.getPropertiesInt("serverPort");
			//后台端口
			bkPort = PropertiesTools.getPropertiesInt("bkPort");
			//充值端口
			rechargePort = PropertiesTools.getPropertiesInt("rechargePort");
			//初始化中央服务器端口
			globalPort=PropertiesTools.getPropertiesInt("globalPort");
			//中央服务器ip
			globalIp = PropertiesTools.getProperties("globalIp");
			//时区
			timeZone = TimeZone.getTimeZone(PropertiesTools.getProperties("timezone"));
			//初始化开服时间
			serverOpenTime = initServerOpenTime(PropertiesTools.getProperties("serverOpenTime"));
			//GM是否打开
			gmFlag = PropertiesTools.getPropertiesInt("gmFlag")==1;
			//下线清除玩家信息间隔时间
			CLEARGAP = PropertiesTools.getPropertiesInt("cleargap");
			//auto起始值
			initAutoId();
			//buffer100%触发开关
			bufferOn = PropertiesTools.getPropertiesInt("bufferOn")==1;
			//技能100%触发开关
			skillOn = PropertiesTools.getPropertiesInt("skillOn")==1;
			//是否显示ui界面
			showUI = PropertiesTools.getPropertiesInt("showUI")==1;
			//是否测试服
			isTest = PropertiesTools.getPropertiesInt("isTest")==1;
			//防沉迷标识
			antiFlag = PropertiesTools.getPropertiesInt("antiFlag")==1;
			//openApi地址
			openApiAddress = PropertiesTools.getProperties("openApiAddress");
			//读取合服配制
			String hefstr = PropertiesTools.getProperties("hefuFlag");
			hefuFlag=!StringUtils.isBlank(hefstr)&&StringUtils.equals(hefstr,"1");
			//联运平台标识
			String transids = PropertiesTools.getProperties("transid");
			if(!StringUtils.isBlank(transids)){
				transid = transids;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 重新设置系统配置缓存值
	 */
	public static void reSetGamePropertiesVal(String key,String val){
		try {
			switch (key) {
			case "timezone"://时区
				timeZone = TimeZone.getTimeZone(val);
				break;
			case "gmFlag"://GM
				gmFlag = StringUtils.equals("1",val);
				break;
			case "cleargap"://下线清除玩家信息间隔时间
				CLEARGAP = NumberUtils.toInt(val);
				break;
			case "isTest"://测试服标识
				isTest = StringUtils.equals("1",val);
				break;
			case "antiFlag"://防沉迷标识
				antiFlag = StringUtils.equals("1",val);
				break;
			case "openApiAddress"://openApi地址
				openApiAddress = val;
				break;				
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化自增id、道具起始id
	 * 1+5位区号+10位流水号
	 * @throws Exception 
	 */
	public static void initAutoId() throws Exception{
		int i = zoneidSet[zoneidSet.length-1];
		String result = (i+"");
		char[] chars = result.toCharArray();
		for(int j=chars.length;j<5;j++){
			result="0"+result;
		}
		//初始化区号
		autoId = NumberUtils.toLong("1"+result+"0000000001");
		//初始将领自启id
		generalAutoId = NumberUtils.toLong("3"+result+"0000000001");
	
	}
	
	/**
	 * 初始化区号
	 * @param zoneidStr
	 * @throws Exception 
	 */
	public static void initZoneId(String zoneidStr) throws Exception{
		String[] zoneidArr = zoneidStr.split("\\_");
		if(zoneidArr!=null && zoneidArr.length>0){
			zoneidSet = new int[zoneidArr.length];
			int i=0;
			for(String zoneid:zoneidArr){
				zoneidSet[i]=Integer.parseInt(zoneid);
				i++;
			}
			isHeQu=i>1;
		}else{
			throw new Exception("initZoneId");
		}
		Arrays.sort(zoneidSet);
	}
	
	/**
	 * 初始化开服时间
	 * @param str
	 * @throws ParseException
	 */
	private static int initServerOpenTime(String str) throws ParseException{
		return TimeDateUtil.getTimeIntByStr(str);
	}
	
	/**
	 * 获取腾讯区号名称
	 * @param zoneid
	 * @return
	 */
	public static String getZoneName(int zoneid){
		return "["+zoneid+"区]";
	}
	
	/**
	 * 获取平台使用的区号id
	 * @param zoneid
	 * @return
	 */
	public static int getZoneId(int zoneid){
		return zoneid;
	}
	
	/**
	 * 判断指定区号是否为当前服务器的合区服
	 * @param zid
	 * @return
	 * @author Lobbyer 
	 * @date 2014-10-13 下午12:27:33
	 */
	public static boolean isHeFuZone(int zid) {
		int[] zoneidSet = GamePropertiesCache.zoneidSet;
		if(zoneidSet == null) {
			return false;
		}
		for(Integer id:zoneidSet) {
			if(id == zid) return true;
		}
		return false;
	}
	
	public static boolean getIsSamePT(int zid,int zoneid){
		return true;
	}
	
}
