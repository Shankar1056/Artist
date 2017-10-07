package bigappcompany.com.artist.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import bigappcompany.com.artist.R;


/**
 * Created by Admin on 18-07-2016.
 */
public class MyCustomBoldButton extends Button {
    public MyCustomBoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),getResources().getString(R.string.bold)));
    }
}
