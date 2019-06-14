package cn.ruicz.contactlist.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import cn.ruicz.contactlist.R;
import cn.ruicz.contactlist.entity.ContactBean;
import cn.ruicz.contactlist.entity.DeptAndUser;
import cn.ruicz.contactlist.entity.PersonTreeItem;
import cn.ruicz.contactlist.holder.SelectDepartHolder;
import cn.ruicz.contactlist.http.HttpManager;
import cn.ruicz.contactlist.treeview.model.TreeNode;
import cn.ruicz.contactlist.treeview.view.AndroidTreeView;
import cn.ruicz.contactlist.utils.ContactConst;
import cn.ruicz.contactlist.utils.SPUtils;
import cn.ruicz.contactlist.utils.Utils;
import io.reactivex.functions.Consumer;

/**
 * cn.ruicz.contactlist.activity
 *
 * @author xyq
 * @time 2019-6-10 15:57
 * Remark -------------选择部门-------------------
 */
public class SelectDepartmentActivity extends BaseActivity implements
        TreeNode.TreeNodeSelectedListener, TreeNode.TreeNodeClickListener, View.OnClickListener {

    private TextView tvDepart;

    private StringBuilder stringBuilder;

    FrameLayout mFlContainer;

    private AndroidTreeView mAndroidTreeView;
    private List<DeptAndUser> mSelectedDatas;
    private TreeNode mRoot;
    private boolean mIsSingleSelect;
    private TextView refresh_txl;
    private String requestUrl;

    public final static String SELECTED_DATA = "selected_depart";
    public final static String IS_SINGLE_SELECT = "is_single_select";

    private String ContactPath;

    public List<TreeNode> mDatas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_department;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvDepart = findViewById(R.id.tv_depart);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);

        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintColor(typedValue.data);//设置状态栏颜色
        systemBarTintManager.setStatusBarTintEnabled(true);//显示状态栏
    }

    @Override
    protected void initialize() {
        mSelectedDatas = getIntent().getParcelableArrayListExtra(SELECTED_DATA);
        mIsSingleSelect = getIntent().getBooleanExtra(IS_SINGLE_SELECT, false);
        if(mSelectedDatas == null){
            mSelectedDatas = new ArrayList<>();
        }

        ContactPath = getFilesDir().getAbsolutePath()+"/deptanduser";
        requestUrl = getIntent().getStringExtra(ContactConst.ContactUrl);

        SPUtils spUtils = new SPUtils(this, ContactConst.SPNAME);
        if (!spUtils.contains(ContactConst.ContactUrl) || !TextUtils.equals(spUtils.getString(ContactConst.ContactUrl), requestUrl)){
            new File(ContactPath).delete();
            spUtils.put(ContactConst.ContactUrl, requestUrl);
        }

        findView();
        initTree();
        initView();
        initListener();
    }

    private void findView() {
        mFlContainer = findViewById(R.id.fl_container);
        refresh_txl = findViewById(R.id.refresh_txl);
        refresh_txl.setOnClickListener(this);
    }

    private void initTree(){
        mRoot = TreeNode.root();
        fillTreeGroup(getDatas(), mRoot);
        mAndroidTreeView = new AndroidTreeView(this, mRoot);
        mAndroidTreeView.setSelectionModeEnabled(true);
        mAndroidTreeView.setDefaultAnimation(false);
        mFlContainer.addView(mAndroidTreeView.getView());
        mAndroidTreeView.setSelectedListener(this);

    }

    private void initView(){
        setTitle("选择部门");

    }

    public List<DeptAndUser> getDatas() {
        String result = Utils.readFile2String(new File(ContactPath), "utf-8");
        if(TextUtils.isEmpty(result)){
            getContactsByAuxiliaryPolice();
            return null;
        }
        Gson gson = new Gson();
        ContactBean contact = gson.fromJson(result, ContactBean.class);
        return contact.getBody();
    }

    public void getContactsByAuxiliaryPolice() {
        final ProgressDialog waitDialog = new ProgressDialog(SelectDepartmentActivity.this);
        waitDialog.setMessage("请稍后...");
        waitDialog.setCancelable(true);
        waitDialog.show();
        HttpManager.getInstance().getPoliceDeptAndUsers(SelectDepartmentActivity.this, requestUrl, new Consumer<String>() {
            @Override
            public void accept(@NonNull String result) throws Exception {
                try {
                    // 将组织架构写入本地文件
                    Utils.writeFileFromString(new File(ContactPath), result, false);
                    // 解析json
                    ContactBean contact = new Gson().fromJson(result, ContactBean.class);

                    mRoot.clearChild();
                    fillTreeGroup(contact.getBody(), mRoot);
                    mAndroidTreeView.expandNode(mRoot);
                    mAndroidTreeView.expandLevel(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    waitDialog.cancel();
                }
            }
        });
    }

    /**
     * 填充部门数据
     * @param userTables
     * @param rootNode
     */
    private void fillTreeGroup(List<DeptAndUser> userTables, TreeNode rootNode) {
        if(userTables != null) {
            for (DeptAndUser table : userTables) {
                TreeNode newNode = null;
                if(!mIsSingleSelect) {
                    if (mRoot.getChildren() != null && mRoot.getChildren().size() > 0) {
                        newNode = new TreeNode(new PersonTreeItem(table))
                                .setViewHolder(new SelectDepartHolder(this))
                                .setContent(true);
                    } else {
                        newNode = new TreeNode(new PersonTreeItem(table))
                                .setViewHolder(new SelectDepartHolder(this))
                                .setContent(true);
                    }
                } else {
                    newNode = new TreeNode(new PersonTreeItem(table))
                            .setViewHolder(new SelectDepartHolder(this))
                            .setContent(true);
                }
                rootNode.addChild(newNode);
                List<DeptAndUser> tables = table.getChildrens();
                if (tables != null && tables.size() > 0) {
                    fillTreeGroup(tables, newNode);
                }

            }
        }
    }


    private void initListener() {
        //mSelectedPersonAdapter.setListener(this);   2019
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("确定").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!mIsSingleSelect) {

            ArrayList<DeptAndUser> tables = new ArrayList<>();
            for (int i = 0; i < mDatas.size(); i++) {
                DeptAndUser user = ((PersonTreeItem)mDatas.get(i).getValue()).deptAndUser;
                tables.add(user);
            }

            if (tables == null || tables.size() == 0) {
                Toast.makeText(this, "请选择部门", Toast.LENGTH_SHORT).show();
                return true;
            }

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(ContactConst.SELECTDEPARTMENT, tables);
            setResult(ContactConst.iGetDeaprt, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void reMove(TreeNode node) {
        mDatas.remove(node);
        showDepartment();
    }

    @Override
    public void add(TreeNode node) {
        mDatas.add(node);
        showDepartment();
    }

    public void showDepartment(){
        stringBuilder = new StringBuilder();

        for (int i = 0; i < mDatas.size(); i++) {
            DeptAndUser user = ((PersonTreeItem)mDatas.get(i).getValue()).deptAndUser;
            if (i == 0) {
                stringBuilder.append(user.deptnName);
            } else {
                stringBuilder.append("," + user.deptnName);
            }
        }

        tvDepart.setText(stringBuilder.toString());
    }


    @Override
    public void onClick(TreeNode node, Object value) {
        Intent intent = new Intent();
        ArrayList<DeptAndUser> tables = new ArrayList<>();
        tables.add(((PersonTreeItem) value).deptAndUser);
        intent.putParcelableArrayListExtra(ContactConst.SELECTDEPARTMENT, tables);
        setResult(ContactConst.iGetDeaprt, intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.refresh_txl){
            getContactsByAuxiliaryPolice();
        }
    }
}
