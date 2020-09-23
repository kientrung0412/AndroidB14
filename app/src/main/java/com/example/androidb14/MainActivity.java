package com.example.androidb14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidb14.adapter.ViewAdapter;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class MainActivity extends AppCompatActivity implements ViewAdapter.OnClickItemListener, View.OnClickListener, FileManager.FileDownloadCallBack {

    private RecyclerView rcFile;
    private ViewAdapter adapter;
    private FileManager fileManager;
    private EditText edtLink;
    private Button button;
    private final String path = Environment.getExternalStorageDirectory().getPath();
    private String newPath = "";


    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        if (checkPermission(true, PERMISSIONS)) {
            loadFile(path);
        }

    }

    private void loadFile(String parent) {
        newPath = parent;
        adapter.setFiles(fileManager.getFiles(parent));
    }

    private void initViews() {
        rcFile = findViewById(R.id.rc_file);
        edtLink = findViewById(R.id.edt_link);
        button = findViewById(R.id.btn_download);
        fileManager = new FileManager();
        adapter = new ViewAdapter(getLayoutInflater());
        rcFile.setAdapter(adapter);
        adapter.setListener(this);
        button.setOnClickListener(this);
    }

    private boolean checkPermission(boolean withRequest, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permissions) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    if (withRequest) {
                        requestPermissions(permissions, 0);
                    }
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission(false, PERMISSIONS)) {
            loadFile(Environment.getExternalStorageDirectory().getPath());
        }
    }


    @Override
    public void onBackPressed() {
        if (newPath.equals(path)) {
            super.onBackPressed();
            return;
        }
        if (!path.isEmpty()) {
            loadFile(newPath + "/..");
        }
    }

    @Override
    public void onClickDirectory(File file) {
        if (file != null) {
            loadFile(file.getPath());
        }
    }

    @Override
    public void onClickFile(File file) {

    }


    @Override
    public void onClick(View v) {
        String link = edtLink.getText().toString();
        String type = ".jpg";
        fileManager.download(link, type, this);
    }

    @Override
    public void onSuccess(String path) {
//        Toast.makeText(this, "Luu thanh cong vao: " + path, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setData(Uri.parse(path));
        startActivity(intent);
    }
    @Override
    public void onFail(Exception ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}