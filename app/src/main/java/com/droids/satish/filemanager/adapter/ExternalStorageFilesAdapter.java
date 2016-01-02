package com.droids.satish.filemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.droids.satish.filemanager.activity.ExternalStorageFragment;
import com.droids.satish.filemanager.model.ExternalStorageFilesModel;
import com.example.satish.filemanager.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Satish on 26-12-2015.
 */
public class ExternalStorageFilesAdapter extends BaseAdapter implements Filterable {
    public CustomListener customListener;
    private LayoutInflater inflater;
    private Activity activity;
    private ArrayList<ExternalStorageFilesModel> filesModelArrayList;
    private String fileExtension;

    public ExternalStorageFilesAdapter(ArrayList<ExternalStorageFilesModel> filesModelArrayList, FragmentActivity activity) {
        this.activity = activity;
        this.filesModelArrayList = filesModelArrayList;
    }

    public void setCustomListener(ExternalStorageFragment customListener) {
        this.customListener = customListener;
    }

    @Override
    public int getCount() {
        return filesModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return filesModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            view = inflater.inflate(R.layout.files_item_view, null);
        TextView lblFileName = (TextView) view.findViewById(R.id.file_name);
        TextView lblFilePath = (TextView) view.findViewById(R.id.file_path);
        ImageView imgItemIcon = (ImageView) view.findViewById(R.id.icon);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        final ExternalStorageFilesModel model = filesModelArrayList.get(position);
        fileExtension = model.getFileName().substring(model.getFileName().lastIndexOf(".") + 1);//file extension

        if (model.isDir()) {//if list item folder the set icon
            imgItemIcon.setImageResource(R.mipmap.ic_folder);
        } else if (fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {//if list item any image then
            File imgFile = new File(model.getFilePath());
            if (imgFile.exists()) {
                Log.d("action", model.getFilePath());
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgItemIcon.setImageBitmap(myBitmap);
            }
        } else if (fileExtension.equals("pdf")) {
            imgItemIcon.setImageResource(R.mipmap.ic_pdf);
        } else if (fileExtension.equals("mp3")) {
            imgItemIcon.setImageResource(R.mipmap.ic_mp3);
        } else if (fileExtension.equals("txt")) {
            imgItemIcon.setImageResource(R.mipmap.ic_file);
        } else if (fileExtension.equals("zip") || fileExtension.equals("rar")) {
            imgItemIcon.setImageResource(R.mipmap.ic_zip);
        } else if (fileExtension.equals("html") || fileExtension.equals("xml")) {
            imgItemIcon.setImageResource(R.mipmap.ic_html_xml);
        } else imgItemIcon.setImageResource(R.mipmap.ic_unknown_file);
        lblFileName.setText(model.getFileName());
        if (model.getFileName().equals("/")) {
            lblFilePath.setText("/sdcard");
            lblFileName.setText("parent");
            imgItemIcon.setImageResource(R.mipmap.ic_parent_folder);
        } else {
            lblFilePath.setText(model.getFilePath());
        }
        checkBox.setChecked(model.isSelected());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.isSelected()) {
                    checkBox.setChecked(true);
                    customListener.isCheckboxSelectedListener(position, checkBox.isChecked());
                } else {
                    checkBox.setChecked(false);
                    customListener.isCheckboxSelectedListener(position, checkBox.isChecked());
                }
            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filesModelArrayList = (ArrayList<ExternalStorageFilesModel>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<ExternalStorageFilesModel> FilteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = filesModelArrayList;
                    results.count = filesModelArrayList.size();
                } else {
                    for (int i = 0; i < filesModelArrayList.size(); i++) {
                        ExternalStorageFilesModel data = filesModelArrayList.get(i);
                        FilteredList.add(data);
                    }
                    results.values = FilteredList;
                    results.count = FilteredList.size();
                }
                return results;
            }
        };
        return filter;
    }

    public interface CustomListener {
        void isCheckboxSelectedListener(int position, boolean isChecked);
    }
}