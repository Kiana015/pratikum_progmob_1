package id.pe.pratikum_progmob_1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragmentProfile extends Fragment {
    TextView nama;
    ListView menuProfile;
    String[] menuList;
    sharedPrefManager spm;
    dataHelper dbHelper;
    SQLiteDatabase dbRead, dbWrite;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_profile, container, false);
        menuProfile = view.findViewById(R.id.list_menu);
        nama = view.findViewById(R.id.nama_akun);
        menuList = new String[5];
        menuList[0] = "Riwayat Belanja";
        menuList[1] = "lihat Profile";
        menuList[2] = "Edit Profile";
        menuList[3] = "Hapus Akun";
        menuList[4] = "Logout";
        dbHelper = new dataHelper(view.getContext());
        dbRead = dbHelper.getReadableDatabase();
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        Cursor profile = dbRead.rawQuery("SELECT*FROM user WHERE id = "+spm.getSPId(view.getContext()),null);
        profile.moveToFirst();
        nama.setText(profile.getString(profile.getColumnIndex("nama")));
        menuProfile.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, menuList));
        menuProfile.setSelected(true);
        menuProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent toRiwayat = new Intent(view.getContext(),riwayatPembelian.class);
                        startActivity(toRiwayat);
                        break;
                    case 1:
                        Intent todetail = new Intent(view.getContext(),mainMenu.class);
                        startActivity(todetail);
                        break;
                    case 2:
                        Intent toEdit = new Intent(view.getContext(),editProfile.class);
                        startActivity(toEdit);
                        break;
                    case 3:
                        dbWrite.execSQL("UPDATE user SET statusAccount = 0 WHERE id = "+spm.getSPId(view.getContext()));
                        Intent toLogin = new Intent(view.getContext(),ActivityLogin.class);
                        spm.clearLoggedInUser(view.getContext());
                        startActivity(toLogin);
                        break;
                    case 4:
                        Intent toLogin1 = new Intent(view.getContext(),ActivityLogin.class);
                        spm.clearLoggedInUser(view.getContext());
                        startActivity(toLogin1);
                        break;
                }


            }
        });
        return view;

    }
}
