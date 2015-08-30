package dao.pojo.alter;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Component
public class DDLDao {
	private DDLDao(){}
	
	private ComboPooledDataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate=new JdbcTemplate(dataSource);
	}

	public void createTable(String sql) throws SQLException{
		this.jdbcTemplate.update("create table " +sql);
	}
	
	public List<FieldStru> descTable(String tableName){
		try {
			QueryRunner qr=new QueryRunner(dataSource);
			return qr.query("desc "+tableName, new BeanListHandler<>(FieldStru.class));
		} catch (SQLException e) {
			return null;
		}
	}
	
	public void addColumn(AlterColumn alterTable) throws SQLException{
		String sql=" alter table "+alterTable.getTbname()+" add column "+alterTable.getField()+" "+alterTable.getType()+" "+alterTable.getIsNull()+" "+alterTable.getDefVal()+" COMMENT '"+alterTable.getComment()+"'";
		this.jdbcTemplate.update(sql);
	}
	
	public void modifyColumn(AlterColumn alterTable) throws SQLException{
		String sql=" alter table "+alterTable.getTbname()+" modify column "+alterTable.getField()+" "+alterTable.getType()+" "+alterTable.getIsNull()+" "+alterTable.getDefVal()+" COMMENT '"+alterTable.getComment()+"'";
		this.jdbcTemplate.update(sql);
	}
	
	public void dropColumn(String tbname,String fieldName) throws SQLException{
		String sql="ALTER TABLE "+tbname+" DROP COLUMN "+fieldName;
		this.jdbcTemplate.update(sql);
	}
	
	public List<KeyStru> showKeys(String tbname) throws SQLException{
		String sql="show keys from "+tbname;
		QueryRunner qr=new QueryRunner(dataSource);
		return qr.query(sql, new BeanListHandler<>(KeyStru.class));
	}
	
	public void modifyIndex(String sql) throws SQLException{
		String exesql="alter table "+sql+" USING BTREE";
		this.jdbcTemplate.update(exesql);
	}
	
	public void dropIndex(String tbname,String keyname) throws SQLException{
		String exesql="alter table "+tbname+" DROP INDEX "+keyname;
		this.jdbcTemplate.update(exesql);
	}
}
