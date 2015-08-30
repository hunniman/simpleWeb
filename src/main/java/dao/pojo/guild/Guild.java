package dao.pojo.guild;

import java.io.Serializable;

import dao.pojo.BaseEntity;
import dao.pojo.annotation.ColumnVar;
import dao.pojo.annotation.Table;

@SuppressWarnings("serial")
@Table(name="guild")
public class Guild extends BaseEntity implements Serializable{
   
	@ColumnVar(len=50,defVal="", comment = "帮会名称")
	private String guildName;

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}
	
}
