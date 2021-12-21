package id.pe.pratikum_progmob_1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class barang_dibeli_adapter extends RecyclerView.Adapter<barang_dibeli_adapter.ListViewHolder> {
    private Cursor listBarangDibeli;
    private Context context;
    public dataHelper dbHelper;

    public barang_dibeli_adapter(Cursor cursor)
    {
        this.listBarangDibeli = cursor;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product,parent,false);
        return new barang_dibeli_adapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        listBarangDibeli.moveToPosition(position);
        list_cart list_cart = new list_cart();
        list_cart.setSatuan(listBarangDibeli.getString(listBarangDibeli.getColumnIndex("satuan")));
        list_cart.setIdInCart(listBarangDibeli.getInt(listBarangDibeli.getColumnIndex("id")));
        list_cart.setId_barang(listBarangDibeli.getInt(listBarangDibeli.getColumnIndex("idBarang")));
        list_cart.setNama_barang(listBarangDibeli.getString(listBarangDibeli.getColumnIndex("namaAlat")));
        list_cart.setJumlah_barang(listBarangDibeli.getInt(listBarangDibeli.getColumnIndex("jumlahBeli")));
        list_cart.setFoto(listBarangDibeli.getInt(listBarangDibeli.getColumnIndex("gambarIcon")));
        list_cart.setHarga_barang(listBarangDibeli.getFloat(listBarangDibeli.getColumnIndex("hargaAlat"))*listBarangDibeli.getInt(listBarangDibeli.getColumnIndex("jumlahBeli")));
        Glide.with(holder.itemView.getContext())
                .load(list_cart.getFoto())
                .apply(new RequestOptions().override(55,55))
                .into(holder.imgPhoto);
        holder.namaBarang.setText(list_cart.getNama_barang());
        holder.hargaBarang.setText("Rp."+String.valueOf(list_cart.getHarga_barang())+"0");
        holder.satuan.setText(list_cart.getJumlah_barang()+" "+list_cart.getSatuan());
    }

    @Override
    public int getItemCount() {
        return listBarangDibeli.getCount();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView namaBarang, hargaBarang, satuan;
        public ListViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.foto_barang);
            namaBarang = itemView.findViewById(R.id.nama_riwayat);
            hargaBarang = itemView.findViewById(R.id.harga_total_riwayat);
            satuan = itemView.findViewById(R.id.banyak_belanja);

        }
    }
}
