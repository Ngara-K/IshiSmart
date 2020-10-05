package app.web.ishismart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.web.ishismart.R;

import static app.web.ishismart.utils.AppUtils.firebaseAuth;
import static app.web.ishismart.utils.AppUtils.firebaseUser;

public class AnonymousAccount extends AppCompatActivity {

    private static String TAG = "Anonymous Account Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_account);

        /*signing user anonymously*/
        firebaseAuth.signInAnonymously().addOnSuccessListener(authResult -> {
            Log.d(TAG, "onSuccess() returned: " + authResult.getUser());
            toHomeActivity();
        }).addOnFailureListener(e -> {
                Log.d(TAG, "onFailure() returned: " + e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser != null) {
            Log.d(TAG, "onStart() returned: " + firebaseUser.getUid());
            toHomeActivity();
        }
    }

    /*start  home activity*/
    private void toHomeActivity() {
        startActivity(new Intent(AnonymousAccount.this, Home.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        /*push activity to the background*/
        moveTaskToBack(true);
    }
}