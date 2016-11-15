package com.hcz.core.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.hcz.core.utils.FileUtils;
import com.hcz.R;

import java.io.File;
import java.util.List;

/**
 * Created by chz on 2015/6/16.
 */
public class SimpleFileSelector extends Activity implements View.OnClickListener{

    private static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String RESULT_PATH = "result_path";
    public static final String ACTION_TYPE = "action_type";
    public static final int ACTION_TYPE_DIR = 0;
    public static final int ACTION_TYPE_FILE = 1;

    private int mAction;

    private View mHeaderView, mOkBtn;
    private TextView mPath;
    private ListView mListView;

    private File mCurrentDir;
    private List<File> mCurrentFiles;

    private FileAdapter mFileAdapter;

    public static void startFileSelector(Activity activity, int type){
        Intent intent = new Intent(activity, SimpleFileSelector.class);
        intent.putExtra(SimpleFileSelector.ACTION_TYPE, type);
        activity.startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_selector_activity);
        Intent intent = getIntent();
        if(intent == null){
            finish();
        }
        mAction = intent.getIntExtra(ACTION_TYPE, -1);
        if(mAction == -1){
            finish();
        }

        findViewById(R.id.file_selector_back).setOnClickListener(this);
        mOkBtn = findViewById(R.id.file_selector_ok);
        mOkBtn.setOnClickListener(this);
        if(mAction == ACTION_TYPE_FILE){
            mOkBtn.setVisibility(View.GONE);
        }
        mCurrentDir = Environment.getExternalStorageDirectory();
        mPath = (TextView)findViewById(R.id.file_selector_path);
        mPath.setText(mCurrentDir.getAbsolutePath());

        mCurrentFiles = FileUtils.getFileList(mCurrentDir);
        mListView = (ListView)findViewById(R.id.file_selector_list);
        mListView.addHeaderView(getHeaderView());
        mHeaderView.setVisibility(View.GONE);
        mFileAdapter = new FileAdapter();
        mListView.setAdapter(mFileAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < mListView.getHeaderViewsCount()){
                    if(!mCurrentDir.getAbsolutePath().equals(ROOT)) {
                        refreshList(mCurrentDir.getParentFile());
                    }
                    return;
                }
                File file = mCurrentFiles.get(position - mListView.getHeaderViewsCount());
                if(file.isDirectory()){
                    refreshList(file);
                }
                else{
                    if(mAction == ACTION_TYPE_FILE){
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_PATH, file.getAbsolutePath());
                        setResult(0, intent);
                        finish();
                    }
                }
            }
        });
    }

    private View getHeaderView(){
        View view = LayoutInflater.from(this).inflate(R.layout.file_selector_item, null);
        mHeaderView = view.findViewById(R.id.file_selector_item_layout);
        ImageView img = (ImageView)mHeaderView.findViewById(R.id.file_selector_item_img);
        img.setImageResource(R.drawable.folder);
        TextView text = (TextView)mHeaderView.findViewById(R.id.file_selector_item_name);
        text.setText("返回上一级");
        return view;
    }

    private void refreshList(File dir){
        mCurrentDir = dir;
        mPath.setText(mCurrentDir.getAbsolutePath());
        mCurrentFiles = FileUtils.getFileList(mCurrentDir);
        mFileAdapter.notifyDataSetChanged();
        if(dir.getAbsolutePath().equals(ROOT)){
            mHeaderView.setVisibility(View.GONE);
        }
        else {
            mHeaderView.setVisibility(View.VISIBLE);
        }
        mListView.setSelection(0);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.file_selector_back){
            finish();
        }
        else if(id == R.id.file_selector_ok){
            Intent intent = new Intent();
            intent.putExtra(RESULT_PATH, mCurrentDir.getAbsolutePath());
            setResult(0, intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mCurrentDir.getAbsolutePath().equals(ROOT)){
                return super.onKeyDown(keyCode, event);
            }
            else{
                refreshList(mCurrentDir.getParentFile());
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    class FileAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if(mCurrentFiles == null){
                return 0;
            }
            return mCurrentFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if(convertView == null){
                convertView = LayoutInflater.from(SimpleFileSelector.this).inflate(R.layout.file_selector_item, null);
                viewHolder.setImageView((ImageView)convertView.findViewById(R.id.file_selector_item_img));
                viewHolder.setNameView((TextView)convertView.findViewById(R.id.file_selector_item_name));
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder)convertView.getTag();
            File file = mCurrentFiles.get(position);
            if(file.isDirectory()){
                viewHolder.getImageView().setImageResource(R.drawable.folder);
            }
            else{
                viewHolder.getImageView().setImageResource(R.drawable.file);
            }
            viewHolder.getNameView().setText(file.getName());
            return convertView;
        }
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView nameView;
        public ImageView getImageView() {
            return imageView;
        }
        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
        public TextView getNameView() {
            return nameView;
        }
        public void setNameView(TextView nameView) {
            this.nameView = nameView;
        }
    }
}
