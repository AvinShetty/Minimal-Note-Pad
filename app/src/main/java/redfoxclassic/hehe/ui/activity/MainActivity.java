package redfoxclassic.hehe.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.DimOverlayFrameLayout;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redfoxclassic.hehe.Fab;
import redfoxclassic.hehe.R;
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
    private LinearLayoutManager layoutManager;
    private List<NoteModel> noteModelList = new ArrayList<>();

    private Integer adapterClickedPosition;

    private Toolbar toolbar;

    private static int notifyValueAdd = 0;
    private static int notifyValueUpdate = 0;


    private SearchView searchView;


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
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);

        touchListenerRecycler();

        loadDataAll();
        numberOfRecords();


    }

    void setupToolbar() {

        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.theme_primary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.theme_primary_dark2));
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(TAG, "onNewIntent()");
        super.onNewIntent(intent);
        setIntent(intent);

        loadDataAll();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Log.e(TAG, "onResume()");
        loadDataAll();

        if (notifyValueAdd == 1) {
            MySnackBarUtil.showSnackBar(MainActivity.this, MyConstants.SNACKBAR_ADD, "saved");
            notifyValueAdd = 0;
        }
        if (notifyValueUpdate == 1) {
            MySnackBarUtil.showSnackBar(MainActivity.this, MyConstants.SNACKBAR_UPDATE, "update");
            notifyValueUpdate = 0;
        }


    }

    public static void notifySnackbarAdd(int value) {
        notifyValueAdd = value;
    }

    public static void notifySnackbarUpdate(int value) {
        notifyValueUpdate = value;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void loadDataAll() {
        Log.e(TAG, "loadDataAll()");
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
            Log.e(TAG, " size : " + noteModelList.size());

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            imvEmpty.setVisibility(View.GONE);
            Log.e(TAG, " size : " + noteModelList.size());
            noteAdapter = new NoteAdapter(noteModelList, MainActivity.this);
            recyclerView.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
        }

        dbManager.closeDataBase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //this i for searchView widget, a bit different
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();


        searchViewCloseBtnListener();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               /* Animation slideLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left2);
                toolbar.setAnimation(slideLeft);*/
                toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.toolbar_search));
                getSearchQuery(newText);
                return false;
            }
        });

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

    // Material Sheet Fab Operation
    //----------------------------------------------------------------------------------------------------------------------------
    @OnClick({R.id.fab_sheet_item_favorite, R.id.fab_sheet_item_create_note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_sheet_item_favorite:
                Log.e(TAG, "item_Fav clicked)");
                break;
            case R.id.fab_sheet_item_create_note:
                Log.e(TAG, "item_create clicked)");

                transitionToActivity();
                break;
        }
    }


    private void setupFab() {

        Log.e(TAG, "setupFab()");
        Fab fab = (Fab) findViewById(R.id.fab);

        //Life Saver
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

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public static void hideSheetPlease() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        }
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
    //----------------------------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Log.e(TAG, "setupWindowAnimations()");
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.START);
        slideTransition.setDuration(800);
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }


    @SuppressWarnings("unchecked")
    private void transitionToActivity() {
        Intent i = new Intent(MainActivity.this, NoteAddActivity.class);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        startActivity(i, transitionActivityOptions.toBundle());
    }
    //----------------------------------------------------------------------------------------------------------


    private void touchListenerRecycler() {

        recyclerView.addOnItemTouchListener(new MyRecyclerTouchListener(MainActivity.this, recyclerView,
                new MyRecyclerTouchListener.ClickListenerTouch() {
                    @Override
                    public void onClickTouch(View view, int position) {
                        Log.e(TAG, "onItemClick at :" + position);

                        adapterClickedPosition = position;

                        NoteModel noteModel = noteModelList.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putString(MyConstants.WISH_TITLE, noteModel.getTitle());
                        bundle.putString(MyConstants.WISH_CONTENT, noteModel.getContent());
                        bundle.putInt(MyConstants.WISH_POSITION, noteModelList.get(position).getId()); //return 3
                        bundle.putString(MyConstants.WISH_DATE, noteModel.getDate());
                        Log.e(TAG, " bundle entered " + bundle.toString());

                        startActivity(new Intent(MainActivity.this, NoteUpdateActivity.class).putExtras(bundle));

                    }

                    @Override
                    public void onLongClickTouch(View view, int position) {
                        Log.e(TAG, "onItem LONG Click at :" + position);

                        registerForContextMenu(view);
                        adapterClickedPosition = position;


                    }
                }));
    }
    //---------------------------------------------------------------------------------------------------------------


    private void numberOfRecords() {

        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDataBase();
        Log.i(TAG, " Total Number of Records Available " + dbManager.getTotalNumberOfRecords());
        dbManager.closeDataBase();

    }

    @Override
    public void onBackPressed() {

        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();

        }
        if (!searchView.isIconified()) {
            System.out.println("--------------------________________---------------");
            searchView.onActionViewCollapsed();
          /*  Animation slideRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_right2);
            toolbar.setAnimation(slideRight);*/
            toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.theme_primary));
            loadDataAll();
        } else {
            super.onBackPressed();
        }

    }


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
                break;
            default:

        }

        return true;
    }


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

        //have the adapter position
        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDataBase();


        NoteModel noteModel = new NoteModel();
        noteModel.setId(noteModelList.get(adapterClickedPosition).getId());


        int status = dbManager.deleteNote(noteModelList.get(adapterClickedPosition).getId());
        if (status > 0) {
            //now remove from list
            noteAdapter.removeAt(adapterClickedPosition);
            MySnackBarUtil.showSnackBar(MainActivity.this, MyConstants.SNACKBAR_DELETE, "delete");

        }
        dbManager.closeDataBase();
        loadDataAll();
    }

    private void getSearchQuery(String query) {
        Log.e(TAG, "query : " + query);

        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        dbManager.openDataBase();

        noteModelList = dbManager.experiment(query);

        noteAdapter = new NoteAdapter(noteModelList, MainActivity.this);
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();

        System.out.println("count");
        dbManager.closeDataBase();


    }

    private void searchViewCloseBtnListener() {
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
               /* Animation slideRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_right2);
                toolbar.setAnimation(slideRight);*/
                toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.theme_primary));
                return false;
            }
        });

    }

}
