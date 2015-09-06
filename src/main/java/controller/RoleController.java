package controller;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dao.pojo.role.Role;
import dao.pojo.role.RoleDao;
import dao.utils.BeanUtils;

@RestController
@EnableAutoConfiguration
//@EnableAutoConfiguration注解用来自动配置，我们pom中配置了    spring-boot-starter-web所以spring会来创建一 个web应用来配置程序
public class RoleController {
	
    private RoleDao roleDao=BeanUtils.getBean(RoleDao.class);

	@RequestMapping(value="/")
	String home(){
	   return "helloWorld ddd";  
    }
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RoleController.class, args);
    }
    
    @RequestMapping(value="/greeting", method = RequestMethod.GET)
    public Role greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	System.out.println(roleDao==null);
    	Role role = null;
		try {
			role = roleDao.getById(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return role;
    }
}
