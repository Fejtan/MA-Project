package com.project.cardgame.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.project.cardgame.R
import kotlinx.android.synthetic.main.dialog_game.view.*
import kotlinx.android.synthetic.main.dialog_progress.view.*

fun Context.getProgressDialog(@StringRes bodyResId: Int?): AlertDialog
{
	val text = if (bodyResId != null) getString(bodyResId) else null
	return this.getProgressDialog(text)
}

fun Context.getProgressDialog(body: String? = null): AlertDialog
{
	val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_progress, null)
	val tvBody = dialogView.tvBody

	// Dialog Body Text
	if (body != null && body.isNotEmpty())
	{
		tvBody.text = body
	}
	else
	{
		tvBody.visibility = View.GONE
	}

	return AlertDialog.Builder(this)
		.setView(dialogView)
		.setCancelable(false)
		.create()
		.apply { window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) }
}

fun Context.getGameDialog(
	title: String,
	@ColorRes titleColorResId: Int,
	info1: String? = null,
	info2: String? = null,
	info3: String? = null,
	@ColorRes infoBackgroundColorResId: Int,
	playButtonText: String,
	playButtonClickListener: () -> Unit,
	leaderboardButtonText: String? = null,
	leaderboardButtonClickListener: (() -> Unit)? = null
): AlertDialog
{
	val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_game, null)
	val titleColor = ContextCompat.getColor(this, titleColorResId)
	val infoBackgroundColor = ContextCompat.getColor(this, infoBackgroundColorResId)
	// Dialog Title
	dialogView.userTitle.text = title
	dialogView.userTitle.setTextColor(titleColor)
	// Info 1
	if (info1 != null)
	{
		dialogView.userMainActInfo1.text = info1
		dialogView.userMainActInfo1.setBackgroundColor(infoBackgroundColor)
	}
	else
	{
		dialogView.userMainActInfo1.visibility = View.GONE
	}
	// Info 2
	if (info2 != null)
	{
		dialogView.userMainActInfo2.text = info2
		dialogView.userMainActInfo2.setBackgroundColor(infoBackgroundColor)
	}
	else
	{
		dialogView.userMainActInfo2.visibility = View.GONE
	}
	// Info 3
	if (info3 != null)
	{
		dialogView.userMainActInfo3.text = info3
		dialogView.userMainActInfo3.setBackgroundColor(infoBackgroundColor)
	}
	else
	{
		dialogView.userMainActInfo3.visibility = View.GONE
	}
	// Button 1
	dialogView.playButton.text = playButtonText
	dialogView.playButton.setOnClickListener {
		playButtonClickListener.invoke()
	}
	// Button 2
	if (leaderboardButtonText != null)
	{
		dialogView.leaderboardButton.text = leaderboardButtonText
		dialogView.leaderboardButton.setOnClickListener {
			leaderboardButtonClickListener?.invoke()
		}
	}
	else
	{
		dialogView.leaderboardButton.visibility = View.GONE
	}

	return AlertDialog.Builder(this)
		.setView(dialogView)
		.setCancelable(true)
		.create()
		.apply { window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) }
}