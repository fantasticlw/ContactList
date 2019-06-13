package cn.ruicz.contactlist.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeptAndUser implements Parcelable {
	@SerializedName("id")
	public String deptCode;
	@SerializedName("name")
	public String deptnName ;

	protected DeptAndUser(Parcel in) {
		deptCode = in.readString();
		deptnName = in.readString();
		pDeptCode = in.readString();
		users = in.createTypedArrayList(ContactUser.CREATOR);
		childrens = in.createTypedArrayList(DeptAndUser.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(deptCode);
		dest.writeString(deptnName);
		dest.writeString(pDeptCode);
		dest.writeTypedList(users);
		dest.writeTypedList(childrens);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DeptAndUser> CREATOR = new Creator<DeptAndUser>() {
		@Override
		public DeptAndUser createFromParcel(Parcel in) {
			return new DeptAndUser(in);
		}

		@Override
		public DeptAndUser[] newArray(int size) {
			return new DeptAndUser[size];
		}
	};

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
