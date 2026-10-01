# "Beruniy davomchilari" Ilovasi

> **Shior:** "Yoshlar 2030 — Kelajak yoshlar qo'lida"  
> **Logotip shiori:** "Bilim · Innovatsiya · Yangi avlod"

"Beruniy davomchilari" — O'zbekiston yoshlarini xalqaro va milliy sertifikatlar (IELTS, TOEFL, SAT, IT, Matematika va aniq fanlar) olishda qo'llab-quvvatlash, ularni zamonaviy fan va texnologiyalarga yo'naltiruvchi interaktiv mobil ilovadir.

---

## 🚀 ASOSIY IMKONIYATLAR

1. **Kirish va Ro'yxatdan o'tish:**
   - 14 ta O'zbekiston viloyat va shaharlari bo'yicha tanlov.
   - Maxsus foydalanuvchi nomi tekshiruvi: `/^[\p{L}\p{N}](?:\S*[\p{L}\p{N}])?$/u` (30 belgigacha).
   - Foydalanuvchilar soni sanagichi orqali tartib raqami berilishi (`counters/users`).
   - "Xush kelibsiz! 🎉 — Siz N-bo'lib ro'yxatdan o'tdingiz" tabrik bildirishnomasi va toasti.
2. **Asosiy sahifa (Dashboard):**
   - Zamonaviy gradient banner (Abu Rayhon Beruniy va yoshlar innovatsiyasi).
   - 3 ustunli qulay tugmalar:
     - 📚 **Kurslar** (12 ta fan: IELTS, TOEFL, SAT, Matematika, Ona tili, Ingliz tili, Tarix, Robototexnika, Kimyo, Biologiya, Fizika, IT).
     - 📝 **Testlar** (Diagnostik va tayyorgarlik testlari).
     - 🏆 **Musobaqalar** (Hakatonlar, olimpiadalar va tanlovlar).
     - 📊 **Statistika** (Real vaqtdagi 2x2 kartalar).
     - ℹ️ **Biz haqimizda** (Maqsad, fotolavhalar va foydali havolalar).
     - 👥 **Foydalanuvchilar** (Faqat admin uchun: barcha a'zolar ro'yxati, qidiruv).
3. **Telegram uslubidagi Chat tizimi:**
   - **Barchasi / Shaxsiy / Guruhlar** tablari.
   - Doimiy umumiy guruh: **"Beruniy X avlod"**.
   - Shaxsiy chat (DM) va Yangi guruh ochish imkoniyati.
   - Chiroyli gradient fon va doodle naqshlari.
   - O'ng tomonda o'z xabarlaringiz (och ko'k `#DCEFFF`, vaqt va `✓✓`), chap tomonda boshqalar xabarlari (oq pufakcha, rangli ism-familiya).
   - Sana ajratgichlari ("Bugun", "30-sentabr" o'zbek oylari bilan).
   - Emoji paneli va qulay xabar yuborish.
4. **Foydalanuvchi Profili:**
   - 96dp katta avatar (ism-familiya bosh harflari yoki fotosurat).
   - Tahrirlash, parolni almashtirish, xavfsiz chiqish.
   - Boshqa foydalanuvchi profilini ko'rish va to'g'ridan-to'g'ri unga "💬 Xabar yozish".
5. **Bildirishnomalar va Tarqatish (Broadcast):**
   - Bildirishnomalar ro'yxati, o'qilmaganlik nishoni (badge).
   - Admin uchun "📣 Tarqatish" bo'limi orqali barcha talabalarga yangilik yuborish.

---

## 🛠️ FIREBASE SOZLASH QO'LLANMASI

### 1. Firebase loyihasi yaratish
1. [Firebase Console](https://console.firebase.google.com/) ga kiring.
2. **"Add project"** tugmasini bosing va loyihaga `beruniy-davomchilari` nomini bering.
3. Google Analytics'ni xohishingizga ko'ra yoqing yoki o'chiring.

### 2. Authentication yoqish
1. Chap menyudan **Build > Authentication** bo'limiga o'ting.
2. **Get Started** tugmasini bosing.
3. **Sign-in method** ro'yxatidan **Email/Password** ni tanlang.
4. "Enable" qilib saqlang (Save).

### 3. Cloud Firestore yaratish
1. Chap menyudan **Build > Firestore Database** bo'limiga o'ting.
2. **Create database** tugmasini bosing.
3. Joylashuvni tanlang (masalan, `asia-southeast1` yoki `europe-west1`).
4. Xavfsizlik rejimida **"Start in test mode"** yoki **"production mode"** tanlang.

### 4. Firestore qoidalarini (Rules) joylash
1. Firestore panelida **Rules** tabiga o'ting.
2. Loyihadagi `firestore.rules` faylining to'liq tarkibini nusxalab, u yerdagi matn o'rniga qo'ying:
   - Admin emaili: `pirnazarovshoxruz567@gmail.com`
3. **Publish** tugmasini bosing.

### 5. Admin akkauntini yaratish
1. **Authentication > Users** bo'limiga o'ting.
2. **Add user** tugmasini bosing.
3. Email qismiga aynan: `pirnazarovshoxruz567@gmail.com` kiriting.
4. O'zingiz uchun maxfiy parol o'rnating.
5. Ilovaga kirganingizda ushbu hisob avtomatik ravishda **Admin** huquqlari bilan ishlaydi (material qo'shish, o'chirish, tahrirlash, foydalanuvchilarni ko'rish va xabar tarqatish).

---

## 📱 ANDROID ILOVA TUZILISHI

- **Til:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Arxitektura:** Clean MVVM / Unidirectional Data Flow
- **Dizayn:** Dark & Light Theme mosligi (Dark: `#0E121A`, Card: `#1A202B`, Accent: `#FF9A1F`, Primary: `#3D6DF2`)
- **Holat boshqaruvi:** Kotlin Coroutines & StateFlow
