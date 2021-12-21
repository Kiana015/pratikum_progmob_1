package id.pe.pratikum_progmob_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import static java.sql.Types.NULL;

public class mainMenu extends AppCompatActivity {
    int ses_id;
    sharedPrefManager spm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataHelper db = new dataHelper(this);
        SQLiteDatabase dbRead = db.getReadableDatabase();
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ses_id =  spm.getSPId(getBaseContext());
        if(ses_id == 0){
            Intent login = new Intent(getApplicationContext(), id.pe.pratikum_progmob_1.ActivityLogin.class);
            startActivity(login);
        }
        else if (ses_id > 0){
            Cursor getUserData = dbRead.rawQuery("select*from user where id = "+ses_id,null);
            if(getUserData.getCount() == 0)
            {
                Toast.makeText(this, "Error While Getting Data", Toast.LENGTH_SHORT).show();
            }
            else
            {
                getUserData.moveToFirst();
                String user = getUserData.getString(getUserData.getColumnIndex("username"));
                String pass = getUserData.getString(getUserData.getColumnIndex("password"));
                String name = getUserData.getString(getUserData.getColumnIndex("nama"));
                String address = getUserData.getString(getUserData.getColumnIndex("alamat"));
                String jk = getUserData.getString(getUserData.getColumnIndex("jk"));
                String hobi = getUserData.getString(getUserData.getColumnIndex("hobi"));
                int ketertarikan = getUserData.getInt(getUserData.getColumnIndex("interest"));
                TextView nama = (TextView) findViewById(R.id.textView10);
                TextView alamat = (TextView) findViewById(R.id.textView13);
                TextView username = (TextView) findViewById(R.id.textView16);
                TextView password = (TextView) findViewById(R.id.textView18);
                TextView gender = (TextView) findViewById(R.id.textView20);
                TextView interest = (TextView) findViewById(R.id.textView22);
                TextView hobby = (TextView) findViewById(R.id.textView24);
                nama.setText(name);
                alamat.setText(address);
                username.setText(user);
                password.setText(pass);
                gender.setText(jk);
                interest.setText(ketertarikan+"%");
                hobby.setText(hobi);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Aplikasi Sedang di-pause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Anda Keluar Dari aplikasi", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Anda keluar dari main menu", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Anda Melanjutkan Activity Ini", Toast.LENGTH_SHORT).show();
    }

}