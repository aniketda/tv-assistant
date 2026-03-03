# TV Assistant - APK Build Guide

## Step 1: Android Studio তে Project খোলো

1. Android Studio open করো
2. **"Open"** click করো (New Project না)
3. **TVAssistant** folder টা select করো
4. OK / Open click করো

---

## Step 2: Project Sync করো

- উপরে হলুদ bar দেখাবে **"Gradle sync"**
- **"Sync Now"** click করো
- ২-৩ মিনিট অপেক্ষা করো

---

## Step 3: APK Build করো

উপরের menu থেকে:
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

Build শেষ হলে নিচে notification আসবে:
**"APK(s) generated successfully"**

---

## Step 4: APK কোথায় পাবে?

```
TVAssistant/app/build/outputs/apk/debug/app-debug.apk
```

এই file টা USB দিয়ে TV তে নিয়ে install করো।

---

## TV তে Install করার নিয়ম

1. TV Settings → **Unknown Sources** ON করো
2. USB দিয়ে APK copy করো
3. TV File Manager থেকে APK open করো
4. Install করো

---

## Commands যা কাজ করবে

| বলো | কাজ হবে |
|-----|---------|
| "নতুন বাংলা মুভি" | YouTube এ search করবে |
| "আজকের খবর" | News search করবে |
| "বাংলা গান" | গান search করবে |
| "নাটক" | নাটক search করবে |
| "YouTube খোলো" | YouTube open করবে |
| "Settings" | TV Settings খুলবে |
| যেকোনো কথা | YouTube এ সেটাই search করবে |

---

## Remote Button Setup

Remote এর **Mic / Search button** চাপলেই নিচে bar আসবে।
কথা বললে কাজ হবে। ৩ সেকেন্ড পরে bar আবার লুকাবে।

---

## সমস্যা হলে

Android Studio তে error দেখালে screenshot নিয়ে জানাও।
