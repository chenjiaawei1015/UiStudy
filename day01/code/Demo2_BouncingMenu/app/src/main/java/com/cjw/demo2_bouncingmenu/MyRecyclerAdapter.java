package com.cjw.demo2_bouncingmenu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<String> list;
    private OnItemClickListener mOnItemClickListener;

    public MyRecyclerAdapter(List<String> list) {
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(android.R.id.text1);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // 绑定数据
        holder.tv.setText(list.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
        // 创建ViewHolder
        return new MyViewHolder(View.inflate(viewGroup.getContext(),
                android.R.layout.simple_list_item_1, null));
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
