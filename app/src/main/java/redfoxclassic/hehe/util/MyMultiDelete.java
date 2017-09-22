package redfoxclassic.hehe.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import redfoxclassic.hehe.data.maindb.DBManager;
import redfoxclassic.hehe.model.NoteModel;
import redfoxclassic.hehe.ui.activity.MainActivity;

public class MyMultiDelete {
    private final static String TAG = MyMultiDelete.class.getSimpleName();

    public static void deleteMulti(final Context context, int position,
                                   List<NoteModel> wishModelList, DBManager dbManager) {

       // Log.i(TAG, "inside deleteMulti()");
        System.out.println(TAG + " -  " + position);
        NoteModel wishModel = new NoteModel();
        wishModel.setId(wishModelList.get(position).getId());

        int result = dbManager.deleteNote(wishModelList.get(position).getId());

        if (result > 0) {
            Log.i(TAG, " successfully MULTI deleted ");


            //notifying after delete
           // ((MainActivity) context).setPositionNotifyDelete(position);


            ((MainActivity) context).loadDataAll();

            //MySnackBarUtil.showSnackBar((MainActivity) view.getContext(), MyConstants.SNACKBAR_DELETE, "delete");

        } else {
           // Log.i(TAG, " oops ! Not able to delete ");
            Toast.makeText(context, "Unable to Delete !!", Toast.LENGTH_SHORT).show();
        }


    }
}
