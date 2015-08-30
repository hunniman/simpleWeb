package dao.pojo.role;

import java.io.Serializable;

import dao.pojo.BaseEntity;
import dao.pojo.annotation.ColumnVar;
import dao.pojo.annotation.Table;

@SuppressWarnings("serial")
@Table(name = "role")
public class Role extends BaseEntity implements Serializable {

	
	@ColumnVar(comment="角色名",len=50)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
