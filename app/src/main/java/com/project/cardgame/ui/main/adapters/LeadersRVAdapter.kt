package com.project.cardgame.ui.main.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.cardgame.data.model.dto.LeadersDTO
import com.project.cardgame.ui.main.viewholders.LeaderVH

class LeadersRVAdapter(private val leadersList: List<LeadersDTO>) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LeaderVH(parent)

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is LeaderVH) {
			holder.bind(leadersList[position])
		}
	}

	override fun getItemCount() = leadersList.size
}
