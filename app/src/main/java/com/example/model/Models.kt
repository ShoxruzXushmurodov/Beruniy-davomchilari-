package com.example.model

data class User(
    val uid: String,
    val name: String,
    val surname: String,
    val username: String,
    val ukey: String, // lowercase username
    val region: String,
    val age: Int,
    val phone: String,
    val email: String,
    val photoUrl: String? = null,
    val role: String = "user", // "admin" or "user"
    val createdAt: Long = System.currentTimeMillis(),
    val order: Int = 1
) {
    val fullName: String
        get() = "$name $surname".trim()

    val initials: String
        get() {
            val n = name.trim()
            val s = surname.trim()
            return when {
                n.isNotEmpty() && s.isNotEmpty() -> "${n.first().uppercase()}${s.first().uppercase()}"
                n.length >= 2 -> n.take(2).uppercase()
                n.isNotEmpty() -> n.first().uppercase()
                else -> "BD"
            }
        }

    val isAdmin: Boolean
        get() = role == "admin" || email.equals("pirnazarovshoxruz567@gmail.com", ignoreCase = true)
}

data class Course(
    val id: String,
    val name: String,
    val info: String,
    val link: String? = null,
    val subject: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class TestItem(
    val id: String,
    val name: String,
    val info: String,
    val link: String? = null,
    val subject: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class Contest(
    val id: String,
    val name: String,
    val date: String,
    val info: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class NotificationItem(
    val id: String,
    val title: String,
    val text: String,
    val ts: Long = System.currentTimeMillis(),
    val from: String = "Admin",
    val isRead: Boolean = false
)

data class ChatGroup(
    val id: String,
    val name: String,
    val members: List<String>, // list of uids
    val ownerUid: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isGeneral: Boolean = false,
    val lastMessage: String = "",
    val lastMessageTs: Long = 0L
)

data class ChatMessage(
    val id: String,
    val chatId: String,
    val fromUid: String,
    val senderName: String,
    val senderUsername: String,
    val text: String,
    val ts: Long = System.currentTimeMillis()
)

data class AboutInfo(
    val text: String,
    val links: List<AboutLink>,
    val images: List<String>,
    val updatedAt: Long = System.currentTimeMillis()
)

data class AboutLink(
    val id: String,
    val title: String,
    val url: String
)

val UZBEKISTAN_REGIONS = listOf(
    "Toshkent shahri",
    "Toshkent viloyati",
    "Andijon",
    "Buxoro",
    "Farg'ona",
    "Jizzax",
    "Xorazm",
    "Namangan",
    "Navoiy",
    "Qashqadaryo",
    "Qoraqalpog'iston Respublikasi",
    "Samarqand",
    "Sirdaryo",
    "Surxondaryo"
)

val SUBJECT_LIST = listOf(
    "IELTS" to "🎯",
    "TOEFL" to "🌐",
    "SAT" to "📝",
    "Matematika" to "➗",
    "Ona tili" to "📖",
    "Ingliz tili" to "🇬🇧",
    "Tarix" to "🏛️",
    "Robototexnika" to "🤖",
    "Kimyo" to "🧪",
    "Biologiya" to "🧬",
    "Fizika" to "⚛️",
    "IT" to "💻"
)
