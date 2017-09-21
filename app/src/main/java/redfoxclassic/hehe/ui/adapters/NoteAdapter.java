package redfoxclassic.hehe.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import redfoxclassic.hehe.R;
import redfoxclassic.hehe.model.NoteModel;
import redfoxclassic.hehe.util.MyColor;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private final static String TAG = NoteAdapter.class.getSimpleName();

    private List<NoteModel> noteModelList = new ArrayList<>();
    private Context context;


    public NoteAdapter(List<NoteModel> noteModelList, Context context) {
        this.noteModelList = noteModelList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_row_Title)
        TextView noteRowTitle;
        @BindView(R.id.note_row_Date)
        TextView noteRowDate;
        @BindView(R.id.note_row_Content)
        TextView noteRowContent;
        @BindView(R.id.note_row_CardView)
        CardView noteRowCardView;
        @BindView(R.id.note_row_line)
        TextView noteRowLine;
        @BindView(R.id.note_row_checkbox)
        CheckBox checkBox;
        /*@BindView(R.id.note_row_relative)
        RelativeLayout noteRowRelative;*/


        public MyViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ctor");
            ButterKnife.bind(this, itemView);
        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_custom_row, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(v);


        myViewHolder.noteRowLine.setBackgroundColor(MyColor.getRandomColor(context));


        int paddingTop = (myViewHolder.noteRowTitle.getVisibility() != View.VISIBLE) ? 0
                : myViewHolder.itemView.getContext().getResources()
                .getDimensionPixelSize(R.dimen.cardview_content_padding);

        myViewHolder.noteRowContent.setPadding(myViewHolder.noteRowContent.getPaddingLeft(), paddingTop,
                myViewHolder.noteRowContent.getPaddingRight(), myViewHolder.noteRowContent.getPaddingBottom());


        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder()");

        NoteModel noteModel = noteModelList.get(position);

        myViewHolder.noteRowTitle.setText(noteModel.getTitle());
        myViewHolder.noteRowContent.setText(noteModel.getContent());
        myViewHolder.noteRowDate.setText(noteModel.getDate());

    }


    @Override
    public int getItemCount() {
        return (noteModelList != null ? noteModelList.size() : 0);
    }

    public void removeAt(int position) {
        Log.d(TAG, " removing at position : " + position);
        noteModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, noteModelList.size());
        notifyDataSetChanged();
    }


}
