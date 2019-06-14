package cn.ruicz.contactlist.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;

import cn.ruicz.contactlist.R;
import cn.ruicz.contactlist.entity.PersonTreeItem;
import cn.ruicz.contactlist.treeview.model.TreeNode;

/**
 * cn.ruicz.contactlist.holder.SelectDepartHolder
 *
 * @author xyq
 * @time 2019-6-10 15:57
 * Remark -----------防止顶级栏目点击选择和展开事件同时进行-----------------
 */

public class SelectDepartHolder extends TreeNode.BaseNodeViewHolder<PersonTreeItem> {

    private LayoutInflater mInflater;
    private CheckBox mCbCheck;
    private PrintView mPvHead;
    private TextView mTvContent;
    private PrintView mPvDown;

    public SelectDepartHolder(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View createNodeView(final TreeNode node, PersonTreeItem value) {
        View view = mInflater.inflate(R.layout.select_person_item, null);
        initView(view);
        if (node.isLeaf()){
            mPvDown.setVisibility(View.GONE);
        }
        mCbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    getTreeView().selectNode(node, isChecked);
                    checkedSelect(node, isChecked);
                }
            }
        });

        mCbCheck.setChecked(node.isSelected());
        mTvContent.setText(value.getText());
        mPvHead.setIconText(context.getResources().getString(value.getIcon()));
        return view;
    }

    private void checkedSelect(TreeNode node, boolean isChecked){
        if(node != null) {
            for (TreeNode n : node.getChildren()) {
                getTreeView().selectNode(n, isChecked);
                checkedSelect(n, isChecked);
            }
        }
    }

    @Override
    public void toggle(boolean active) {
        mPvDown.setIconText(context.getResources().getString(active ?
                R.string.ic_keyboard_arrow_down :
                R.string.ic_keyboard_arrow_right));
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        mCbCheck.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);
        mCbCheck.setChecked(mNode.isSelected());
    }

    private void initView(View view) {
        mCbCheck = (CheckBox) view.findViewById(R.id.cb_check);
        mPvHead = (PrintView) view.findViewById(R.id.pv_head);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mPvDown = (PrintView) view.findViewById(R.id.pv_down);
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }
}
