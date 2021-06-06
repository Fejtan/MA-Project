package com.project.cardgame.ui.registration

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.project.cardgame.R
import com.project.cardgame.data.model.internal.api.Status
import com.project.cardgame.ui.ParentFragment
import com.project.cardgame.utils.*
import com.project.cardgame.utils.helper.buildMainIntent
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : ParentFragment()
{

	private val onboardViewModel by lazy { ViewModelProviders.of(this).get(RegistrationViewModel::class.java) }
	private val navController by lazy { findNavController() }

	private val progressDialog by lazy { context?.getProgressDialog(R.string.loginProgress) }

	override fun getLayoutResId() = R.layout.fragment_login

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		// Set maxLengths to editTexts
		etNickname.setMaxLength(resources.getInteger(R.integer.nickname_max_char))
		etPassword.setMaxLength(resources.getInteger(R.integer.password_max_char))
		observeLoginResult()
		etNickname.focusAndShowKeyboard()
		setListeners()
	}

	private fun setListeners()
	{
		/* Nickname EditText'S TextChangedListener */
		etNickname.addTextChangedListenerWithUpdateFunction(::updateLoginButtonEnableStatus)
		/* Password EditText'S TextChangedListener */
		etPassword.addTextChangedListenerWithUpdateFunction(::updateLoginButtonEnableStatus)
		/* Login Button's ClickListener */
		btnLogin.setOnClickListener {
			onboardViewModel.login(etNickname.trimmedText(), etPassword.trimmedText())
		}
		/* Register Text's ClickListener*/
		tvRegister.setOnClickListener {
			navController.navigate(R.id.action_loginFragment_to_registerFragment)
		}
	}

	private fun observeLoginResult()
	{
		onboardViewModel.loginResult.observe(viewLifecycleOwner, { result ->
			if (result.status == Status.LOADING)
			{
				progressDialog?.show()
			}
			else
			{
				progressDialog?.dismiss()
			}
			when (result.status)
			{
				Status.SUCCESS -> {
					startActivity(context?.buildMainIntent())
					activity?.finish()
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

	private fun updateLoginButtonEnableStatus()
	{
		btnLogin.isEnabled = etNickname.trimmedText().isNotEmpty()
				&& etPassword.trimmedText().isNotEmpty()
	}
}
