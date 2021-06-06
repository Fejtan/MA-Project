package com.project.cardgame.ui.main.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.project.cardgame.R
import com.project.cardgame.data.model.internal.api.Status
import com.project.cardgame.data.model.internal.user.UserData
import com.project.cardgame.ui.ParentFragment
import com.project.cardgame.ui.userinfo.UserInfoViewModel
import com.project.cardgame.utils.helper.buildOnboardIntent
import com.project.cardgame.utils.loadImageUrl
import com.project.cardgame.utils.toast
import com.project.cardgame.utils.toastStringRes
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : ParentFragment()
{

	private val userInfoViewModel by lazy { ViewModelProviders.of(this).get(UserInfoViewModel::class.java) }
	private val navController by lazy { findNavController() }

	override fun getLayoutResId() = R.layout.fragment_home

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		setProfileInfo()
		observeUserInfo()
		userInfoViewModel.getUserInfo()
		setListeners()
	}

	private fun setListeners()
	{
		// LogOut Icon's ClickListener
		imgLogOut.setOnClickListener {
			userInfoViewModel.userInfoResult.removeObservers(this)
			userInfoViewModel.signOut()
			startActivity(context?.buildOnboardIntent())
			activity?.finish()
		}

		// Play Button's ClickListener
		btnPlay.setOnClickListener {
			navController.navigate(R.id.action_homeFragment_to_gameFragment)
		}

		// Leaders Button's ClickListener
		btnLeaders.setOnClickListener {
			navController.navigate(R.id.action_homeFragment_to_leadersFragment)
		}
	}

	private fun observeUserInfo()
	{
		userInfoViewModel.userInfoResult.observe(viewLifecycleOwner, Observer { result ->
			when (result.status)
			{
				Status.SUCCESS -> {
					// Save user info
					val userDto = result.data ?: run {
						context?.toastStringRes(R.string.errorOccured)
						return@Observer
					}
					UserData.nickname = userDto.nickname
					UserData.avatarUrl = userDto.avatarUrl
					UserData.lastScore = userDto.lastScore
					UserData.highScore = userDto.highScore
					// Update profile area
					setProfileInfo()
				}
				Status.ERROR -> {
					if (result.exception != null)
					{
						context?.toast(result.exception.localizedMessage)
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

	private fun setProfileInfo()
	{
		imgAvatar.loadImageUrl(UserData.avatarUrl)
		tvNickname.text = UserData.nickname
		tvScores.text = getString(R.string.userScores, UserData.lastScore, UserData.highScore)
	}
}
