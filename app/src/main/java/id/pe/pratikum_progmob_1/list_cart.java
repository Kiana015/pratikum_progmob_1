package id.pe.pratikum_progmob_1;

public class list_cart {
    private String nama_barang;
    private int idInCart;
    private int foto;
    private float harga_barang;
    private int jumlah_barang;
    private int id_barang;
    private String satuan;

    public int getIdInCart() {
        return idInCart;
    }

    public void setIdInCart(int idInCart) {
        this.idInCart = idInCart;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public float getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(float harga_barang) {
        this.harga_barang = harga_barang;
    }

    public int getJumlah_barang() {
        return jumlah_barang;
    }

    public void setJumlah_barang(int jumlah_barang) {
        this.jumlah_barang = jumlah_barang;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
