package bigappcompany.com.artist.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 8/18/16.
 */
public class ImageFrameLayout extends FrameLayout {

    public ImageFrameLayout(final Context context) {
        super(context);
    }

    public ImageFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > height) {
            setMeasuredDimension(height, height);
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            setMeasuredDimension(width, width);
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }
}
