# 台北旅遊網 App

[App Demo Video](https://youtu.be/CVJD96hzTug?si=ZMzlYit79taVZdUr)


| Home Screen | News Screen | Webview Screen | Attraction Screen |
|--------------|---------------|--------------|-----------------|
| ![Search](https://github.com/user-attachments/assets/bd4946aa-f901-4f4d-9e4d-1c170f9d9b9a) | ![Detail](https://github.com/user-attachments/assets/6d2f659b-4230-4bd9-b902-0684148ebb6f) | ![Share](https://github.com/user-attachments/assets/0210671a-3a47-46b4-8f12-42a3321f7f8a) | ![Settings](https://github.com/user-attachments/assets/aa9b8434-0a7c-44f0-bdd8-894b8640956d) |

### Libraries 
- 使用 Jetpack Compose + MVI 架構
- Koin 做 Dependency Injection
- OKHttp 和 Retrofit 處理 HTTP Request
- Coil 處理 Jetpack Compose 上的圖片載入
- Mockk 撰寫測試

### 功能
- 支援多國語言動態切換，也可依據 Android 系統目前語系顯示
- 多國語言切換時 API 也會自動重打
- 支援 Dark 和 Light Mode 動態切換
- 支援螢幕轉向，有處理螢幕 cutout
- 比示意圖多了一個新聞列表，從首頁的「看更多」可以進入
- 首頁景點支援 paging 機制，可以一直下滑載入更多
- 景點內容有 Gallery 和 Indicator 左右滑動圖片
- 首頁 HomeViewModel 有使用 Mockk 寫測試
  
### 可能會有的問題
- API 不是全部的語言都有完整資料
- 有些景點 API 沒有給出圖片，UI 會顯示不出來
- 因為 API 的限制，不是所有頁面的內容都支援 onConfigurationChanged 切換語言
