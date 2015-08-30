package dao.pojo.role;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dao.pojo.BaseDaoImpl;
import dao.pojo.StaticConst;

@Service
public class RoleDao extends BaseDaoImpl<Role>{
	private static final Logger logger=LoggerFactory.getLogger(RoleDao.class);
	
	private static final String INSERT_SQL="INSERT INTO role(id,name,createTime)VALUES(NULL,?,?);";
	
	/**
	 * 批量保存 纯JDBC
	 * @param roleList
	 */
	public void saveBatch(List<Role>roleList) {
		Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection= dataSource.getConnection();
			connection.setAutoCommit(false);
		    statement=connection.prepareStatement(INSERT_SQL);
			for(int i=0;i<roleList.size();i++){
				Role r=roleList.get(i);
				statement.setString(1, r.getName());
				statement.setInt(2, r.getCreateTime());
				statement.addBatch();
				if(i>0&&i%StaticConst.BATCH_SIZE==0){
					statement.executeBatch();
					connection.commit();
				}
			}
			int[] updateCounts =statement.executeBatch();
			connection.commit();
			roleList.clear();
			logger.debug("扑街了="+updateCounts.length);
		} catch (Exception e) {
			logger.error("batchSave error:",e);
		}finally{
			releaseResouce(null,statement,connection);
		}
	}
	
	/**
	 * 释放资源
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	private void releaseResouce(ResultSet resultSet, Statement statement,Connection connection){
		try {
			if (resultSet != null) {
	             resultSet.close();
	        }
	        if (statement != null) {
	             statement.close();
	        }
	        if (connection != null) {
	              connection.close();
	        }
		} catch (Exception e) {
			logger.error("releaseResouce error:",e);
		}
	}
	
	
	/**
	 * 批量保存
	 * @param roleList
	 */
	public void saveBatchD(List<Role>roleList){
		try {
		    Connection	connection= dataSource.getConnection();
			connection.setAutoCommit(false);
			Object params[][] = new Object[roleList.size()][];
			Role r=null;
			for (int i = 0; i < roleList.size(); i++) {
				r=roleList.get(i);
				params[i] = new Object[] { r.getName(),r.getCreateTime() };
			}
			getQueryRunner().batch(connection,INSERT_SQL, params);
			connection.commit();
			connection.close();
		} catch (Exception e) {
		   logger.error("saveBatchD error:",e);
		}
	}

}
