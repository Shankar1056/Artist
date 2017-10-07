package bigappcompany.com.artist;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ravi on 3/31/17.
 */

public class SuperActivity extends AppCompatActivity{
    ProgressDialog dialog;
    void showDailog()
    {
        dialog=new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void closeDialog()
    {
        if(dialog!=null)
            dialog.cancel();
    }
}
