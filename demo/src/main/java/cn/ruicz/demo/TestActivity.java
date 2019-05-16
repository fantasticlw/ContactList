package cn.ruicz.demo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import cn.ruicz.contactlist.activity.BaseActivity;
import cn.ruicz.contactlist.activity.SelectPersonActivity;
import cn.ruicz.contactlist.entity.ContactUser;
import cn.ruicz.contactlist.utils.ContactConst;

public class TestActivity extends BaseActivity {

    TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initialize() {
        Button button = findViewById(R.id.button);
        textView = findViewById(R.id.textview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, SelectPersonActivity.class);
                intent.putExtra(ContactConst.ContactUrl, "http://192.168.20.90:8888/res111901/platform/api/management/contacts/getContactsByPolice?userId=ruice1&userType=1");
                startActivityForResult(intent, 0);
            }
        });
    }


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
}