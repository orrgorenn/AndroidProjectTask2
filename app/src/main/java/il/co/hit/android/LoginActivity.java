package il.co.hit.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String CONST_USERNAME = "hit";
    private static final String CONST_PASSWORD = "123";

    private EditText username;
    private EditText password;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences(Config.TASK2_PERFERENCES, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        errorMessage = findViewById(R.id.tv_error_message);
    }

    /**
     * Click handler for login button
     * User will get authenticated and logged in and moved to MainActivity
     * @param button
     */
    public void login(View button) {
        String text_username = username.getText().toString();
        String text_password = password.getText().toString();
        if(text_username.equals(CONST_USERNAME)) {
            if(text_password.equals(CONST_PASSWORD)) {
                // Username & password match - move to other intent
                errorMessage.setText("");

                // Set SharedPerf
                editor.putString("Username", text_username);
                editor.commit();

                // Move user to main intent
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);

                setResult(RESULT_OK);
                finish();
            } else {
                errorMessage.setText("Password is wrong.");
                Log.e("username", "Wrong password: (" + text_password + ")");
            }
        } else {
            errorMessage.setText("Username is wrong.");
            Log.e("username", "Wrong username: (" + text_username + ")");
        }
    }
}