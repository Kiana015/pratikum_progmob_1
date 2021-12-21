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

public class list_barang_adapter extends RecyclerView.Adapter<list_barang_adapter.ListViewHolder>{
    private Cursor list_barang_cursor;
    private Context context;
    public list_barang_adapter(Cursor cursor){
        this.list_barang_cursor = cursor;

    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        list_barang_cursor.moveToPosition(position);
        list_barang listBarang = new list_barang();
        listBarang.setNama_barang(list_barang_cursor.getString(list_barang_cursor.getColumnIndex("namaAlat")));
        listBarang.setFoto(list_barang_cursor.getInt(list_barang_cursor.getColumnIndex("gambarResource")));
        listBarang.setHarga_barang(list_barang_cursor.getFloat(list_barang_cursor.getColumnIndex("hargaAlat")));
        listBarang.setSatuan(list_barang_cursor.getString(list_barang_cursor.getColumnIndex("satuan")));
        listBarang.setId_barang(list_barang_cursor.getInt(list_barang_cursor.getColumnIndex("id")));
        Glide.with(holder.itemView.getContext())
                .load(listBarang.getFoto())
                .apply(new RequestOptions().override(55,55))
                .into(holder.imgPhoto);
        holder.namaBarang.setText(listBarang.getNama_barang());
        holder.hargaBarang.setText("Rp."+String.valueOf(listBarang.getHarga_barang()));
        holder.satuan.setText(listBarang.getSatuan());
        holder.rowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                Intent toDetail = new Intent(v.getContext(),detailBarang.class);
                toDetail.putExtra("id_barang",listBarang.getId_barang());
                context.startActivity(toDetail);

            }
        });
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
