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


public class DeptRootHolder extends TreeNode.BaseNodeViewHolder<PersonTreeItem> {
    private LayoutInflater mInflater;
    private PrintView mPvHead;
    private TextView mTvContent;
    private PrintView mPvDown;
    private ImageView mIvHead;

    public DeptRootHolder(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    public void setAvatar(Context context, String userid, ImageView imageView) {
        /*Glide.with(context).load("http://183.63.34.174:8020/msgfiles/avatar_" + userid + "_L.jpg").
                placeholder(R.drawable.chat_group_deflent_ic_new).error(R.drawable.chat_group_deflent_ic_new).into(imageView);*/

    }

    @Override
    public View createNodeView(TreeNode node, PersonTreeItem value) {
        View view = mInflater.inflate(R.layout.root_select_person_item, null);
        initView(view);
        mTvContent.setText(value.getText());
        if(node.isContent()){
            mIvHead.setVisibility(View.VISIBLE);
            mPvHead.setVisibility(View.GONE);
            mPvDown.setVisibility(View.GONE);
            //setAvatar(context, value.user.getUserId(), mIvHead);
        } else {
            mIvHead.setVisibility(View.GONE);
            mPvHead.setVisibility(View.VISIBLE);
            mPvDown.setVisibility(View.VISIBLE);
            //mPvHead.setIconText(context.getResources().getString(value.getIcon()));
        }
        return view;
    }

    private void initView(View view) {
        mPvHead = (PrintView) view.findViewById(R.id.pv_head);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mPvDown = (PrintView) view.findViewById(R.id.pv_down);
        mIvHead = (ImageView) view.findViewById(R.id.iv_head);
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
