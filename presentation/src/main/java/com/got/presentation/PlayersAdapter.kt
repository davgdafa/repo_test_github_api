package com.got.presentation

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.got.domain.models.GotPlayer

class PlayersAdapter(private val players: List<GotPlayer>): RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder =
        PlayersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_card_item_row, parent, false))

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        val currentPlayer = players[position]
        holder.apply {
            Glide.with(holder.itemView)
                .load(currentPlayer.imageUrl)
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
            tvPlayerName.text = currentPlayer.firstName
            tvPlayerLeague.text = currentPlayer.lastName
            tvPlayerClub.text = currentPlayer.fullName
            tvPlayerRating.text = currentPlayer.title
            tvPlayerAge.text = currentPlayer.family
        }
    }

    override fun getItemCount(): Int = players.size

    class PlayersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUrl: ImageView = itemView.findViewById(R.id.iv_player_profile)
        val tvPlayerName: TextView = itemView.findViewById(R.id.tv_player_name)
        val tvPlayerLeague: TextView = itemView.findViewById(R.id.tv_player_league)
        val tvPlayerClub: TextView = itemView.findViewById(R.id.tv_player_club)
        val tvPlayerRating: TextView = itemView.findViewById(R.id.tv_player_rating)
        val tvPlayerAge: TextView = itemView.findViewById(R.id.tv_player_age)
        val pbLoadingPlaceholder: ProgressBar = itemView.findViewById(R.id.pb_loading_placeholder)
    }
}