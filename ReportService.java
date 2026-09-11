import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.text.NumberFormat;

public class ReportService {

    // Helper privat untuk memformat angka menjadi Rupiah standar Indonesia (misal: Rp150.000)
    private String formatRupiah(double nominal) {
        Locale localeID = Locale.forLanguageTag("id-ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(nominal);
    }

    // 1. Ringkasan penjualan harian
    public void tampilkanRingkasanHarian(ArrayList<Transaction> daftarTransaksi) {
        System.out.println("\n========================================");
        System.out.println("          LAPORAN PENJUALAN HARIAN       ");
        System.out.println("========================================");

        // Pengecekan jika belum ada transaksi sama sekali
        if (daftarTransaksi == null || daftarTransaksi.isEmpty()) {
            System.out.println(" Status : Belum ada transaksi tercatat.");
            System.out.println("========================================");
            return;
        }

        double totalPendapatan = 0;
        for (Transaction t : daftarTransaksi) {
            totalPendapatan += t.getTotalAkhir();
        }

        System.out.println("Jumlah Transaksi Berhasil : " + daftarTransaksi.size());
        System.out.println(" Total Pendapatan Toko     : " + formatRupiah(totalPendapatan));
        System.out.println("========================================");
    }

    // 2. Daftar stok menipis (< 10 unit)
    public void tampilkanStokMenipis(ArrayList<Product> daftarProduk) {
        System.out.println("\n========================================");
        System.out.println("     PERINGATAN: STOK MENIPIS (< 10)    ");
        System.out.println("========================================");

        if (daftarProduk == null || daftarProduk.isEmpty()) {
            System.out.println(" Daftar produk kosong.");
            System.out.println("========================================");
            return;
        }

        boolean adaPeringatan = false;
        for (Product p : daftarProduk) {
            if (p.getStok() < 10) {
                System.out.println(p);
                adaPeringatan = true;
            }
        }
        if (!adaPeringatan) {
            System.out.println("Seluruh stok produk berada dalam kondisi aman.");
        }
        System.out.println("============================================");
    }

    // 3. Top 3 produk terlaris berdasarkan unit terjual
    public void tampilkanTop3Terlaris(ArrayList<Product> daftarProduk) {
        System.out.println("\n========================================");
        System.out.println("          TOP 3 PRODUK TERLARIS         ");
        System.out.println("========================================");

        if (daftarProduk == null || daftarProduk.isEmpty()) {
            System.out.println(" Belum ada data produk.");
            System.out.println("========================================");
            return;
        }

        ArrayList<Product> copyList = new ArrayList<>(daftarProduk);

        // Pengurutan menurun berdasarkan atribut unit terjual
        copyList.sort((p1, p2) -> Integer.compare(p2.getTerjual(), p1.getTerjual()));

        int limit = Math.min(3, copyList.size());
        for (int i = 0; i < limit; i++) {
            Product p = copyList.get(i);
            System.out.printf("%d. %-20s | Terjual: %d unit\n", (i + 1), p.getNama(), p.getTerjual());
        }
        System.out.println("=============================");
    }
}