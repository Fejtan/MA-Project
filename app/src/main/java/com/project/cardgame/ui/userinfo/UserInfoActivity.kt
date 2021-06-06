package com.project.cardgame.ui.userinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.project.cardgame.R
import com.project.cardgame.data.model.internal.api.Status
import com.project.cardgame.data.model.internal.user.UserData
import com.project.cardgame.utils.helper.buildMainIntent
import com.project.cardgame.utils.helper.buildOnboardIntent
import com.project.cardgame.utils.toast
import com.project.cardgame.utils.toastStringRes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserInfoActivity : AppCompatActivity()
{
	private val userInfoViewModel by lazy { ViewModelProviders.of(this).get(UserInfoViewModel::class.java) }
	private val currentUser: FirebaseUser? by lazy { FirebaseAuth.getInstance().currentUser }

	override fun onCreate(savedInstanceState: Bundle?) 
	{
		super.onCreate(savedInstanceState)
		checkIfUserLoggedIn()
	}

	override fun onPause() 
	{
		super.onPause()
		finish()
	}

	private fun checkIfUserLoggedIn() 
	{
		if (currentUser != null) 
		{
			observeUserInfo()
			userInfoViewModel.getUserInfo()
		} 
		else 
		{
			startActivity(this.buildOnboardIntent())
		}
	}

	private fun observeUserInfo() 
	{
		userInfoViewModel.userInfoResult.observe(this, Observer { result ->
			when (result.status) 
			{
				Status.SUCCESS -> {
					// Save user info
					val userDto = result.data ?: run {
						toastStringRes(R.string.errorOccured)
						FirebaseAuth.getInstance().signOut()
						finish()
						return@Observer
					}
					UserData.nickname = userDto.nickname
					UserData.avatarUrl = userDto.avatarUrl
					UserData.lastScore = userDto.lastScore
					UserData.highScore = userDto.highScore
					// Get user in
					startActivity(this.buildMainIntent())
				}
				Status.ERROR -> {
					if (result.exception != null) 
					{
						toast(result.exception.localizedMessage)
					} 
					else 
					{
						toastStringRes(R.string.errorOccured)
					}
					FirebaseAuth.getInstance().signOut()
					finish()
				}
				Status.LOADING -> {
				}
			}
		})
	}
}
