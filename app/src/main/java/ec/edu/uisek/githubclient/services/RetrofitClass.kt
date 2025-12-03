package ec.edu.uisek.githubclient.services

import ec.edu.uisek.githubclient.interceptors.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL base de la API de GitHub
    private const val BASE_URL = "https://api.github.com/"
    private var apiService: GitHubApiService? = null

    fun createAuthenticatedClient(user: String, pass: String): GitHubApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(user, pass))
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(GitHubApiService::class.java)
        return apiService!!
    }

    fun getApiService(): GitHubApiService {
        return apiService ?: throw IllegalStateException ("El Api Service no pudo ser inicializado")
    }
}