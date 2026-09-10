import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static ArrayList<Product> daftarProduk = new ArrayList<>();
    private static ArrayList<Transaction> riwayatTransaksi = new ArrayList<>();
    private static ReportService reportService = new ReportService();
    private static Scanner scanner = new Scanner(System.in);
    private static int counterTransaksi = 1;

    public static void main(String[] args) {
        inisialisasiDataAwal();

        boolean berjalan = true;
        while (berjalan) {
            try {
                System.out.println("\n=== SISTEM MANAJEMEN TOKO RETAIL (TR) ===");
                System.out.println("1. Tampilkan Semua Produk");
                System.out.println("2. Cari Produk (Nama / Kategori)");
                System.out.println("3. Transaksi Penjualan Baru");
                System.out.println("4. Ringkasan Penjualan Harian");
                System.out.println("5. Cek Stok Menipis");
                System.out.println("6. Cek Top 3 Produk Terlaris");
                System.out.println("7. Keluar");
                System.out.print("Pilih menu [1-7]: ");

                int pilihan = scanner.nextInt();
                scanner.nextLine(); // Bersihkan buffer newline

                switch (pilihan) {
                    case 1:
                        tampilkanSemuaProduk();
                        break;
                    case 2:
                        cariProduk();
                        break;
                    case 3:
                        prosesTransaksiBaru();
                        break;
                    case 4:
                        reportService.tampilkanRingkasanHarian(riwayatTransaksi);
                        break;
                    case 5:
                        reportService.tampilkanStokMenipis(daftarProduk);
                        break;
                    case 6:
                        reportService.tampilkanTop3Terlaris(daftarProduk);
                        break;
                    case 7:
                        berjalan = false;
                        System.out.println("Sistem ditutup. Terima kasih!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan pilih 1-7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Masukan harus berupa angka!");
                scanner.nextLine(); // Reset scanner jika salah tipe input
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }
    }

    // Inisialisasi dummy data sesuai kategori studi kasus
    private static void inisialisasiDataAwal() {
        daftarProduk.add(new Product("P01", "Roti Tawar", "Makanan", 15000, 20));
        daftarProduk.add(new Product("P02", "Susu Kotak 1L", "Minuman", 20000, 8));
        daftarProduk.add(new Product("P03", "Sabun Cuci Piring", "Kebersihan", 12000, 15));
        daftarProduk.add(new Product("P04", "Baterai AA Pack", "Elektronik", 25000, 5));
        daftarProduk.add(new Product("P05", "Air Mineral 600ml", "Minuman", 4000, 50));
    }

    private static void tampilkanSemuaProduk() {
        System.out.println("\n----------------- DAFTAR KATALOG PRODUK -----------------");
        for (Product p : daftarProduk) {
            System.out.println(p);
        }
    }

    private static void cariProduk() {
        System.out.print("Masukkan kata kunci (nama atau kategori): ");
        String keyword = scanner.nextLine().toLowerCase();
        boolean ditemukan = false;

        for (Product p : daftarProduk) {
            if (p.getNama().toLowerCase().contains(keyword) || 
                p.getKategori().toLowerCase().contains(keyword)) {
                System.out.println(p);
                ditemukan = true;
            }
        }
        if (!ditemukan) {
            System.out.println("Produk tidak ditemukan.");
        }
    }

    private static void prosesTransaksiBaru() {
        String idTrx = "TRX-" + String.format("%03d", counterTransaksi++);
        Transaction trx = new Transaction(idTrx);

        boolean tambahLagi = true;
        while (tambahLagi) {
            tampilkanSemuaProduk();
            System.out.print("Masukkan ID Produk: ");
            String id = scanner.nextLine();

            Product produkDipilih = null;
            for (Product p : daftarProduk) {
                if (p.getId().equalsIgnoreCase(id)) {
                    produkDipilih = p;
                    break;
                }
            }

            if (produkDipilih == null) {
                System.out.println("Produk tidak ditemukan!");
            } else {
                System.out.print("Masukkan jumlah beli: ");
                int qty = scanner.nextInt();
                scanner.nextLine();

                if (qty > produkDipilih.getStok()) {
                    System.out.println("Stok tidak mencukupi! Sisa: " + produkDipilih.getStok());
                } else {
                    trx.tambahItem(produkDipilih, qty);
                    produkDipilih.kurangiStok(qty);
                    System.out.println("Item berhasil ditambahkan ke keranjang.");
                }
            }

            System.out.print("Tambah produk lain? (y/n): ");
            String jawab = scanner.nextLine();
            if (!jawab.equalsIgnoreCase("y")) {
                tambahLagi = false;
            }
        }

        if (trx.getItems().isEmpty()) {
            System.out.println("Transaksi dibatalkan karena tidak ada item yang dibeli.");
            return;
        }

        trx.terapkanDiskon();
        System.out.printf("Total yang harus dibayar: Rp%.2f\n", trx.getTotalAkhir());

        System.out.print("Pilih Metode Pembayaran (1. Tunai / 2. Transfer): ");
        int metode = scanner.nextInt();
        scanner.nextLine();
        trx.setMetodePembayaran(metode == 1 ? "Tunai" : "Transfer");

        double bayar = 0;
        while (bayar < trx.getTotalAkhir()) {
            System.out.print("Masukkan nominal pembayaran: Rp");
            bayar = scanner.nextDouble();
            scanner.nextLine();
            if (bayar < trx.getTotalAkhir()) {
                System.out.println("Nominal kurang! Silakan bayar sesuai atau lebih dari total tagihan.");
            }
        }

        trx.cetakStruk(bayar);
        riwayatTransaksi.add(trx);
    }
}