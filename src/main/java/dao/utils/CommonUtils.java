package dao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import dao.cache.GamePropertiesCache;

/**
 * 
 * 类名称：CommonUtils
 * 类描述：常用工具类
 * 创建人：yxh
 * 创建时间：2012-7-16 上午07:43:40
 * 修改备注：
 * @version 1.0.0
 *
 */
public class CommonUtils {
	/**
	 * 操作系统分隔符
	 */
	public static String SYSTEM_SEPARATE = System.getProperty("file.separator");
	/**
	 * 工程的绝对地址,直接定位到bin外的目录
	 */
	public static String USER_DIR;
	/**
	 * 下载日志的基础目录,通过读取logback.xml配置
	 */
	public static String LOG_DOWNLOAD_DIR;
	static{
		USER_DIR= System.getProperty("user.dir");
		Pattern pattern = Pattern.compile("bin");
		Matcher matcher = pattern.matcher(USER_DIR);
		if(matcher.find()){
			USER_DIR = matcher.replaceAll("");
		}
		if(USER_DIR.lastIndexOf(SYSTEM_SEPARATE)==USER_DIR.length()-1){
			USER_DIR = USER_DIR.substring(0, USER_DIR.length()-1);
		}
	}
	/**
	 * isInteger(判断传入的对象是否是Integer类型值)
	 * @param data 待判断参数值
	 * @return  boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean isInteger(Object data){
		if(data==null||"".equals(data))return false;
		String reg = "[\\d]+";
		Pattern  p = Pattern.compile(reg); 
		Matcher  m = p.matcher(data.toString()); 
		return Integer.MAX_VALUE>=Double.parseDouble(data.toString())
		&&m.matches();
	}

	/**
	 * 判断传入的obj是否是float类型的值
	 * @name isFloat
	 * @param data 传入的参数
	 * @return boolean 
	 * @author yxh
	 * @date：2012-7-31 上午07:47:55
	 * @throws 
	 * @version 1.0.0
	 */
	public static boolean isFloat(Object data){
		if(data==null||"".equals(data))return false;
		try{
			Float.parseFloat(data.toString());
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * 判断一个对象值是否在byte的取值范围内
	 * @name isByte
	 * @param data 待验证的对象值
	 * @return 
	 * boolean
	 * @author yxh
	 * @date：2012-7-26 上午01:04:55
	 * @version 1.0.0
	 */
	public static boolean isByte(Object data){
		if(data==null||"".equals(data))return false;
		try{
			Byte.parseByte(data.toString());
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * 判断一个对象值是否在short类型的取值范围内
	 * @name isShort
	 * @param data 待验证的对象值
	 * @return 
	 * boolean
	 * @author yxh
	 * @date：2012-7-26 上午01:04:55
	 * @version 1.0.0
	 */
	public static boolean isShort(Object data){
		if(data==null||"".equals(data))return false;
		try{
			Short.parseShort(data.toString());
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一个对象值是否在Long类型的取值范围内
	 * @name isLong
	 * @condition 验证时候调用
	 * @param data
	 * @return  boolean
	 * @author yxh
	 * @date：2012-9-10 上午02:55:20
	 * @version 1.0.0
	 */
	public static boolean isLong(Object data){
		if(data==null||"".equals(data))return false;
		try{
			Long.parseLong(data.toString());
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/**
	 * 判断一个excel获取的字符串是否为空<BR/>
	 * null，0,0.0，空串均为空
	 * @param data
	 * @return ture为不为空
	 */
	public static boolean isStrEmptyFromExcel(String data){
		if(data!=null && !"0.0".equals(data) && !"0".equals(data)&&!"".equals(data)){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args){
		
//		System.out.println(getCurrentTimeByFormat("hh:mm:ss"));
//		System.out.println(Float.MAX_VALUE);
		
//		System.out.println(getIntNumAndCeil(10, 3));
//		
//		Object[] data = new Object[10];
//		System.out.println(ArrayUtils.isEmpty(removeNull(data)));
		
		int[] tests = {1,0,1,0,1};
		System.out.println("before"+Arrays.toString(tests));
		int[] arrs = removeNull(tests);
		System.out.println("after"+Arrays.toString(tests)+" arrs="+Arrays.toString(arrs));
	}

	
	
	/**
	 * 
	 * 根据传入的时间格式（如：yyyy-MM-dd HH:mm:ss）
	 * 获取当前时间。如果传入的为null获取""
	 * 则会把当前的时间以毫秒形式返回
	 * @name getCurrentTimeByFormat
	 * @param formt
	 * @return String hh:mm:ss
	 * @author yxh
	 * @date：2012-7-26 上午09:00:22
	 * @throws 
	 * @version 1.0.0
	 */
	public static String getCurrentTimeByFormat(String formt){
		SimpleDateFormat sdf=null;
		if(formt==null||"".equals(formt)){
			sdf= new SimpleDateFormat();
		}else{
			sdf= new SimpleDateFormat(formt);
		}
		return sdf.format(new Date());
	}
	
	/**
	 * 转换日期格式并返回字符型日期
	 * @name formatDate
	 * @param formt 格式如："yyyy-MM-dd"
	 * @param date 日期
	 * @return  String 日期字符型
	 * @author yxh
	 * @date：2012-9-18 上午02:48:59
	 * @version 1.0.0
	 */
	public static String formatDate(String formt,Date date){
		if(date==null||formt==null)return null;
		SimpleDateFormat sdf= new SimpleDateFormat(formt);
		return sdf.format(date);
	}
	/*********************************** 身份证验证开始 ****************************************/	
	/**
	 * 身份证号码验证 
	 * 1、号码的结构
	 * 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。
	 * 2、地址码(前六位数） 
	 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 
	 * 3、出生日期码（第七位至十四位）
	 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 
	 * 4、顺序码（第十五位至十七位）
	 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
	 * 顺序码的奇数分配给男性，偶数分配给女性。 
	 * 5、校验码（第十八位数）
	 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
	 * X 9 8 7 6 5 4 3 2
	 */

	/**
	 * 功能：身份证的有效验证
	 * @param IDStr 身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	public static boolean IDCardValidate(String IDStr) {
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			return false;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			return false;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			return false;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			return false;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String,String>  h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			return false;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				return false;
			}
		} else {
			return true;
		}
		// =====================(end)=====================
		return true;
	}

	/**
	 * 功能：设置地区编码
	 * @return Hashtable 对象
	 */
	private static Hashtable<String,String> GetAreaCode() {
		Hashtable<String,String>  hashtable = new Hashtable<String,String> ();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}
/*********************************** 身份证验证结束 ****************************************/

	
	/**
	 * 把int值的obj类型转换为int类型 
	 * 只能用于加载execl缓存的时候调用
	 * @name transforObjtoInto
	 * @param obj 待转化的obj对象
	 * @return int 返回转化后的值  0 表示异常值
	 * @author yxh
	 * @date：2012-8-8 上午08:19:23
	 * @version 1.0.0
	 */
	public static int transforObjtoInt(Object obj){
		try{
			if(obj==null)return 0;
			return !CommonUtils.isFloat(obj)?0:(int)Float.parseFloat((obj.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * 把byte值的obj类型转换为byte类型
	 * 只能用于加载execl缓存的时候调用
	 * @name transforObjtoByte
	 * @param obj 待转化的obj对象
	 * @return byte 返回转化后的值  0 表示异常值
	 * @author yxh
	 * @date：2012-8-8 上午08:19:23
	 * @version 1.0.0
	 */
	public static byte transforObjtoByte(Object obj){
		try{
			return (byte)Float.parseFloat(obj.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 把long值的obj类型转换为long类型
	 * 只能用于加载execl缓存的时候调用
	 * @name transforObjtoLong
	 * @param obj 待转化的obj对象
	 * @return Long 返回转化后的值  0 表示异常值
	 * @author yxh
	 * @date：2012-8-8 上午08:19:23
	 * @version 1.0.0
	 */
	public static long transforObjtoLong(Object obj){
		try{
			return (long)Double.parseDouble(obj.toString());
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 把Float值的obj类型转换为float类型
	 * @name transforObjtoFloat
	 * @param obj 待转化的obj对象
	 * @return Float 返回转化后的值  0 表示异常值
	 * @author yxh
	 * @date：2012-8-8 上午08:19:23
	 * @version 1.0.0
	 */
	public static float transforObjtoFloat(Object obj){
		try{
			return !CommonUtils.isFloat(obj)?0:Float.parseFloat((obj.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获得两个整数的整除值向上取整
	 * @param div 除数
	 * @param dived 被除数
	 * @return
	 */
	public static int getIntNumAndCeil(int div,int dived){
		if(dived==0)return 0;
		return (int)Math.ceil((double)div/(double)dived);
	}
	
	/**
	 * 获得两个整数的整除值向下取整
	 * @param div 除数
	 * @param dived 被除数
	 * @return
	 */
	public static int getIntNumAndFloor(int div,int dived){
		if(dived==0)return 0;
		return (int)Math.floor((double)div/(double)dived);
	}
	
	/**
	 * 计算传入的时间之间的秒数差
	 * 如果插入的end为空则与当前时间比
	 * @name caclBetweenTimes
	 * @param start 开始时间存储以秒单位的时间
	 * @param end 结束时间以秒为单位的时间
	 * @return int 返回相差的秒数
	 * @author yxh
	 * @date：2012-10-13 上午09:35:50
	 * @version 1.0.0
	 */
	public static int caclBetweenTimes(int start,int end){
		int result = 0;
		if(start<=0)return result;
		if(end==0){
			end = (int)Math.ceil(((double)System.currentTimeMillis()/(double)1000));
		}
		//开始时间不应该大于结束时间
		if(start>end)return result;
		result = end-start;
		return result;
	}
	

	
	/**
	 * 计算传入的时间之间的秒数差
	 * 如果插入的end为空则与当前时间比
	 * @name caclBetweenTimes
	 * @param start 开始时间存储以秒单位的时间
	 * @param end 结束时间以秒为单位的时间
	 * @return long 返回相差的豪秒数
	 * @author yxh
	 */
	public static long caclBetweenTimes(long start,long end){
		long result = 0;
		if(start<=0)return result;
		if(end==0){
			end = System.currentTimeMillis();
		}
		//开始时间不应该大于结束时间
		if(start>end)return result;
		result = end-start;
		return result;
	}
	
	/**
	 * 去除数组中null值并减少长度
	 * @param arrays
	 */
	public static <T extends Object> T[] removeNull(T[] arrays){
		if(ArrayUtils.isEmpty(arrays))return arrays;
		int size = arrays.length;
		for(int i=0;i<size;i++){
			if(arrays[i]==null){
				return removeNull(ArrayUtils.remove(arrays,i));
			}
		}
		return arrays;
	}
	/**
	 * 去除数组中null值并减少长度
	 * @param arrays
	 */
	public static  int[] removeNull(int[] arrays){
		if(ArrayUtils.isEmpty(arrays))return arrays;
		int size = arrays.length;
		for(int i=0;i<size;i++){
			if(arrays[i]==0){
				return removeNull(ArrayUtils.remove(arrays,i));
			}
		}
		return arrays;
	}
	
	/**
	 * 去除数组中null值并减少长度
	 * @param arrays
	 */
	public static  long[] removeNull(long[] arrays){
		if(ArrayUtils.isEmpty(arrays))return arrays;
		int size = arrays.length;
		for(int i=0;i<size;i++){
			if(arrays[i]==0){
				return removeNull(ArrayUtils.remove(arrays,i));
			}
		}
		return arrays;
	}
	
	/**
	 * 把Object[]{Object[]{obj....},...,..}格式的数组变为：
	 * Object[]{obj....}形式的数据
	 * @param result 
	 * @return
	 */
	public static Object[] transMtriArray2SingleArray(Object[] result){
		if(ArrayUtils.isEmpty(result))return new Object[0];
		int dataLen = 0;
		Object[] temps = new Object[100];
		Object[] arrays = null;
		int len = 0;
		int size = 0;
		int i=0;
		for(Object obj:result){
			if(obj==null||!obj.getClass().isArray())continue;
			dataLen = ((Object[])obj).length;
			len = temps.length;
			if(len<size+dataLen){
				arrays = new Object[2*(size+dataLen)];
				System.arraycopy(temps,0,arrays,0,len);
				temps = arrays;
			}
			System.arraycopy(obj,0,temps,i*dataLen,dataLen);
			size+=dataLen;
			i++;
		}
		return CommonUtils.removeNull(temps);
	}
	
	/**
	 * 转换数组为："312,31232,34234,23423"
	 * @param idList
	 * @return
	 */
	public static String transferArray2Str(long[] idList){
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(long id:idList){
			if(i==0){
				sb.append(id);
			}else{
				sb.append(","+id);
			}
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * 转换数组为："312,31232,34234,23423"
	 * @param idList 基本类型的数组  或者string数组
	 * @param split 分割符
	 * @return
	 */
	public static <T extends Object> String transferArray2Str(T[] idList,String split){
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(T id:idList){
			if(!id.getClass().isPrimitive()&&
					!(id instanceof String))return null;
			if(i==0){
				sb.append(id);
			}else{
				sb.append(split+id);
			}
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * 转换list为："312,31232,34234,23423"
	 * @param idList
	 * @return
	 */
	public static String transferArray2Str(Collection<Long> idList){
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(long id:idList){
			if(i==0){
				sb.append(id);
			}else{
				sb.append(","+id);
			}
			i++;
		}
		return sb.toString();
	}
	
	
	/**
	 * 转换list为："312,31232,34234,23423"
	 * @param idList
	 * @return
	 */
	public static <E extends Object> String transferCollenction2Str(Collection<E> idList){
		if(idList==null||idList.isEmpty())return null;
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(E id:idList){
			if(i==0){
				sb.append(id);
			}else{
				sb.append(","+id);
			}
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * 转换list为："312,31232,34234,23423"
	 * @param idList
	 * @param delimite 分隔符
	 * @return
	 */
	public static String transferArrayToStr(List<Long> idList,String delimite){
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(long id:idList){
			if(i==0){
				sb.append(id);
			}else{
				//sb.append(","+id);
				sb.append(delimite+id);
			}
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * 转换int[]为："312,31232,34234,23423"
	 * @param idList
	 * @return
	 */
	public static String transferCollenction2Str(int[] idList){
		if(ArrayUtils.isEmpty(idList))return null;
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(int id:idList){
			if(i==0){
				sb.append(id);
			}else{
				sb.append(","+id);
			}
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * 根据远程地址获取ip
	 * @param remote
	 * @return
	 */
	public static String getIp(String remote){
		String[] ipArr = remote.toString().split("\\:");
		return ipArr[0].substring(1,ipArr[0].length());
	}
	
	
	/**
	 * 把string数组转换为int[] 
	 * @param strs
	 */
	public static int[] transforStr2IntArray(String[] strs){
		if(ArrayUtils.isEmpty(strs))return null;
		int size = strs.length;
		int[] intArray = new int[size];
		for(int i=0;i<size;i++){
			intArray[i]=transforObjtoInt(strs[i]);
		}
		return intArray;
	}

	/**
	 * 把string数组转换为int[] 
	 * @param strs
	 */
	public static float[] transforStr2FloatArray(String[] strs){
		if(ArrayUtils.isEmpty(strs))return null;
		int size = strs.length;
		float[] intArray = new float[size];
		for(int i=0;i<size;i++){
			intArray[i]=transforObjtoFloat(strs[i]);
		}
		return intArray;
	}
	

	
	/**
	 * 字符串转换成List<Integer>
	 * @param str
	 * @param split 分隔符
	 * @author Lobbyer 
	 * @date 2014-5-18 下午10:07:32
	 */
	public static List<Integer> transforStr2IntList(String str,String split) {
		List<Integer> list = new ArrayList<Integer>();
		if(StringUtils.isBlank(str)) return list;
		String[] strs = str.split(split);
		for(String value:strs) {
			list.add(transforObjtoInt(value));
		}
		return list;
	}
	
	/**
	 * 字符串转换成List<Long>
	 * @param str
	 * @param split 分隔符
	 * @author Lobbyer 
	 * @date 2014-5-18 下午10:07:32
	 */
	public static List<Long> transforStr2LongList(String str,String split) {
		List<Long> list = new ArrayList<Long>();
		if(StringUtils.isBlank(str)) return list;
		String[] strs = str.split(split);
		for(String value:strs) {
			list.add(transforObjtoLong(value));
		}
		return list;
	}
	
	/**
	 * 玩家登陆合法性校验
	 * $accid.$accname.$zoneid.$tstamp.$fcm
	 * @return true 合法  false 非法
	 */
	public static final boolean vildateLogin(String openid,String accname,String zoneid,
			String tstamp,String fcm,String key){
		if(GamePropertiesCache.isTest)return true;
		String ckey = PropertiesTools.getProperties("bkHttpKey");
		String md = openid+accname+zoneid+tstamp+fcm+ckey;
		MD5 md5 = new MD5();
		String digest = md5.toDigest(md);
		return digest.equals(key);
	}
	
	
	
	/**
	 * 把对象集合转换为字符串
	 * @param list Object一定要重写toString 方法
	 * @param split 
	 * @return
	 */
	public static String ListToStr(List<?> list,String split){
		StringBuilder sb = new StringBuilder();
		if(list==null||list.isEmpty())return "";
		int size = list.size();
		for(int i=0;i<size;i++){
			if(i==size-1){
				sb.append(list.get(i));
			}else{
				sb.append(list.get(i));
				sb.append(split);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 把map集合转换为字符串
	 * @param map key 为基本类型或者string
	 * @param osp key:value
	 * @param tsp <key:value>|<key:value>
	 * @return
	 */
	public static String mapToStr(Map<?,?> map,String osp,String tsp){
		StringBuilder sb = new StringBuilder();
		if(map==null||map.isEmpty())return "";
		int size = map.size();
		int i=0;
		for(Object obj:map.keySet()){
			if(i==size-1){
				sb.append(obj+osp+map.get(obj));
			}else{
				sb.append(obj+osp+map.get(obj));
				sb.append(tsp);
			}
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * 统计数组中包含val值的个数
	 * @param arrs
	 * @param val
	 * @return
	 */
	public static int countEqualVals(int[] arrs,int val){
		if(arrs==null||arrs.length==0)return 0;
		int count = 0;
		for(int v:arrs){
			if(v==val)count++;
		}
		return count;
	}
	
	/**
	 * 复制二维数组
	 * @param srcs
	 * @return
	 */
	public static final int[][] copyArrays(int[][] srcs){
		if(srcs==null||srcs.length==0)return new int[0][];
		int[][] result = new int[srcs.length][];
		int i=0;
		int[] as = null;
		for(;i<srcs.length;i++){
			as = srcs[i];
			int[] sc = new int[as.length];
			for(int j=0;j<as.length;j++){
				sc[j]=as[j];
			}
			result[i]=sc;
		}
		return result;
	}
	
	/**
	 * 复制一维数组
	 * @param src
	 * @return
	 */
	public static final int[] copyArray(int[] src){
		if(src==null||src.length==0)return new int[0];
		int[] result = new int[src.length];
		for(int i=0;i<src.length;i++){
			result[i]=src[i];
		}
		return result;
	}
	
	/**
	 * 设置值到map中如果有id相同的则数值叠加
	 * @param map
	 * @param val
	 */
	public static final void setMapNum(Map<Integer,Integer> map,int val,int id){
		Integer value = map.get(id);
		map.put(id,value==null?val:value+val);
	}
}
