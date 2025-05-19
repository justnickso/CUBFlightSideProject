# CUBFlightSideProject

一款 Kotlin 編寫的 Android App，提供小港機場即時航班資訊查詢與匯率換算功能，設計上支援浮動視窗模式，方便在使用其他 App 時快速進行計算。

## ✨ 特色功能

- **小港機場航班查詢**：整合即時 API，查詢入境與出境航班資訊。
- **匯率計算工具**：可自訂匯率計算，支援多種貨幣顯示。
- **Floating Calculator**：以浮動視窗呈現小鍵盤，可在任意畫面快速輸入並換算金額。
- **UI**：使用 Lottie 動畫增強使用者體驗。

## 🧱 技術架構

- **語言**：Kotlin
- **架構模式**：MVVM
- **依賴注入**：Koin
- **協程處理**：Kotlin Coroutine、Flow、SharedFlow
- **網路通訊**：Retrofit + OkHttp + Moshi
- **UI 動畫**：Lottie
- **浮動視窗實作**：WindowManager + Foreground Service
- **圖片載入**：Coil
- **資料儲存**：Proto DataStore*

## 📷 影片
[App Demo](https://www.youtube.com/watch?v=6fbo8Wa45UQ)

## 🛠️ 開發環境

- Android Studio Meerkat
- minSdk: 26
- targetSdk: 35

---
