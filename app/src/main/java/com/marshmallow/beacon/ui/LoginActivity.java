package com.marshmallow.beacon.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.broadcasts.CreateUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.LoadUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.SignInStatusBroadcast;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // GUI handles
    private TextView usernameTextEntry;
    private TextView passwordTextEntry;
    private LinearLayout progressBarLinearLayout;
    private TextView progressBarText;

    // BroadcastReceiver
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set GUI handles
        usernameTextEntry = findViewById(R.id.username_text);
        passwordTextEntry = findViewById(R.id.password_text);
        progressBarLinearLayout = findViewById(R.id.progress_bar_layout);
        progressBarText = findViewById(R.id.progress_bar_text);

        findViewById(R.id.create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Set up broadcastReceiver and its filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(CreateUserStatusBroadcast.action);
        intentFilter.addAction(SignInStatusBroadcast.action);
        intentFilter.addAction(LoadUserStatusBroadcast.action);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Handle back end status pertaining to the login screen
                if (intent.getAction().equals(CreateUserStatusBroadcast.action)) {
                    switch (intent.getStringExtra(CreateUserStatusBroadcast.statusKey)) {
                        case CreateUserStatusBroadcast.CREATE_ACCOUNT_SUCCESSFUL:
                            accountCreationSuccess();
                            break;
                        case CreateUserStatusBroadcast.CREATE_ACCOUNT_FAILED:
                        default:
                            accountCreationFailed(intent.getStringExtra(CreateUserStatusBroadcast.statusMessageKey));
                            break;
                    }
                } else if (intent.getAction().equals(SignInStatusBroadcast.action)) {
                    switch (intent.getStringExtra(SignInStatusBroadcast.statusKey)) {
                        case SignInStatusBroadcast.SIGN_IN_SUCCESSFUL:
                            signInSucceeded();
                            break;
                        case SignInStatusBroadcast.SIGN_IN_FAILED:
                        default:
                            signInFailed(intent.getStringExtra(SignInStatusBroadcast.statusMessageKey));
                            break;
                    }
                } else if (intent.getAction().equals(LoadUserStatusBroadcast.action)) {
                    switch (intent.getStringExtra(LoadUserStatusBroadcast.statusKey)) {
                        case LoadUserStatusBroadcast.USER_LOADED_SUCCESSFUL:
                            accountLoadingSucceeded();
                            break;
                        case LoadUserStatusBroadcast.USER_LOADED_FAILED:
                            accountLoadingFailed(intent.getStringExtra(LoadUserStatusBroadcast.statusMessageKey));
                            break;
                    }
                }
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO turn on loading wheel so the log in screen isn't really showing?
        // TODO turn this back on when debugging login capabilities is complete
        // Check if the user is already signed in and if so move to home screen
//        if (BeaconBackend.getInstance().isUserSignedIn()) {
//            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(intent);
//        }
    }

    @Override
    public void onClick(View view) {
        // Handle Account creation
        if (view.getId() == R.id.create_account_button) {
            if (usernameIsValid() && passwordIsValid()) {
                showProgressBar("Creating Account...");
                // Append gmail for now
                String username = usernameTextEntry.getText().toString();
                username += "@gmail.com";
                BeaconBackend.getInstance().createUserWithEmailAndPassword(getApplicationContext(),
                        this,
                        username,
                        passwordTextEntry.getText().toString());
            }
        }
        // Handle Sign in
        else if (view.getId() == R.id.sign_in_button) {
            if (usernameIsValid() && passwordIsValid()) {
                showProgressBar("Signing in...");
                // Append gmail for now
                String username = usernameTextEntry.getText().toString();
                username += "@gmail.com";
                BeaconBackend.getInstance().signInWithEmailAndPassword(getApplicationContext(),
                        this,
                        username,
                        passwordTextEntry.getText().toString());
            }
        }
    }

    public Boolean usernameIsValid() {
        String username = usernameTextEntry.getText().toString();
        if (TextUtils.isEmpty(username)) {
            usernameTextEntry.setError("Username Required");
            return false;
        } else {
            usernameTextEntry.setError(null);
            return true;
        }
    }

    public Boolean passwordIsValid() {
        String password = passwordTextEntry.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTextEntry.setError("Empty Password Not Allowed");
            return false;
        } else {
            passwordTextEntry.setError(null);
            return true;
        }
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
        hideProgressBar();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
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
