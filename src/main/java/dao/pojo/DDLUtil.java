package dao.pojo;



import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dao.cache.GamePropertiesCache;
import dao.cache.PoJoCache;
import dao.pojo.alter.AlterColumn;
import dao.pojo.alter.DDLDao;
import dao.pojo.alter.FieldStru;
import dao.pojo.alter.KeyStru;
import dao.pojo.annotation.ColumnBigInt;
import dao.pojo.annotation.ColumnFloat;
import dao.pojo.annotation.ColumnInt;
import dao.pojo.annotation.ColumnSmallInt;
import dao.pojo.annotation.ColumnText;
import dao.pojo.annotation.ColumnTinyInt;
import dao.pojo.annotation.ColumnVar;
import dao.pojo.annotation.Index;
import dao.pojo.annotation.IndexEnum;
import dao.pojo.annotation.Table;
import dao.utils.DBUtils;
import dao.utils.PackageUtils;

/**
 * 初始化系统表结构类：
 * 统计表(m_开头的表)只创建不修改
 * @author yxh
 *
 */
public class DDLUtil {
	private static final Logger logger = LoggerFactory.getLogger(DDLUtil.class);
	private  DDLDao ddlDao ;
	private static final String BL = " ";
	private static final String QU = ",";
	
	@Autowired
	public void setDdlDao(DDLDao ddlDao) {
		this.ddlDao = ddlDao;
	}

	private  void createTable(String tbname, List<AlterColumn> alterTableList) throws Exception {
		if(GamePropertiesCache.autoId<1000000000000000l){
			GamePropertiesCache.initAutoId();
		}
		if(GamePropertiesCache.autoId<1000000000000000l)
			throw new Exception("autoId is rong autoId="+GamePropertiesCache.autoId);
		StringBuilder sb = new StringBuilder();
		sb.append(tbname).append(BL);
		sb.append("(");
		String mtriKey = " PRIMARY KEY (";
		boolean needMtri = false;
		long autoId = -1;
		for (AlterColumn col : alterTableList) {
			IndexEnum key = col.getKey();
			sb.append(col.getField()).append(BL).append(col.getType()).append(BL).append(col.getIsNull()).append(BL);
			if (key == null || key != IndexEnum.PRIMARY) {
				sb.append(" ").append(col.getDefVal()).append(" ").append(BL);
			}
			sb.append("COMMENT \'").append(col.getComment()).append("\'");
			if (key != null) {
				if (key == IndexEnum.PRIMARY) {
					//将领表跟其他表起始id不设一样
//					autoId = StringUtils.equalsIgnoreCase(tbname,"general")?GamePropertiesCache.generalAutoId:
//						GamePropertiesCache.autoId;
					autoId=1;
					sb.append(BL).append("AUTO_INCREMENT").append(QU);
					sb.append("PRIMARY KEY").append(BL).append("(").append(col.getField()).append(")").append(QU);
				} else if (key == IndexEnum.NORMAL) {
					sb.append(QU).append("KEY").append(BL).append(col.getKeyName()).append(BL).append("(").append(col.getField()).append(")").append(QU);
				} else if (key == IndexEnum.UNIQUE) {
					sb.append(QU).append("UNIQUE KEY").append(BL).append(col.getKeyName()).append(BL).append("(").append(col.getField()).append(")").append(QU);
				} else if(key == IndexEnum.DPRIMARY){
					needMtri = true;
					sb.append(QU);
					mtriKey+=col.getField()+",";
				}else if(key == IndexEnum.PRI){//不设置自启值的id
					sb.append(QU).append("PRIMARY KEY").append(BL).append("(").append(col.getField()).append(")").append(QU);
				}
			} else {
				sb.append(QU);
			}
		}
		mtriKey = mtriKey.substring(0, mtriKey.length()-1)+")";
		String sql = sb.substring(0, sb.length() - 1);
		if(needMtri)sql+=","+mtriKey;
		sql = sql + (")ENGINE=InnoDB "+(autoId>=0?"AUTO_INCREMENT="+autoId+"":"")+" DEFAULT CHARSET=utf8");
		ddlDao.createTable(sql);
	}

	public  void main(String[] args) throws Exception {
		initTableModel();
	}

	public   void initTableModel() throws Exception {
		Map<String, Class<?>> resMap = new HashMap<String, Class<?>>();
		 Set<Class<?>> allClass = PackageUtils.getClasses("dao.pojo");  
		 for (Class<?> clas :allClass) {  
			 Annotation ab= clas.getAnnotation(Table.class);
			 if(ab==null)continue;
             String tableName=DBUtils.getTableName(clas);
             resMap.put(tableName, clas);
		 }  
		checkField(resMap);
		checkIndex(resMap);
	}

	private  void checkField(Map<String, Class<?>> resMap) throws Exception {
		Iterator<Entry<String,  Class<?>>> resIt = resMap.entrySet().iterator();
		while (resIt.hasNext()) {
			Entry<String, Class<?>> entry = resIt.next();
			String tbname = entry.getKey();
			Class<?> resClass = entry.getValue();
			List<FieldStru> descTable = ddlDao.descTable(tbname);
			List<AlterColumn> alterTableList = getAlterTableList(resClass, tbname);
			if (descTable == null) {
				// 新表
				createTable(tbname, alterTableList);
			}else{//if(StringUtils.indexOf(tbname,"m_")<0)
				Map<String, FieldStru> fieldMap = new HashMap<String, FieldStru>();
				for (FieldStru sqlStru : descTable) {
					fieldMap.put(sqlStru.getField(), sqlStru);
				}
				// 表字段和类字段对比
				for (AlterColumn alterColumn : alterTableList) {
					compareField(alterColumn, fieldMap);
				}
				if (fieldMap.size() > 0) {
					// 删除字段
					Iterator<Entry<String, FieldStru>> fieldIt = fieldMap.entrySet().iterator();
					while (fieldIt.hasNext()) {
						Entry<String, FieldStru> fieldEntry = fieldIt.next();
						String key = fieldEntry.getKey();
						ddlDao.dropColumn(tbname, key);
					}
				}
			}
		}
	}

	// 处理字段
	public  void compareField(AlterColumn col, Map<String, FieldStru> fieldMap) throws Exception {
		FieldStru sqlStru = fieldMap.remove(col.getField());
		if(sqlStru==null)sqlStru = fieldMap.remove(col.getField().toLowerCase());
		if (sqlStru == null) {
			// 新增字段
			ddlDao.addColumn(col);
		} else {
			boolean typeSame = sqlStru.getType().equals(col.getType());
			boolean nullSame = sqlStru.getNull().equals(col.isNullCompare());
			boolean defSame = true;
			if(col.getKey()==null){
				if (StringUtils.isBlank(sqlStru.getDefault())) {
					if (!StringUtils.isBlank(col.getDefValEqauls())) {
						defSame = false;
					}
				}else{
					defSame = defSame && col.getDefValEqauls().equals(sqlStru.getDefault());
				}
			}
			if (typeSame && nullSame && defSame) {

			} else {
				ddlDao.modifyColumn(col);
			}
		}
	}

	private  void checkIndex(Map<String, Class<?>> resMap) throws Exception {
		Iterator<Entry<String, Class<?>>> resIt = resMap.entrySet().iterator();
		while (resIt.hasNext()) {
			Entry<String, Class<?>> entry = resIt.next();
			String tbname = entry.getKey();
			Class<?> resClass = entry.getValue();
			List<KeyStru> showKeys = ddlDao.showKeys(tbname);
			List<AlterColumn> alterTableList = getAlterTableList(resClass, tbname);
			if (showKeys != null) {
				Map<String, KeyStru> keysMap = new HashMap<String, KeyStru>();
				for (KeyStru keyStru : showKeys) {
					keysMap.put(keyStru.getColumn_name(), keyStru);
				}
				for (AlterColumn alterColumn : alterTableList) {
//					if(!HeFuFunction.checkIndex(alterColumn)){
//						continue;
//					}
					compareIndex(alterColumn, keysMap);
				}
				if (keysMap.size() > 0) {
					// 删除索引
					Iterator<Entry<String, KeyStru>> keyIt = keysMap.entrySet().iterator();
					while (keyIt.hasNext()) {
						Entry<String, KeyStru> keyEntry = keyIt.next();
						String key_name = keyEntry.getValue().getKey_name();
						ddlDao.dropIndex(tbname, key_name);
					}
				}
			}
		}
	}

	// 处理索引
	public  void compareIndex(AlterColumn col, Map<String, KeyStru> keysMap) throws Exception {
		IndexEnum key = col.getKey();
		if (key != null) {
			// 有key
			KeyStru keyStru = keysMap.get(col.getField());
			if (keyStru == null) {
				// 新增key
				String addKey = getAddIndex(key, col);
				addKey = col.getTbname() + " " + addKey;
				try {
					ddlDao.modifyIndex(addKey);
				} catch (Exception e) {
					logger.error("",e);
				}
			} else {
				// 对比
				if ((key == IndexEnum.NORMAL && keyStru.getNon_unique() == 0) || key == IndexEnum.UNIQUE && keyStru.getNon_unique() == 1) {
					String addKey = getAddIndex(key, col);
					addKey = col.getTbname() + " DROP INDEX " + keyStru.getKey_name() + "," + addKey;
					try {
						ddlDao.modifyIndex(addKey);
					} catch (Exception e) {
						logger.error("",e);
					}
				}
				keysMap.remove(col.getField());
			}
		}
	}
	private  String getAddIndex(IndexEnum key, AlterColumn col) {
		StringBuilder sb = new StringBuilder();
		sb.append("ADD ");
		if (key == IndexEnum.UNIQUE) {
			// 暂时不支持primary key
			sb.append("UNIQUE ");
		}
		sb.append("INDEX ");
		sb.append(col.getKeyName()).append(BL).append("(").append(col.getField()).append(")");
		return sb.toString();
	}
	private  List<AlterColumn> getAlterTableList(Class<?> resClass, String tbname) throws Exception {
		List<AlterColumn> list = new ArrayList<AlterColumn>();
		List<Field>declaredFields=new ArrayList<Field>();
		getFilds(declaredFields, resClass);
		ArrayList<Field>fList=new ArrayList<Field>();//缓存File
		PoJoCache.getPojoMethodNameMap().put(resClass.getName(), fList);
		for (Field field : declaredFields) {
			fList.add(field);
			Annotation[] annotations = field.getAnnotations();
			String fieldName = null;
			String fieldType = "";
			int len = 0;
			int dec = 0;//精度
			boolean isNull = false;
			String defVal = null;
			String comment = "";
			IndexEnum key = null;
			String keyName = null;
			for (Annotation annotation : annotations) {
				if (annotation instanceof Index) {
					Index index = (Index) annotation;
					String indexName = index.name();
					if ("".equals(indexName)) {
						keyName = "i_" + field.getName();
					} else {
						keyName = indexName;
					}
					key = index.key();
				} else {
					if (annotation instanceof ColumnBigInt) {
						// bigint
						ColumnBigInt bigint = (ColumnBigInt) annotation;
						fieldName = bigint.field();
						if ("".equals(fieldName)) {
							fieldName = field.getName();
						}
						fieldType = "bigint";
						len = bigint.len();
						defVal = bigint.defVal()+"";
						comment = bigint.comment();
						isNull = bigint.isNull();
					} else if (annotation instanceof ColumnInt) {
						// int
						ColumnInt colInt = (ColumnInt) annotation;
						fieldName = colInt.field();
						if ("".equals(fieldName)) {
							fieldName = field.getName();
						}
						fieldType = "int";
						len = colInt.len();
						defVal = colInt.defVal()+"";
						comment = colInt.comment();
						isNull = colInt.isNull();
					} else if (annotation instanceof ColumnTinyInt) {
						// tinyint
						ColumnTinyInt tinyInt = (ColumnTinyInt) annotation;
						fieldName = tinyInt.field();
						if ("".equals(fieldName)) {
							fieldName = field.getName();
						}
						fieldType = "tinyint";
						len = tinyInt.len();
						defVal = tinyInt.defVal()+"";
						comment = tinyInt.comment();
						isNull = tinyInt.isNull();
					} else if (annotation instanceof ColumnSmallInt) {
						// smallint
						ColumnSmallInt tinyInt = (ColumnSmallInt) annotation;
						fieldName = tinyInt.field();
						if ("".equals(fieldName)) {
							fieldName = field.getName();
						}
						fieldType = "smallint";
						len = tinyInt.len();
						defVal = tinyInt.defVal()+"";
						comment = tinyInt.comment();
						isNull = tinyInt.isNull();
					} else if (annotation instanceof ColumnVar) {
						// varchar
						ColumnVar varchar = (ColumnVar) annotation;
						fieldName = varchar.field();
						if ("".equals(fieldName)) {
							fieldName = field.getName();
						}
						fieldType = "varchar";
						len = varchar.len();
						defVal = varchar.defVal();
						comment = varchar.comment();
						isNull = varchar.isNull();
					} else if (annotation instanceof ColumnText) {
						// text
						ColumnText colText = (ColumnText) annotation;
						fieldName = colText.field();
						if ("".equals(fieldName)) {
							fieldName = field.getName();
						}
						fieldType = "text";
						comment = colText.comment();
						defVal = "";
						isNull = colText.isNull();
					}else if(annotation instanceof ColumnFloat){
						//float
						ColumnFloat cfloat = (ColumnFloat) annotation;
						fieldName = cfloat.field();
						if ("".equals(fieldName)) {
							fieldName = field.getName();
						}
						fieldType = "float";
						len = cfloat.len();
						defVal = cfloat.defVal()+"";
						comment = cfloat.comment();
						isNull = cfloat.isNull();
						dec = cfloat.dec();
					}
				}
			}
			if (fieldName != null) {
				//浮点型特别处理
				if(StringUtils.equals("float",fieldType)){
					fieldType=fieldType + "(" + len + ","+dec+")";
				}else if(!StringUtils.equals("text",fieldType)){
					fieldType=fieldType + "(" + len + ")";
				}
				AlterColumn column = new AlterColumn(tbname,fieldName,fieldType, isNull,defVal,comment, key, keyName);
				list.add(column);
			}
		}
		return list;
	}
	
	/**
	 * 递归回去fileid
	 * @param list
	 * @param clas
	 */
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
