/******************************************************************************
 *  Copyright (C) Cambridge Silicon Radio Limited 2014
 *
 *  This software is provided to the customer for evaluation
 *  purposes only and, as such early feedback on performance and operation
 *  is anticipated. The software source code is subject to change and
 *  not intended for production. Use of developmental release software is
 *  at the user's own risk. This software is provided "as is," and CSR
 *  cautions users to determine for themselves the suitability of using the
 *  beta release version of this software. CSR makes no warranty or
 *  representation whatsoever of merchantability or fitness of the product
 *  for any particular purpose or use. In no event shall CSR be liable for
 *  any consequential, incidental or special damages whatsoever arising out
 *  of the use of or inability to use this software, even if the user has
 *  advised CSR of the possibility of such damages.
 *
 ******************************************************************************/

package com.csr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csr.displayvalueview.R;

/**
 * Custom View for displaying data readings from a BLE sensor.
 * To use the custom attributes for the DataView in an XML layout include this line in the containing View:
 * xmlns:custom="http://schemas.android.com/apk/res-auto"
 * Then attributes are specified with "custom:x", where x is one of the custom attributes listed below.
 * Available custom attributes are:
 * 	titleText - set the text for the title.
 * 	unitText - set the text for the units.
 * 	valueText - set the text for the value.
 * 	icon - set an image resource to display in the DataView (only applicable for data_big_no_title layout).
 * 	layout - one of three possible custom layouts to use.
 *  The value of layout can be:
 *  	custom:layout="@layout/data_normal" - displays the title, unit and value in a medium sized font.
 *  	custom:layout="@layout/data_big" - same as data_normal but using a larger font for title and value.
 *  	custom:layout="@layout/data_big_no_title" - just displays the value and a unit positioned centrally 
 *  												with the value in a large font. Optional icon to the left
 *  												of the value.
 * 
 * Example XML:
 *     <com.csr.view.DataView
        android:id="@+id/heartRateData"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" 
        custom:valueText="--"
        custom:unitText="BPM"        
        custom:layout="@layout/data_big_no_title" />
 */

public class DataView extends LinearLayout {

    private static final int USER_LAYOUT = 0;

    public DataView(Context context, AttributeSet attrs, int layoutResource) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DataViewOptions, 0, 0);
        String titleText = a.getString(R.styleable.DataViewOptions_titleText);
        String unitText = a.getString(R.styleable.DataViewOptions_unitText);
        String valueText = a.getString(R.styleable.DataViewOptions_valueText);
        if (layoutResource == USER_LAYOUT) {
            // Layout not specified in constructor, so should be in XML.
            // If not found in XML then default to data_normal.
            layoutResource = a.getResourceId(R.styleable.DataViewOptions_dvlayout, R.layout.data_normal);
        }
        Drawable mIconDrawable = a.getDrawable(R.styleable.DataViewOptions_dvicon);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResource, this, true);

        final TextView title = (TextView) findViewById(R.id.title);
        final TextView unit = (TextView) findViewById(R.id.unit);
        final TextView value = (TextView) findViewById(R.id.value);
        final ImageView icon = (ImageView) findViewById(R.id.icon);

        if (unit != null)
            unit.setText(unitText);
        if (value != null)
            value.setText(valueText);
        if (title != null)
            title.setText(titleText);
        if (icon != null)
            icon.setBackground(mIconDrawable);
    }

    public DataView(Context context, AttributeSet attrs) {
        this(context, attrs, USER_LAYOUT);
    }

    public DataView(Context context) {
        this(context, null);
    }

    public void setValueText(String value) {
        ((TextView) findViewById(R.id.value)).setText(value);
    }

    public String getValueText() {
        return ((TextView) findViewById(R.id.value)).getText().toString();
    }
}
