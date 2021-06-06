package com.project.cardgame.ui.registration

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.cardgame.R
import com.project.cardgame.utils.AvatarDrawableResCallBack
import com.project.cardgame.utils.startClickAlphaAnimation
import com.makeramen.roundedimageview.RoundedImageView

class AvatarVH(itemView: View) : RecyclerView.ViewHolder(itemView)
{
	fun bind(@DrawableRes avatarDrawableRes: Int, clickListener: AvatarDrawableResCallBack)
	{
		if (itemView !is RoundedImageView) return

		itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
		if (avatarDrawableRes != 0) {
			itemView.setImageResource(avatarDrawableRes)
		} else {
			itemView.setImageResource(R.drawable.ic_plus_blue700_24dp)
		}

		itemView.setOnClickListener {
			it.startClickAlphaAnimation()
			clickListener.invoke(avatarDrawableRes)
		}
	}
}
