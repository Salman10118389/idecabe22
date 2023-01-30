package com.example.idecabe2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.BottomNavigationActivity
import com.example.idecabe2.R
import com.example.idecabe2.databinding.FragmentLoginBinding
import com.example.idecabe2.databinding.FragmentNotificationsBinding
import com.example.idecabe2.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var binding: FragmentLoginBinding

    private val authViewModel: AuthViewModel by viewModels()
    private val TAG = "LoginFragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.buttonLogin.setOnClickListener{
            val email = binding.email.text.toString()
            val pass = binding.pass.text.toString()
            if (validation()){
                authViewModel.login(
                    email = email,
                    password = pass
                )
            }
        }

        binding.account.setOnClickListener {
            findNavController().navigate(R.id.navigation_to_register)
        }
    }
    fun observer(){
        authViewModel.login.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.Loading -> {
                    binding.progressBar.show()
                    binding.buttonLogin.setText("")
                }
                is UiState.failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                    binding.buttonLogin.setText("Sign In")
                }

                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(state.data)
                    binding.buttonLogin.hide()
                    val intent = Intent(context, BottomNavigationActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }

    fun validation(): Boolean{
        var isValid = true
        val email = binding.email.text.toString()
        val password = binding.pass.text.toString()

        if (email.isNullOrEmpty()){
            toast("Please enter the Email")
            isValid = false
        }else {
            if(!email.isValidEmail()){
                toast("Please enter the Right Email")
                isValid = false
            }
        }

        if(password.isNullOrEmpty()){
            toast("Please enter the Password")
            isValid = false
        }else {
            if(password.length < 6){
                toast("Please enter minimum 6 Characters Password...")
                isValid = false
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
    }
}