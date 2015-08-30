package dao.pojo.role;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dao.cache.PoJoCache;
import dao.pojo.annotation.Table;
import dao.utils.DBUtils;
import dao.utils.PackageUtils;

public class JdbcTemplateTest {

	 public static void main( String[] args ){
		 Set<Class<?>> allClass = PackageUtils.getClasses("simple.pojo.role");  
		 for (Class clas :allClass) {  
			 Annotation ab= clas.getAnnotation(Table.class);
			 if(ab!=null){
				 List<Field>tempList=new ArrayList<Field>();
				 getFilds(tempList,clas);
				 ArrayList<Field>fList=new ArrayList<Field>();
				 for(Field f:tempList){
					 System.out.println(f.getName());
					 fList.add(f);
				 }
				 PoJoCache.getPojoMethodNameMap().put(clas.getName(), fList);
			 }
		 }  
		 Role role =new Role();
		 role.setId(33l);
		 role.setCreateTime(333332222);
		 role.setName("打我啊");
		 
		 System.out.println("====================================================================");
		 String tableName =DBUtils.getTableName(role.getClass());
		 StringBuilder sb=new StringBuilder("INSERT　INTO "+tableName+" (");
		 List<Field>fList= PoJoCache.getPojoMethodNameMap().get(role.getClass().getName());
		 List<Object>param=new ArrayList<Object>(fList.size());
		 StringBuilder parSb=new StringBuilder();
		 for(Field f:fList){
			try {
				sb.append(f.getName()+",");
				parSb.append("?,");
				f.setAccessible(true);
				param.add(f.get(role));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		sb=sb.replace(sb.length()-1, sb.length(), "");
		parSb.replace(parSb.length()-1, parSb.length(), "");
		sb.append(")VALUES(");
		sb.append(parSb.toString());
		sb.append(");");
		
		System.out.println(sb);
	     
	 }
	 
	 
	 private static void getFilds(List<Field>list,Class<?>clas){
		  Class<?>abs=clas.getSuperclass();
		  if(abs!=null){
			  getFilds(list,abs);
		  }
		  Field[]fs= clas.getDeclaredFields();
		  for(Field f:fs){
			  list.add(f);
		  }
	 }
	 
	

}
