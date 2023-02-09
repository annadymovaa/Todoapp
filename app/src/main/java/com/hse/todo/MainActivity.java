package com.hse.todo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hse.todo.adapter.OnTodoClickL;
import com.hse.todo.adapter.RecViewAdapter;
import com.hse.todo.data.Repository;
import com.hse.todo.model.Priority;
import com.hse.todo.model.SharedViewModel;
import com.hse.todo.model.Task;
import com.hse.todo.model.TaskView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickL {

    private TaskView taskView;
    private RecyclerView recyclerView;
    private RecViewAdapter recViewAdapter;
    private BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;
    private int PERMISSION_CODE = 1;
    private Button permissionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button tagsButton = findViewById(R.id.tags);
        tagsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, TagActivity.class);
            startActivity(intent);
        });

        permissionButton = findViewById(R.id.add_calendar);
        permissionButton.setOnClickListener(view14 -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Вы уже дали на это разрешение", Toast.LENGTH_LONG).show();
            } else {
                requestCalendarPermission();
            }
        });

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        taskView = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskView.class);

        taskView.getAllTasks().observe(this, tasks -> {
            recViewAdapter = new RecViewAdapter(tasks, this);
            recyclerView.setAdapter(recViewAdapter);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showBottomSheetDialog();
        });
    }


    private void requestCalendarPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {
            new AlertDialog.Builder(this).setTitle("Нужно разрешение").
                    setMessage("Разрешение нужно, чтобы приложение смогло добавлять задачи в ваш календарь")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String [] {
                                    Manifest.permission.WRITE_CALENDAR}, PERMISSION_CODE);
                        }
                    }).setNegativeButton("нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String [] {
                    Manifest.permission.WRITE_CALENDAR}, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение получено", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Разрешение отклонено", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(),
                bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_tags) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.selectItem(task);
        sharedViewModel.setIsEdit(true);
        showBottomSheetDialog();
    }

    @Override
    public void onTodoRadioButtonClick(Task task) {
        TaskView.delete(task);
        recViewAdapter.notifyDataSetChanged();
    }
}