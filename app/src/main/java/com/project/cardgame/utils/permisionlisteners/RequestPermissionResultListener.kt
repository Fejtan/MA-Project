package com.project.cardgame.utils.permisionlisteners

interface RequestPermissionResultListener
{
	fun onPermissionGranted()
	fun onPermissionDenied()
	fun onPermissionPermanentlyDenied()
}