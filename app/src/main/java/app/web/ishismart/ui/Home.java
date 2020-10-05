package app.web.ishismart.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import app.web.ishismart.R;
import app.web.ishismart.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {

    private static String TAG = "Anonymous Account Activity";
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onBackPressed() {
        /*push activity to the background*/
        moveTaskToBack(true);
    }
}