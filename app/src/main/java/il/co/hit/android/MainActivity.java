package il.co.hit.android;

import androidx.appcompat.app.AppCompatActivity;

import static il.co.hit.android.Config.MAIN_EXTRA_USERNAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private TextView tv_username;
    String sp_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(Config.TASK2_PERFERENCES, Context.MODE_PRIVATE);
        sp_username = sharedPref.getString("Username", null);

        // If not logged in, move to login intent
        if(!checkLogin(sp_username)) {
            Log.i("MainActivity", "Not logged in. moved to main activity.");
            moveToLogin();
        }

        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(String.format("Hello, %s!", sp_username));
    }

    private boolean checkLogin(String username) {
        return username != null && !username.equals("");
    }

    public void logoutUser(View button) {
        sharedPref.edit().remove("Username").commit();
        moveToLogin();
    }

    private void moveToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public void moveToProfile(View view) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra(MAIN_EXTRA_USERNAME, sp_username);
        startActivity(profileIntent);
    }
}