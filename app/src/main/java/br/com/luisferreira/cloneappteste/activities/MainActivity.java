package br.com.luisferreira.cloneappteste.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.luisferreira.cloneappteste.model.Clone;
import br.com.luisferreira.cloneappteste.R;
import br.com.luisferreira.cloneappteste.adapter.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "ANONYMOUS_FIREBASE";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private FloatingActionButton fabNovoClone;
    protected ProgressBar progressBar;

    private List<Clone> clones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Clones Cadastrados");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in: " + firebaseUser.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_clones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(this, clones);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdapter.notifyDataSetChanged();

        fabNovoClone = findViewById(R.id.fabNovoClone);

        progressBar = findViewById(R.id.main_progress);
    }

    @Override
    protected void onStart () {
        super.onStart ();

        firebaseAuth.addAuthStateListener (authStateListener);
        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "OnComplete : " + task.isSuccessful());

                if (!task.isSuccessful ()) {
                    Log.w (TAG, "Failed : ", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }

                fetchData();
            }
        });
    }

    @Override
    protected void onStop () {
        super.onStop ();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener (authStateListener);
        }
    }

    private void fetchData() {
        openProgressBar();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("clones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clones.removeAll(clones);

                if (!dataSnapshot.exists()) {
                    showSnackbar("Não existem clones cadastrados!");
                } else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Clone clone = snapshot.getValue(Clone.class);
                        clones.add(clone);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                }

                closeProgressBar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showSnackbar("Não foi possível buscar os dados!");
                closeProgressBar();
            }
        });
    }

    public void callInsert(View view) {
        Intent intent = new Intent(this, InsertActivity.class);
        startActivity(intent);
    }

    protected void openProgressBar() {
        progressBar.setFocusable(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void closeProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    protected void showSnackbar(String message) {
        Snackbar.make(progressBar,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}