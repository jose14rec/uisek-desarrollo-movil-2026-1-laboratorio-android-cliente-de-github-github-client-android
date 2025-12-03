package ec.edu.uisek.githubclient.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(private val user: String, private val pass: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val credentials = Credentials.basic(user, pass)
        request = request.newBuilder()
            .header("Autorization", credentials)
            .build()
        return chain.proceed(request)
    }
}