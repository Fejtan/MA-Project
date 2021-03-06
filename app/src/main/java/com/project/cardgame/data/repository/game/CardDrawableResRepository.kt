package com.project.cardgame.data.repository.game

import com.project.cardgame.R

class CardDrawableResRepository
{
	private val cardDrawableResList = listOf(
		R.drawable.img1,
		R.drawable.img2,
		R.drawable.img3,
		R.drawable.img4,
		R.drawable.img5,
		R.drawable.img6,
		R.drawable.img7,
		R.drawable.img8,
		R.drawable.img9,
		R.drawable.img10,
		R.drawable.img11,
		R.drawable.img12,
		R.drawable.img13,
		R.drawable.img14,
		R.drawable.img15,
		R.drawable.img16,
		R.drawable.img17,
		R.drawable.img18
	)
	fun getRandomCardsDrawableRes(count: Int) = cardDrawableResList.shuffled().take(count)
}