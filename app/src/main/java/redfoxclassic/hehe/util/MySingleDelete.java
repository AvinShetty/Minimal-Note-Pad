package redfoxclassic.hehe.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.List;

import redfoxclassic.hehe.model.NoteModel;
import redfoxclassic.hehe.ui.adapters.NoteAdapter;

public class MySingleDelete {

    private final static String TAG = MySingleDelete.class.getSimpleName();


    public static void deleteDialog(final Context context, final NoteAdapter.MyViewHolder myViewHolder,
                                    final List<NoteModel> wishModelList, final View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Do you want to delete !");
        builder.setCancelable(false);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                deleteOperation(context, myViewHolder, wishModelList, view, dialogInterface);

            }
        });
        builder.setNeutralButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private static void deleteOperation(final Context context, NoteAdapter.MyViewHolder myViewHolder,
                                        List<NoteModel> wishModelList, View view, DialogInterface dialogInterface) {

    /*    DBManagerFab dbManager = new DBManagerFab(context);
        dbManager.openDataBase();

        NoteModel wishModel = new NoteModel();
        wishModel.setId(wishModelList.get(myViewHolder.getAdapterPosition()).getId());

        int result = dbManager.deleteWish(wishModelList.get(myViewHolder.getAdapterPosition()).getId());

        if (result > 0) {
            Log.i(TAG, " successfully deleted ");


            //notifying after delete
            ((MainActivity) context).setPositionNotifyDelete(MiscellaneousUtil.getAdapterPositionMiss());


            ((MainActivity) context).loadDataAll();
            dialogInterface.dismiss();
            MySnackBarUtil.showSnackBar((MainActivity) view.getContext(), MyConstants.SNACKBAR_DELETE, "delete");

        } else {
            Log.i(TAG, " oops ! Not able to delete ");
            Toast.makeText(context, "Unable to Delete !!", Toast.LENGTH_SHORT).show();
        }

        dbManager.closeDataBase();*/

    }
}
