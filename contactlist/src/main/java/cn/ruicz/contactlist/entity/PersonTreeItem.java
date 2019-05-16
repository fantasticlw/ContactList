package cn.ruicz.contactlist.entity;

import java.io.Serializable;

import cn.ruicz.contactlist.R;


/**
 * @Created_time: 2017/12/26 10:47
 * @Author: wr
 * @Description: ${TODO}(用一句话描述该文件做什么)
 */

public class PersonTreeItem implements Serializable {
    public ContactUser user;
    public DeptAndUser deptAndUser;

    public PersonTreeItem(ContactUser user) {
        this.user = user;
    }

    public PersonTreeItem(DeptAndUser deptAndUser) {
        this.deptAndUser = deptAndUser;
    }

    public boolean isDept(){
        return deptAndUser != null;
    }

    public String getText(){
        if (isDept()){
            return deptAndUser.deptnName;
        }else{
            return user.getUserName();
        }
    }

    public int getIcon(){
        if (isDept()){
            return R.string.ic_group;
        }else{
            return R.string.ic_person;
        }
    }
}
