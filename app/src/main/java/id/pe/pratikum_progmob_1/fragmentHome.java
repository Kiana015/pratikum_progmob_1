package id.pe.pratikum_progmob_1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragmentHome extends Fragment{
    dataHelper dbHelper;
    private RecyclerView listBarangAll;
    private RecyclerView.Adapter adapter;
    public Cursor cursor;
    public ImageView circle_image;
    Context thisContext;
    FloatingActionButton cart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisContext = container.getContext();
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_home, container, false);
        Resources res = getResources();
        //masalah utama (fixed)
        dbHelper = new dataHelper(view.getContext());
        SQLiteDatabase dbview = dbHelper.getReadableDatabase();
        cursor = dbview.rawQuery("SELECT*FROM alatTulis",null);
        listBarangAll = view.findViewById(R.id.listBarang);
        RecyclerView.LayoutManager mymanager = new LinearLayoutManager(view.getContext());
        listBarangAll.setLayoutManager(mymanager);
        list_barang_adapter list_barang_adapter = new list_barang_adapter(cursor);
        listBarangAll.setAdapter(list_barang_adapter);
        cart = view.findViewById(R.id.fab1);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context startToCarting = v.getContext();
                Intent toCarting = new Intent(v.getContext(), id.pe.pratikum_progmob_1.carting.class);
                startToCarting.startActivity(toCarting);
            }
        });
        return view;
    }
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
