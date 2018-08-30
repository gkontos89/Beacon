package com.marshmallow.beacon.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.user.User;


public class LoginActivity extends AppCompatActivity {

    // GUI handles
    private TextView emailTextEntry;
    private TextView passwordTextEntry;
    private LinearLayout progressBarLinearLayout;
    private TextView progressBarText;
    private Button createAccountButton;
    private Button loginButton;

    // Firebase
    private FirebaseAuth firebaseAuth = null;
    private FirebaseDatabase firebaseInst;
    private DatabaseReference userReference;
    private ValueEventListener userValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();

        // Set GUI handles
        emailTextEntry = findViewById(R.id.email_text);
        passwordTextEntry = findViewById(R.id.password_text);
        progressBarLinearLayout = findViewById(R.id.progress_bar_layout);
        progressBarText = findViewById(R.id.progress_bar_text);
        createAccountButton = findViewById(R.id.create_account_button);
        loginButton = findViewById(R.id.sign_in_button);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTextEntry.getText().toString();
                String password = passwordTextEntry.getText().toString();
                if (emailIsValid(email) && passwordIsValid(password)) {
                    showProgressBar("Creating Account...");

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(email);
                                UserManager.getInstance().setUser(user);
                                storeNewUser(user);
                                accountCreationSuccess();
                            } else {
                                if (task.getException() != null) {
                                    accountCreationFailed(task.getException().getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar("Logging in...");
                final String email = emailTextEntry.getText().toString();
                String password = passwordTextEntry.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            initializeUserListener();
                        } else {
                            if (task.getException() != null) {
                                signInFailed(task.getException().getMessage());
                            }
                        }
                    }
                });
            }
        });
    }

    private void storeNewUser(User user) {
        DatabaseReference databaseReference = firebaseInst.getReference("users");
        if (firebaseAuth.getUid() != null) {
            databaseReference.child(firebaseAuth.getUid()).setValue(user);
        }
    }

    public Boolean emailIsValid(String email) {
        if (TextUtils.isEmpty(email)) {
            emailTextEntry.setError("Email Required");
            return false;
        } else {
            emailTextEntry.setError(null);
            return true;
        }
    }

    public Boolean passwordIsValid(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordTextEntry.setError("Empty Password Not Allowed");
            return false;
        } else {
            passwordTextEntry.setError(null);
            return true;
        }
    }

    public void initializeUserListener() {
        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserManager.getInstance().setUser(dataSnapshot.getValue(User.class));
                accountLoadingSucceeded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                accountLoadingFailed(databaseError.getMessage());
            }
        };

        if (firebaseAuth.getUid() != null) {
            userReference = firebaseInst.getReference("users").child(firebaseAuth.getUid());
            userReference.child("signedIn").setValue(true);
            userReference.addValueEventListener(userValueEventListener);
        }
    }

    private void removeListeners() {
        userReference.removeEventListener(userValueEventListener);
        userValueEventListener = null;
    }

    public void accountCreationSuccess() {
        hideProgressBar();
        Intent intent = new Intent(this, WelcomePrimaryActivity.class);
        startActivity(intent);
    }

    public void accountCreationFailed(String failureString) {
        hideProgressBar();
        Toast.makeText(this, "Account Creation Failed: " + failureString, Toast.LENGTH_SHORT).show();
    }

    public void signInSucceeded() {
        // TODO handle intermediary screen till user data has loaded
    }

    public void signInFailed(String failureString) {
        hideProgressBar();
        Toast.makeText(this, "Sign In Failed: " + failureString, Toast.LENGTH_SHORT).show();
    }

    public void accountLoadingSucceeded() {
        removeListeners();
        hideProgressBar();
        if (UserManager.getInstance().getUser().getAccountCreationComplete()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, WelcomePrimaryActivity.class);
            startActivity(intent);
        }
    }

    public void accountLoadingFailed(String failureString) {
        hideProgressBar();
        Toast.makeText(this, "Loading account on application launch failed: " + failureString, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar(String progressBarText) {
        // Hide all views in this layout and only show the progress bar linear layout
        findViewById(R.id.login_button_layout).setVisibility(View.GONE);
        findViewById(R.id.account_info_layout).setVisibility(View.GONE);

        this.progressBarText.setText(progressBarText);
        progressBarLinearLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBarLinearLayout.setVisibility(View.GONE);
        findViewById(R.id.login_button_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.account_info_layout).setVisibility(View.VISIBLE);
    }
}
