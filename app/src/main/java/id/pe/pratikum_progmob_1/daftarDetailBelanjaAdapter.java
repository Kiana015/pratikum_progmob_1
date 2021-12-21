package id.pe.pratikum_progmob_1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class daftarDetailBelanjaAdapter extends RecyclerView.Adapter<daftarDetailBelanjaAdapter.ListViewHolder>{
    private Cursor list_barang_cursor;
    private Context context;
    public daftarDetailBelanjaAdapter(Cursor cursor){
        this.list_barang_cursor = cursor;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product,parent,false);
        return new daftarDetailBelanjaAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        list_barang_cursor.moveToPosition(position);
        Glide.with(holder.itemView.getContext())
                .load(list_barang_cursor.getInt(list_barang_cursor.getColumnIndex("gambarIcon")))
                .apply(new RequestOptions().override(55,55))
                .into(holder.imgPhoto);
        holder.namaBarang.setText(list_barang_cursor.getString(list_barang_cursor.getColumnIndex("namaAlat")));
        holder.hargaBarang.setText("Rp."+list_barang_cursor.getFloat(list_barang_cursor.getColumnIndex("hargaSatuan")));
        holder.satuan.setText(String.valueOf(list_barang_cursor.getInt(list_barang_cursor.getColumnIndex("jumlahBeli"))+" "+list_barang_cursor.getString(list_barang_cursor.getColumnIndex("satuan"))));
    }

    @Override
    public int getItemCount() {
        return list_barang_cursor.getCount();
    }
    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView namaBarang, hargaBarang, satuan;
        LinearLayout rowItem;
        public ListViewHolder(View itemview) {
            super(itemview);
            imgPhoto = itemview.findViewById(R.id.foto_barang);
            namaBarang = itemview.findViewById(R.id.nama_riwayat);
            hargaBarang = itemview.findViewById(R.id.harga_total_riwayat);
            satuan = itemview.findViewById(R.id.banyak_belanja);
            rowItem = itemview.findViewById(R.id.row_item);
        }
    }
}
