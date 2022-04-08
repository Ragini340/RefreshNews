package com.kiit.eee.ragini.refreshnews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.kiit.eee.ragini.refreshnews.R;

/**
 * Created by 1807340_RAGINI on 08,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class CustomButton extends AppCompatButton {
    private final static int LIGHT = 0;
    private final static int REGULAR = 1;
    private final static int MEDIUM = 2;

    public CustomButton(@NonNull Context context) {
        super(context);
    }

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        //The value 0 is a default, but shouldn't ever be used since the attr is an enum
        int typeface = values.getInt(R.styleable.CustomView_typeface, 0);
        setTypeface(context, typeface);
    }

    private Typeface setTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = null;

        switch (typefaceValue) {

            case REGULAR:

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/helvetic_normal.ttf");
                setTypeface(typeface);
                break;

            case LIGHT:

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_light.ttf");
                setTypeface(typeface);
                break;

            case MEDIUM:

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_medium.ttf");
                setTypeface(typeface);
                break;


            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }
}
