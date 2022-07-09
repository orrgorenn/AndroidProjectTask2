package il.co.hit.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static il.co.hit.android.Config.MAIN_EXTRA_USERNAME;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private SharedPreferences sharedPref;

    private TextView tv_username;
    private EditText et_phone;
    private EditText et_share_text;

    private String sp_username;


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

        et_phone = findViewById(R.id.et_phone);
        et_share_text = findViewById(R.id.et_share_text);
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

    public void shareData (View view) {
        String shareText = et_share_text.getText().toString();
        if(!shareText.equals("")) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, et_share_text.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        } else {
            Toast.makeText(this, "Please input some text in the share field.", Toast.LENGTH_SHORT).show();
        }
    }

    public void makeCall (View view) {
        String num = et_phone.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + num));

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            try {
                startActivity(callIntent);
            } catch(SecurityException e) {
                Toast.makeText(this, "Could not make a call.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void showContacts (View view) {
        Intent contactsIntent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
        startActivity(contactsIntent);
    }

    public void moveToProfile(View view) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra(MAIN_EXTRA_USERNAME, sp_username);
        startActivity(profileIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted! call again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }
}