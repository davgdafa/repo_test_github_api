package com.got.data.remote

import com.got.domain.models.GotPlayer

object GotPlayersResponseMapper {
    fun map(gotPlayersList: List<PlayerData>?): List<GotPlayer> {
        val listOfGotPlayers = ArrayList<GotPlayer>()
        gotPlayersList?.let { listPlayers ->
            for (playerData in listPlayers) {
                playerData.let player@{ (id, first, last, full, title, family, imageUrl) ->
                    listOfGotPlayers.add(
                        GotPlayer(
                            id = id ?: return@player,
                            firstName = first ?: return@player,
                            lastName = last ?: return@player,
                            fullName = full ?: return@player,
                            title = title ?: return@player,
                            family = family ?: return@player,
                            imageUrl = imageUrl ?: return@player
                        )
                    )
                }
            }
        }
        return listOfGotPlayers
    }
}
