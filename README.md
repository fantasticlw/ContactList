# ContactList
ContactList

## 调用方式

Intent intent = new Intent(TestActivity.this, SelectPersonActivity.class);

intent.putExtra(ContactConst.ContactUrl, "requestUrl");

startActivityForResult(intent, 0);

## 返回结果


protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    
    if (resultCode == RESULT_OK) {
    
        ArrayList<ContactUser> userInfos = data.getParcelableArrayListExtra("select_data");
        
    }
    
}
