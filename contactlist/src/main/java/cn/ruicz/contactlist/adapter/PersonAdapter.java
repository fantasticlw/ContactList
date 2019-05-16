package cn.ruicz.contactlist.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ruicz.contactlist.R;
import cn.ruicz.contactlist.entity.ContactUser;
import cn.ruicz.contactlist.entity.PersonTreeItem;
import cn.ruicz.contactlist.treeview.model.TreeNode;
import cn.ruicz.contactlist.treeview.view.AndroidTreeView;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<TreeNode> mTreeNodes = new ArrayList<>();
    private Activity mActivity;
    private LayoutInflater mInflater;
    private AndroidTreeView mAndroidTreeView;
    private boolean mIsSingleSelect;
    private onItemClickListener mListener;

    public PersonAdapter(Activity activity, AndroidTreeView androidTreeView, boolean isSingleSelect) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mAndroidTreeView = androidTreeView;
        mIsSingleSelect = isSingleSelect;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.template_two_select_person_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TreeNode treeNode = mTreeNodes.get(position);
        final ContactUser table = ((PersonTreeItem)treeNode.getValue()).user;
        holder.mTvName.setText(table.getUserName());
        //setAvatar(mActivity, table.getUserId(), holder.mIvHead);
        if(mIsSingleSelect){
            holder.mCbCheck.setVisibility(View.GONE);
        } else {
            holder.mCbCheck.setVisibility(View.VISIBLE);
            holder.mCbCheck.setChecked(treeNode.isSelected());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsSingleSelect){
                    if(mListener != null){
                        mListener.onClick(table);
                    }
                } else {
                    if (treeNode.isSelected()) {
                        holder.mCbCheck.setChecked(false);
                        mAndroidTreeView.selectNode(treeNode, false);
                    } else {
                        holder.mCbCheck.setChecked(true);
                        mAndroidTreeView.selectNode(treeNode, true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTreeNodes.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private TextView mTvLetter;
        private CheckBox mCbCheck;
        private ImageView mIvHead;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvLetter = (TextView) itemView.findViewById(R.id.tv_letter);
            mCbCheck = (CheckBox) itemView.findViewById(R.id.cb_check);
            mIvHead = (ImageView) itemView.findViewById(R.id.iv_head);
            mTvLetter.setVisibility(View.GONE);
        }
    }

    public void add(List<TreeNode> treeNodes){
        mTreeNodes.clear();
        mTreeNodes.addAll(treeNodes);
        notifyDataSetChanged();
    }

    public void setListener(onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener{
        void onClick(ContactUser table);
    }
}
