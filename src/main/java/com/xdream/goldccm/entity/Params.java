package com.xdream.goldccm.entity;
import com.xdream.kernel.entity.Entity;
import com.xdream.kernel.dao.jdbc.Table;
import java.util.Date;

   /**
    * tbl_params 实体类<br/>
    * 2017-04-08 04:30:30 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="tbl_params")
public class Params  extends Entity {
	private String paramName;
	private String paramText;
	private String remark;
	
	public void setParamName(String paramName){
		this.paramName=paramName;
	}
	public String getParamName(){
		return paramName;
	}
	public void setParamText(String paramText){
		this.paramText=paramText;
	}
	public String getParamText(){
		return paramText;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}
	
	
}

