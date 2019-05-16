package cn.ruicz.contactlist.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LeeQiuuu on 2017/6/2.
 */
public class ContactUser implements Comparable<ContactUser>,Parcelable {
    private Long ID;
    private String userCode;//警号
    @SerializedName("userId")
    private String userId;  //用户id
    @SerializedName("name")
    private String userName;//用户名
    @SerializedName("tel")
    private String userTelephone;//用户电话号码
    @SerializedName("e")
    private String userEmail;//用户邮箱
    @SerializedName("deptName")
    private String DeptName; //部门名字
    @SerializedName("deptCode")
    private String DeptCode; //部门编码
    private String userImage;//用户头像
    private String companyName;//公司名称
    private String companyId; //公司id
    private int focusStatus;//是否关注 0:未关注|1:已关注
    private String uid; //对应账号的id
    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母

    public String getPinyin() {
        return pinyin;
    }


    public String getFirstLetter() {
        return firstLetter;
    }



    public ContactUser() {
    }

    @Override
    public int compareTo(@NonNull ContactUser another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }


    public Long getID() {
        return this.ID;
    }


    public void setID(Long ID) {
        this.ID = ID;
    }


    public String getUserCode() {
        return this.userCode;
    }


    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


    public String getUserId() {
        return this.userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return this.userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserTelephone() {
        return this.userTelephone;
    }


    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }


    public String getUserEmail() {
        return this.userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getDeptName() {
        return this.DeptName;
    }


    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }


    public String getDeptCode() {
        return this.DeptCode;
    }


    public void setDeptCode(String DeptCode) {
        this.DeptCode = DeptCode;
    }


    public String getUserImage() {
        return this.userImage;
    }


    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


    public String getCompanyName() {
        return this.companyName;
    }


    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getCompanyId() {
        return this.companyId;
    }


    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public int getFocusStatus() {
        return this.focusStatus;
    }


    public void setFocusStatus(int focusStatus) {
        this.focusStatus = focusStatus;
    }


    public String getUid() {
        return this.uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ID);
        dest.writeString(this.userCode);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.userTelephone);
        dest.writeString(this.userEmail);
        dest.writeString(this.DeptName);
        dest.writeString(this.DeptCode);
        dest.writeString(this.userImage);
        dest.writeString(this.companyName);
        dest.writeString(this.companyId);
        dest.writeInt(this.focusStatus);
        dest.writeString(this.uid);
        dest.writeString(this.pinyin);
        dest.writeString(this.firstLetter);
    }

    protected ContactUser(Parcel in) {
        this.ID = (Long) in.readValue(Long.class.getClassLoader());
        this.userCode = in.readString();
        this.userId = in.readString();
        this.userName = in.readString();
        this.userTelephone = in.readString();
        this.userEmail = in.readString();
        this.DeptName = in.readString();
        this.DeptCode = in.readString();
        this.userImage = in.readString();
        this.companyName = in.readString();
        this.companyId = in.readString();
        this.focusStatus = in.readInt();
        this.uid = in.readString();
        this.pinyin = in.readString();
        this.firstLetter = in.readString();
    }


    public static final Creator<ContactUser> CREATOR = new Creator<ContactUser>() {
        @Override
        public ContactUser createFromParcel(Parcel source) {
            return new ContactUser(source);
        }

        @Override
        public ContactUser[] newArray(int size) {
            return new ContactUser[size];
        }
    };
}
