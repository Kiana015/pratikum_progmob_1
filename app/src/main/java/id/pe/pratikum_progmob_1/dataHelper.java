package id.pe.pratikum_progmob_1;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bumptech.glide.load.engine.Resource;

public class dataHelper extends SQLiteOpenHelper {
    Resources resources;
    public dataHelper (Context context)
    {
        super(context, "jbAlatTulis.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT, nama VARCHAR(50), alamat VARCHAR(200), jk VARCHAR(30), hobi VARCHAR(100), interest INTEGER(10), username VARCHAR(30), password VARCHAR(30), statusAccount BOOLEAN)");
        db.execSQL("CREATE TABLE alatTulis(id INTEGER PRIMARY KEY AUTOINCREMENT, namaAlat VARCHAR(50), hargaAlat FLOAT(20,2), satuan VARCHAR(10), gambarIcon INTEGER(100), gambarResource INTEGER(100))");
        db.execSQL("CREATE TABLE pembelian(id INTEGER PRIMARY KEY AUTOINCREMENT, id_user INTEGER, alamat_pengiriman VARCHAR(100), tanggalPembelian DATETIME, statusPembelian VARCHAR(50), hargaTotal FLOAT(20,2), statusPembayaran VARCHAR(50), jenisPembayaran VARCHAR(50))");
        db.execSQL("CREATE TABLE detailPembelian(id INTEGER PRIMARY KEY AUTOINCREMENT, idPembelian INTEGER, idBarang INTEGER, hargaSatuan FLOAT(20,2), jumlahBeli INTEGER)");
        db.execSQL("CREATE TABLE cart(id INTEGER PRIMARY KEY AUTOINCREMENT, id_user INTEGER, idBarang INTEGER, jumlahBeli INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
/*
    public void daftarAkun(String nama, String alamat, String jk, String hobi, int interest, String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        SQLiteDatabase db1 = getReadableDatabase();
        Cursor cek = db1.rawQuery("select * from user",null);
        int getId = cek.getCount();
        if(getId == 0)
        {
            getId = 1;
        }
        else
        {
            getId = getId+1;
        }
        db.rawQuery("insert into user (id, nama, alamat, jk, hobi, interest, username, password) values("+getId+",'"+nama+"','"+alamat+"','"+jk+"','"+hobi+"',"+interest+",'"+username+"','"+password+"')",null);
    }

    public Cursor getUserId(String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user", null);
        return cursor;
    }
 */
    public Cursor getUserData(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select id from user where id = "+ id, null);
        return cursor;
    }

}
