package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object BeruniyRepository {
    private const val ADMIN_EMAIL = "pirnazarovshoxruz567@gmail.com"

    // In-memory persistent state (persists across app navigation)
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _tests = MutableStateFlow<List<TestItem>>(emptyList())
    val tests: StateFlow<List<TestItem>> = _tests.asStateFlow()

    private val _contests = MutableStateFlow<List<Contest>>(emptyList())
    val contests: StateFlow<List<Contest>> = _contests.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _groups = MutableStateFlow<List<ChatGroup>>(emptyList())
    val groups: StateFlow<List<ChatGroup>> = _groups.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _aboutInfo = MutableStateFlow(
        AboutInfo(
            text = "\"Beruniy davomchilari\" — O'zbekiston yoshlarini zamonaviy ilm-fan, xalqaro ta'lim va innovatsion texnologiyalar sari yo'naltiruvchi milliy platformadir. Bizning maqsadimiz 2030-yilga qadar iqtidorli yoshlarga IELTS, SAT, IT va aniq fanlar bo'yicha eng ilg'or bilim va resurslarni taqdim etishdir.",
            links = listOf(
                AboutLink(UUID.randomUUID().toString(), "Rasmiy Telegram kanali", "https://t.me/beruniy_yoshlar"),
                AboutLink(UUID.randomUUID().toString(), "Yoshlar Agentligi portali", "https://yoshlar.gov.uz"),
                AboutLink(UUID.randomUUID().toString(), "Xalqaro sertifikatlar tizimi", "https://edu.uz")
            ),
            images = listOf(
                "beruniy_astronomy_science",
                "beruniy_youth_innovation",
                "beruniy_future_tech"
            )
        )
    )
    val aboutInfo: StateFlow<AboutInfo> = _aboutInfo.asStateFlow()

    private val _toastEvent = MutableStateFlow<String?>(null)
    val toastEvent: StateFlow<String?> = _toastEvent.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        val adminUser = User(
            uid = "admin_uid_001",
            name = "Shoxruz",
            surname = "Pirnazarov",
            username = "pirnazarov",
            ukey = "pirnazarov",
            region = "Toshkent shahri",
            age = 24,
            phone = "+(998) 90 123 45 67",
            email = ADMIN_EMAIL,
            photoUrl = null,
            role = "admin",
            createdAt = System.currentTimeMillis() - 86400000L * 30,
            order = 1
        )

        val user2 = User(
            uid = "user_uid_002",
            name = "Madina",
            surname = "Karimova",
            username = "madina_k",
            ukey = "madina_k",
            region = "Samarqand",
            age = 19,
            phone = "+(998) 91 234 56 78",
            email = "madina@example.com",
            photoUrl = null,
            role = "user",
            createdAt = System.currentTimeMillis() - 86400000L * 15,
            order = 2
        )

        val user3 = User(
            uid = "user_uid_003",
            name = "Jasur",
            surname = "Aliyev",
            username = "jasur_it",
            ukey = "jasur_it",
            region = "Farg'ona",
            age = 21,
            phone = "+(998) 93 345 67 89",
            email = "jasur@example.com",
            photoUrl = null,
            role = "user",
            createdAt = System.currentTimeMillis() - 86400000L * 5,
            order = 3
        )

        _users.value = listOf(adminUser, user2, user3)

        // General Group
        val generalGroup = ChatGroup(
            id = "group_general_beruniy",
            name = "Beruniy X avlod",
            members = listOf(adminUser.uid, user2.uid, user3.uid),
            ownerUid = adminUser.uid,
            isGeneral = true,
            lastMessage = "Assalomu alaykum, bilimga intiluvchi aziz yoshlar!",
            lastMessageTs = System.currentTimeMillis() - 3600000L * 2
        )

        // Study Group
        val studyGroup = ChatGroup(
            id = "group_ielts_masters",
            name = "IELTS 8.0+ Klub",
            members = listOf(adminUser.uid, user2.uid),
            ownerUid = adminUser.uid,
            isGeneral = false,
            lastMessage = "Bugungi writing mavzusi bo'yicha fikrlar bormi?",
            lastMessageTs = System.currentTimeMillis() - 1800000L
        )

        _groups.value = listOf(generalGroup, studyGroup)

        // Seed Messages
        _messages.value = listOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                chatId = generalGroup.id,
                fromUid = adminUser.uid,
                senderName = adminUser.fullName,
                senderUsername = adminUser.username,
                text = "Assalomu alaykum! 'Beruniy davomchilari' platformasiga xush kelibsiz! Kelajak yoshlar qo'lida!",
                ts = System.currentTimeMillis() - 3600000L * 3
            ),
            ChatMessage(
                id = UUID.randomUUID().toString(),
                chatId = generalGroup.id,
                fromUid = user2.uid,
                senderName = user2.fullName,
                senderUsername = user2.username,
                text = "Assalomu alaykum! Platforma juda qulay chiqibdi, IELTS materiallari bormi?",
                ts = System.currentTimeMillis() - 3600000L * 2
            ),
            ChatMessage(
                id = UUID.randomUUID().toString(),
                chatId = generalGroup.id,
                fromUid = user3.uid,
                senderName = user3.fullName,
                senderUsername = user3.username,
                text = "Ha, Kurslar bo'limida barcha fanlar joylashtirilgan. Ayniqsa IT va Matematika zo'r!",
                ts = System.currentTimeMillis() - 3600000L
            )
        )

        // Seed Courses
        _courses.value = listOf(
            Course(
                id = UUID.randomUUID().toString(),
                name = "IELTS Band 7.5+ To'liq Intensiv Kursi",
                info = "Listening, Reading, Writing va Speaking bo'limlari bo'yicha to'liq amaliy qo'llanma va video darsliklar.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "IELTS"
            ),
            Course(
                id = UUID.randomUUID().toString(),
                name = "TOEFL iBT Tayyorgarlik kursi",
                info = "Universitetlarga topshiruvchilar uchun TOEFL test strategiyalari va eshitish mashqlari.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "TOEFL"
            ),
            Course(
                id = UUID.randomUUID().toString(),
                name = "SAT Math & Verbal 1500+ Dasturi",
                info = "AQSh va Yevropa yetakchi oliygohlari granti uchun SAT imtihoniga professional tayyorgarlik.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "SAT"
            ),
            Course(
                id = UUID.randomUUID().toString(),
                name = "Oliy Matematika va Mantiq asoslari",
                info = "Abu Rayhon Beruniy uslubida chuqurlashtirilgan algebra, geometriya va trigonometriya.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "Matematika"
            ),
            Course(
                id = UUID.randomUUID().toString(),
                name = "Full-Stack Dasturlash va Sun'iy Intellekt",
                info = "Python, Kotlin, Web dasturlash va AI texnologiyalari bo'yicha amaliy loyihalar.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "IT"
            ),
            Course(
                id = UUID.randomUUID().toString(),
                name = "Robototexnika va IoT tizimlari",
                info = "Mikrokontrollerlar, sensorlar va avtomatlashtirilgan robotlar yasash kursi.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "Robototexnika"
            ),
            Course(
                id = UUID.randomUUID().toString(),
                name = "Zamonaviy Fizika va Astronomiya",
                info = "Astrofizika asoslari va kvant mexanikasiga qiziqarli kirish.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "Fizika"
            )
        )

        // Seed Tests
        _tests.value = listOf(
            TestItem(
                id = UUID.randomUUID().toString(),
                name = "IELTS Mock Test #1 (General & Academic)",
                info = "Haqiqiy imtihon formatidagi 40 ta savoldan iborat to'liq sinov testi.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "IELTS"
            ),
            TestItem(
                id = UUID.randomUUID().toString(),
                name = "Matematika Diagnostik Testi (Milliy Sertifikat)",
                info = "Milliy sertifikat talablari darajasidagi 35 ta masalali test sinovi.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "Matematika"
            ),
            TestItem(
                id = UUID.randomUUID().toString(),
                name = "IT & Algoritmlash Kirish Testi",
                info = "Dasturlash asoslari va algoritmik fikrlash darajasini aniqlovchi test.",
                link = "https://t.me/beruniy_yoshlar",
                subject = "IT"
            )
        )

        // Seed Contests
        _contests.value = listOf(
            Contest(
                id = UUID.randomUUID().toString(),
                name = "Respublika Yosh Dasturchilar Hakatonı 2030",
                date = "2026-10-25",
                info = "Iqtidorli yoshlar o'rtasida sun'iy intellekt va ta'lim texnologiyalari bo'yicha 48 soatlik yirik xakaton. Sovrin jamg'armasi: 100 000 000 so'm."
            ),
            Contest(
                id = UUID.randomUUID().toString(),
                name = "Beruniy Nomidagi Xalqaro Matematika Turniri",
                date = "2026-11-10",
                info = "Maktab va litsey o'quvchilari o'rtasida xalqaro olimpiada darajasidagi mantiqiy va analitik masalalar bellashuvi."
            ),
            Contest(
                id = UUID.randomUUID().toString(),
                name = "Yoshlar Innovatsiya va Startap Tanlovi",
                date = "2026-12-05",
                info = "O'zbekiston yoshlarining ilmiy va texnologik ixtirolarini qo'llab-quvvatlash bo'yicha grantlar tanlovi."
            )
        )

        // Seed Notifications
        _notifications.value = listOf(
            NotificationItem(
                id = UUID.randomUUID().toString(),
                title = "Xush kelibsiz! 🎉 — Siz 1-bo'lib ro'yxatdan o'tdingiz",
                text = "'Beruniy davomchilari' portalida sizni ko'rganimizdan mamnunmiz! Ilm olishda ulkan zafarlar tilaymiz!",
                ts = System.currentTimeMillis() - 86400000L * 2,
                isRead = false
            ),
            NotificationItem(
                id = UUID.randomUUID().toString(),
                title = "Yangi Musobaqa e'lon qilindi 🏆",
                text = "Respublika Yosh Dasturchilar Hakatoniga ro'yxatdan o'tish boshlandi. Musobaqalar bo'limida tanishing.",
                ts = System.currentTimeMillis() - 3600000L * 5,
                isRead = false
            )
        )

        // Start logged in as Admin for instant convenience
        _currentUser.value = adminUser
    }

    fun showToast(msg: String) {
        _toastEvent.value = msg
    }

    fun clearToast() {
        _toastEvent.value = null
    }

    // AUTH METHODS
    fun login(emailInput: String, passwordInput: String): Result<User> {
        val cleanEmail = emailInput.trim().lowercase()
        if (cleanEmail.isEmpty() || passwordInput.isEmpty()) {
            return Result.failure(Exception("Email yoki parol noto'g'ri. Ro'yxatdan o'tmagan bo'lsangiz, \"Ro'yxatdan o'tish\" tugmasini bosing."))
        }

        // Check if admin email or existing user
        val existing = _users.value.find { it.email.equals(cleanEmail, ignoreCase = true) }
        if (existing != null) {
            _currentUser.value = existing
            return Result.success(existing)
        }

        if (cleanEmail == ADMIN_EMAIL) {
            val admin = User(
                uid = "admin_uid_001",
                name = "Admin",
                surname = "Beruniy",
                username = "admin",
                ukey = "admin",
                region = "Toshkent shahri",
                age = 25,
                phone = "+(998) 90 123 45 67",
                email = ADMIN_EMAIL,
                role = "admin",
                order = 1
            )
            _users.value = listOf(admin) + _users.value.filter { it.uid != admin.uid }
            _currentUser.value = admin
            return Result.success(admin)
        }

        return Result.failure(Exception("Email yoki parol noto'g'ri. Ro'yxatdan o'tmagan bo'lsangiz, \"Ro'yxatdan o'tish\" tugmasini bosing."))
    }

    fun register(
        name: String,
        surname: String,
        username: String,
        region: String,
        age: Int?,
        phone: String,
        email: String,
        pass1: String,
        pass2: String
    ): Result<User> {
        val cleanName = name.trim()
        val cleanSurname = surname.trim()
        val cleanUsername = username.trim()
        val cleanEmail = email.trim().lowercase()
        val cleanPhone = phone.trim()

        if (cleanName.isEmpty() || cleanSurname.isEmpty() || cleanUsername.isEmpty() ||
            region.isEmpty() || age == null || cleanPhone.isEmpty() || cleanEmail.isEmpty() ||
            pass1.isEmpty() || pass2.isEmpty()
        ) {
            return Result.failure(Exception("Barcha maydonlarni to'g'ri to'ldiring."))
        }

        if (pass1.length < 6) {
            return Result.failure(Exception("Parol kamida 6 ta belgidan iborat bo'lishi kerak."))
        }

        if (pass1 != pass2) {
            return Result.failure(Exception("Parollar bir xil emas."))
        }

        if (age !in 5..99) {
            return Result.failure(Exception("Yoshi 5 va 99 oralig'ida bo'lishi kerak."))
        }

        // Username validation:
        // /^[\p{L}\p{N}](?:\S*[\p{L}\p{N}])?$/u, max 30 chars
        val usernameRegex = Regex("^[\\p{L}\\p{N}](\\S*[\\p{L}\\p{N}])?\$")
        if (cleanUsername.length > 30 || !usernameRegex.matches(cleanUsername)) {
            return Result.failure(Exception("Yaroqsiz foydalanuvchi nomi."))
        }

        val ukey = cleanUsername.lowercase()
        if (_users.value.any { it.ukey == ukey }) {
            return Result.failure(Exception("Bu havola band."))
        }

        if (cleanEmail == ADMIN_EMAIL) {
            return Result.failure(Exception("Bu email band."))
        }

        if (_users.value.any { it.email.equals(cleanEmail, ignoreCase = true) }) {
            return Result.failure(Exception("Bu email allaqachon ro'yxatdan o'tgan."))
        }

        val nextOrder = (_users.value.maxOfOrNull { it.order } ?: 0) + 1
        val newUser = User(
            uid = UUID.randomUUID().toString(),
            name = cleanName,
            surname = cleanSurname,
            username = cleanUsername,
            ukey = ukey,
            region = region,
            age = age,
            phone = cleanPhone,
            email = cleanEmail,
            role = "user",
            createdAt = System.currentTimeMillis(),
            order = nextOrder
        )

        _users.value = _users.value + newUser
        _currentUser.value = newUser

        // Add to general group
        _groups.value = _groups.value.map { group ->
            if (group.isGeneral) {
                group.copy(members = group.members + newUser.uid)
            } else group
        }

        // Add personalized welcome notification
        val welcomeNotif = NotificationItem(
            id = UUID.randomUUID().toString(),
            title = "Xush kelibsiz! 🎉 — Siz $nextOrder-bo'lib ro'yxatdan o'tdingiz.",
            text = "Siz O'zbekiston yoshlari safidasiz! Bilim, innovatsiya va yangi marralar sari qadamingiz qutlug' bo'lsin.",
            ts = System.currentTimeMillis(),
            isRead = false
        )
        _notifications.value = listOf(welcomeNotif) + _notifications.value

        showToast("Xush kelibsiz! 🎉 — Siz $nextOrder-bo'lib ro'yxatdan o'tdingiz")
        return Result.success(newUser)
    }

    fun logout() {
        _currentUser.value = null
    }

    fun switchUser(user: User) {
        _currentUser.value = user
    }

    fun updateProfile(
        name: String,
        surname: String,
        username: String,
        region: String,
        age: Int,
        phone: String,
        photoUrl: String?
    ): Result<User> {
        val user = _currentUser.value ?: return Result.failure(Exception("Tizimga kirilmagan"))
        val cleanUsername = username.trim()
        val usernameRegex = Regex("^[\\p{L}\\p{N}](\\S*[\\p{L}\\p{N}])?\$")
        if (cleanUsername.length > 30 || !usernameRegex.matches(cleanUsername)) {
            return Result.failure(Exception("Yaroqsiz foydalanuvchi nomi."))
        }

        val ukey = cleanUsername.lowercase()
        if (_users.value.any { it.ukey == ukey && it.uid != user.uid }) {
            return Result.failure(Exception("Bu havola band."))
        }

        val updated = user.copy(
            name = name.trim(),
            surname = surname.trim(),
            username = cleanUsername,
            ukey = ukey,
            region = region,
            age = age,
            phone = phone.trim(),
            photoUrl = photoUrl ?: user.photoUrl
        )

        _currentUser.value = updated
        _users.value = _users.value.map { if (it.uid == user.uid) updated else it }
        return Result.success(updated)
    }

    // COURSES & TESTS
    fun addCourse(name: String, info: String, link: String?, subject: String) {
        val course = Course(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            info = info.trim(),
            link = link?.trim()?.ifEmpty { null },
            subject = subject
        )
        _courses.value = listOf(course) + _courses.value
    }

    fun updateCourse(id: String, name: String, info: String, link: String?, subject: String) {
        _courses.value = _courses.value.map {
            if (it.id == id) it.copy(name = name.trim(), info = info.trim(), link = link?.trim()?.ifEmpty { null }, subject = subject)
            else it
        }
    }

    fun deleteCourse(id: String) {
        _courses.value = _courses.value.filter { it.id != id }
    }

    fun addTest(name: String, info: String, link: String?, subject: String) {
        val test = TestItem(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            info = info.trim(),
            link = link?.trim()?.ifEmpty { null },
            subject = subject
        )
        _tests.value = listOf(test) + _tests.value
    }

    fun updateTest(id: String, name: String, info: String, link: String?, subject: String) {
        _tests.value = _tests.value.map {
            if (it.id == id) it.copy(name = name.trim(), info = info.trim(), link = link?.trim()?.ifEmpty { null }, subject = subject)
            else it
        }
    }

    fun deleteTest(id: String) {
        _tests.value = _tests.value.filter { it.id != id }
    }

    // CONTESTS
    fun addContest(name: String, date: String, info: String) {
        val contest = Contest(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            date = date.trim(),
            info = info.trim()
        )
        _contests.value = listOf(contest) + _contests.value
    }

    fun updateContest(id: String, name: String, date: String, info: String) {
        _contests.value = _contests.value.map {
            if (it.id == id) it.copy(name = name.trim(), date = date.trim(), info = info.trim())
            else it
        }
    }

    fun deleteContest(id: String) {
        _contests.value = _contests.value.filter { it.id != id }
    }

    // NOTIFICATIONS & BROADCAST
    fun broadcastNotification(title: String, text: String): Result<Unit> {
        val current = _currentUser.value
        if (current?.isAdmin != true) {
            return Result.failure(Exception("Faqat admin xabar tarqata oladi."))
        }
        val cleanTitle = title.trim()
        val cleanText = text.trim()
        if (cleanTitle.isEmpty() || cleanText.isEmpty()) {
            return Result.failure(Exception("Sarlavha va matnni kiriting."))
        }

        val item = NotificationItem(
            id = UUID.randomUUID().toString(),
            title = cleanTitle,
            text = cleanText,
            ts = System.currentTimeMillis(),
            from = "Admin",
            isRead = false
        )
        _notifications.value = listOf(item) + _notifications.value
        showToast("Xabar tarqatildi.")
        return Result.success(Unit)
    }

    fun deleteNotification(id: String) {
        _notifications.value = _notifications.value.filter { it.id != id }
    }

    fun markNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    // ABOUT SITE
    fun updateAboutText(text: String) {
        _aboutInfo.value = _aboutInfo.value.copy(text = text.trim(), updatedAt = System.currentTimeMillis())
    }

    fun addAboutLink(title: String, url: String) {
        val link = AboutLink(UUID.randomUUID().toString(), title.trim(), url.trim())
        _aboutInfo.value = _aboutInfo.value.copy(links = _aboutInfo.value.links + link)
    }

    fun deleteAboutLink(id: String) {
        _aboutInfo.value = _aboutInfo.value.copy(links = _aboutInfo.value.links.filter { it.id != id })
    }

    fun addAboutImage(imageTag: String) {
        _aboutInfo.value = _aboutInfo.value.copy(images = _aboutInfo.value.images + imageTag)
    }

    fun deleteAboutImage(imageTag: String) {
        _aboutInfo.value = _aboutInfo.value.copy(images = _aboutInfo.value.images.filter { it != imageTag })
    }

    // CHAT
    fun sendMessage(chatId: String, text: String): Result<ChatMessage> {
        val user = _currentUser.value ?: return Result.failure(Exception("Tizimga kiring."))
        val cleanText = text.trim()
        if (cleanText.isEmpty()) {
            return Result.failure(Exception("Bo'sh xabar yuborib bo'lmaydi."))
        }

        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            fromUid = user.uid,
            senderName = user.fullName,
            senderUsername = user.username,
            text = cleanText,
            ts = System.currentTimeMillis()
        )

        _messages.value = _messages.value + msg

        // Update group last message
        _groups.value = _groups.value.map {
            if (it.id == chatId) it.copy(lastMessage = cleanText, lastMessageTs = msg.ts)
            else it
        }

        return Result.success(msg)
    }

    fun deleteMessage(messageId: String) {
        _messages.value = _messages.value.filter { it.id != messageId }
    }

    fun createGroup(name: String, memberUids: List<String>): ChatGroup {
        val user = _currentUser.value ?: throw Exception("Tizimga kiring")
        val allMembers = (memberUids + user.uid).distinct()
        val newGroup = ChatGroup(
            id = "group_" + UUID.randomUUID().toString().take(8),
            name = name.trim(),
            members = allMembers,
            ownerUid = user.uid,
            isGeneral = false,
            lastMessage = "Guruh yaratildi",
            lastMessageTs = System.currentTimeMillis()
        )
        _groups.value = listOf(newGroup) + _groups.value
        return newGroup
    }

    fun getOrCreateDirectChat(otherUser: User): ChatGroup {
        val me = _currentUser.value ?: throw Exception("Tizimga kiring")
        val pairId = if (me.uid < otherUser.uid) "dm_${me.uid}_${otherUser.uid}" else "dm_${otherUser.uid}_${me.uid}"
        val existing = _groups.value.find { it.id == pairId }
        if (existing != null) return existing

        val dmGroup = ChatGroup(
            id = pairId,
            name = otherUser.fullName,
            members = listOf(me.uid, otherUser.uid),
            ownerUid = me.uid,
            isGeneral = false,
            lastMessage = "@${otherUser.username}",
            lastMessageTs = System.currentTimeMillis()
        )
        _groups.value = listOf(dmGroup) + _groups.value
        return dmGroup
    }
}
