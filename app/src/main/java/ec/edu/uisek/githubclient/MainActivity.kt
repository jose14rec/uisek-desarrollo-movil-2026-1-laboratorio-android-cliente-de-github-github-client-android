package ec.edu.uisek.githubclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityMainBinding
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var reposAdapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newRepoFab.setOnClickListener {
            displayNewRepoForm()
        }

        setUpRecycleView()
        fetchRepositories()
    }

    override fun onResume() {
        super.onResume()
        fetchRepositories()
    }

    // -------------------- RecyclerView --------------------
    private fun setUpRecycleView() {
        reposAdapter = ReposAdapter(
            onEdit = { repo -> editRepo(repo) },
            onDelete = { repo -> confirmDelete(repo) }
        )

        binding.reposRecyclerView.adapter = reposAdapter
    }

    // -------------------- API: Obtener Repos --------------------
    private fun fetchRepositories() {
        val apiService = RetrofitClient.getApiService()
        val call = apiService.getRepos()

        call.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                if (response.isSuccessful) {
                    val repos = response.body()
                    if (repos != null) {
                        reposAdapter.updaterepositories(repos)
                    }
                } else {
                    val errMsg = when (response.code()) {
                        401 -> "Error de autenticación"
                        403 -> "Error de autorización"
                        404 -> "Recurso no encontrado"
                        else -> "Error desconocido ${response.code()}: ${response.message()}"
                    }
                    Log.e("MainActivity", errMsg)
                    showMessage(errMsg)
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                val errMsg = "Error de conexión: ${t.message}"
                Log.e("MainActivity", errMsg, t)
                showMessage(errMsg)
            }
        })
    }

    // -------------------- MOSTRAR TOAST --------------------
    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    // -------------------- CREAR NUEVO REPO --------------------
    private fun displayNewRepoForm() {
        Intent(this, RepoForm::class.java).apply {
            startActivity(this)
        }
    }

    // -------------------- EDITAR REPO --------------------
    private fun editRepo(repo: Repo) {
        val intent = Intent(this, RepoForm::class.java)
        intent.putExtra("repo_id", repo.id)
        intent.putExtra("repo_name", repo.name)
        intent.putExtra("repo_desc", repo.description)
        startActivity(intent)
    }

    // -------------------- CONFIRMAR ELIMINACIÓN --------------------
    private fun confirmDelete(repo: Repo) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar repositorio")
            .setMessage("¿Seguro que quieres eliminar '${repo.name}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteRepo(repo)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // -------------------- API: ELIMINAR REPO --------------------
    private fun deleteRepo(repo: Repo) {
        val apiService = RetrofitClient.getApiService()
        val call = apiService.deleteRepo(
            repo.owner.login,
            repo.name
        )

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showMessage("Repositorio eliminado")
                    fetchRepositories()
                } else {
                    showMessage("Error al eliminar: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showMessage("Error de conexión: ${t.message}")
            }
        })
    }
}