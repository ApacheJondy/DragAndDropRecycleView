package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sqk.draganddroprecycleview.R;

import java.util.ArrayList;

import drag.DragListener;

/**
 * Created by qikai on 16/3/1.
 */
public class Madapter extends RecyclerView.Adapter<Madapter.MViewHolder> implements DragListener{

    ArrayList<Integer> lists = new ArrayList<>();
    Context context;
    MadapterListener listener;
    private int dragItemId = DragListener.NO_ID;


    public interface MadapterListener {
        void onStartDrag(View view, Integer integer);
    }

    public Madapter(Context context, ArrayList<Integer> lists, MadapterListener listener) {
        this.context = context;
        this.listener = listener;
        if (lists != null && lists.size() > 0) {
            this.lists.addAll(lists);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adapter, parent, false);
        return new MViewHolder(view, new MViewHolder.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                listener.onStartDrag(view, lists.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        holder.itemView.setVisibility(dragItemId == lists.get(position) ? View.GONE : View.VISIBLE);
        holder.textView.setText(lists.get(position) + "");
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public boolean onMoveItem(int fromPosition, int toPosition) {
        lists.add(toPosition, lists.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void notifyItemChange(int position, int dragItemId) {
        this.dragItemId = dragItemId;
        notifyItemChanged(position);
    }

    @Override
    public int getPosition(Object object) {
        return lists.indexOf(object);
    }

    @Override
    public void onDragEnd(int position) {

    }

    static class MViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView textView;
        private OnLongClickListener listener;
        public MViewHolder(View itemView, OnLongClickListener listener) {
            super(itemView);
            this.listener = listener;
            textView = (TextView) itemView.findViewById(R.id.text);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClick(v, getAdapterPosition());
            return true;
        }

        interface OnLongClickListener {
            void onLongClick(View view, int position);
        }
    }
}
