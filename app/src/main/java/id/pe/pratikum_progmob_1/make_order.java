package id.pe.pratikum_progmob_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class make_order extends AppCompatActivity {
    BottomNavigationView bottomNavbar;
    EditText alamatPengiriman;
    TextView harga;
    dataHelper dbHelper;
    private RecyclerView listBarangCart;
    private RecyclerView.Adapter adapter;
    sharedPrefManager spm;
    public Cursor cursor, viewPembelian, viewDetailPembelian;
    public ImageView circle_image;
    public Button pesan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        dbHelper = new dataHelper(this);
        alamatPengiriman = findViewById(R.id.alamat_pengiriman);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        cursor = dbRead.rawQuery("SELECT cart.id, id_user, jumlahBeli, idBarang, namaAlat, hargaAlat, satuan, gambarIcon, gambarResource FROM cart INNER JOIN alatTulis ON cart.idBarang = alatTulis.id WHERE id_user = "+spm.getSPId(getBaseContext()),null);
        Cursor checkalamat = dbRead.rawQuery("SELECT*FROM user WHERE id ="+spm.getSPId(getBaseContext()),null);
        checkalamat.moveToFirst();
        alamatPengiriman.setText(checkalamat.getString(checkalamat.getColumnIndex("alamat")));
        listBarangCart = findViewById(R.id.list_barang_daricart);
        RecyclerView.LayoutManager mymanager = new LinearLayoutManager(this);
        listBarangCart.setLayoutManager(mymanager);
        barang_dibeli_adapter list_cart_adapter = new barang_dibeli_adapter(cursor);
        listBarangCart.setAdapter(list_cart_adapter);
        bottomNavbar = findViewById(R.id.navbar_pesans);
        Spinner jenisPembayaran = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.jenis_pembayaran, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        jenisPembayaran.setAdapter(adapter);
        cursor.moveToFirst();
        float hargaTotal = 0;
        for(int i = 0; i < cursor.getCount(); i++)
        {
            hargaTotal = hargaTotal+(cursor.getFloat(cursor.getColumnIndex("hargaAlat"))*cursor.getInt(cursor.getColumnIndex("jumlahBeli")));
            cursor.moveToNext();
        }
        harga = findViewById(R.id.hargaTotal);
        harga.setText("Rp."+String.valueOf(hargaTotal)+"0");
        float finalHargaTotal = hargaTotal;
        bottomNavbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String jenisPembayaranSelecter = jenisPembayaran.getSelectedItem().toString();

                viewPembelian = dbRead.rawQuery("SELECT*FROM pembelian",null);
                int id_pembelian, id_detail_pembelian;
                if(viewPembelian.getCount() == 0)
                {
                    id_pembelian = 1;
                }
                else
                {
                    viewPembelian.moveToLast();
                    id_pembelian = viewPembelian.getInt(viewPembelian.getColumnIndex("id"))+1;
                }
                dbWrite.execSQL("INSERT INTO pembelian VALUES("+id_pembelian+","+spm.getSPId(getBaseContext())+",'"+alamatPengiriman.getText()+"',"+dateFormat.format(Calendar.getInstance().getTime())+",'Sedang Diantar',"+finalHargaTotal +",'Lunas','"+jenisPembayaranSelecter+"')");
                cursor.moveToFirst();
                viewDetailPembelian = dbRead.rawQuery("SELECT*FROM detailPembelian",null);
                if(viewDetailPembelian.getCount() == 0)
                {
                    id_detail_pembelian = 1;
                }
                else
                {
                    viewDetailPembelian.moveToLast();
                    id_detail_pembelian = viewDetailPembelian.getInt(viewDetailPembelian.getColumnIndex("id"))+1;
                }
                for(int j = 0; j < cursor.getCount(); j++)
                {
                    dbWrite.execSQL("INSERT INTO detailPembelian VALUES("+id_detail_pembelian+","+id_pembelian+","+cursor.getInt(cursor.getColumnIndex("idBarang"))+","+cursor.getFloat(cursor.getColumnIndex("hargaAlat"))+","+cursor.getInt(cursor.getColumnIndex("jumlahBeli"))+")");
                    id_detail_pembelian++;
                    cursor.moveToNext();
                }
                dbWrite.execSQL("DELETE FROM cart WHERE id_user = "+spm.getSPId(getBaseContext()));
                Toast.makeText(make_order.this, "Pembelian telah selesai diproses. Item akan dikirimkan ke rumahmu. Silahkan cek histori pembelian untuk melakukan validasi pengiriman. Selamat berbelanja kembali", Toast.LENGTH_SHORT).show();
                Intent toMainMenu = new Intent(make_order.this,dashboard.class);
                startActivity(toMainMenu);
                finish();
                return true;
            }
        });
    }
}