package id.pe.pratikum_progmob_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class editProfile extends AppCompatActivity {
    sharedPrefManager spm;
    dataHelper dbHelper;
    SQLiteDatabase dbRead;
    EditText name, address, username, password;
    RadioGroup gender;
    SeekBar inter;
    Button edit_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        gender = findViewById(R.id.gender);
        inter = findViewById(R.id.interest);
        CheckBox fishing = findViewById(R.id.fishing);
        CheckBox jogging = findViewById(R.id.jogging);
        CheckBox hiking = findViewById(R.id.hiking);
        CheckBox biking = findViewById(R.id.biking);
        int ses_id =  spm.getSPId(getBaseContext());
        dbHelper = new dataHelper(this);
        dbRead = dbHelper.getReadableDatabase();
        Cursor checkProfile = dbRead.rawQuery("SELECT*FROM user WHERE id = "+ses_id,null);
        checkProfile.moveToFirst();
        name.setText(checkProfile.getString(checkProfile.getColumnIndex("nama")));
        address.setText(checkProfile.getString(checkProfile.getColumnIndex("alamat")));
        username.setText(checkProfile.getString(checkProfile.getColumnIndex("username")));
        password.setText(checkProfile.getString(checkProfile.getColumnIndex("password")));
        final int[] progressChangedValue = {checkProfile.getInt(checkProfile.getColumnIndex("interest"))};
        int seek_min = 0;
        int seek_max = 100;
        inter.setProgress(checkProfile.getInt(checkProfile.getColumnIndex("interest")));
        inter.setMax(seek_max);
        inter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue[0] = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(editProfile.this, "Intersting Kamu " + progressChangedValue[0] + "%",
                        Toast.LENGTH_SHORT).show();
            }
        });
        edit_profile = findViewById(R.id.editData);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                if(username.getText().toString().length() == 0 || name.getText().toString().length() == 0 || address.getText().toString().length() == 0 || password.getText().toString().length() == 0 || gender.getCheckedRadioButtonId() == -1)
                {
                    if(username.getText().toString().length() == 0){
                        username.setError("Username diperlukan!");
                    }
                    if(name.getText().toString().length() == 0){
                        name.setError("Nama diperlukan!");
                    }
                    if(address.getText().toString().length() == 0){
                        address.setError("Alamat diperlukan!");
                    }
                    if(password.getText().toString().length() == 0){
                        password.setError("Password diperlukan!");
                    }
                    Toast.makeText(editProfile.this, "Please Fill The Form First To Continue!!", Toast.LENGTH_SHORT).show();
                }
                else if(username.getText().toString().length() > 0 && name.getText().toString().length() > 0 && address.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    String hobby = "";
                    if (fishing.isChecked() == true) {
                        hobby = hobby + "fishing";
                    }
                    if (jogging.isChecked() == true) {
                        if (hobby.length() > 0) {
                            hobby = hobby + ",";
                        }
                        hobby = hobby + "jogging";
                    }
                    if (hiking.isChecked() == true) {
                        if (hobby.length() > 0) {
                            hobby = hobby + ",";
                        }
                        hobby = hobby + "hiking";
                    }
                    if (biking.isChecked() == true) {
                        if (hobby.length() > 0) {
                            hobby = hobby + ",";
                        }
                        hobby = hobby + "biking";
                    }

                    Intent mainMenu = new Intent(getApplicationContext(), dashboard.class);
                    RadioButton selected_jk = (RadioButton) findViewById(gender.getCheckedRadioButtonId());
                    dbWrite.execSQL("UPDATE user" +
                            " SET nama = '"+name.getText()+"', alamat = '"+address.getText()+"', username = '"+username.getText()+"'," +
                            "password = '"+password.getText()+"', jk = '"+selected_jk.getText().toString()+"'," +
                            "hobi = '"+hobby+"',interest = "+progressChangedValue[0]+"" +
                            " WHERE id = "+ses_id);
                    Toast.makeText(editProfile.this, "Data sudah diubah", Toast.LENGTH_SHORT).show();
                    startActivity(mainMenu);
                    finish();
                }
            }
        });
    }
}