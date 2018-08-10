package com.shenjing.dimension.dimension.base.select_picture;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.device.DeviceInfo;


/**
 * Created by yons on 2018/2/23.
 * Desc:
 */

public class Utils {


    //--------------通用对话框------------
    public static Dialog showFolderListDialog(final Context context, FolderAdapter mFolderAdapter, AdapterView.OnItemClickListener listener, CharSequence categoryText){
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_multi_image_folder);
        android.view.WindowManager.LayoutParams windowLp = dialog.getWindow().getAttributes();
        windowLp.width = (int)(DeviceInfo.getScreenWidth(context));
        dialog.getWindow().setAttributes(windowLp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.dialog_animation_300);

        ListView folderList = (ListView) dialog.findViewById(R.id.folderList);
        TextView category_btn = (TextView) dialog.findViewById(R.id.category_btn);
        category_btn.setText(categoryText);
        folderList.setAdapter(mFolderAdapter);
        int index = mFolderAdapter.getSelectIndex();
        index = index == 0 ? index : index - 1;
        folderList.setSelection(index);
        folderList.setOnItemClickListener(listener);
        category_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
}
