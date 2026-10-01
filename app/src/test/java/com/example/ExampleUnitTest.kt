package com.example

import com.example.data.BeruniyRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testUsernameRegexValidation() {
        val usernameRegex = Regex("^[\\p{L}\\p{N}](\\S*[\\p{L}\\p{N}])?\$")

        // Valid usernames
        assertTrue(usernameRegex.matches("beruniy"))
        assertTrue(usernameRegex.matches("al_biruni2030"))
        assertTrue(usernameRegex.matches("shoxruz123"))
        assertTrue(usernameRegex.matches("yosh-olim"))

        // Invalid usernames
        assertFalse(usernameRegex.matches(" invalid space "))
        assertFalse(usernameRegex.matches("_start_with_symbol"))
        assertFalse(usernameRegex.matches("ends_with_symbol_"))
    }

    @Test
    fun testLoginAdminSuccess() {
        val res = BeruniyRepository.login("pirnazarovshoxruz567@gmail.com", "any_password")
        assertTrue(res.isSuccess)
        val admin = res.getOrNull()
        assertNotNull(admin)
        assertTrue(admin!!.isAdmin)
        assertEquals("admin", admin.role)
    }

    @Test
    fun testRegisterDuplicateUsernameFails() {
        val res = BeruniyRepository.register(
            name = "Test",
            surname = "Foydalanuvchi",
            username = "pirnazarov", // Already taken by admin
            region = "Toshkent shahri",
            age = 20,
            phone = "+(998) 90 000 00 00",
            email = "new_unique_email@example.com",
            pass1 = "password123",
            pass2 = "password123"
        )
        assertTrue(res.isFailure)
        assertEquals("Bu havola band.", res.exceptionOrNull()?.message)
    }

    @Test
    fun testRegisterAdminEmailBlockedForUsers() {
        val res = BeruniyRepository.register(
            name = "Test",
            surname = "User",
            username = "test_user_unique",
            region = "Samarqand",
            age = 22,
            phone = "+(998) 90 111 22 33",
            email = "pirnazarovshoxruz567@gmail.com", // admin email is blocked on registration
            pass1 = "password123",
            pass2 = "password123"
        )
        assertTrue(res.isFailure)
        assertEquals("Bu email band.", res.exceptionOrNull()?.message)
    }

    @Test
    fun testCoursesAndContests() {
        val initialCourses = BeruniyRepository.courses.value
        assertTrue(initialCourses.isNotEmpty())

        val initialContests = BeruniyRepository.contests.value
        assertTrue(initialContests.isNotEmpty())
    }
}
