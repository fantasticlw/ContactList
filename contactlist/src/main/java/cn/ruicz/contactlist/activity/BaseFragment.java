package cn.ruicz.contactlist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cn.ruicz.contactlist.R;

/**
 * Created by CLW on 2017/3/20.
 * 基类
 */

public abstract class BaseFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    public final String TAG = getClass().getName();
    protected View mRootView;
    protected Toolbar mToolbar;

    public View getRootView() {
        return mRootView;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        if (mRootView != null) {
            setHasOptionsMenu(true);
            return mRootView;
        } else {
            throw new IllegalArgumentException("can’t find layout");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolbar();
        initialize();
    }

    protected void initToolbar(){
        initToolbar("", true);
    }

    protected void initToolbar(@StringRes int title, boolean hasBack){
        initToolbar(getString(title), hasBack);
    }

    protected void initToolbar(@NonNull String title, boolean hasBack) {
        mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        if (null == mToolbar) {
            return;
        }
        mToolbar.setOnMenuItemClickListener(this);
        onCreateToolbarMenu(mToolbar, mToolbar.getMenu());
        if (title != null) {
            mToolbar.setTitle(title);
        }
        if (hasBack) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }
    }


    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void removeBackMenu(){
        mToolbar.setNavigationIcon(null);
    }

    public void setTitle(String title){
        mToolbar.setTitle(title);
    }

    public void setTitle(@StringRes int resId){
        mToolbar.setTitle(resId);
    }

    public abstract int getLayoutId();

    protected abstract void initialize();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    final public boolean onMenuItemClick(MenuItem item) {
        return onToolbarMenuClick(item);
    }

    public boolean onToolbarMenuClick(MenuItem item){
        return false;
    }

    public void onCreateToolbarMenu(Toolbar toolbar, Menu menu){

    }

    /**
     * 禁止子类重写
     * @param menu
     * @param inflater
     */
    @Override
    final public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 禁止子类重写
     * @param item
     * @return
     */
    @Override
    final public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        intent.setClass(getActivity(), cls);
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
