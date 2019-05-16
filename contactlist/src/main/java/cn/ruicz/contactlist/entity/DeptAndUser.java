package cn.ruicz.contactlist.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeptAndUser {
	@SerializedName("id")
	public String deptCode;
	@SerializedName("name")
	public String deptnName ;

	public void setChildrens(List<DeptAndUser> childrens) {
		this.childrens = childrens;
	}

	@SerializedName("pid")
	public String pDeptCode ;

	public void setUsers(List<ContactUser> users) {
		this.users = users;
	}

	@SerializedName("users")
	public List<ContactUser> users;


	@SerializedName("departments")
	public List<DeptAndUser> childrens;



	public DeptAndUser(){

	}



	public DeptAndUser(String deptCode, String deptnName,
					   String pDeptCode) {
		this.deptCode = deptCode;
		this.deptnName = deptnName;
		this.pDeptCode = pDeptCode;
	}



	 public boolean hasChild(){
		if (childrens != null && childrens.size() > 0){
			return true;
		}
		return false;
	}



		public String getDeptCode() {
			return this.deptCode;
		}



		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}



		public String getDeptnName() {
			return this.deptnName;
		}



		public void setDeptnName(String deptnName) {
			this.deptnName = deptnName;
		}




		public String getPDeptCode() {
			return this.pDeptCode;
		}



		public void setPDeptCode(String pDeptCode) {
			this.pDeptCode = pDeptCode;
		}



		public List<ContactUser> getUsers() {
			return users;
		}



		public synchronized void resetUsers() {
			users = null;
		}



		public List<DeptAndUser> getChildrens() {
			return childrens;
		}



		public synchronized void resetChildrens() {
			childrens = null;
		}




}
