package com.project.cardgame.ui.main.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.cardgame.R
import com.project.cardgame.data.model.dto.LeadersDTO
import com.project.cardgame.utils.loadImageUrl
import kotlinx.android.synthetic.main.item_leader.view.*

class LeaderVH(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_leader, parent, false))
{
	fun bind(leader: LeadersDTO)
	{
		/* Leader's Order */
		itemView.tvOrder.text = (adapterPosition + 1 + 3).toString()
		/* Leader's Avatar */
		itemView.imgAvatar.loadImageUrl(leader.avatarUrl)
		/* Leader's Nickname */
		itemView.tvNickname.text = leader.nickname
		/* Leader's High Score */
		itemView.tvScore.text = leader.highScore.toString()
	}
}