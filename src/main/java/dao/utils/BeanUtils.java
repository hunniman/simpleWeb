package dao.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BeanUtils {

	private static ApplicationContext content;

	public static <E> E  getBean (Class<E> E){
	     return content.getBean(E);
	}
	
	public static Object getBean(String name){
		return content.getBean(name);
	}
	
	public static void initSpring() throws Exception{
		try {
			content=new ClassPathXmlApplicationContext("applicationContext.xml");
		} catch (Exception e) {
			throw new Exception("initSpring");
		}
	}
}
