package com.hty.browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

public class FavoriteActivity extends Activity {
    EditText editText;
    ImageButton imageButton_clear;
    InputMethodManager IMM;
    ListView listView;
    SimpleCursorAdapter adapter;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        IMM = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imageButton_clear = (ImageButton) findViewById(R.id.imageButton_clear);
        imageButton_clear.setOnClickListener(new OnClickListener());
        imageButton_clear.setVisibility(View.GONE);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new EditChangedListener());
        listView = (ListView) findViewById(R.id.listView1);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String url = ((TextView) arg1.findViewById(R.id.website)).getText().toString();
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                intent.putExtra("url", url);
                setResult(RESULT_OK, intent);
                IMM.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                finish();
            }
        });
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
                String title = ((TextView) info.targetView.findViewById(R.id.title)).getText().toString();
                menu.setHeaderTitle(title);
                menu.add(0, 0, 0, "复制链接");
                menu.add(0, 1, 1, "删除");
                menu.add(0, 2, 2, "修改");
                menu.add(0, 3, 3, "分享");
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                IMM.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                return false;
            }
        });
        search(editText.getText().toString());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String[] sm = { "导出HTML", "导出CSV" };
        for (int i=0; i<sm.length; i++) {
            menu.add(0, i, i, sm[i]);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 0:
                String s = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>\n<title>收藏夹</title>\n<style>a { text-decoration:none; }\ntable { table-layout:fixed; width:100%; border-collapse:collapse; }\nth, td { border:1px solid black; padding:5px; overflow:hidden; text-overflow: ellipsis; }\n</style>\n</head>\n<body>\n<h2 align=center>收藏夹" + adapter.getCount() + "</h2>\n<table>\n<tr><th>标题</th><th>网址</th></tr>\n";
                for (int i=0; i<adapter.getCount(); i++){
                    LinearLayout layout = (LinearLayout) listView.getAdapter().getView(i, null, null);
                    TextView textView_title = (TextView) layout.findViewById(R.id.title);
                    TextView textView_website = (TextView) layout.findViewById(R.id.website);
                    s = s + "<tr><td>" + textView_title.getText().toString() + "</td><td><a href=\"" + textView_website.getText().toString() + "\" target=\"_blank\">" + textView_website.getText().toString() + "</a></td></tr>\n";
                }
                s += "</table>\n</body>\n</html>";
                writeFile("webfav.htm", s);
                break;
            case 1:
                s = "";
                for (int i=0; i<adapter.getCount(); i++){
                    LinearLayout layout = (LinearLayout) listView.getAdapter().getView(i, null, null);
                    TextView textView_title = (TextView) layout.findViewById(R.id.title);
                    TextView textView_website = (TextView) layout.findViewById(R.id.website);
                    s = s + textView_title.getText().toString().replace(",", "，") + "," + textView_website.getText().toString() + "\n";
                }
                writeFile("webfav.csv", s);
                break;
			case android.R.id.home:
                IMM.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        position = menuInfo.position;
        switch (item.getItemId()) {
            case 0:
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String url = ((TextView) menuInfo.targetView.findViewById(R.id.website)).getText().toString();
                cm.setPrimaryClip(ClipData.newPlainText("link", url));
                Toast.makeText(getApplicationContext(), "链接已复制", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                int id = Integer.parseInt(((TextView) menuInfo.targetView.findViewById(R.id.id)).getText().toString());
                DBHelper helper = new DBHelper(getApplicationContext());
                helper.del(id);
                search(editText.getText().toString());
                break;
            case 2:
                final String sid = ((TextView) menuInfo.targetView.findViewById(R.id.id)).getText().toString();
                final String stitle = ((TextView) menuInfo.targetView.findViewById(R.id.title)).getText().toString();
                final String surl = ((TextView) menuInfo.targetView.findViewById(R.id.website)).getText().toString();
                LinearLayout layout = new LinearLayout(FavoriteActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText ET_title = new EditText(FavoriteActivity.this);
                ET_title.setHint("标题");
                ET_title.setText(stitle);
                layout.addView(ET_title);
                final EditText ET_url = new EditText(FavoriteActivity.this);
                ET_title.setHint("网址");
                ET_url.setText(surl);
                layout.addView(ET_url);
                AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
                builder.setIcon(android.R.drawable.btn_star_big_on);
                builder.setTitle("修改收藏");
                builder.setView(layout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String stitle = ET_title.getText().toString();
                        String surl = ET_url.getText().toString();
                        Field field = null;
                        try {
                            //通过反射获取dialog中的私有属性mShowing
                            field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);//设置该属性可以访问
                        } catch (Exception ex) {

                        }
                        if (!stitle.equals("") && (surl.startsWith("http") || !surl.startsWith("file:///"))) {
                            DBHelper dbHelper = new DBHelper(getApplicationContext());
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("website", surl);
                            values.put("title", stitle);
                            db.update(DBHelper.TableName, values, "_id = " + sid, null);
                            IMM.hideSoftInputFromWindow(ET_title.getWindowToken(), 0);
                            search(editText.getText().toString());
                            try {
                                //关闭
                                field.set(dialog, true);
                                dialog.dismiss();
                            } catch (Exception ex) {
                            }
                        } else {
                            if (stitle.equals("")){
                                ET_title.setError("标题不能为空！");
                            }
                            if (!surl.startsWith("http") || !surl.startsWith("file:///")){
                                ET_url.setError("网址错误！");
                            }
                            try {
                                //设置dialog不可关闭
                                field.set(dialog, false);
                                dialog.dismiss();
                            } catch (Exception ex) {
                            }
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IMM.hideSoftInputFromWindow(ET_title.getWindowToken(), 0);
                        Field field = null;
                        try {
                            //通过反射获取dialog中的私有属性mShowing
                            field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);//设置该属性可以访问
                        } catch (Exception e) {
                        }
                        try {
                            field.set(dialog, true);
                            dialog.dismiss();
                        } catch (Exception e) {
                        }
                    }
                });
                builder.create().show();
                break;
            case 3:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, ((TextView) menuInfo.targetView.findViewById(R.id.title)).getText().toString() + "\n" + ((TextView) menuInfo.targetView.findViewById(R.id.website)).getText().toString());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "分享"));
                break;
        }
        return true;
    }

    class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageButton_clear:
                    editText.setText("");
                    break;
            }
        }
    }

    void search(String s) {
        DBHelper helper = new DBHelper(this);
        Cursor cursor1 = helper.query(s);
        int count = cursor1.getCount();
        setTitle("收藏夹" + count);
        String[] from = { "_id", "title", "website", "website" };
        int[] to = { R.id.id, R.id.title, R.id.website, R.id.imageView_favicon };
        adapter = new SimpleCursorAdapter(this, R.layout.favorite_row, cursor1, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            public boolean setViewValue(View view, Cursor cursor, int columnIndex){
                //Log.e(Thread.currentThread().getStackTrace()[2] + "", view.toString() + columnIndex);
                if (view.getId() == R.id.imageView_favicon) {
                    String website = cursor.getString(columnIndex);
                    if (website.startsWith("https://")) {
                        ((ImageView) view).setImageResource(android.R.drawable.ic_secure);
                    } else if (website.startsWith("http://")) {
                        ((ImageView) view).setImageResource(android.R.drawable.ic_partial_secure);
                    } else if (website.startsWith("file://")) {
                        ((ImageView) view).setImageResource(android.R.drawable.stat_notify_sdcard);
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.network);
                    }
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(adapter);
        Log.e(Thread.currentThread().getStackTrace()[2] + "", position + "");
        listView.setSelection(position);
    }

    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().equals("")){
                imageButton_clear.setVisibility(View.GONE);
            }else{
                imageButton_clear.setVisibility(View.VISIBLE);
            }
            search(s.toString());
        }
    }

    void writeFile(String filename, String s) {
        File file = new File(MainActivity.dir, filename);
        try {
            BufferedWriter BW = new BufferedWriter(new FileWriter(file, false)); //false覆盖
            if(filename.endsWith(".csv")){
                BW.write('\ufeff'); // Excel通过文件的BOM头来判断文件编码
                BW.flush();
            }
            BW.write(s);
            BW.flush();
            Toast.makeText(FavoriteActivity.this, "写文件 " + MainActivity.dir + File.separator + filename + " 成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace()[2] + "", e.toString());
        }
    }

}