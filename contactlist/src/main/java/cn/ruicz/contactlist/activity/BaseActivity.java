package cn.ruicz.contactlist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.ruicz.contactlist.R;


/**
 * Created by CLW on 2017/4/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //设置状态栏颜色  //activity跟布局要加上   android:fitsSystemWindows="true"
        //BarUtils.setBarTopColor(BaseActivity.this);
        initToolbar();
        initialize();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar("", true);
    }

    public void setUpToolbar(String title, boolean hasBack) {
        if (null == mToolbar) {
            return;
        }
        setSupportActionBar(mToolbar);
        if (title != null) {
            mToolbar.setTitle("");
        }
        if (hasBack) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }
    public void setTitle(String title){
        getSupportActionBar().setTitle(title);
        //TextView textView = (TextView) findViewById(R.id.toolbar_title);
        //textView.setText(title);
    }
    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void removeBackMenu(){
        mToolbar.setNavigationIcon(null);
    }

   /* public void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }*/

    public void setTitle(@StringRes int resId){
        mToolbar.setTitle(resId);
    }

    public abstract int getLayoutId();

    protected abstract void initialize();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
