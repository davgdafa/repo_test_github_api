package com.fifa.data.remote

import com.fifa.domain.models.FifaPlayer

object FifaPlayersResponseMapper {
    fun map(responseBody: FifaPlayersResponseBody?): List<FifaPlayer> {
        val listOfFifaPlayers = ArrayList<FifaPlayer>()
        responseBody?.let { fifaPlayersResponse ->
            fifaPlayersResponse.items?.let { listPlayers ->
                for (playerData in listPlayers) {
                    playerData.let player@{ (name, league, nation, club, headshot, height, weight, birthdate, age, position, id, rating) ->
                        listOfFifaPlayers.add(
                            FifaPlayer(
                                id = id ?: return@player,
                                name = name ?: return@player,
                                imageUrl = headshot?.imgUrl ?: return@player,
                                nationality = nation?.name ?: return@player,
                                height = height ?: return@player,
                                weight = weight ?: return@player,
                                birthdate = birthdate ?: return@player,
                                clubName = club?.name ?: return@player,
                                position = position ?: return@player,
                                age = age ?: return@player,
                                leagueName = league?.name ?: return@player,
                                rating = rating ?: return@player
                            )
                        )
                    }
                }
            }
        }
        return listOfFifaPlayers
    }
}