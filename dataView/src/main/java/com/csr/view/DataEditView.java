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

import com.csr.displayvalueview.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Custom View that allows the displayed value to be edited by the user.
 * 
 */
public class DataEditView extends DataView {

    EditText editView = null;

    public DataEditView(Context context, AttributeSet attrs) {
        // Force the edit layout to be used so that user can't modify it in xml.
        super(context, attrs, R.layout.data_normal_edit);
        editView = (EditText) findViewById(R.id.value);
    }

    public DataEditView(Context context) {
        this(context, null);
    }

    /**
     * Allows a listener to be specified to handle edit actions.
     * 
     * @param l
     *            The listener to execute.
     */
    public void setOnEditorActionListener(TextView.OnEditorActionListener l) {
        editView.setOnEditorActionListener(l);
    }

    /**
     * Allows a listener to be specified to handle change of focus on the editable value field.
     * 
     * @param l
     *            The listener to execute.
     */
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        editView.setOnFocusChangeListener(l);
    }

    public void hideSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }
}
