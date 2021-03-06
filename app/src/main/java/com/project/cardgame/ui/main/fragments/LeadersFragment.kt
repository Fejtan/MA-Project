package com.project.cardgame.ui.main.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.cardgame.R
import com.project.cardgame.data.model.dto.LeadersDTO
import com.project.cardgame.data.model.internal.api.Status
import com.project.cardgame.ui.ParentFragment
import com.project.cardgame.ui.main.adapters.LeadersRVAdapter
import com.project.cardgame.ui.main.viewmodels.LeadersViewModel
import com.project.cardgame.utils.helper.LinearMarginItemDecoration
import com.project.cardgame.utils.loadImageUrl
import com.project.cardgame.utils.toast
import com.project.cardgame.utils.toastStringRes
import kotlinx.android.synthetic.main.fragment_leaders.*

class LeadersFragment : ParentFragment()
{
	private val leadersViewModel by lazy { ViewModelProviders.of(this).get(LeadersViewModel::class.java) }
	override fun getLayoutResId() = R.layout.fragment_leaders

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		// Set leadersRecyclerView
		val context = context ?: return
		rvLeaders.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
		val spacing1px = resources.getDimensionPixelSize(R.dimen.spacing_1)
		rvLeaders.addItemDecoration(LinearMarginItemDecoration(RecyclerView.VERTICAL, spacing1px, false))
		// Get leaders
		observeLeaders()
		val leadersCountLimit = resources.getInteger(R.integer.leaders_count_limit)
		leadersViewModel.getLeaders(leadersCountLimit)
		setListeners()
	}

	private fun setListeners()
	{
		// Back Icon Image's ClickListener
		imgToolbarBack.setOnClickListener {
			activity?.onBackPressed()
		}
	}

	private fun observeLeaders()
	{
		leadersViewModel.leadersResult.observe(viewLifecycleOwner, { leadersResult ->
			if (leadersResult.status == Status.LOADING)
			{
				progressBar.visibility = View.VISIBLE
			}
			else
			{
				progressBar.visibility = View.INVISIBLE
			}
			when (leadersResult.status)
			{
				Status.SUCCESS -> {
					leadersResult.data?.let {
						if (it.size > 3)
						{
							setFirstThreeLeaders(it.take(3))
							setLeadersListToRecyclerView(it.drop(3))
						}
						else
						{
							setFirstThreeLeaders(it)
							setLeadersListToRecyclerView(listOf())
						}
					} ?: run {

					}
				}
				Status.ERROR -> {
					if (leadersResult.exception != null)
					{
						context?.toast(leadersResult.exception.localizedMessage)
					}
					else
					{
						context?.toastStringRes(R.string.errorOccured)
					}
				}
				Status.LOADING -> {
				}
			}
		})
	}

	private fun setLeadersListToRecyclerView(leadersList: List<LeadersDTO>)
	{
		rvLeaders.adapter = LeadersRVAdapter(leadersList)
	}

	private fun setFirstThreeLeaders(leadersList: List<LeadersDTO>)
	{
		// Leader 1
		if (leadersList.isEmpty()) return
		val leader1 = leadersList[0]
		viewFirstThreeLeadersArea.visibility = View.VISIBLE
		tvLeader1Order.visibility = View.VISIBLE
		imgLeader1Avatar.loadImageUrl(leader1.avatarUrl)
		imgLeader1Avatar.visibility = View.VISIBLE
		tvLeader1Nickname.text = leader1.nickname
		tvLeader1Nickname.visibility = View.VISIBLE
		tvLeader1Score.text = leader1.highScore.toString()
		tvLeader1Score.visibility = View.VISIBLE
		// Leader 2
		if (leadersList.size < 2) return
		val leader2 = leadersList[1]
		tvLeader2Order.visibility = View.VISIBLE
		imgLeader2Avatar.loadImageUrl(leader2.avatarUrl)
		imgLeader2Avatar.visibility = View.VISIBLE
		tvLeader2Nickname.text = leader2.nickname
		tvLeader2Nickname.visibility = View.VISIBLE
		tvLeader2Score.text = leader2.highScore.toString()
		tvLeader2Score.visibility = View.VISIBLE
		// Leader 3
		if (leadersList.size < 3) return
		val leader3 = leadersList[2]
		tvLeader3Order.visibility = View.VISIBLE
		imgLeader3Avatar.loadImageUrl(leader3.avatarUrl)
		imgLeader3Avatar.visibility = View.VISIBLE
		tvLeader3Nickname.text = leader3.nickname
		tvLeader3Nickname.visibility = View.VISIBLE
		tvLeader3Score.text = leader3.highScore.toString()
		tvLeader3Score.visibility = View.VISIBLE
	}
}
