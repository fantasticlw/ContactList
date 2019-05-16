# ContactList
ContactList

## 调用方式

Intent intent = new Intent(TestActivity.this, SelectPersonActivity.class);
intent.putExtra(ContactConst.ContactUrl, "requestUrl");
startActivityForResult(intent, 0);

## 返回结果

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
        ArrayList<ContactUser> userInfos = data.getParcelableArrayListExtra("select_data");
        String text = "";
        for (ContactUser user : userInfos){
            text+=user.getUserName()+", ";
        }
        textView.setText(text);
    }
}
