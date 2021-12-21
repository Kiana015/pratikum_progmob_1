package id.pe.pratikum_progmob_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

public class ActivityLogin extends AppCompatActivity {
    int session_id;
    sharedPrefManager spm;
    TextView a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataHelper db = new dataHelper(this);
        SQLiteDatabase dbRead = db.getReadableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session_id = spm.getSPId(this);
        if(session_id > 0)
        {
            Intent mainMenu = new Intent(getApplicationContext(), dashboard.class);
            startActivity(mainMenu);
        }
        final Button login = (Button) findViewById(R.id.login);
        final Button register = (Button) findViewById(R.id.Register);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length() == 0 || password.getText().toString().length() == 0)
                {
                    if(username.getText().toString().length() == 0)
                    {
                        username.setError("Username Diperlukan");
                    }
                    if(password.getText().toString().length() == 0)
                    {
                        password.setError("Password Diperlukan");
                    }
                    Toast.makeText(ActivityLogin.this, "Silahkan Isi Username dan Password Dengan Lengkap!", Toast.LENGTH_SHORT).show();
                }
                else if(username.getText().toString().length() > 0 && password.getText().toString().length() > 0)
                {
                    //cek akun
                    Cursor id = dbRead.rawQuery("select * from user where username = '"+username.getText().toString()+"' and password = '"+password.getText().toString()+"'",null);
                    id.moveToFirst();
                    if(id.getCount() == 0)
                    {
                        Toast.makeText(ActivityLogin.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                    }
                    else if(id.getInt(id.getColumnIndex("statusAccount")) == 0)
                    {
                        Toast.makeText(ActivityLogin.this, "Akun ini tidak aktif", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        id.moveToFirst();
                        spm.saveSPInt(getBaseContext(), spm.SP_ID, id.getInt(id.getColumnIndex("id")));
                        Intent mainMenu1 = new Intent(getApplicationContext(),dashboard.class);
                        startActivity(mainMenu1);
                    }
                    //lempar lemparan

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(register);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Selamat Datang!", Toast.LENGTH_SHORT).show();
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}