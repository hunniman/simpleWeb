package dao.pojo;

import dao.pojo.annotation.ColumnBigInt;
import dao.pojo.annotation.ColumnInt;
import dao.pojo.annotation.Index;
import dao.pojo.annotation.IndexEnum;


public class BaseEntity {

	@ColumnBigInt(comment="")
	@Index(key=IndexEnum.PRIMARY)
	private long id;

	@ColumnInt(comment="createTime")
	private  int createTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

}
