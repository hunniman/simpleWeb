package dao.pojo;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import dao.cache.PoJoCache;
import dao.pojo.annotation.Index;
import dao.pojo.annotation.IndexEnum;
import dao.utils.DBUtils;
import dao.utils.GenericUtils;


public class BaseDaoImpl <E>{

	private Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);
	
	protected ComboPooledDataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	
	protected QueryRunner qr;
	
	protected Class<E> clazz;
	
//	protected SimpleJdbcInsert insertPojo;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		try {
			clazz = GenericUtils.getActualClass(this.getClass(), 0);
		} catch (Exception e) {
			logger.error("base dao can not get  clazz!", e);
		}
	}
	
	protected QueryRunner getQueryRunner(){
		return new QueryRunner(dataSource);
	}
	
	@Autowired
    public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate=new JdbcTemplate(dataSource);
//		this.insertPojo=new SimpleJdbcInsert(dataSource).withTableName(DBUtils.getTableName(clazz)).usingGeneratedKeyColumns("id");
	}

	/**
	 * 保存单个
	 * @param pojo
	 * @return
	 * @throws SQLException
	 */
	public long save(E pojo) throws SQLException {
	   List<Object>paraList=new ArrayList<Object>();
	   String sql=generateSaveSql(pojo, paraList);
	   QueryRunner qr=getQueryRunner();
	   Object[]realPara=new Object[paraList.size()];
	   paraList.toArray(realPara);
	   logger.debug(sql);
	   Map<String, Object> result=qr.insert(sql, new MapHandler(), realPara);
       long id=(long)result.get("GENERATED_KEY");
       return id;
	}

	/**
	 * 修改全部
	 * @param pojo
	 * @return
	 * @throws SQLException
	 */
	public int update(E pojo) throws SQLException{
	   List<Object>paraList=new ArrayList<Object>();
	   String sql=generateUpdateSql(pojo, paraList);
	   logger.debug(sql);
	   QueryRunner qr=getQueryRunner();
	   Object[]realPara=new Object[paraList.size()];
	   paraList.toArray(realPara);
	   int result=qr.update(sql,realPara);
	   return result;
	}
	

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	public E getById(int id) throws SQLException{
		String tableName=DBUtils.getTableName(clazz);
		String sql="SELECT * FROM "+tableName +" WHERE id=?";
		QueryRunner qr=getQueryRunner();
		return qr.query(sql, new Object[]{id},new BeanHandler<E>(clazz));
	}
	
	/**
	 * 获取全部
	 * @return
	 * @throws SQLException
	 */
	public List<E>getAll() throws SQLException{
		String tableName=DBUtils.getTableName(clazz);
		String sql="SELECT * FROM "+tableName +" ORDER BY id=?";
		QueryRunner qr=getQueryRunner();
		return qr.query(sql, new BeanListHandler<E>(clazz));
	}
	
	
	/**
	 * 生成保存的sql  INSERT　ＩＮＴＯ
	 * @param pojo
	 * @param paraList
	 * @return
	 */
	private String generateSaveSql(E pojo,List<Object>paraList){
		try {
			String tableName =DBUtils.getTableName(clazz);
			StringBuilder sb=new StringBuilder("INSERT INTO "+tableName+" (");
			List<Field>fList= PoJoCache.getPojoMethodNameMap().get(clazz.getName());
			StringBuilder parSb=new StringBuilder();
			for(Field f:fList){
				if(f.getAnnotation(Index.class)!=null){
					Index indexAnnotion=(Index)f.getAnnotation(Index.class);
					if(indexAnnotion.key()==IndexEnum.PRIMARY)continue;
				}
				sb.append(f.getName()+",");
				parSb.append("?,");
				f.setAccessible(true);
				paraList.add(f.get(pojo));
			}
			sb=sb.replace(sb.length()-1, sb.length(), "");
			parSb.replace(parSb.length()-1, parSb.length(), "");
			sb.append(")VALUES(");
			sb.append(parSb.toString());
			sb.append(");");
			return sb.toString();
		} catch (Exception e) {
			logger.error("generateSaveSql error:",clazz.getName());
			return null;
		}
	}
	
	
	private String generateUpdateSql(E pojo,List<Object>paraList){
		try {
			String tableName =DBUtils.getTableName(clazz);
			StringBuilder sb=new StringBuilder("UPDATE "+tableName+" SET ");
			List<Field>fList= PoJoCache.getPojoMethodNameMap().get(clazz.getName());
			long id=0;
			for(Field f:fList){
				f.setAccessible(true);
				if(f.getAnnotation(Index.class)!=null){
					Index indexAnnotion=(Index)f.getAnnotation(Index.class);
					if(indexAnnotion.key()==IndexEnum.PRIMARY){
						id=(long) f.get(pojo);
						continue;
					}
				}
				sb.append(f.getName());
				sb.append("=?,");
				paraList.add(f.get(pojo));
			}
			paraList.add(id);
			sb=sb.replace(sb.length()-1, sb.length(), "");
			sb.append(" WHERE id=?");
			return sb.toString();
		} catch (Exception e) {
			logger.error("generateUpdateSql error:",clazz.getName());
			return null;
		}
	}
}
