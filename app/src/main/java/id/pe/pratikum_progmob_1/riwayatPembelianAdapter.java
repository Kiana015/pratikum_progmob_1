package id.pe.pratikum_progmob_1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class riwayatPembelianAdapter extends RecyclerView.Adapter<riwayatPembelianAdapter.ListViewHolder> {
    Cursor data, forGambar;
    Context parentContext;
    dataHelper dbHelper;
    SQLiteDatabase dbRead;
    public  riwayatPembelianAdapter(Cursor cursor){
        this.data = cursor;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.riwayat_belanja,parent,false);
        parentContext = parent.getContext();
        return new riwayatPembelianAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        data.moveToFirst();
        dbHelper = new dataHelper(holder.itemView.getContext());
        dbRead = dbHelper.getReadableDatabase();
        forGambar = dbRead.rawQuery("SELECT gambarIcon FROM detailPembelian INNER JOIN alatTulis ON detailPembelian.idBarang = alatTulis.id WHERE idPembelian = "+data.getInt(data.getColumnIndex("id"))+" LIMIT 1",null);
        Cursor checkTotalBelanja = dbRead.rawQuery("SELECT*FROM detailPembelian WHERE idPembelian = "+data.getInt(data.getColumnIndex("id"))+" LIMIT 1",null);
        forGambar.moveToFirst();
        Glide.with(holder.itemView.getContext())
                .load(forGambar.getInt(forGambar.getColumnIndex("gambarIcon")))
                .apply(new RequestOptions().override(55,55))
                .into(holder.imgPhoto);
        DateFormat input = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat output_date = new SimpleDateFormat("dd-MM-yyy");
        DateFormat output_time = new SimpleDateFormat("HH:mm:ss");
        String tanggalBeli = data.getString(data.getColumnIndex("tanggalPembelian"));
        Date dateTanggalBeli = null;
        try {
            dateTanggalBeli = input.parse(tanggalBeli);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textBelanja.setText("Pembelanjaan Tanggal "+output_date.format(dateTanggalBeli)+" "+output_time.format(dateTanggalBeli));
        holder.textHarga.setText("Rp."+String.valueOf(data.getFloat(data.getColumnIndex("hargaTotal")))+"0");
        holder.textItem.setText(String.valueOf(checkTotalBelanja.getCount()));
        holder.itemRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetailRiwayat = new Intent(v.getContext(),detail_riwayat_belanja.class);
                toDetailRiwayat.putExtra("id_belanja",data.getInt(data.getColumnIndex("id")));
                holder.itemView.getContext().startActivity(toDetailRiwayat);
            }
        });
        holder.itemRiwayat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(parentContext, "Ini dihold", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.getCount();
    }
    public class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView textBelanja, textHarga, textItem;
        LinearLayout itemRiwayat;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.foto_barang);
            textBelanja = itemView.findViewById(R.id.nama_riwayat);
            textHarga = itemView.findViewById(R.id.harga_total_riwayat);
            textItem = itemView.findViewById(R.id.banyak_belanja);
            itemRiwayat = itemView.findViewById(R.id.row_item_riwayat);
        }
    }
}
