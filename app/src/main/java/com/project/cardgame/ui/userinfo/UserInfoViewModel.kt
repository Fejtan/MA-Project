package com.project.cardgame.ui.userinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.cardgame.data.model.dto.UserDTO
import com.project.cardgame.data.model.internal.api.Resource
import com.project.cardgame.data.repository.ApiRepository
import com.project.cardgame.utils.logError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserInfoViewModel : ViewModel() 
{
	private val apiRepository by lazy { ApiRepository() }
	val userInfoResult by lazy { MutableLiveData<Resource<UserDTO>>() }

	fun getUserInfo() 
	{
		userInfoResult.value = Resource.loading()
		val currentUser = apiRepository.getCurrentUser() ?: run {
			onGetUserInfoError()
			return
		}
		apiRepository.getUser(currentUser.uid)
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) 
				{
					userInfoResult.value = Resource.success(dataSnapshot.getValue(UserDTO::class.java))
				}

				override fun onCancelled(databaseError: DatabaseError) 
				{
					onGetUserInfoError()
				}
			})
	}

	private fun onGetUserInfoError(exception: Exception? = null) 
	{
		"GetUserInfo Error: $exception".logError()
		apiRepository.signOut()
		userInfoResult.value = Resource.error(exception)
	}

	fun signOut() = apiRepository.signOut()
}
