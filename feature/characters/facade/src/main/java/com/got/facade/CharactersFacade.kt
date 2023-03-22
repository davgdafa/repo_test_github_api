package com.got.facade

import android.content.Context
import android.content.Intent
import com.got.presentation.characters.CharactersComposeActivity

interface CharactersFacade {
    fun launchCharactersFlow(context: Context)
}

class CharactersFacadeImpl: CharactersFacade {
    override fun launchCharactersFlow(context: Context) {
        context.startActivity(Intent(context, CharactersComposeActivity::class.java))
    }
}