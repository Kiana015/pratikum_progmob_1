package id.pe.pratikum_progmob_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import static java.lang.Float.valueOf;

public class MainActivity extends AppCompatActivity {
    sharedPrefManager spm;
    int session_id = 1;
    int seek_min = 0;
    int seek_max = 100;
    dataHelper db = new dataHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText name = findViewById(R.id.name);
        EditText address = findViewById(R.id.address);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button register = findViewById(R.id.editData);
        RadioGroup gender = findViewById(R.id.gender);
        SeekBar inter = findViewById(R.id.interest);
        CheckBox fishing = findViewById(R.id.fishing);
        CheckBox jogging = findViewById(R.id.jogging);
        CheckBox hiking = findViewById(R.id.hiking);
        CheckBox biking = findViewById(R.id.biking);
        final int[] progressChangedValue = {0};
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
                Toast.makeText(MainActivity.this, "Intersting Kamu " + progressChangedValue[0] + "%",
                        Toast.LENGTH_SHORT).show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbWrite = db.getWritableDatabase();
                SQLiteDatabase dbRead = db.getReadableDatabase();
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
                    Toast.makeText(MainActivity.this, "Please Fill The Form First To Continue!!", Toast.LENGTH_SHORT).show();
                }
                else if(username.getText().toString().length() > 0 && name.getText().toString().length() > 0 && address.getText().toString().length() > 0 && password.getText().toString().length() > 0)
                {
                    String hobby = "";
                    if(fishing.isChecked() == true)
                    {
                        hobby = hobby+"fishing";
                    }
                    if(jogging.isChecked() == true)
                    {
                        if(hobby.length() > 0)
                        {
                            hobby = hobby+",";
                        }
                        hobby = hobby+"jogging";
                    }
                    if(hiking.isChecked() == true)
                    {
                        if(hobby.length() > 0)
                        {
                            hobby = hobby+",";
                        }
                        hobby = hobby+"hiking";
                    }
                    if(biking.isChecked() == true)
                    {
                        if(hobby.length() > 0)
                        {
                            hobby = hobby+",";
                        }
                        hobby = hobby+"biking";
                    }

                    Intent mainMenu = new Intent(getApplicationContext(),dashboard.class);
                    RadioButton selected_jk = (RadioButton) findViewById(gender.getCheckedRadioButtonId());
                    Cursor cek = dbRead.rawQuery("select * from user",null);
                    int getId = cek.getCount();
                    if(getId == 0)
                    {
                        getId = 1;
                    }
                    else
                    {
                        getId = getId+1;
                    }
                    dbWrite.execSQL("insert into user (id, nama, alamat, jk, hobi, interest, username, password, statusAccount) values("+getId+",'"+name.getText().toString()+"','"+address.getText().toString()+"','"+selected_jk.getText().toString()+"','"+hobby+"',"+progressChangedValue[0]+",'"+username.getText().toString()+"','"+password.getText().toString()+"',1)");
                    //db.daftarAkun(name.getText().toString(), address.getText().toString(), selected_jk.getText().toString(), hobby, progressChangedValue[0], username.getText().toString(), password.getText().toString());
                    //Cursor id = db.geUserId(username.getText().toString(), password.getText().toString());
                    Cursor id = dbRead.rawQuery("select * from user where username = '"+username.getText().toString()+"' and password = '"+password.getText().toString()+"'",null);
                    if(id.getCount() == 0)
                    {
                        Toast.makeText(MainActivity.this, "Tidak ada Data", Toast.LENGTH_SHORT).show();
                    }
                    else if(id.getCount() == 1)
                    {

                        id.moveToFirst();
                        spm.saveSPInt(getBaseContext(),spm.SP_ID, Integer.parseInt(id.getString(id.getColumnIndexOrThrow("id"))));
                        //Toast.makeText(MainActivity.this, "Ada Data dengan ID "+spm.getSPId(getBaseContext()), Toast.LENGTH_SHORT).show();
                        startActivity(mainMenu);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Tidak Bisa Login", Toast.LENGTH_SHORT).show();
                    }

                    /*mainMenu.putExtra("session_id",session_id);
                    mainMenu.putExtra("username",username.getText().toString());
                    mainMenu.putExtra("nama",name.getText().toString());
                    mainMenu.putExtra("alamat",address.getText().toString());
                    mainMenu.putExtra("password",password.getText().toString());
                    mainMenu.putExtra("gender",selected_jk.getText());
                    mainMenu.putExtra("seekbar", progressChangedValue[0]);
                    mainMenu.putExtra("hobby", hobby);
                    startActivity(mainMenu);
                     */
                }
            }
        });
    }
}