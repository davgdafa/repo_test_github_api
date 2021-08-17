package com.got.presentation.characters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.got.domain.models.GotCharacter
import com.got.presentation.R

class CharactersAdapter(private val characters: List<GotCharacter>, private val onGotCharacterClickAction: (Int) -> Unit): RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder =
        CharactersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_character_card, parent, false))

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val currentCharacter = characters[position]
        holder.apply {
            Glide.with(itemView)
                .load(currentCharacter.imageUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbLoadingPlaceholder.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbLoadingPlaceholder.visibility = View.GONE
                        return false
                    }
                })
                .into(holder.imageUrl)
            tvCharacterName.text = currentCharacter.firstName
            tvCharacterLeague.text = currentCharacter.lastName
            tvCharacterClub.text = currentCharacter.fullName
            tvCharacterRating.text = currentCharacter.title
            tvCharacterAge.text = currentCharacter.family
            ivFavorite.visibility = if (currentCharacter.isFavorite == true) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onGotCharacterClickAction(currentCharacter.id) }
        }
    }

    override fun getItemCount(): Int = characters.size

    class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUrl: ImageView = itemView.findViewById(R.id.iv_character_profile)
        val tvCharacterName: TextView = itemView.findViewById(R.id.tv_character_name)
        val tvCharacterLeague: TextView = itemView.findViewById(R.id.tv_character_league)
        val tvCharacterClub: TextView = itemView.findViewById(R.id.tv_character_club)
        val tvCharacterRating: TextView = itemView.findViewById(R.id.tv_character_rating)
        val tvCharacterAge: TextView = itemView.findViewById(R.id.tv_character_age)
        val pbLoadingPlaceholder: ProgressBar = itemView.findViewById(R.id.pb_loading_placeholder)
        val ivFavorite: ImageView = itemView.findViewById(R.id.iv_favorite)
    }
}