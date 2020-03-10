package com.master.molemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.master.molemate.RoomDB.Entities.Entity_Users;
import com.master.molemate.RoomDB.MoleMateDB;
import com.master.molemate.RoomDB.MoleMateDB_Repository;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MoleMateDB moleMateDB;
    private MoleMateDB_Repository moleMateDBRepository;
    private MoleMateDB_ViewModel moleMateDBViewModel;
    private List<Entity_Users>users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "The User " + users.get(0).getUid()+"", Toast.LENGTH_LONG).show();
            }
        });

        moleMateDB = moleMateDB.getInstance(this);
        moleMateDBRepository = new MoleMateDB_Repository(moleMateDB);
        moleMateDBViewModel = new MoleMateDB_ViewModel(moleMateDBRepository);

        moleMateDBViewModel.getAllUsers().observe(this, new Observer<List<Entity_Users>>() {
            @Override
            public void onChanged(List<Entity_Users> entity_users) {
                users = entity_users;
                Toast.makeText(MainActivity.this, "onChange() " + users.get(0).getUid() +"", Toast.LENGTH_LONG).show();
            }
        });
    }
}
