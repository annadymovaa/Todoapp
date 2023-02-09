package com.hse.todo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TagActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private EditText editTextTag;
    private final String saveKey = "save_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        init();
    }

    public void onClickSave(View view) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(saveKey, editTextTag.getText().toString());
        edit.apply();
    }

    private void init() {
        pref = getSharedPreferences("tags", MODE_PRIVATE);
        editTextTag = findViewById(R.id.editTextTag);
    }
}
