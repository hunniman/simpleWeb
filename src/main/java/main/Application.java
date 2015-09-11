package main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import controller.RoleController;

import dao.cache.GamePropertiesCache;
import dao.pojo.DDLUtil;
import dao.utils.BeanUtils;
import dao.utils.PropertiesTools;

@SpringBootApplication
public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Logger logger = LoggerFactory.getLogger(Application.class);
		// org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean
//		ApplicationContext content = new FileSystemXmlApplicationContext(Application.class.getResource("/").getFile()+"applicationContext.xml");
		try {
			BeanUtils.initSpring();
			PropertiesTools.initPropreties(Application.class.getResource("/").getFile());
			GamePropertiesCache.initGameProperties();
			logger.debug(GamePropertiesCache.globalIp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DDLUtil ddLUtil = BeanUtils.getBean(DDLUtil.class);
		// RoleDao roleDao=content.getBean(RoleDao.class);
		try {
			 SpringApplication.run(RoleController.class, args);
			 ddLUtil.initTableModel();
			 logger.debug("初始化表完成！");
			// Role role =new Role();
			// role.setId(604144);
			// role.setName("我就思考减肥了快速将拉伸件地方拉萨看见对方蓝色的空间放拉斯看得见法律思考发");
			// role.setCreateTime((int)System.currentTimeMillis()/1000);
			// long id=roleDao.update(role);
			// logger.debug("保存成功，dddd");
		} catch (Exception e) {
			// // TODO Auto-generated catch block
			e.printStackTrace();
		}
		// roleDao.getAll();
		// List<Role>roleList=new ArrayList<Role>();
		//
		// for(int i=0;i<10000;i++){
		// Role role =new Role();
		// role.setName("Role_"+i);
		// role.setCreateTime((int)System.currentTimeMillis()/1000);
		// roleList.add(role);
		// }
		//
		//
		//
		// try {
		// long beginTime=System.currentTimeMillis();
		//
		// // roleDao.saveBatchD(roleList);
		// //
		// // Role role=new Role();
		// // role.setCreateTime((int)System.currentTimeMillis()/1000);
		// // role.setName("jjj");
		// // roleDao.saveOne(role);
		// String sql="test(id int ,name varchar (20))";
		// roleDao.createTable(sql);
		//
		// long endTime=System.currentTimeMillis();
		//
		// System.out.println("总共耗时="+(endTime-beginTime)/1000);
		// // roleDao.save(role);
		// // String[]pidArray={"10000012","10000013"};
		// // roleDao.deleteLogicByIds(pidArray);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
