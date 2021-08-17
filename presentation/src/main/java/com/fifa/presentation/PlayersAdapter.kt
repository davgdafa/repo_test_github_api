package com.fifa.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fifa.domain.models.FifaPlayer

class PlayersAdapter(private val players: List<FifaPlayer>): RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder =
        PlayersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_card_item_row, parent, false))

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        val currentPlayer = players[position]
        holder.apply {
            Glide.with(holder.itemView).load(currentPlayer.imageUrl).into(holder.imageUrl)
            tvPlayerName.text = currentPlayer.name
            tvPlayerLeague.text = currentPlayer.leagueName
            tvPlayerClub.text = currentPlayer.clubName
            tvPlayerRating.text = currentPlayer.rating.toString()
            tvPlayerAge.text = currentPlayer.age.toString()
            tvPlayerBirthdate.text = currentPlayer.birthdate
            tvPlayerPosition.text = currentPlayer.position
            tvPlayerWeight.text = currentPlayer.weight.toString()
            tvPlayerHeight.text = currentPlayer.height.toString()
            tvPlayerNationality.text = currentPlayer.nationality
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
        val tvPlayerBirthdate: TextView = itemView.findViewById(R.id.tv_player_birthdate)
        val tvPlayerPosition: TextView = itemView.findViewById(R.id.tv_player_position)
        val tvPlayerWeight: TextView = itemView.findViewById(R.id.tv_player_weight)
        val tvPlayerHeight: TextView = itemView.findViewById(R.id.tv_player_height)
        val tvPlayerNationality: TextView = itemView.findViewById(R.id.tv_player_nationality)
    }
}