package ec.edu.uisek.githubclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ec.edu.uisek.githubclient.databinding.FragmentRepoItemBinding
import ec.edu.uisek.githubclient.models.Repo

class ReposAdapter(
    private val onEdit: (Repo) -> Unit,
    private val onDelete: (Repo) -> Unit
) : RecyclerView.Adapter<ReposAdapter.RepoViewHolder>() {

    private var repositories: List<Repo> = emptyList()

    inner class RepoViewHolder(val binding: FragmentRepoItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {

            binding.repoName.text = repo.name
            binding.repoDescription.text = repo.description ?: "No existe descripción en el Repositorio"
            binding.repoLanguage.text = repo.language ?: "No existe lenguaje en el Repositorio"

            Glide.with(binding.root.context)
                .load(repo.owner.avatarUrl)
                .circleCrop()
                .into(binding.repoOwnerImage)

            // Botón EDITAR
            binding.btnEdit.setOnClickListener {
                onEdit(repo)
            }

            // Botón ELIMINAR
            binding.btnDelete.setOnClickListener {
                onDelete(repo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = FragmentRepoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    override fun getItemCount(): Int = repositories.size

    fun updaterepositories(newRepositories: List<Repo>) {
        repositories = newRepositories
        notifyDataSetChanged()
    }
}
