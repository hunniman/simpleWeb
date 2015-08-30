package dao.utils;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.StringUtils;

import dao.pojo.annotation.Table;

public class DBUtils {
	
	/**
	 * 获取表名字
	 * @param c
	 * @return
	 */
	public static String getTableName(Class<?> c){
		Annotation at= c.getAnnotation(Table.class);
		if(c.getAnnotation(Table.class)==null)
			return null;
		
		Table tb=(Table)at;
		if(StringUtils.isBlank(tb.name())){
			String[]className=c.getName().split("\\.");
			return className[className.length-1];
		}else{
			return tb.name();
		}
	}
}
