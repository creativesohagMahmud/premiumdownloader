# Premium Downloader (Java + XML)

A Java/XML Android Studio starter project with a custom logo, direct permitted-media downloads, a Media3 player, device media library, download history, YouTube official-link opening, premium screen placeholder, and GitHub Actions debug APK build workflow.

## Included
- Custom app logo (`assets/premium_downloader_logo.png`) and launcher icon resources.
- HTTPS direct-media URL validation and Android DownloadManager integration.
- Media3 playback for supported remote URLs and local MediaStore `content://` URIs.
- Device audio/video library with Android 13+ and legacy permission handling.
- Download history from Android DownloadManager.
- YouTube URL validation and opening the official YouTube app/site; no extraction or bypass.
- GitHub Actions workflow: `.github/workflows/android-build.yml`.

## Build
1. Extract this ZIP.
2. Upload the project contents to a GitHub repository (the `.github` directory must be included).
3. Open **Actions** and run the Android Cloud Build workflow, or push to `main`.
4. Download the generated `premium-downloader-debug-apk` artifact.

## Important configuration / limitations
This is a functional starter, not a certified production release. Google Play Billing and AdMob require your own Play Console product IDs, AdMob app/ad unit IDs, app signing, privacy policy, and testing; they are not active until configured. The Premium screen is informational until billing is connected. Some streaming URLs may not be playable/downloadable due to server restrictions or unsupported formats. Download only media you own or are authorized to save. YouTube content should be played through official YouTube services and is not extracted by this app.
