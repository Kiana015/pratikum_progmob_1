package id.pe.pratikum_progmob_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class detail_riwayat_belanja extends AppCompatActivity {
    TextView tanggal_pembelian, alamat_pengiriman, status_pengiriman, total_harga, status_pembayaran, jenis_pembayaran;
    RecyclerView daftar_belanja;
    RecyclerView.Adapter adapter;
    dataHelper dbHelper;
    SQLiteDatabase dbRead, dbWrite;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_belanja);
        tanggal_pembelian = findViewById(R.id.tgl_pembelian_riwayat);
        alamat_pengiriman = findViewById(R.id.alamat_pengiriman_riwayat);
        status_pengiriman = findViewById(R.id.status_pengiriman_riwayat);
        total_harga = findViewById(R.id.harga_total_riwayat1);
        status_pembayaran = findViewById(R.id.status_pembayaran_riwayat);
        jenis_pembayaran = findViewById(R.id.jenis_pembayaran_riwayat);
        dbHelper = new dataHelper(this);
        dbRead = dbHelper.getReadableDatabase();
        dbWrite = dbHelper.getWritableDatabase();
        daftar_belanja = findViewById(R.id.daftar_belanja_riwayat);
        confirm = findViewById(R.id.confirm);
        Intent intent = getIntent();
        int id_pembelian = intent.getIntExtra("id_belanja",0);
        if(id_pembelian > 0)
        {
            Cursor viewPembelian = dbRead.rawQuery("SELECT*FROM pembelian WHERE id ="+id_pembelian,null);
            viewPembelian.moveToFirst();
            DateFormat input = new SimpleDateFormat("yyyyMMddHHmmss");
            DateFormat output_date = new SimpleDateFormat("dd-MM-yyy");
            DateFormat output_time = new SimpleDateFormat("HH:mm:ss");
            String tanggalBeli = viewPembelian.getString(viewPembelian.getColumnIndex("tanggalPembelian"));
            Date dateTanggalBeli = null;
            try {
                dateTanggalBeli = input.parse(tanggalBeli);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tanggal_pembelian.setText(output_date.format(dateTanggalBeli)+" "+output_time.format(dateTanggalBeli));
            alamat_pengiriman.setText(viewPembelian.getString(viewPembelian.getColumnIndex("alamat_pengiriman")));
            status_pengiriman.setText(viewPembelian.getString(viewPembelian.getColumnIndex("statusPembelian")));
            total_harga.setText("Rp."+String.valueOf(viewPembelian.getFloat(viewPembelian.getColumnIndex("hargaTotal")))+"0");
            jenis_pembayaran.setText(viewPembelian.getString(viewPembelian.getColumnIndex("jenisPembayaran")));
            status_pembayaran.setText(viewPembelian.getString(viewPembelian.getColumnIndex("statusPembayaran")));
            Cursor detailPembelian = dbRead.rawQuery("SELECT gambarIcon, namaAlat, hargaSatuan, jumlahBeli, satuan FROM detailPembelian INNER JOIN alatTulis ON detailPembelian.idBarang = alatTulis.id WHERE idPembelian = "+id_pembelian,null);
            RecyclerView.LayoutManager mymanager = new LinearLayoutManager(this);
            daftar_belanja.setLayoutManager(mymanager);
            daftarDetailBelanjaAdapter daftarDetailBelanjaAdapter = new daftarDetailBelanjaAdapter(detailPembelian);
            daftar_belanja.setAdapter(daftarDetailBelanjaAdapter);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setTitle("Apakah Barang Ini Sudah Diterima?");
                    alertDialogBuilder
                            .setMessage("Click yes to confirm!")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dbWrite.execSQL("UPDATE pembelian SET statusPembelian = 'Barang Telah Sampai' WHERE id = "+id_pembelian);
                                            Toast.makeText(detail_riwayat_belanja.this, "Barang sudah sampai. Terima kasih sudah membeli di toko kami", Toast.LENGTH_SHORT).show();
                                            Intent backToDaftar = new Intent(v.getContext(), riwayatPembelian.class);
                                            startActivity(backToDaftar);
                                            finish();
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
            });
        }
        else
        {
            Toast.makeText(this, "Error, cant load data", Toast.LENGTH_SHORT).show();
        }
        Cursor viewRiwayat;
    }
}