package com.project.cardgame.utils.helper

import android.content.Context
import android.content.Intent
import com.project.cardgame.ui.main.MainActivity
import com.project.cardgame.ui.registration.RegistrationActivity

fun Context.buildOnboardIntent(): Intent
{
	return Intent(this, RegistrationActivity::class.java).apply {
		flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
	}
}

fun Context.buildMainIntent(): Intent
{
	return Intent(this, MainActivity::class.java).apply {
		flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
	}
}