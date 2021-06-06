package com.project.cardgame.data.model.internal.game

import androidx.annotation.DrawableRes

data class Card(
	@DrawableRes val drawableResId: Int,
	var isFlippedFront: Boolean = false,
	var isMatchFound: Boolean = false
)