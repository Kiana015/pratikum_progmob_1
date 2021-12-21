package id.pe.pratikum_progmob_1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class list_cart_adapter extends RecyclerView.Adapter<list_cart_adapter.ListViewHolder>{
    private Cursor list_cart_cursor;
    private Context context;
    public dataHelper dbHelper;
    public int count;
    public Context parentCursor;
    public  list_cart_adapter(Cursor cursor){
        this.list_cart_cursor = cursor;
    }
    @NonNull
    @Override
    public list_cart_adapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_carting,parent,false);
        parentCursor = parent.getContext();
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull list_cart_adapter.ListViewHolder holder, int position) {
        list_cart_cursor.moveToPosition(position);
        list_cart list_cart = new list_cart();
        list_cart.setIdInCart(list_cart_cursor.getInt(list_cart_cursor.getColumnIndex("id")));
        list_cart.setId_barang(list_cart_cursor.getInt(list_cart_cursor.getColumnIndex("idBarang")));
        list_cart.setNama_barang(list_cart_cursor.getString(list_cart_cursor.getColumnIndex("namaAlat")));
        list_cart.setJumlah_barang(list_cart_cursor.getInt(list_cart_cursor.getColumnIndex("jumlahBeli")));
        list_cart.setFoto(list_cart_cursor.getInt(list_cart_cursor.getColumnIndex("gambarIcon")));
        list_cart.setHarga_barang(list_cart_cursor.getFloat(list_cart_cursor.getColumnIndex("hargaAlat"))*list_cart_cursor.getInt(list_cart_cursor.getColumnIndex("jumlahBeli")));
        Glide.with(holder.itemView.getContext())
                .load(list_cart.getFoto())
                .apply(new RequestOptions().override(55,55))
                .into(holder.imgPhoto);
        holder.jumlahBeli.setEnabled(false);
        holder.namaBarang.setText(list_cart.getNama_barang());
        String hargaCart = "Rp."+String.valueOf(list_cart.getHarga_barang());
        holder.hargaBarang.setText(hargaCart);
        holder.jumlahBeli.setText(String.valueOf(list_cart.getJumlah_barang()));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new dataHelper(parentCursor);
                SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                list_cart.setJumlah_barang(list_cart.getJumlah_barang()+1);
                dbWrite.execSQL("UPDATE cart SET jumlahBeli = "+list_cart.getJumlah_barang()+" WHERE id = "+list_cart.getIdInCart());
                holder.jumlahBeli.setText(String.valueOf(list_cart.getJumlah_barang()));
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new dataHelper(parentCursor);
                SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                if(list_cart.getJumlah_barang() == 1)
                {
                    AlertDialog.Builder allert = new AlertDialog.Builder(v.getContext());
                    allert.setTitle("Apakah Anda Ingin Menghapus Daftar Belanja Ini?");
                    allert.setMessage("Klik ya untuk menghapus!")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbWrite.execSQL("DELETE FROM cart WHERE id = "+list_cart_cursor.getInt(list_cart_cursor.getColumnIndex("id")));
                                    Intent refresh = new Intent(v.getContext(), carting.class);
                                    v.getContext().startActivity(refresh);
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = allert.create();
                    alertDialog.show();
                }
                else
                {
                    list_cart.setJumlah_barang(list_cart.getJumlah_barang()-1);
                    dbWrite.execSQL("UPDATE cart SET jumlahBeli = "+list_cart.getJumlah_barang()+" WHERE id = "+list_cart.getIdInCart());
                    holder.jumlahBeli.setText(String.valueOf(list_cart.getJumlah_barang()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        count = list_cart_cursor.getCount();
        return count;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView namaBarang, hargaBarang;
        EditText jumlahBeli;
        LinearLayout rowItem;
        ImageButton plus,minus;
        public ListViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.foto_profil);
            jumlahBeli = itemView.findViewById(R.id.jumlahBeli);
            namaBarang = itemView.findViewById(R.id.nama_barang_carting);
            hargaBarang = itemView.findViewById(R.id.hargaCarting);
            rowItem = itemView.findViewById(R.id.row_item_carting);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);

        }
    }
}
