package com.example.shopping.Database;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuth {
    private final FirebaseAuth mAuth;
    private final Context context;

    public UserAuth(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, OnAuthCompleteListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(mAuth.getCurrentUser());
                    } else {
                        listener.onFailure(task.getException());
                        // Show a toast message
                        Toast.makeText(context, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void loginUser(String email, String password, OnAuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(mAuth.getCurrentUser());
                    } else {
                        listener.onFailure(task.getException());
                        // Show a toast message
                        Toast.makeText(context, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public interface OnAuthCompleteListener {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }

    public void signOut() {
        mAuth.signOut();
    }
}

