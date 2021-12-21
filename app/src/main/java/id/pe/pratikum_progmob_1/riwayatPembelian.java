package id.pe.pratikum_progmob_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class riwayatPembelian extends AppCompatActivity {
    sharedPrefManager spm;
    Cursor viewData;
    dataHelper dbHelper;
    private RecyclerView listPembelian;
    private RecyclerView.Adapter adapter;
    SQLiteDatabase dbRead,dbWrite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pembelian);
        dbHelper = new dataHelper(this);
        dbRead = dbHelper.getReadableDatabase();
        dbWrite = dbHelper.getWritableDatabase();
        viewData = dbRead.rawQuery("SELECT*FROM pembelian WHERE id_user = "+spm.getSPId(this),null);
        listPembelian = findViewById(R.id.riwayat_pembelian);
        RecyclerView.LayoutManager mymanager = new LinearLayoutManager(this);
        listPembelian.setLayoutManager(mymanager);
        riwayatPembelianAdapter list_riwayat_pembelian = new riwayatPembelianAdapter(viewData);
        listPembelian.setAdapter(list_riwayat_pembelian);
    }
}