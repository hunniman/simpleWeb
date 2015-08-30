package dao.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.cache.GamePropertiesCache;

/**
 * 时间日期工具类
 * @author Administrator
 */
public class TimeDateUtil {
	private static Logger log = LoggerFactory.getLogger(TimeDateUtil.class);
	/**
	 * 一天的时间
	 */
	public static final long ONEDAY = 3600*24*1000;
	public static final long ONEHOUR = 3600*1000;
	public static final int ONE_HOUR_INT = 3600;
	public static final int ONE_DAY_INT = 3600*24;
	public static final int ONE_SECOND = 1;//1秒
	public static final int ONE_MINUTE = 60;//1分钟
	public static final int FIVE_MINUTE = 60*5;//5分钟
	
	/**
	 * 获取当前时间
	 * @name getCurrentTime
	 * @return 当前以int类型返回的数值
	 */
	public static int getCurrentTime(){
		return (int) (System.currentTimeMillis()/1000);
	}
	
	/**
	 * 获取当前时间毫秒作为道具的主键
	 * 只用于获取倒计时的道具时候调用
	 * @return
	 */
	public static final synchronized long getCurrentTimeId(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 获取今天零时的时间
	 * @return 零时以long类型返回的数值
	 */
	public static long getTodayZeroTime(){
		Calendar calendar = Calendar.getInstance(GamePropertiesCache.timeZone);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 比较两个时间相差的天数
	 * @param start
	 * @param end
	 * @return 
	 */
	public static int getDayBetween(long start,long end){
		if(end<=start)return 0;
		long l = end-start;
		return (int) (l / (24 * 60 * 60 * 1000)); 
	}
	
	/**
	 * 获取传入时间的零点跟现在时间的零点相差天数
	 * @param start
	 * @return
	 */
	public static int getZeroDayBetween(int start){
		long zeroTime = getDayZeroTime(start*1000l);
		return getDayBetween(zeroTime, getTodayZeroTime());
	}
	
	/**
	 * 获取今天零时的时间
	 * @return 零时以long类型返回的数值
	 */
	public static int getTodayZeroTimeInt(){
		Calendar calendar = Calendar.getInstance(GamePropertiesCache.timeZone);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (int) (calendar.getTimeInMillis()/1000);
	}
	
	/**
	 * 取当天23点59分59秒   
	 * @param d 想往后再追加几天就填几,否为0
	 * @return
	 */
    public static String getTodayEnd(int d) {  
    	SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, 23);   
        calendar.set(Calendar.MINUTE, 59);  
        calendar.set(Calendar.SECOND, 59);  
        calendar.set(Calendar.DATE,calendar.get(Calendar.DAY_OF_MONTH)+d);
        Date date = new Date(calendar.getTimeInMillis());  
        return longDateFormat.format(date);  
   }   
    /**
	 * 取当天23点59分59秒   
	 * @param d 想往后再追加几天就填几,否为0
	 * @return
	 */
    public static int getTodayEndrInt(int d) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, 23);   
        calendar.set(Calendar.MINUTE, 59);  
        calendar.set(Calendar.SECOND, 59);  
        calendar.set(Calendar.DATE,calendar.get(Calendar.DAY_OF_MONTH)+d);
        Date date = new Date(calendar.getTimeInMillis());  
        return (int)(date.getTime()/1000); 
   }  
	/**
	 * 获取当前周的周一零点时间，以int返回
	 * @return
	 */
	public static int getMonDayZeroTime(){
		Calendar calendar = Calendar.getInstance(GamePropertiesCache.timeZone);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return (int)(calendar.getTimeInMillis()/1000);
	}
	/**
	 * 获取明天零时的时间
	 * @return 零时以long类型返回的数值
	 */
	public static long getTomorrowZeroTime(){
		long zeroTime = getTodayZeroTime();
		zeroTime += ONEDAY;
		return zeroTime;
	}
	/**
	 * 获取今天零时的时间
	 * @return 零时以int类型返回的数值
	 */
	public static int getTodayZeroTimeReturnInt(){
		long todayZeroTime = getTodayZeroTime();
		return (int) (todayZeroTime/1000);
	}
	
	/**
	 * 判断当日的零点是否大于传入的零点处理时间
	 * @param zeroTime
	 * @return
	 */
	public static boolean isLessTodayZeroTime(final int zeroTime){
		return getTodayZeroTimeReturnInt()>zeroTime;
	}
	
	/**
	 * 获取明天零时的时间
	 * @return 零时以int类型返回的数值
	 */	
	public static int getTomorrowZeroTimeReturnInt(){
		long tomorrowZeroTime = getTomorrowZeroTime();
		return (int) (tomorrowZeroTime/1000);
	}
	
	public static int getHour(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GamePropertiesCache.timeZone);
		return calendar.get(Calendar.HOUR_OF_DAY);  
	}
	
	/**
	 * @return true 表示 周 2,4,6 否则反之
	 */
	public static boolean getOddDayOfWeek(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GamePropertiesCache.timeZone);
		return calendar.get(Calendar.DAY_OF_WEEK)%2!=0;
	}
	
	/**
	 * @return true 表示今天是周日
	 */
	public static boolean isSunday(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GamePropertiesCache.timeZone);
		return calendar.get(Calendar.DAY_OF_WEEK)==1;
	}
	/**
	 * 获取某天的0点
	 * @param time 某天的某个时间
	 * @return 那天的0点
	 */
	public static int getOneDayZeroTime(int time){
		long recTime = time*1000l;
		return (int) (getDayZeroTime(recTime)/1000);
	}
	
	/**
	 * 获取指定时间当天零点的时间戳
	 * @param time
	 * @return
	 */
	public static long getDayZeroTime(long time){
		Calendar calendar = Calendar.getInstance(GamePropertiesCache.timeZone);
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 返回当前时间,处理秒数和分钟为0
	 * @return 当前时间,处理秒数和分钟为0
	 */
	public static int getCurrTimeWithZeroSec(){
		Calendar c=Calendar.getInstance();
		c.setTimeZone(GamePropertiesCache.timeZone);
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int timeInMillis = (int) (c.getTimeInMillis()/1000);
		return timeInMillis;
	}
	
	public static String printTime(int time){
		long arg = time*1000l;
		Date date = new Date(arg);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		format.setTimeZone(GamePropertiesCache.timeZone);
		String printStr = format.format(date);
		String printData = printStr + "s";
		return printData;
	}
	
	/**
	 * 获取年月日 时分格式的日期
	 * @param time
	 * @return
	 */
	public static String getTimeHM(int time){
		long arg = time*1000l;
		Date date = new Date(arg);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
		format.setTimeZone(GamePropertiesCache.timeZone);
		String printStr = format.format(date);
		return printStr;
	}
	
	public static String printTime(long time){
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		format.setTimeZone(GamePropertiesCache.timeZone);
		String printData = format.format(date);
//		System.out.println(printData);
		return printData;
	}
	
	/**
	 * 仅仅返回时间
	 * @name printOnlyTime
	 * @condition 这里描述这个方法适用条件
	 * @param time
	 * @return 时分秒
	 * @author lobbyer
	 * @date：2012-8-30 下午07:52:08
	 */
	public static String printOnlyTime(int time){
		long arg = time*1000l;
		Date date = new Date(arg);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(GamePropertiesCache.timeZone);
		String printData = format.format(date);
//		System.out.println(printData);
		return printData;
	}
	
	/**
	 * 根据int类型时间以及转换格式转换为字符型的time
	 * @param time
	 * @param pattern 格式：为空则默认为 “yyyy-MM-dd HH:mm:ss” 格式 
	 * @return
	 */
	public static String formatIntTime2StrTime(int time,String pattern){
		if(StringUtils.isBlank(pattern))pattern = "yyyy-MM-dd HH:mm:ss";
		long arg = time*1000l;
		Date date = new Date(arg);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(GamePropertiesCache.timeZone);
		return format.format(date);
	}
	
	/**
	 * 返回当前时间（不包括日期）时分秒换算为秒表示
	 * @name getTimeForDay
	 * @return 时分秒换算成秒的值
	 */
	public static int getTimeOfDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GamePropertiesCache.timeZone);
		calendar.setTime(new Date());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int times = hour*3600+minute*60+second;
		return times;
	}
	
	/**
	 * 返回当前日期
	 * @name getTimeForDay
	 * @return 日期换算成秒的值
	 */
	public static int getDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GamePropertiesCache.timeZone);
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, date, 0, 0, 0);
//		int times  = getCurrentTime()-getTimeOfDay();
		int times = (int)(calendar.getTimeInMillis()/1000);
		return times;
	}
	
	/**
	 * 获取当前日期的过去或未来的日期
	 * @param apartDays 1 日期向后推一天（明天）, -1 日期向前推一天（昨天）
	 * @return 日期换算成秒的值
	 */
	public static int getDayPassOrFuture(int apartDays,long currTime){
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTimeZone(GamePropertiesCache.timeZone);
		 calendar.setTimeInMillis(currTime);
		 calendar.add(Calendar.DATE,apartDays);//把日期往后增加一天.整数往后推,负数往前移动
		 int times =(int)(calendar.getTimeInMillis()/1000); //这个时间就是日期往后推一天的结果 
		return times;
	}
	
	/**
	  * 根据一个日期，返回是星期几的字符串
	  * 
	  * @param sdate
	  * @return
	  */
	 public static int getWeek() {
		  // 再转换为时间
		 Calendar c = Calendar.getInstance(GamePropertiesCache.timeZone);
		 if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
			 return c.get(Calendar.DAY_OF_WEEK);
		  }
		 return c.get(Calendar.DAY_OF_WEEK);
	 }
	 
	/**
	  * 根据一个日期，返回星期
	  * @return 周日 1~周六 7
	  */
	 public static int getLocalWeek() {
		  // 再转换为时间
		 Calendar c = Calendar.getInstance(GamePropertiesCache.timeZone);
		 return c.get(Calendar.DAY_OF_WEEK);
	 }
	 
	 /**
	  * 根据传入的："hh:MM:ss"格式的字符时间格式换算成秒数
	  * @param time
	  * @return 秒数返回
	  */
	 public static long getTimeByStrTime(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("hh:MM:ss");
		 long result = 0;
		try {
			Date date =  sf.parse(time);
			result = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	 }
	 
	 /**
	  * 根据传入的："hh:MM:ss"格式的字符时间格式换算成秒数
	  * @param time
	  * @return 秒数返回
	  */
	 public static int getTimeIntByStrTime(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("hh:MM:ss");
		 int result = 0;
		try {
			Date date =  sf.parse(time);
			result = (int)(date.getTime()/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	 }
	 
	 /**
	  * 根据传入的：pattern 格式的字符时间格式换算成秒数
	  * @param time 时间字符
	  * @param pattern 时间格式如："yyyy-MM-dd hh:mm:ss"
	  * @return 秒数返回  0 失败
	 * @throws Exception 
	  */
	 public static int getTimeIntByStrTime(String time,String pattern) throws Exception{
		 if(StringUtils.isBlank(pattern)||StringUtils.isBlank(time)) 
			 throw new Exception(" time or pattern paramter is validate");
		 SimpleDateFormat sf = new SimpleDateFormat(pattern);
		Date date = sf.parse(time);
		return (int)(date.getTime()/1000);
	 }
	 
	 /**
	  * 根据传入的："HH:mm:ss"格式的字符时间判断当前时间是否大于传入的时间
	  * @param time  格式："HH:mm:ss"
	  * @return boolean 大于 false 不大于
	  */
	 public static boolean isLargeTime(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date date1 = new Date();
			String dateTime = sf.format(date1);
			String times  = dateTime+" "+time;
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date =  sf.parse(times);
			return date1.getTime()>date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	 }
	 
	 /**
	  * 根据传入的："HH:mm:ss"格式的字符时间判断当前时间是否小于传入的时间
	  * @param time  格式："HH:mm:ss"
	  * @return boolean 大于 false 不大于
	  */
	 public static boolean isLessTime(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date date1 = new Date();
			String dateTime = sf.format(date1);
			String times  = dateTime+" "+time;
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date =  sf.parse(times);
			return date1.getTime()<date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	 }
	 
	 /**
	  * 根据传入的："HH:mm:ss"格式的字符时间判断当前时间是否大于等于传入的时间
	  * @param time  格式："HH:mm:ss"
	  * @return boolean 大于 false 不大于
	  */
	 public static boolean isLargeOrEqualTime(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date date1 = new Date();
			String dateTime = sf.format(date1);
			String times  = dateTime+" "+time;
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date =  sf.parse(times);
			return date1.getTime()>=date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	 }
	 
	 /**
	  * 当前时间和传进来的时候比较时间差
	  * @param ptime
	  * @return
	  */
	 public static long timeDifference(String ptime){
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date date1 = new Date();
			//当前时间年月日
			String currDateTime = sf.format(date1);
			
			String ptimes  = currDateTime+" "+ptime;
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date pdate =  sf.parse(ptimes);
			long time=pdate.getTime()-date1.getTime();
			if(time>0){
				return time;
			}else{
				return 0l;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;
	 }
	 
	 /**
	  * 判断当前时间是否在给定的时间段内
	  * @param start 开始时间 "HH:mm:ss"
	  * @param end 结束时间 "HH:mm:ss"
	  * @return
	  */
	public static boolean isBetweenTimes(String start, String end) {
		return !isLargeOrEqualTime(end)&&isLargeOrEqualTime(start);
	}
	
	/**
	 * 根据传入的："HH:mm:ss"时间
	 * 返回与当前时间的时间间隔
	 * @param time  "HH:mm:ss" 时间点
	 * @return time 小于当前时间 则为 负值反之则为 正值 
	 */
	public static int getTimeAndNowBetweenTimes(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date date1 = new Date();
			String dateTime = sf.format(date1);
			String times  = dateTime+" "+time;
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date =  sf.parse(times);
			return (int) ((date.getTime()-date1.getTime())/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return 0;
	}
	/**
	 * 比较两个时间是否是同一天
	 * @param compTime1
	 * @param compTime2
	 * @return
	 */
	public static boolean compareTimeForSameDay(int compTime1, int compTime2){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis((long)compTime1*1000);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTimeInMillis((long)compTime2*1000);
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);
		int day2 = calendar.get(Calendar.DAY_OF_MONTH);
		if(year==year2){
			if(month==month2){
				if(day==day2){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取传入时间中的（1年，2月，3日，4时，5分，6秒）其中一个数值
	 * @param type 1, 2, 3, 4, 5, 6
	 * @param time
	 */
	public static int getSingleValueOfTime(int time, int type){
		int value = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date((long)time*1000));
		switch (type) {
		case 1:
			value = calendar.get(Calendar.YEAR);
			break;

		case 2:
			value = calendar.get(Calendar.MONTH);
			break;
			
		case 3:
			value = calendar.get(Calendar.DATE);
			break;

		case 4:
			value = calendar.get(Calendar.HOUR_OF_DAY);
			break;

		case 5:
			value = calendar.get(Calendar.MINUTE);
			break;
			
		case 6:
			value = calendar.get(Calendar.SECOND);
			break;

		default:
			break;
		}
		return value;
	}
	
	/**
	 * 根据传入的："MM-dd"月日int类型时间 返回
	 * @param time  "MM-dd"格式 字符
	 * @return int 值时间
	 */
	public static int getTimeIntByDayStr(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		 try {
			String dateTime = sf.format(new Date());
			String times  = dateTime+"-"+time+" 00:00:00";
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date =  sf.parse(times);
			return (int) (date.getTime()/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return 0;
	}
	
	/**
	 * 获取两个时间差的天数
	 * @param small 小一点的时间
	 * @param big 大一点的时间
	 * @return 如果small比big要打 返回0
	 */
	public static int getDaysBetween(int small,int big) {   
		if (small>=big||small==0||big==0) {
			return 0;
		}
        int add = (big-small)%(24*60*60);
        int a = (big-small)/(24*60*60)+(add>0?1:0);
        return a;
	}
	
	/**
	 * 根据传入的格式取得当前的时间字符串
	 * @param formt 格式如："yyyy-MM-dd HH:mm:ss"/"yyyyMMddHHmm"
	 * @return 字符串格式日期时间值
	 */
	public static String getCurrentDateTimeStr(String formt){
		SimpleDateFormat sf = new SimpleDateFormat(formt);
		return sf.format(new Date());
	}
	/**
	 * 根据传入的："yyyy-MM-dd HH:mm:ss"时间转换成时间戳
	 * @param time  "yyyy-MM-dd HH:mm:ss"格式的时间
	 * @return time 对应的时间戳
	 */
	public static int getTimeIntByStr(String time){
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 try {
			Date date =  sf.parse(time);
			return (int) (date.getTime()/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return 0;
	}
	/*
	 * 检查两个日期相差几天
	 */
	public static int delta_T_Days(Date fDate, Date oDate) {
		if (null == fDate || null == oDate) {
			return -1;
		}
		long intervalMilli = oDate.getTime()-fDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}
	
	/**
	 * 根据传入的时间(秒)，对比当前时间，是否进行重置；只对周一重置的情景有效
	 * @param time
	 * @return
	 */
	public static boolean isSameWeekWithToday(int time){
		int weekTime = 3600*24*7;
		if((TimeDateUtil.getCurrentTime()-time)>weekTime){		//如果时间间距大于一周；必定重置
			return true;
		}
		long dateTime = (long)time*1000;
		long currentTime = System.currentTimeMillis();
		
		Date date = new Date(dateTime);
		Calendar dataCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		dataCal.setTime(date);
		currentCal.setTime(new Date(currentTime));
		
		int nowDay = currentCal.get(Calendar.DAY_OF_WEEK)-1;	
		int passDay = dataCal.get(Calendar.DAY_OF_WEEK)-1;
		if(nowDay==0){
			nowDay = 7;
		}
		if(passDay==0){
			passDay = 7;
		}
		if(nowDay<passDay){
            return true;
        }else{
            return false;
        }
	}
	
	/**
	 * 获取当前时间周一0点1分时间
	 * @return
	 */
	public static int getMondayTime(){
		Calendar dataCal = Calendar.getInstance(GamePropertiesCache.timeZone);
		dataCal.setFirstDayOfWeek(Calendar.MONDAY);
		dataCal.set(Calendar.DAY_OF_WEEK, 2);
		dataCal.set(Calendar.HOUR_OF_DAY, 0);
		dataCal.set(Calendar.MINUTE, 1);
		dataCal.set(Calendar.SECOND, 0);
		return (int) (dataCal.getTimeInMillis()/1000);
	}
	
	/**
	 * 判断传入的时间是否小于本周周一零点1分
	 * @param time
	 * @return 小于 true  大于等于  false
	 */
	public static boolean isLessMondayZeroOneMinutine(int time){
		return getMondayTime()>=time;
	}
	
	/**
	 * 获取两个时间是否为同一个小时
	 * @param time1
	 * @param time2
	 * @return true是一个小时
	 */
	public static boolean isSameHour(int time1,int time2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(time1*1000l);
		c2.setTimeInMillis(time2*1000l);
		c1.setTimeZone(GamePropertiesCache.timeZone);
		c2.setTimeZone(GamePropertiesCache.timeZone);
		return c1.get(Calendar.HOUR_OF_DAY)==c2.get(Calendar.HOUR_OF_DAY);  
		
	}
	/**
	 * 现在是否处于重置时间
	 * @return true为是
	 */
	public static boolean isNowZeroHandleTime(){
		int currTime = TimeDateUtil.getCurrTimeWithZeroSec();
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(currTime*1000l);
		c1.setTimeZone(GamePropertiesCache.timeZone);
		int hour = c1.get(Calendar.HOUR_OF_DAY);
		int minute = c1.get(Calendar.MINUTE);
		try {
			if((hour==23 && minute>50) || (hour==0 && minute<10)){
				log.info("execute suspend,now:"+TimeDateUtil.printOnlyTime(currTime));
				return true;
			}
		} catch (Exception e1) {
			log.error(" isNowZeroHandleTime exception:",e1);
			return false;
		}
		return false;
	}
	
	// 获取当月第一天  
	public String getFirstDayOfMonth() {  
	    String str = "";  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	  
	    Calendar lastDate = Calendar.getInstance();  
	    lastDate.set(Calendar.DATE, 1);// 设为当前月的1号  
	    str = sdf.format(lastDate.getTime());  
	    return str;  
	}
	
	// 获得当前日期与本周日相差的天数  
	private static int getMondayPlus() {  
	    Calendar cd = Calendar.getInstance();  
	    // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......  
	    int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1  
	    if (dayOfWeek == 0) {  
	        return -6;  
	    }  
	    if (dayOfWeek == 1) {  
	        return 0;  
	    } else {  
	        return 1 - dayOfWeek;  
	   }  
	}

	// 根据当前时间获得下周星期一的日期  
	public static String getNextMonday() {  
	    int mondayPlus = getMondayPlus();  
	    GregorianCalendar currentDate = new GregorianCalendar();  
	    currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);  
	    Date monday = currentDate.getTime();  
	    DateFormat df = DateFormat.getDateInstance();  
	    String preMonday = df.format(monday);  
	    return preMonday;  
	} 
	
	public static int getEightClock(){
		Calendar calendar = Calendar.getInstance(GamePropertiesCache.timeZone);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (int) (calendar.getTimeInMillis()/1000);
	}
	
	/**
	 * 获取当前时间周一0点0分时间
	 * @return
	 */
	public static int getMondayZeroTime(){
		Calendar dataCal = Calendar.getInstance(GamePropertiesCache.timeZone);
		dataCal.setFirstDayOfWeek(Calendar.MONDAY);
		dataCal.set(Calendar.DAY_OF_WEEK, 2);
		dataCal.set(Calendar.HOUR_OF_DAY, 0);
		dataCal.set(Calendar.MINUTE, 0);
		dataCal.set(Calendar.SECOND, 0);
		return (int) (dataCal.getTimeInMillis()/1000);
	}
	
	/**
	 * 判断传入的时间是否小于本周周一零点1分
	 * @param time
	 * @return 小于 true  大于等于  false
	 */
	public static boolean isLessMondayZeroMinutine(int time){
		return getMondayZeroTime()>time;
	}
	
	/**
	 * 判断传入的时间是否大于周一的零点零分的时间
	 * @param time 时间
	 * @return
	 */
	public static boolean isAfterMonDayZeroTime(int time){
		boolean result = false;
		//获取下周一零点的时间
		//将时间转为Date,
		Calendar instance = Calendar.getInstance(GamePropertiesCache.timeZone);
		instance.setFirstDayOfWeek(Calendar.MONDAY);
		instance.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		int timeInMillis = (int) (instance.getTimeInMillis()/1000);
//		System.err.println(timeInMillis);
		if(timeInMillis>time){
			result = true;
		}
		return result ;
	}

	/**
	 * 获取当前时间的时间（秒）
	 * @param times
	 * @author Lobbyer 
	 * @date 2014-11-7 上午10:19:09
	 */
	public static int getTimeSecond(String times) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = new Date();
			//当前时间年月日
			String currDateTime = sf.format(date1);
			
			String ptimes  = currDateTime+" "+times;
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date pdate =  sf.parse(ptimes);
			int time=(int) (pdate.getTime()/1000);
			return time;
		} catch (Exception e) {
		}
		return 0;
	}
	
	public static int getNextFewDayTime(int days){
		return getCurrentTime() + ONE_DAY_INT*days;
	}
	/**
	 *判断当前时间是否在传入的：startTime，endTime时间之间
	 *是 true  否 false
	 */
	public static boolean isBetweenTimes(int startTime,int endTime){
		int currentTime = getCurrentTime();
		return (startTime<=currentTime&&endTime>=currentTime);
	}
	
	/**
	 * 计算传入的时间之间的秒数差
	 * 如果插入的end为空则与当前时间比
	 * @name caclBetweenTimes
	 * @param start 开始时间存储以秒单位的时间 >=0 
	 * @param end 结束时间以秒为单位的时间  >=0
	 * @return int 返回相差的秒数
	 * @author yxh
	 */
	public static int caclBetweenTimes(int start,int end){
		int result = 0;
		if(start<0||end <0)return result;
		if(end==0){
			end = (int)Math.ceil(((double)System.currentTimeMillis()/(double)1000));
		}
		//开始时间不应该大于结束时间
		if(start>end)return result;
		result = end-start;
		return result;
	}
	
	public static void main(String[] args) {
//		long t = TimeDateUtil.timeDifference("14:50:00");
//		System.out.println(t+" "+(t/1000));
//		
		System.out.println(printTime(1425312060));
		
//		System.out.println(getTimeIntByStr("2015-01-12 00:01:55"));
	}
	
}