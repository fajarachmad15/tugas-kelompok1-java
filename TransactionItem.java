public class TransactionItem {
    private Product product;
    private int jumlah;

    // Constructor
    public TransactionItem(Product product, int jumlah) {
        if (product == null) {
            throw new IllegalArgumentException(
                "Produk tidak boleh kosong."
            );
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException(
                "Jumlah barang harus lebih dari 0."
            );
        }
        this.product = product;
        this.jumlah = jumlah;
    }

    // Getter dan Setter
    public Product getProduct() { return product; }
    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException(
                "Produk tidak boleh kosong."
            );
        }
        this.product = product;
    }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException(
                "Jumlah barang harus lebih dari 0."
            );
        }
        this.jumlah = jumlah;
    }

    // Hitung subtotal per baris barang
    public double getSubtotal() {
        return product.getHarga() * jumlah;
    }

    public void tampilkanItem() {
        System.out.printf("  %-18s x%-3d  @Rp%-9.2f  = Rp%.2f\n",
            product.getNama(), jumlah, product.getHarga(), getSubtotal());
    }
}