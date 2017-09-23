package redfoxclassic.hehe.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.DimOverlayFrameLayout;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redfoxclassic.hehe.Fab;
import redfoxclassic.hehe.R;
import redfoxclassic.hehe.data.favdb.DBManagerFav;
import redfoxclassic.hehe.data.maindb.DBManager;
import redfoxclassic.hehe.model.NoteModel;
import redfoxclassic.hehe.ui.adapters.NoteAdapter;
import redfoxclassic.hehe.util.MyConstants;
import redfoxclassic.hehe.util.MyMiscellaneousUtil;
import redfoxclassic.hehe.util.MyRecyclerTouchListener;
import redfoxclassic.hehe.util.MySnackBarUtil;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_rv)
    RecyclerView recyclerView;
    @BindView(R.id.overlay)
    DimOverlayFrameLayout overlay;
    @BindView(R.id.fab_sheet_item_favorite)
    TextView fabSheetFav;
    @BindView(R.id.fab_sheet_item_create_note)
    TextView fabSheetCreate;
    @BindView(R.id.main_cardView)
    CardView mainCardView;
    @BindView(R.id.activity_main_imvEmpty)
    ImageView imvEmpty;


    private int statusBarColor;
    private static MaterialSheetFab materialSheetFab;

    private NoteAdapter noteAdapter;
    private List<NoteModel> noteModelList = new ArrayList<>();

    private Integer adapterClickedPosition;

    private Toolbar toolbar;

    private static int notifyValueAdd = 0;
    private static int notifyValueUpdate = 0;
    private static int notifyFavourite = 0;


    private MaterialSearchView materialSearchView;

    private StaggeredGridLayoutManager layoutManager;


    //Defined globally to remove callbacks when unused
    private Handler handler = new Handler();
    private Runnable runnableForAdd = new Runnable() {
        @Override
        public void run() {

            transitionToNoteAddActivity();

        }
    };
    private Runnable runnableForFav = new Runnable() {
        @Override
        public void run() {
            transitionToFavouriteActivity();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        if (BuildConfig.DEBUG) {          //by default returns false, but in production true :
            Log.e(TAG, "onCreate()");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
        setupFab();

        setupToolbar();

        noteAdapter = new NoteAdapter(noteModelList, MainActivity.this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);

        touchListenerRecycler();

        loadDataAll();
        numberOfRecords();


    }
    //---------------------------------------------------------------------------------------------------------------

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        loadDataAll();

    }
    //---------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume()");
        super.onResume();
        loadDataAll();

        if (notifyValueAdd == 1) {
            MySnackBarUtil.showSnackBar(MainActivity.this, MyConstants.SNACKBAR_ADD, "saved");
            materialSheetFab.showFab();
            notifyValueAdd = 0;
        }
        if (notifyValueUpdate == 1) {
            MySnackBarUtil.showSnackBar(MainActivity.this, MyConstants.SNACKBAR_UPDATE, "update");
            materialSheetFab.showFab();
            notifyValueUpdate = 0;
        }
        if (notifyFavourite == 1) {
            materialSheetFab.showFab();
            notifyFavourite = 0;
        }


    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause()");
        super.onPause();
    }

    //Methods to notify snackbar and show FAB
    public static void notifySnackbarAdd(int value) {
        notifyValueAdd = value;
    }

    public static void notifyFavourite(int value) {
        notifyFavourite = value;

    }

    public static void notifySnackbarUpdate(int value) {
        notifyValueUpdate = value;
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop()");
        super.onStop();
        hideSoftKeyboard();
        if (handler != null && runnableForAdd != null) {
            handler.removeCallbacksAndMessages(runnableForAdd);
        }
        if (handler != null && runnableForFav != null) {
            handler.removeCallbacksAndMessages(runnableForFav);
        }
    }
    //---------------------------------------------------------------------------------------------------------------

    void setupToolbar() {

        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.theme_primary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.theme_primary_dark2));
        }
    }

    //---------------------------------------------------------------------------------------------------------------

    private void setupFab() {

        Fab fab = (Fab) findViewById(R.id.fab);
        fab.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.fab_sheet_bg), PorterDuff.Mode.SRC_IN);

        int sheetColor = ContextCompat.getColor(MainActivity.this, R.color.background_card);
        int fabColor = ContextCompat.getColor(MainActivity.this, R.color.fab_sheet_bg);

        materialSheetFab = new MaterialSheetFab<>(fab, mainCardView, overlay, sheetColor, fabColor);

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                statusBarColor = getStatusBarColor();
                setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                setStatusBarColor(statusBarColor);
            }
        });

    }

    //----------------------------------------------------------------------------------------------------------------------------
    @OnClick({R.id.fab_sheet_item_favorite, R.id.fab_sheet_item_create_note})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.fab_sheet_item_favorite:
                materialSheetFab.hideSheetThenFab();
                materialSearchView.closeSearch();
                handler.postDelayed(runnableForFav, 500);

                break;
            case R.id.fab_sheet_item_create_note:

                materialSheetFab.hideSheetThenFab();
                materialSearchView.closeSearch();

                handler.postDelayed(runnableForAdd, 500);
                break;
        }
    }


    @SuppressWarnings("unchecked")
    private void transitionToNoteAddActivity() {
        Intent i = new Intent(MainActivity.this, NoteAddActivity.class);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        startActivity(i, transitionActivityOptions.toBundle());
    }

    @SuppressWarnings("unchecked")
    private void transitionToFavouriteActivity() {
        Intent i = new Intent(MainActivity.this, NoteFavoriteActivity.class);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        startActivity(i, transitionActivityOptions.toBundle());
    }


    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }


    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------


    private void touchListenerRecycler() {

        recyclerView.addOnItemTouchListener(new MyRecyclerTouchListener(MainActivity.this, recyclerView,
                new MyRecyclerTouchListener.ClickListenerTouch() {
                    @Override
                    public void onClickTouch(View view, int position) {

                        adapterClickedPosition = position;

                        NoteModel noteModel = noteModelList.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putString(MyConstants.WISH_TITLE, noteModel.getTitle());
                        bundle.putString(MyConstants.WISH_CONTENT, noteModel.getContent());
                        bundle.putInt(MyConstants.WISH_POSITION, noteModelList.get(position).getId()); //return 3
                        bundle.putString(MyConstants.WISH_DATE, noteModel.getDate());

                        //   startActivity(new Intent(MainActivity.this, NoteUpdateActivity.class).putExtras(bundle));

                        Intent i = new Intent(MainActivity.this, NoteUpdateActivity.class);
                        i.putExtras(bundle);
                        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                        startActivity(i, transitionActivityOptions.toBundle());

                    }

                    @Override
                    public void onLongClickTouch(View view, int position) {

                        registerForContextMenu(view);
                        adapterClickedPosition = position;


                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        materialSearchView.setMenuItem(menuItem);


        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                getSearchQuery(newText);
                //onActivityResult(1,1,newText);
                return false;
            }
        });

        materialSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "NOT YET IMPLEMENTED", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //---------------------------------------------------------------------------------------------------------------


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.context_delete:
                deleteDialogOperation();

                break;
            case R.id.context_share:
                MyMiscellaneousUtil.shareDataOnClick(adapterClickedPosition, noteModelList, MainActivity.this);

                break;
            case R.id.context_addToFav:

                DBManagerFav dbManagerFav = DBManagerFav.getInstance(MainActivity.this);
                dbManagerFav.openDataBase();

                NoteModel noteModel = new NoteModel();
                noteModel.setTitle(noteModelList.get(adapterClickedPosition).getTitle());
                noteModel.setContent(noteModelList.get(adapterClickedPosition).getContent());
                noteModel.setDate(noteModelList.get(adapterClickedPosition).getDate());
                noteModel.setId(noteModelList.get(adapterClickedPosition).getId());

                boolean status = dbManagerFav.insertNoteFav(noteModel);
                if (status == true) {
                    Toast.makeText(MainActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                }
                dbManagerFav.closeDataBase();

                break;
            default:

        }

        return true;
    }
    //---------------------------------------------------------------------------------------------------------------


    private void deleteDialogOperation() {

        new AlertDialog.Builder(MainActivity.this).setCancelable(false)
                .setMessage("Delete Selected Note ?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteOperation();
                        dialogInterface.dismiss();
                    }
                }).setNeutralButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        }).show();
    }


    private void deleteOperation() {
        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDataBase();

        NoteModel noteModel = new NoteModel();
        noteModel.setId(noteModelList.get(adapterClickedPosition).getId());

        int status = dbManager.deleteNote(noteModelList.get(adapterClickedPosition).getId());
        if (status > 0) {
            noteAdapter.removeAt(adapterClickedPosition);
            MySnackBarUtil.showSnackBar(MainActivity.this, MyConstants.SNACKBAR_DELETE, "delete");

        }
        dbManager.closeDataBase();
        //  loadDataAll();
    }
    //---------------------------------------------------------------------------------------------------------------

    private void getSearchQuery(String query) {

        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDataBase();

        noteModelList = dbManager.experiment(query);

        noteAdapter = new NoteAdapter(noteModelList, MainActivity.this);
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
        dbManager.closeDataBase();


    }
    //---------------------------------------------------------------------------------------------------------------

    private void numberOfRecords() {

        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDataBase();
        dbManager.closeDataBase();

    }

    //----------------------------------------------------------------------------------------------------------------
    public void loadDataAll() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        moreGenericLoadData();

    }

    private void moreGenericLoadData() {
        noteModelList.clear();

        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDataBase();

        noteModelList = dbManager.getAllNoteList();

        if (noteModelList.size() == 0) {

            recyclerView.setVisibility(View.GONE);
            imvEmpty.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            imvEmpty.setVisibility(View.GONE);
            noteAdapter = new NoteAdapter(noteModelList, MainActivity.this);
            recyclerView.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
        }

        dbManager.closeDataBase();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slideExit = new Slide();
        slideExit.setSlideEdge(Gravity.START);
        slideExit.setDuration(800);
        slideExit.excludeTarget(R.id.toolbar, true);
/*
        slideExit.excludeTarget(View.SYSTEM_UI_FLAG_LOW_PROFILE, true); //System Bar
*/
        getWindow().setExitTransition(slideExit);
        //  getWindow().setReenterTransition(slideExit);


    }


    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {

        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();

        } else {
            if (materialSearchView.isSearchOpen()) {
                materialSearchView.closeSearch();
                toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.theme_primary));
                loadDataAll();
            } else {
                super.onBackPressed();
            }
        }
    }

}
