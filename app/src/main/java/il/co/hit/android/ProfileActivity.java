package il.co.hit.android;

import static il.co.hit.android.Config.MAIN_EXTRA_USERNAME;
import static il.co.hit.android.Config.PROFILE_WEBSITE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    private int PERMISSION_CODE_SET_ALARM = 1;

    private String local_username;
    private TextView tv_username;

    private EditText et_fullName;
    private EditText et_phone;
    private EditText et_time;

    private TimePickerDialog timePickerDialog;
    private Calendar calendar;

    private String fullName;
    private String contactNumber;

    private int currentHour;
    private int currentMinute;

    private EditText websiteURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            local_username = extras.getString(MAIN_EXTRA_USERNAME);

            tv_username = findViewById(R.id.tv_username);
            tv_username.setText(String.format("Hello, %s! (Comes from bundle extras)", local_username));
        }

        et_fullName = findViewById(R.id.et_fullName);
        et_phone = findViewById(R.id.et_phone);

        et_time = findViewById(R.id.et_time);

        websiteURL = findViewById(R.id.et_website_url);
    }

    public void saveContact(View view) {
        fullName = et_fullName.getText().toString();
        contactNumber = et_phone.getText().toString();

        if(fullName.equals("") || contactNumber.equals("")) {
            sendMessage("Error", "Please provide full name and contact number.");
        } else {
            Intent addContactIntent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, ContactsContract.Contacts.CONTENT_URI);
            addContactIntent.setData(Uri.parse("tel:" + contactNumber));
            addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, fullName);
            addContactIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, "ZoomInfo");
            startActivity(addContactIntent);
        }
    }

    public void setTime(View view) {
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(ProfileActivity.this, (timePicker, hourOfDay, minutes) -> {
            et_time.setText(String.format("%02d:%02d", hourOfDay, minutes));
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }

    public void setAlarm(View view) {
        if(!et_time.getText().toString().equals("")) {
            checkPermission(Manifest.permission.SET_ALARM, PERMISSION_CODE_SET_ALARM);
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            String fullTime = et_time.getText().toString();
            String[] splitTime = fullTime.split(":");
            intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(splitTime[0]));
            intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(splitTime[1]));
            et_time.setText("");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please input a time for the alarm", Toast.LENGTH_SHORT).show();;
        }
    }

    public void goToBrowser(View view) {
        Intent browserIntent = new Intent(this, BrowserActivity.class);
        String url = websiteURL.getText().toString();

        // Process website URL
        boolean websitePattern = Pattern.matches("https?:\\/\\/(www\\.)?.+", url);

        if(websitePattern) {
            browserIntent.putExtra(PROFILE_WEBSITE_URL, url);
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, "Please make sure to include http/s and www.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage(String status, String text) {
        Toast.makeText(this, status + ": " + text, Toast.LENGTH_SHORT).show();
    }

    public void checkPermission(String permission, int requestCode) {
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            sendMessage("Info", "Permissions already granted.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}