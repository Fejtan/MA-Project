package com.project.cardgame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class ParentFragment : Fragment()
{
	@LayoutRes
	protected abstract fun getLayoutResId(): Int

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		return inflater.inflate(getLayoutResId(), container, false)
	}
}