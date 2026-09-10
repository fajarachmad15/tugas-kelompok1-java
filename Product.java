public class Product {
    private String id;
    private String nama;
    private String kategori;
    private double harga;
    private int stok;
    private int terjual;

    // Constructor
    public Product(String id, String nama, String kategori, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.terjual = 0;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public int getTerjual() { return terjual; }
    public void setTerjual(int terjual) { this.terjual = terjual; }

    // Logika pengurangan stok dan penambahan unit terjual
    public void kurangiStok(int qty) {
        // TODO: Validasi kecukupan stok sebelum dikurangi
        this.stok -= qty;
        this.terjual += qty;
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-20s | %-15s | Rp%-10.2f | Stok: %-4d | Terjual: %-4d",
                id, nama, kategori, harga, stok, terjual);
    }
}