package id.pe.pratikum_progmob_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class carting extends AppCompatActivity {
    dataHelper dbHelper;
    private RecyclerView listBarangCart;
    private RecyclerView.Adapter adapter;
    public TextView hargaTotalText;
    sharedPrefManager spm;
    public Cursor cursor;
    public ImageView circle_image;
    public Button pesan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carting);
        dbHelper = new dataHelper(this);
        SQLiteDatabase dbview = dbHelper.getReadableDatabase();
        float hargaTotal = 0;
        cursor = dbview.rawQuery("SELECT cart.id, id_user, jumlahBeli, idBarang, namaAlat, hargaAlat, satuan, gambarIcon, gambarResource FROM cart INNER JOIN alatTulis ON cart.idBarang = alatTulis.id WHERE id_user = "+spm.getSPId(getBaseContext()),null);
        listBarangCart = findViewById(R.id.listCart);
        RecyclerView.LayoutManager mymanager = new LinearLayoutManager(this);
        listBarangCart.setLayoutManager(mymanager);
        list_cart_adapter list_cart_adapter = new list_cart_adapter(cursor);
        listBarangCart.setAdapter(list_cart_adapter);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++)
        {
            hargaTotal = hargaTotal + (cursor.getFloat(cursor.getColumnIndex("hargaAlat"))*cursor.getInt(cursor.getColumnIndex("jumlahBeli")));
            cursor.moveToNext();
        }
        hargaTotalText = findViewById(R.id.totalHarga);
        hargaTotalText.setText("Rp."+String.valueOf(hargaTotal)+"0");
        pesan = findViewById(R.id.buttonPesan);
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toOrder = new Intent(v.getContext(),make_order.class);
                startActivity(toOrder);
            }
        });
        //Toast.makeText(this, String.valueOf(hargaTotal), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}