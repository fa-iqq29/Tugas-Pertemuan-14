# 🧩 Tugas Pertemuan 14 — Sistem Manajemen Data Dosen, Mata Kuliah, dan Mengajar

Aplikasi ini merupakan sistem manajemen data sederhana yang dibangun menggunakan Java (Swing + JPA + PostgreSQL).
Fitur utamanya meliputi pengelolaan data:

- Dosen

- Mata Kuliah

- Mengajar (relasi antara Dosen dan Mata Kuliah)

Selain CRUD, aplikasi ini telah diperbarui dengan beberapa fitur tambahan seperti:

- Download data ke format CSV

- Ubah Password dengan validasi Nama Ibu (security confirmation)

- Normalisasi database agar relasi antar tabel lebih jelas dan terstruktur

## 🚀 Fitur Utama

### 1. CRUD Data Dosen

- Tambah dosen

- Update dosen

- Hapus dosen

- Tampilkan daftar dosen

### 2. CRUD Mata Kuliah

- Tambah mata kuliah

- Update mata kuliah

- Hapus data mata kuliah

- Penampilan tabel dengan alignment rapi

### 3. CRUD Mengajar

- Input jadwal mengajar berdasarkan dosen dan mata kuliah yang sudah terdaftar

- Relasi otomatis menggunakan objek JPA

- Tampilan tabel dinamis

## 🆕 Fitur Tambahan (Enhanced Features)
### ✔ Download to CSV

Aplikasi kini dilengkapi fitur untuk mengekspor data ke file CSV menggunakan:

- JnaFileChooser untuk memilih direktori

- Opsi format file otomatis tanpa filter bawaan Windows

- Fungsi exportToCSV() yang mengekspor tabel Swing ke file .csv

Fitur ini memudahkan pengguna dalam:

- Backup data

- Membuka data via Excel / Google Sheets

- Mengirim laporan sederhana

### ✔ Ubah Password dengan Konfirmasi Nama Ibu

Pada modul login/reset password, pengguna harus:

- Memasukkan username

- Sistem menampilkan dialog input tambahan berupa Nama Ibu Kandung

- Jika nama ibu sesuai dengan data user, barulah form ubah password muncul

Fitur ini meningkatkan keamanan dengan menambahkan security question.

### ✔ Normalisasi Database

Database telah diperbaiki agar:

- Tidak ada data redundan

- Setiap tabel memiliki primary key yang jelas

- Relasi dikelola dengan foreign key

Dengan normalisasi ini, data menjadi rapi, konsisten, dan scalable.

## ✍️ Penulis
Faiq

Mahasiswa Sistem Informasi

Semester 3

Universitas Islam Negeri Sunan Ampel Surabaya
