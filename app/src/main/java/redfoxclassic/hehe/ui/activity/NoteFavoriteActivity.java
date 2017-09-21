package redfoxclassic.hehe.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redfoxclassic.hehe.R;
import redfoxclassic.hehe.data.favdb.DBManagerFab;
import redfoxclassic.hehe.model.NoteModel;
import redfoxclassic.hehe.ui.adapters.NoteAdapter;

public class NoteFavoriteActivity extends AppCompatActivity {

    @BindView(R.id.fav_main_rv)
    RecyclerView recyclerView;
    @BindView(R.id.activity_note_imvEmpty)
    ImageView activityNoteImvEmpty;
    @BindView(R.id.fab_note_fab)
    FloatingActionButton fabNoteFab;
    private Toolbar toolbar;

    private NoteAdapter noteAdapter;
    private LinearLayoutManager layoutManager;
    private List<NoteModel> noteModelList = new ArrayList<>();

    private final static String TAG = NoteFavoriteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_favorite);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.fav_note_toolbar);
        setSupportActionBar(toolbar);


        noteAdapter = new NoteAdapter(noteModelList, NoteFavoriteActivity.this);
        layoutManager = new LinearLayoutManager(NoteFavoriteActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);

        loadDataAll();


    }

    @OnClick(R.id.fab_note_fab)
    public void onViewClicked() {
    }

    public void loadDataAll() {
        Log.e(TAG, "loadDataAll()");
        moreGenericLoadData();

    }

    private void moreGenericLoadData() {
        noteModelList.clear();

        DBManagerFab dbManagerFab = DBManagerFab.getInstance(NoteFavoriteActivity.this);
        dbManagerFab.openDataBase();
        noteModelList = dbManagerFab.getAllNoteList();

        if (noteModelList.size() == 0) {

            recyclerView.setVisibility(View.GONE);
            activityNoteImvEmpty.setVisibility(View.VISIBLE);
            Log.e(TAG, " size : " + noteModelList.size());

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            activityNoteImvEmpty.setVisibility(View.GONE);
            Log.e(TAG, " size : " + noteModelList.size());
            noteAdapter = new NoteAdapter(noteModelList, NoteFavoriteActivity.this);
            recyclerView.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
        }

        dbManagerFab.closeDataBase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataAll();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.hideSheetPlease();
    }

}
