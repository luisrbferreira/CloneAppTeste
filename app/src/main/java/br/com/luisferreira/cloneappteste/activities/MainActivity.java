package br.com.luisferreira.cloneappteste.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;

import br.com.luisferreira.cloneappteste.R;
import br.com.luisferreira.cloneappteste.util.LibraryClass;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = LibraryClass.getFirebase();

        initViews();
    }

    private void initViews() {

    }
}