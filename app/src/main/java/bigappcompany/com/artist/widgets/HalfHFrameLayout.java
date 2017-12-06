package bigappcompany.com.artist.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class HalfHFrameLayout extends FrameLayout {

    public HalfHFrameLayout(final Context context) {
        super(context);
    }

    public HalfHFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfHFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        /*if (width > height) {
            setMeasuredDimension(width, width);
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {*/
            setMeasuredDimension(width, height/2);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec/2);
        //}
    }
}
