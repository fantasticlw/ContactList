package cn.ruicz.contactlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.ruicz.contactlist.R;
import cn.ruicz.contactlist.entity.ContactUser;
import cn.ruicz.contactlist.entity.PersonTreeItem;
import cn.ruicz.contactlist.treeview.model.TreeNode;


public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.Holder> {
    public List<TreeNode> mDatas = new ArrayList<>();
    private OnItemClickListener mListener;
    private Context mContext;

    public SelectedAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_select_item, parent,
                false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final TreeNode node = mDatas.get(position);
        ContactUser table = ((PersonTreeItem)mDatas.get(position).getValue()).user;
        holder.mTvName.setText(table.getUserName());
        //setAvatar(mContext, table.getUserId(), holder.mIvHead);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(node);
                }
            }
        });
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(TreeNode node){
        mDatas.add(node);
        notifyDataSetChanged();
        //notifyItemInserted(mDatas.size());
        //notifyItemMoved(mDatas.size() -1, mDatas.size());
    }

    public void addDatas(List<TreeNode> treeNodes){
        mDatas.clear();
        mDatas.addAll(treeNodes);
        notifyDataSetChanged();
    }

    public void reMoveData(TreeNode node){
/*        int index = 0;
        for (int i = 0; i < mDatas.size(); ++i){
            TreeNode treeNode = mDatas.get(i);
            if(treeNode.equals(node)){
                break;
            }
            ++index;
        }
        mDatas.remove(index);*/

        mDatas.remove(node);
        notifyDataSetChanged();
        //notifyItemRemoved(index);
    }

    public ArrayList<ContactUser> getSelectedData(){
        ArrayList<ContactUser> list = new ArrayList<>();
        for(TreeNode node : mDatas){
            ContactUser table = ((PersonTreeItem)node.getValue()).user;
            list.add(table);
        }
        return list;
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView mTvName;
        ImageView mIvHead;
        public Holder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mIvHead = (ImageView) itemView.findViewById(R.id.iv_head);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(TreeNode node);
    }
}
