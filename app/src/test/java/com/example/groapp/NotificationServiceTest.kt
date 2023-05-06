import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.groapp.Services.NotificationService
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class NotificationServiceTest {

    private lateinit var notificationService: NotificationService

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)
        notificationService = NotificationService()
    }

    @After
    fun tearDown() {
        FirebaseDatabase.getInstance().getReference("notifications").removeValue()
    }

    @Test
    fun testSaveAndGetAllNotifications() {
        val title = "Test notification title"
        val message = "Test notification message"
        val timestamp = Date()
        val userId = "-NUeURgCQxX2vHkhfi6z"

        notificationService.saveNotifications(title, message)

        notificationService.getAllNotifications { notifications ->
            val notification = notifications.firstOrNull()
            assertEquals(1, notifications.size)
            assertEquals(title, notification?.title)
            assertEquals(message, notification?.message)
            assertEquals(userId, notification?.userId)
            assertEquals(timestamp.toString(), notification?.timestamp.toString())
            assertEquals(false, notification?.isRead)
        }
    }

    @Test
    fun testMarkNotificationAsRead() {
        val title = "Test notification title"
        val message = "Test notification message"
        val userId = "-NUeURgCQxX2vHkhfi6z"

        notificationService.saveNotifications(title, message)

        notificationService.getAllNotifications { notifications ->
            val notification = notifications.firstOrNull()
            assertEquals(false, notification?.isRead)

            if (notification != null) {
                notificationService.markNotificationAsRead(notification.id!!)
                notificationService.getAllNotifications { updatedNotifications ->
                    val updatedNotification = updatedNotifications.firstOrNull()
                    assertEquals(true, updatedNotification?.isRead)
                }
            }
        }
    }

    @Test
    fun testGetUnreadNotifications() {
        val title = "Test notification title"
        val message = "Test notification message"
        val userId = "-NUeURgCQxX2vHkhfi6z"

        notificationService.saveNotifications(title, message)

        notificationService.getUnreadNotifications { notifications ->
            val notification = notifications.firstOrNull()
            assertEquals(1, notifications.size)
            assertEquals(title, notification?.title)
            assertEquals(message, notification?.message)
            assertEquals(userId, notification?.userId)
            assertEquals(false, notification?.isRead)
        }
    }
}
