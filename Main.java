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
                System.out.println("\n=================================================");
                System.out.println("   SISTEM MANAJEMEN TOKO RETAIL (TR) - BANDUNG   ");
                System.out.println("=================================================");
                System.out.println("1. Tampilkan Semua Produk");
                System.out.println("2. Cari Produk (Nama / Kategori)");
                System.out.println("3. Transaksi Penjualan Baru");
                System.out.println("4. Ringkasan Penjualan Harian");
                System.out.println("5. Cek Peringatan Stok Menipis (< 10)");
                System.out.println("6. Cek Top 3 Produk Terlaris");
                System.out.println("7. Keluar Sistem");
                System.out.print("Pilih menu [1-7]: ");

                int pilihan = scanner.nextInt();
                scanner.nextLine();

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
                        System.out.println("\nSistem ditutup. Terima kasih!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Masukkan angka 1 sampai 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Input menu harus berupa angka!");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Terjadi kendala: " + e.getMessage());
            }
        }
    }

    // Data awal katalog produk toko
    private static void inisialisasiDataAwal() {
        daftarProduk.add(new Product("P01", "Roti Tawar", "Makanan", 15000, 20));
        daftarProduk.add(new Product("P02", "Susu Kotak 1L", "Minuman", 20000, 8));
        daftarProduk.add(new Product("P03", "Sabun Cuci Piring", "Kebersihan", 12000, 15));
        daftarProduk.add(new Product("P04", "Baterai AA Pack", "Elektronik", 25000, 5));
        daftarProduk.add(new Product("P05", "Air Mineral 600ml", "Minuman", 4000, 50));
    }

    // Tampilkan list barang
    private static void tampilkanSemuaProduk() {
        System.out.println("\n----------------------- DAFTAR KATALOG PRODUK TR -----------------------");
        for (Product p : daftarProduk) {
            System.out.println(p);
        }
    }

    // Cari produk berdasarkan nama atau kategori
    private static void cariProduk() {
        System.out.print("\nMasukkan nama atau kategori produk: ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        boolean ditemukan = false;

        System.out.println("\n---------------------------- HASIL PENCARIAN ---------------------------");
        for (Product p : daftarProduk) {
            if (p.getNama().toLowerCase().contains(keyword) || 
                p.getKategori().toLowerCase().contains(keyword)) {
                System.out.println(p);
                ditemukan = true;
            }
        }
        if (!ditemukan) {
            System.out.println("Produk \"" + keyword + "\" tidak ditemukan.");
        }
    }

    // Alur transaksi kasir
    private static void prosesTransaksiBaru() {
        String idTrx = "TRX-" + String.format("%03d", counterTransaksi++);
        Transaction trx = new Transaction(idTrx);

        boolean tambahLagi = true;
        while (tambahLagi) {
            tampilkanSemuaProduk();
            System.out.print("\nMasukkan ID Produk: ");
            String id = scanner.nextLine().trim();

            Product produkDipilih = null;
            for (Product p : daftarProduk) {
                if (p.getId().equalsIgnoreCase(id)) {
                    produkDipilih = p;
                    break;
                }
            }

            if (produkDipilih == null) {
                System.out.println("ID produk tidak ditemukan!");
            } else if (produkDipilih.getStok() <= 0) {
                System.out.println("Stok produk ini habis.");
            } else {
                // Validasi jumlah beli agar tidak lolos angka minus atau selain angka
                int qty = 0;
                while (true) {
                    try {
                        System.out.print("Masukkan jumlah beli: ");
                        qty = scanner.nextInt();
                        scanner.nextLine();

                        if (qty <= 0) {
                            System.out.println("Jumlah beli minimal 1 unit!");
                        } else if (qty > produkDipilih.getStok()) {
                            System.out.println("Stok kurang! Sisa stok: " + produkDipilih.getStok());
                        } else {
                            break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Masukkan angka bulat!");
                        scanner.nextLine();
                    }
                }

                // Masuk keranjang dan potong stok
                trx.tambahItem(produkDipilih, qty);
                produkDipilih.kurangiStok(qty);
                System.out.println("Sukses masuk keranjang: " + produkDipilih.getNama() + " (" + qty + " item)");
            }

            System.out.print("Tambah produk lain? (y/n): ");
            String jawab = scanner.nextLine().trim();
            if (!jawab.equalsIgnoreCase("y")) {
                tambahLagi = false;
            }
        }

        if (trx.getItems().isEmpty()) {
            System.out.println("Transaksi dibatalkan karena tidak ada item yang dibeli.");
            return;
        }

        // Terapkan diskon otomatis
        trx.terapkanDiskon();
        System.out.printf("\nTotal Tagihan: Rp%.2f\n", trx.getTotalAkhir());

        // Pilih metode bayar
        int metode = 0;
        while (metode != 1 && metode != 2) {
            try {
                System.out.print("Pilih Metode Pembayaran (1. Tunai / 2. Transfer): ");
                metode = scanner.nextInt();
                scanner.nextLine();
                if (metode != 1 && metode != 2) {
                    System.out.println("Pilihan salah, ketik 1 atau 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Masukkan angka 1 atau 2!");
                scanner.nextLine();
            }
        }

        double nominalBayar = 0;
        if (metode == 1) {
            trx.setMetodePembayaran("Tunai");
            // Validasi uang tunai harus cukup
            while (nominalBayar < trx.getTotalAkhir()) {
                try {
                    System.out.print("Masukkan uang tunai diterima: Rp");
                    nominalBayar = scanner.nextDouble();
                    scanner.nextLine();

                    if (nominalBayar < trx.getTotalAkhir()) {
                        System.out.printf("Uang kurang Rp%.2f, silakan masukkan ulang.\n", (trx.getTotalAkhir() - nominalBayar));
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Masukkan angka nominal yang benar!");
                    scanner.nextLine();
                }
            }
        } else {
            trx.setMetodePembayaran("Transfer");
            System.out.print("Masukkan Nomor Referensi / Bank: ");
            String noRef = scanner.nextLine().trim();
            System.out.println("Pembayaran transfer [" + noRef + "] diverifikasi.");
            nominalBayar = trx.getTotalAkhir(); // Transfer dianggap uang pas
        }

        // Cetak struk dan masukkan ke history transaksi
        trx.cetakStruk(nominalBayar);
        riwayatTransaksi.add(trx);
    }
}