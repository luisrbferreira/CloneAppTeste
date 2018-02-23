package br.com.luisferreira.cloneappteste.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Luis Ferreira on 23/02/2018.
 */

public class LibraryClass {
    private static DatabaseReference firebase;

    //Construtor da classe
    private LibraryClass() {
    }

    public static DatabaseReference getFirebase() {
        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return (firebase);
    }
}
