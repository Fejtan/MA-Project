package com.project.cardgame.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.cardgame.R
import com.project.cardgame.data.model.internal.game.Card
import com.project.cardgame.ui.main.viewholders.CardVH
import com.project.cardgame.utils.CardWithPositionCallBack

class CardsRVAdapter(
	private val cardList: List<Card>,
	private val cardSizePx: Int,
	private val cardClickListener: CardWithPositionCallBack
) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private var isCardsClickable = true

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false).apply {
			layoutParams = ViewGroup.LayoutParams(cardSizePx, cardSizePx)
		}
		return CardVH(itemView)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is CardVH) holder.bind(cardList[position], isCardsClickable, cardClickListener)
	}

	override fun getItemCount() = cardList.size

	fun flipCard(cardPosition: Int) {
		cardList[cardPosition].apply {
			isFlippedFront = !isFlippedFront
		}
		notifyItemChanged(cardPosition)
	}

	fun markCardsAsMatchFound(card1Position: Int, card2Position: Int) {
		cardList[card1Position].apply {
			isFlippedFront = true
			isMatchFound = true
		}
		notifyItemChanged(card1Position)
		cardList[card2Position].apply {
			isFlippedFront = true
			isMatchFound = true
		}
		notifyItemChanged(card2Position)
	}

	fun disableCardsClicks() {
		isCardsClickable = false
		notifyDataSetChanged()
	}

	fun enableCardsClicks() {
		isCardsClickable = true
		notifyDataSetChanged()
	}

}
