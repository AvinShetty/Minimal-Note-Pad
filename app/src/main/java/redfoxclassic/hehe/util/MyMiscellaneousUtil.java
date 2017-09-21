package redfoxclassic.hehe.util;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import redfoxclassic.hehe.model.NoteModel;

public class MyMiscellaneousUtil {

    public static void shareData(String Title, String Content, Context context) {

        String shareBody = "Title : " + Title + "\n" +
                " Content : " + Content;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "HEY ");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share your Note with ..."));
    }


    public static void shareDataOnClick(int adapterPosition, List<NoteModel> noteModelList, Context context) {

        String Title = noteModelList.get(adapterPosition).getTitle();
        String Content = noteModelList.get(adapterPosition).getContent();
        String shareBody = "Title : " + Title + "\n" +
                " Content : " + Content;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plan");
        intent.putExtra(Intent.EXTRA_SUBJECT, "HEY ");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(intent, "Share your Note with ..."));

    }
}
