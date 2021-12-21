package id.pe.pratikum_progmob_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class dashboard extends AppCompatActivity {
    BottomNavigationView bottomNavView;
    dataHelper db_helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db_helper = new dataHelper(this);
        SQLiteDatabase dbread = db_helper.getReadableDatabase();
        SQLiteDatabase db = db_helper.getWritableDatabase();
        Cursor checkItemData = dbread.rawQuery("select*from alatTulis", null);

        if(checkItemData.getCount() == 0)
        {
            db.execSQL("INSERT INTO alatTulis VALUES(1,'Stabillo Pencil 2B',2000.00,'pcs',"+String.valueOf(R.mipmap.pensil)+","+String.valueOf(R.drawable.pensil)+")");
            db.execSQL("INSERT INTO alatTulis VALUES(2,'Stabillo Penghapus Hitam',5000.00,'pcs',"+String.valueOf(R.mipmap.penghapus)+","+String.valueOf(R.drawable.penghapus)+")");
            db.execSQL("INSERT INTO alatTulis VALUES(3,'Penggaris Besi 30cm',10000.00,'pcs',"+String.valueOf(R.mipmap.penggaris)+","+String.valueOf(R.drawable.penggaris)+")");
            db.execSQL("INSERT INTO alatTulis VALUES(4,'Pulpen Snowman V3 Hitam',2000.00,'pcs',"+String.valueOf(R.mipmap.pulpen)+","+String.valueOf(R.drawable.pulpen)+")");
            db.execSQL("INSERT INTO alatTulis VALUES(5,'Paket Pulpen 1 kotak isi 20 PCS',18000.00,'pack',"+String.valueOf(R.mipmap.pulpen)+","+String.valueOf(R.drawable.pulpen)+")");
        }
        Cursor getItemData = dbread.rawQuery("select*from alatTulis", null);
        bottomNavView = findViewById(R.id.bottom_navbar_menu);
        bottomNavView.setSelectedItemId(R.id.homepage);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new fragmentHome()).commit();
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                switch (item.getItemId())
                {
                    case R.id.homepage:
                        f = new fragmentHome();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,f).commit();
                        //fragmentHome.setCursor(getItemData);
                        break;
                    case R.id.profile:
                        f = new fragmentProfile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,f).commit();
                        break;
                    case R.id.cart:
                        Intent toCarting = new Intent(getBaseContext(), id.pe.pratikum_progmob_1.carting.class);
                        startActivity(toCarting);
                }
                return true;
            }
        });
    }
    @Override
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