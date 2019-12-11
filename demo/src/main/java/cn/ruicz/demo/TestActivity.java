package cn.ruicz.demo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import cn.ruicz.contactlist.activity.BaseActivity;
import cn.ruicz.contactlist.activity.SelectDepartmentActivity;
import cn.ruicz.contactlist.activity.SelectDepartmentSelfActivity;
import cn.ruicz.contactlist.activity.SelectPersonActivity;
import cn.ruicz.contactlist.entity.ContactUser;
import cn.ruicz.contactlist.entity.DeptAndUser;
import cn.ruicz.contactlist.utils.ContactConst;

public class TestActivity extends BaseActivity {

    TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initialize() {
        setTitle("联系人demo");

        Button button  = findViewById(R.id.button);
        Button btnDep  = findViewById(R.id.btnDep);
        Button btnDep2 = findViewById(R.id.btnDep2);
        textView = findViewById(R.id.textview);

        //http://192.168.65.70:8182  阳江   192.168.20.90:8888/res111901 梅州
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectPersonActivity.class);
                intent.putExtra(ContactConst.ContactUrl, "http://192.168.20.90:8888/res111901/platform/api/management/contacts/getContactsByPolice?userId=ruice1&userType=1");  //  http://192.168.65.70:8182/platform/api/management/contacts/getContactsByPolice?userId=ruice1&userType=1
                startActivityForResult(intent, 0);
            }
        });

        btnDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectDepartmentActivity.class);
                intent.putExtra(ContactConst.ContactUrl, "http://192.168.20.90:8888/res111901/platform/api/management/contacts/getContactsByPolice?userId=ruice1&userType=1");  //  http://192.168.65.70:8182/platform/api/management/contacts/getContactsByPolice?userId=ruice1&userType=1
                startActivityForResult(intent, ContactConst.iGetDeaprt);
            }
        });

        btnDep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectDepartmentSelfActivity.class);
                intent.putExtra(ContactConst.ContactUrl, "http://192.168.20.90:8888/res111901/platform/api/management/contacts/getContactsByPolice?userId=ruice1&userType=1");  //  http://192.168.65.70:8182/platform/api/management/contacts/getContactsByPolice?userId=ruice1&userType=1
                startActivityForResult(intent, ContactConst.iGetDeaprtSelf);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data){
            if (resultCode == RESULT_OK) {
                ArrayList<ContactUser> userInfos = data.getParcelableArrayListExtra("select_data");
                String text = "";
                for (ContactUser user : userInfos){
                    text+=user.getUserName()+", ";
                }
                textView.setText(text);
            } else if (resultCode == ContactConst.iGetDeaprt){
                ArrayList<DeptAndUser> deptInfos = data.getParcelableArrayListExtra(ContactConst.SELECTDEPARTMENT);

                Log.e("Dep", deptInfos.get(0).deptnName);
            } else if (requestCode == ContactConst.iGetDeaprtSelf){
                ArrayList<DeptAndUser> deptInfos = data.getParcelableArrayListExtra(ContactConst.SELECTDEPARTMENT);

                List<String> lInitator = new ArrayList<>();
                List<String> lInitatorName = new ArrayList<>();

                for (int i = 0; i < deptInfos.size(); i++) {
                    lInitatorName.add(deptInfos.get(i).deptnName);
                    lInitator.add(deptInfos.get(i).deptCode);
                }

                Log.e("Dep Self", lInitator.toString());
            }
        }
    }
}