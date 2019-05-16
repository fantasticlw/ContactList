package cn.ruicz.contactlist.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ruicz.contactlist.R;
import cn.ruicz.contactlist.adapter.PersonAdapter;
import cn.ruicz.contactlist.adapter.SelectedAdapter;
import cn.ruicz.contactlist.entity.ContactBean;
import cn.ruicz.contactlist.entity.ContactUser;
import cn.ruicz.contactlist.entity.DeptAndUser;
import cn.ruicz.contactlist.entity.PersonTreeItem;
import cn.ruicz.contactlist.holder.RootHolder;
import cn.ruicz.contactlist.holder.SelectHolder;
import cn.ruicz.contactlist.http.HttpManager;
import cn.ruicz.contactlist.treeview.model.TreeNode;
import cn.ruicz.contactlist.treeview.view.AndroidTreeView;
import cn.ruicz.contactlist.utils.ContactConst;
import cn.ruicz.contactlist.utils.Utils;
import io.reactivex.functions.Consumer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @Created_time: 2017/12/26 10:05
 */
public class SelectPersonActivity extends BaseActivity implements
        SelectedAdapter.OnItemClickListener, TreeNode.TreeNodeSelectedListener,
        TreeNode.TreeNodeClickListener, PersonAdapter.onItemClickListener,View.OnClickListener {

    RecyclerView mSelectedPersonRecyclerView;   // 已选用户列表
    FrameLayout mFlContainer;
    RecyclerView mSearchPersonRecycler; // 搜索用户列表
    EditText mEtText;
    ImageView mIvSearch;
    private SelectedAdapter mSelectedPersonAdapter;
    private AndroidTreeView mAndroidTreeView;
    private PersonAdapter mSearchPersonAdapter;
    private List<TreeNode> mSelectedPersonData = new ArrayList<>();
    private List<ContactUser> mSelectedDatas;
    private TreeNode mRoot;
    private boolean mIsSingleSelect;
    private TextView refresh_txl;
    private String requestUrl;

    public final static String SELECT_DATA = "select_data";
    public final static String SELECTED_DATA = "selected_data";
    public final static String IS_SINGLE_SELECT = "is_single_select";
    private List<TreeNode> mSelectData = new ArrayList<>();
    private String ContactPath;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_person;
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

        findView();
        initTree();
        initView();
        initListener();
    }

    private void findView() {
        mSelectedPersonRecyclerView = findViewById(R.id.recycler_view);
        mFlContainer = findViewById(R.id.fl_container);
        mSearchPersonRecycler = findViewById(R.id.person_recycler);
        refresh_txl = findViewById(R.id.refresh_txl);
        refresh_txl.setOnClickListener(this);
        mEtText = findViewById(R.id.et_text);
        mIvSearch = findViewById(R.id.iv_search);
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
        setTitle("选择");
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mSelectedPersonRecyclerView.setLayoutManager(manager);
        mSelectedPersonRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSelectedPersonAdapter = new SelectedAdapter(this);
        mSelectedPersonRecyclerView.setAdapter(mSelectedPersonAdapter);
        if(!mIsSingleSelect) {
            for (TreeNode node : mSelectData) {
                mAndroidTreeView.selectNode(node, true);
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mSearchPersonRecycler.setLayoutManager(linearLayoutManager);
        mSearchPersonAdapter = new PersonAdapter(this, mAndroidTreeView, mIsSingleSelect);
        mSearchPersonAdapter.setListener(this);
        mSearchPersonRecycler.setAdapter(mSearchPersonAdapter);
    }

    public List<DeptAndUser> getDatas() {
//        String result = Utils.readFile2String(new File(ContactPath), "utf-8");
        String result = null;
        if(result == null){
            getContactsByAuxiliaryPolice();
            return null;
        }
        Gson gson = new Gson();
        ContactBean contact = gson.fromJson(result, ContactBean.class);
        return contact.getBody();
    }

    public void getContactsByAuxiliaryPolice() {
        final ProgressDialog waitDialog = new ProgressDialog(SelectPersonActivity.this);
        waitDialog.setMessage("请稍后...");
        waitDialog.setCancelable(false);
        waitDialog.show();
        HttpManager.getInstance().getPoliceDeptAndUsers(SelectPersonActivity.this, requestUrl, new Consumer<String>() {
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
                                .setViewHolder(new SelectHolder(this))
                                .setContent(false);
                    } else {
                        newNode = new TreeNode(new PersonTreeItem(table))
                                .setViewHolder(new RootHolder(this))
                                .setContent(false);
                    }
                } else {
                    newNode = new TreeNode(new PersonTreeItem(table))
                            .setViewHolder(new RootHolder(this))
                            .setContent(false);
                }
                rootNode.addChild(newNode);
                List<DeptAndUser> tables = table.getChildrens();
                if (tables != null && tables.size() > 0) {
                    fillTreeGroup(tables, newNode);
                }
                List<ContactUser> contactUsers = table.getUsers();
                if (contactUsers != null && contactUsers.size() > 0) {
                    fillTreePerson(contactUsers, newNode);
                }
            }
        }
    }

    /**
     * 填充用户数据
     * @param tables
     * @param rootNode
     */
    private void fillTreePerson(List<ContactUser> tables, TreeNode rootNode){
        for (ContactUser table : tables){
            TreeNode newNode = null;
            if(!mIsSingleSelect) {
                newNode = new TreeNode(new PersonTreeItem(table))
                        .setViewHolder(new SelectHolder(this))
                        .setContent(true);
                boolean isSameAs = false;
                for (ContactUser t : mSelectedDatas) {
                    if (t.getUserId().equals(table.getUserId())) {
                        isSameAs = true;
                        break;
                    }
                }
                if (isSameAs) {
                    mSelectData.add(newNode);
                }
            } else {
                newNode = new TreeNode(new PersonTreeItem(table))
                        .setViewHolder(new RootHolder(this))
                        .setClickListener(this)
                        .setContent(true);
            }
            rootNode.addChild(newNode);
        }
    }


    private void initListener() {
        mSelectedPersonAdapter.setListener(this);
        mEtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSelectedPersonData.clear();
                if(s == null || s.length() == 0){
                    mFlContainer.setVisibility(View.VISIBLE);
                    mSearchPersonRecycler.setVisibility(View.GONE);
                } else {
                    mFlContainer.setVisibility(View.GONE);
                    mSearchPersonRecycler.setVisibility(View.VISIBLE);
                    getSelectedPersonData(s.toString(), mAndroidTreeView.getRoot().getChildren());
                    mSearchPersonAdapter.add(mSelectedPersonData);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getSelectedPersonData(String s, List<TreeNode> parents){
        if(parents != null && parents.size() > 0) {
            for (TreeNode node : parents) {
                if (node.isContent()) {
                    PersonTreeItem item = (PersonTreeItem) node.getValue();
                    if ((item.user != null && item.user.getUserName() != null) && item.user.getUserName().contains(s)) {
                        mSelectedPersonData.add(node);
                    }
                } else {
                    getSelectedPersonData(s, node.getChildren());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("确定").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!mIsSingleSelect) {
                ArrayList<ContactUser> tables = mSelectedPersonAdapter.getSelectedData();
                if (tables == null || tables.size() == 0) {
                    Toast.makeText(this, "请选择联系人", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(SELECT_DATA, tables);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(TreeNode node) {
        mSelectedPersonAdapter.reMoveData(node);
        mAndroidTreeView.selectNode(node, false);
        int size = mSelectedPersonAdapter.getItemCount();
        checkWidth();
    }

    @Override
    public void reMove(TreeNode node) {
        if(mSelectedPersonAdapter.mDatas.contains(node)) {
            mSelectedPersonAdapter.reMoveData(node);
            int size = mSelectedPersonAdapter.getItemCount();
            checkWidth();
        }
    }

    @Override
    public void add(TreeNode node) {
        mSelectedPersonAdapter.addData(node);
        int size = mSelectedPersonAdapter.getItemCount();
        checkWidth();
    }

    private void checkWidth(){
        LayoutParams params = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        mSelectedPersonRecyclerView.setLayoutParams(params);
    }

    @Override
    public void onClick(TreeNode node, Object value) {
        Intent intent = new Intent();
        ArrayList<ContactUser> tables = new ArrayList<>();
        tables.add(((PersonTreeItem) value).user);
        intent.putParcelableArrayListExtra(SELECT_DATA, tables);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(ContactUser table) {
        Intent intent = new Intent();
        ArrayList<ContactUser> tables = new ArrayList<>();
        tables.add(table);
        intent.putParcelableArrayListExtra(SELECT_DATA, tables);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.refresh_txl){
            getContactsByAuxiliaryPolice();
        }
    }
}
