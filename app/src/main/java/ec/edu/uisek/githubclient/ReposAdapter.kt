package ec.edu.uisek.githubclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ec.edu.uisek.githubclient.databinding.FragmentRepoItemBinding


class RepoViewHolder(private val binding: FragmentRepoItemBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(position: Int) {
        binding.repoOwnerImage.setImageResource(R.mipmap.ic_launcher) // imagen
        binding.repoName.text = "Repositorio #${position + 1}"
        binding.repoDescription.text = "Esta es la descripción del elemento número ${position + 1} en la lista."
        binding.repoLanguage.text = if (position % 2 == 0) "Kotlin" else "Java"
    }
}


class ReposAdapter : RecyclerView.Adapter<RepoViewHolder>() {


    override fun getItemCount(): Int = 3


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {

        val binding = FragmentRepoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(position)
    }
}