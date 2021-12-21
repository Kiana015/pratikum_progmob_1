package id.pe.pratikum_progmob_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class detailBarang extends AppCompatActivity {
    dataHelper dbHelper = new dataHelper(this);
    int ses_id;
    TextView judul,harga,satuan;
    ImageView thumbGambar;
    BottomNavigationView botomNavbar;
    sharedPrefManager spm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);
        Intent intent = getIntent();
        ses_id =  spm.getSPId(getBaseContext());
        thumbGambar = findViewById(R.id.thumb_gambar);
        judul = findViewById(R.id.judulBarangDetail);
        harga = findViewById(R.id.hargaBarangDetail);
        satuan = findViewById(R.id.satuanBarangDetail);
        int id = intent.getIntExtra("id_barang",0);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        Cursor fetch = dbRead.rawQuery("SELECT*FROM alatTulis WHERE id = "+id,null);


        if(fetch.getCount() == 0)
        {
            Toast.makeText(this, "I Cannot Read Data. Please Reset This First", Toast.LENGTH_SHORT).show();
        }
        else
        {
            fetch.moveToFirst();
            int gambarId = fetch.getInt(fetch.getColumnIndex("gambarResource"));
            String judulText = fetch.getString(fetch.getColumnIndex("namaAlat"));
            Float hargaFloat = fetch.getFloat(fetch.getColumnIndex("hargaAlat"));
            thumbGambar.setImageResource(gambarId);
            judul.setText(judulText);
            String hargaText = String.valueOf(hargaFloat);
            harga.setText("Rp."+hargaText+"0");
            satuan.setText(fetch.getString(fetch.getColumnIndex("satuan")));
        }
        botomNavbar = findViewById(R.id.bottom_navbar_menu);
        botomNavbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Cursor checkInCart = dbRead.rawQuery("SELECT*FROM cart WHERE idBarang = "+id+" AND id_user ="+ses_id,null);
                switch (item.getItemId())
                {
                    case R.id.addToCart:
                        int idForCart = 0;
                        Cursor getLastId = dbRead.rawQuery("SELECT*FROM cart",null);
                        if(checkInCart.getCount() == 0)
                        {
                            if(getLastId.getCount() == 0)
                            {
                                idForCart = 1;
                                dbWrite.execSQL("INSERT INTO cart VALUES("+idForCart+","+ses_id+","+id+",1)");
                                Toast.makeText(detailBarang.this, "Daftar Barang Berhasil Ditambahkan. ID = "+idForCart, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                getLastId.moveToLast();
                                idForCart = getLastId.getInt(getLastId.getColumnIndex("id"))+1;
                                dbWrite.execSQL("INSERT INTO cart VALUES("+idForCart+","+ses_id+","+id+",1)");
                                Toast.makeText(detailBarang.this, "Daftar Barang Berhasil Ditambahkan. ID = "+idForCart, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(checkInCart.getCount() == 1)
                        {
                            checkInCart.moveToLast();
                            int jumlahBeli = checkInCart.getInt(checkInCart.getColumnIndex("jumlahBeli"));
                            jumlahBeli = jumlahBeli+1;
                            dbWrite.execSQL("UPDATE cart SET jumlahBeli = "+jumlahBeli+" WHERE id = "+checkInCart.getInt(checkInCart.getColumnIndex("id"))+" AND id_user ="+ses_id);
                            Toast.makeText(detailBarang.this, "Daftar Barang Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });
    }
}