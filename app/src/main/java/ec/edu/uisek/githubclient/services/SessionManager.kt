package ec.edu.uisek.githubclient.services
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ec.edu.uisek.githubclient.models.UserCredentials

class SessionManager (context: Context){
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_session_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object{
        private const val USER_NAME = "user_name"
        private const val USER_PASS = "user_pass"
    }

    fun saveCredentials(user: String, pass: String) {
        sharedPreferences.edit()
            .putString(USER_NAME, user)
            .putString(USER_PASS, pass)
            .apply()
    }

    fun getCredentials(): UserCredentials? {
        val user = sharedPreferences.getString(USER_NAME, null)
        val pass = sharedPreferences.getString(USER_PASS, null)

        return if (user != null && pass != null) {
            UserCredentials(user, pass)
        } else {
            null
        }
    }

    fun clearCredentials() {
        sharedPreferences.edit()
            .remove(USER_NAME)
            .remove(USER_PASS)
            .apply()
    }
}