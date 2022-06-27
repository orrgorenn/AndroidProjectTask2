package il.co.hit.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(Config.TASK2_PERFERENCES, Context.MODE_PRIVATE);
        String sp_username = sharedPref.getString("Username", null);

        // If not logged in, move to login intent
        if(!checkLogin(sp_username)) {
            Log.i("MainActivity", "Not logged in. moved to main activity.");
            moveToLogin();
        }

        TextView username = findViewById(R.id.tv_username);
        username.setText(String.format("Hello, %s!", sp_username));
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
}