package cn.ruicz.contactlist.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;

import cn.ruicz.contactlist.R;
import cn.ruicz.contactlist.entity.PersonTreeItem;
import cn.ruicz.contactlist.treeview.model.TreeNode;

/*
 * @Created_time: 2018/1/5 10:21
 * @Author: wr
 * @Description: ${TODO}(用一句话描述该文件做什么)
 */


public class RootHolder extends TreeNode.BaseNodeViewHolder<PersonTreeItem> {
    private LayoutInflater mInflater;
    private PrintView mPvHead;
    private TextView mTvContent;
    private PrintView mPvDown;

    public RootHolder(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View createNodeView(TreeNode node, PersonTreeItem value) {
        View view = mInflater.inflate(R.layout.root_select_person_item, null);
        initView(view);
        mTvContent.setText(value.getText());
        if(node.isContent()){
            mPvDown.setVisibility(View.GONE);
        } else {
            mPvDown.setVisibility(View.VISIBLE);
        }
        mPvHead.setIconText(context.getResources().getString(value.getIcon()));
        return view;
    }

    private void initView(View view) {
        mPvHead = (PrintView) view.findViewById(R.id.pv_head);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mPvDown = (PrintView) view.findViewById(R.id.pv_down);
    }

    @Override
    public void toggle(boolean active) {
        mPvDown.setIconText(context.getResources().getString(active ?  R.string.ic_keyboard_arrow_down :
                R.string.ic_keyboard_arrow_right));
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }
}
