package com.project.cardgame.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.cardgame.data.model.dto.LeadersDTO
import com.project.cardgame.data.model.internal.api.Resource
import com.project.cardgame.data.repository.ApiRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LeadersViewModel : ViewModel()
{
	private val apiRepository by lazy { ApiRepository() }
	val leadersResult by lazy { MutableLiveData<Resource<List<LeadersDTO>>>() }

	fun getLeaders(leadersCountLimit: Int)
	{
		leadersResult.value = Resource.loading()
		apiRepository.getLeaders(leadersCountLimit)
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot)
				{
					val leaders = arrayListOf<LeadersDTO>()
					for (leaderDataSnapShot in dataSnapshot.children)
					{
						val leader = leaderDataSnapShot.getValue(LeadersDTO::class.java)
						leaders.add(leader ?: continue)
					}
					leaders.reverse()
					leadersResult.value = Resource.success(leaders)
				}

				override fun onCancelled(databaseError: DatabaseError)
				{
					leadersResult.value = Resource.error(databaseError.toException())
				}
			})
	}
}
