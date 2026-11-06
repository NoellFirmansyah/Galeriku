# GaleriKu

Aplikasi galeri foto Android, terinspirasi tampilan aplikasi Photos di iPhone terbaru
(grid foto per-bulan, tab Library/Album/Cari, viewer foto layar penuh dengan swipe &
pinch-zoom). Dibangun dengan Kotlin + Jetpack Compose, **minSdk 28 (Android 9 Pie)**.

> Ini adalah *original design* yang terinspirasi gaya visual Photos iPhone (grid rapat,
> header bulan, bottom bar 3 tab, tema gelap) - bukan tiruan/clone logo atau aset Apple.
> Aman dipakai & dimodifikasi bebas.

## Fitur

- **Library** - semua foto dari galeri perangkat, dikelompokkan per bulan-tahun.
- **Album** - dikelompokkan otomatis dari folder penyimpanan (kamera, WhatsApp, dll).
- **Cari** - cari foto berdasarkan nama file, nama album, atau bulan.
- **Viewer foto** - swipe antar foto, pinch-to-zoom, bagikan, favorit (sesi ini), hapus.
- Mendukung tema terang & gelap otomatis mengikuti sistem.
- Permission storage otomatis menyesuaikan versi Android (legacy di Android 9-12L,
  granular media permission di Android 13+).

## Kebutuhan

| Kebutuhan | Versi |
| --- | --- |
| Android Studio | Ladybug (2024.2) atau lebih baru |
| JDK | 17 (dibundel Android Studio) |
| minSdk | 28 (Android 9) |
| compileSdk / targetSdk | 34 |

## Cara Build

1. Buka folder ini di **Android Studio** (`File > Open`).
2. Biarkan Android Studio men-generate Gradle wrapper & sync dependency
   (butuh koneksi internet saat pertama kali - semua dependency diambil dari
   Google Maven & Maven Central, tidak ada yang eksotis).
3. Hubungkan HP Android 9 via USB (aktifkan USB debugging), lalu klik **Run ▶**.

   Atau build APK manual:
   ```sh
   ./gradlew assembleDebug
   # hasil: app/build/outputs/apk/debug/app-debug.apk
   ```

   > Catatan: project ini belum menyertakan `gradle-wrapper.jar` biner. Saat pertama
   > kali dibuka, Android Studio akan menawarkan untuk membuatkannya otomatis
   > ("Create Gradle Wrapper" / memakai Gradle bawaan Android Studio). Ikuti saja
   > prompt tersebut, atau jalankan `gradle wrapper` sekali jika kamu sudah punya
   > Gradle terpasang di komputer.

4. Install ke HP: `adb install app/build/outputs/apk/debug/app-debug.apk`

## Struktur Proyek

```
app/src/main/java/com/galeriku/app/
├── MainActivity.kt           # entry point, minta izin, setContent
├── GaleriApp.kt               # Application class
├── data/MediaStoreRepository.kt   # query foto dari MediaStore
├── model/Photo.kt              # model Photo, Album, PhotoSection
├── viewmodel/GalleryViewModel.kt  # state & logika (favorit, hapus, refresh)
├── util/                       # helper permission & pengelompokan tanggal
├── theme/                      # warna, tipografi, Material3 theme
└── ui/
    ├── GaleriApp.kt (GaleriAppRoot)  # navigasi antar tab & viewer
    ├── components/              # grid item, search field, bottom nav, dll
    └── screens/                 # LibraryScreen, AlbumsScreen, SearchScreen, PhotoDetailScreen
```

## Keterbatasan yang perlu diketahui

- **Hapus foto**: di Android 9-10, `ContentResolver.delete()` hanya dijamin berhasil
  untuk file yang dibuat sendiri oleh aplikasi ini (tanpa izin `WRITE_EXTERNAL_STORAGE`
  tambahan). Jika gagal, aplikasi menampilkan pesan error, bukan memaksakan.
- **Favorit**: saat ini hanya disimpan di memori (hilang saat aplikasi ditutup paksa).
  Untuk persisten, tambahkan database lokal (mis. Room - dependency-nya tinggal
  ditambahkan di `app/build.gradle.kts`, strukturnya sudah disiapkan agar mudah diperluas).
- **Pinch-zoom** di viewer foto adalah implementasi manual sederhana; pada beberapa
  perangkat gesture zoom bisa sedikit "berebut" dengan swipe ganti foto. Untuk hasil
  lebih presisi, pertimbangkan mengganti `ZoomableAsyncImage` dengan library seperti
  `me.saket.telephoto`.
- Kategori pencarian saat ini berbasis teks (nama file/album/bulan), bukan pengenalan
  objek/wajah berbasis AI.
