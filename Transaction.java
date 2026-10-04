import java.util.ArrayList;

public class Transaction {
    private String idTransaksi;
    private ArrayList<TransactionItem> items;
    private String metodePembayaran;
    private double diskon;
    private double totalAkhir;

    // Constructor
    public Transaction(String idTransaksi) {
        this.idTransaksi = idTransaksi;
        this.items = new ArrayList<>();
        this.diskon = 0;
        this.totalAkhir = 0;
    }

    public String getIdTransaksi() { return idTransaksi; }
    public ArrayList<TransactionItem> getItems() { return items; }
    public double getDiskon() { return diskon; }
    public double getTotalAkhir() { return totalAkhir; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metode) {
        if (metode == null) {
            throw new IllegalArgumentException(
                "Metode pembayaran tidak boleh kosong."
            );
        }
        if (!metode.equalsIgnoreCase("tunai") && !metode.equalsIgnoreCase("transfer")) {
            throw new IllegalArgumentException(
                "Metode pembayaran harus tunai atau transfer."
            );
        }
        this.metodePembayaran = metode;
    }

    // Tambah barang ke keranjang
    public void tambahItem(Product p, int qty) {
        if (p == null) {
            throw new IllegalArgumentException(
                "Produk tidak boleh kosong."
            );
        }
        if (qty <= 0) {
            throw new IllegalArgumentException(
                "Jumlah barang harus lebih dari 0."
            );
        }
        if (qty > p.getStok()) {
            throw new IllegalArgumentException(
                "Stok produk tidak mencukupi."
            );
        }
        items.add(new TransactionItem(p, qty));
    }

    // Hitung kotor sebelum diskon
    public double hitungSubtotalKotor() {
        double subtotal = 0;
        for (TransactionItem item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    // Logika diskon (misal: belanja di atas Rp100.000 diskon 5%)
    public void terapkanDiskon() {
        double kotor = hitungSubtotalKotor();
        // TODO: Sesuaikan aturan diskon kelompok Anda
        if (kotor >= 100000) {
            this.diskon = kotor * 0.05;
        } else {
            this.diskon = 0;
        }
        this.totalAkhir = kotor - this.diskon;
    }

    // Cetak struk ke konsol
    public void cetakStruk(double nominalBayar) {
        if (metodePembayaran == null) {
            throw new IllegalStateException(
                "Metode pembayaran belum dipilih."
            );
        }
        System.out.println("\n========== STRUK PEMBAYARAN TOKO RETAIL ==========");
        System.out.println("ID Transaksi: " + idTransaksi);
        System.out.println("Metode      : " + metodePembayaran);
        System.out.println("--------------------------------------------------");
        for (TransactionItem item : items) {
            item.tampilkanItem();
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Subtotal    : Rp%.2f\n", hitungSubtotalKotor());
        System.out.printf("Diskon      : Rp%.2f\n", diskon);
        System.out.printf("Total Bayar : Rp%.2f\n", totalAkhir);

        // Pembayaran tunai
        if (metodePembayaran.equalsIgnoreCase("tunai")) {
            if (nominalBayar < totalAkhir) {
                throw new IllegalArgumentException(
                    "Nominal pembayaran kurang dari total."
                );
            }
            System.out.printf("Uang Diterima: Rp%.2f\n", nominalBayar);
            System.out.printf("Kembalian   : Rp%.2f\n", nominalBayar - totalAkhir);
        // Pembayaran transfer
        } else {
            System.out.println( "Status      : Pembayaran berhasil melalui transfer");
        }
        System.out.println("==================================================\n");
    }
}