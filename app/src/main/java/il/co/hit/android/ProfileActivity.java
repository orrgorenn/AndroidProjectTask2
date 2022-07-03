package il.co.hit.android;

import static il.co.hit.android.Config.MAIN_EXTRA_USERNAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    private String local_username;
    private TextView tv_username;

    private EditText et_fullName;
    private EditText et_phone;
    private EditText et_date;
    private EditText et_time;

    private String fullName;
    private String contactNumber;
    private String date;
    private String time;

    private Cursor cursor;

    private static final int CAL_READ_PERMISSION_CODE = 1;
    private static final int CAL_WRITE_PERMISSION_CODE = 2;

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

        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);
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

    public void addToCalendar(View view) {
        date = et_date.getText().toString();
        time = et_time.getText().toString();

        long startTime;

        if(date.equals("") || time.equals("") || date == null) {
            sendMessage("Error", "Please provide date and time.");
        } else {
            cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                if(cursor != null) {
                    checkPermission(Manifest.permission.WRITE_CALENDAR, CAL_WRITE_PERMISSION_CODE);
                    try {
                        Date newDate = new SimpleDateFormat("dd/mm/yy hh:mm").parse(date + " " + time);
                        startTime = newDate.getTime();

                        Intent calIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
                        calIntent.setData(CalendarContract.CONTENT_URI);
                        calIntent.putExtra(CalendarContract.Events.TITLE, "Birthday to you!");
                        calIntent.putExtra(CalendarContract.Events.ALL_DAY, false);
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                        calIntent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
                        startActivity(calIntent);
                        sendMessage("Success", "Event added!");
                    } catch (ParseException e) {
                        sendMessage("Error", e.toString());
                    }
                }
            }
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

        if(requestCode == CAL_READ_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage("Permissions", "Read calendar permissions granted.");
            }
        } else if(requestCode == CAL_WRITE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage("Permissions", "Write calendar permissions granted.");
            }
        }
    }
}