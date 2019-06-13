# ContactList
implementation com.github.fantasticlw:ContactList:0.12

## 调用方式

### 1.获取通讯录人员列表
Intent intent = new Intent(getApplicationContext(), SelectPersonActivity.class);<br/>
intent.putExtra(ContactConst.ContactUrl, "requestUrl");<br/>
startActivityForResult(intent, 0);<br/>

#### 返回结果
protected void onActivityResult(int requestCode, int resultCode, Intent data) {<br/>
    super.onActivityResult(requestCode, resultCode, data);<br/>
       if (resultCode == RESULT_OK) {<br/>
          ArrayList<*ContactUser*> userInfos = data.getParcelableArrayListExtra("select_data");<br/>
       }<br/>
}<br/>
    
### 2.获取通讯录部门列表， 同理调用：
Intent intent = new Intent(getApplicationContext(), SelectDepartmentActivity.class);
intent.putExtra(ContactConst.ContactUrl, "requestUrl");
startActivityForResult(intent, ContactConst.iGetDeaprt);

#### 返回结果
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ContactConst.iGetDeaprt){
            ArrayList<*DeptAndUser*> deptInfos = data.getParcelableArrayListExtra(ContactConst.SELECTDEPARTMENT);
        }
}                                                                                                                                         



