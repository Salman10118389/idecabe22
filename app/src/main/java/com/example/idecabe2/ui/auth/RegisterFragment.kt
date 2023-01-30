package com.example.idecabe2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.BottomNavigationActivity
import com.example.idecabe2.R
import com.example.idecabe2.data.model.User
import com.example.idecabe2.databinding.FragmentRegisterBinding
import com.example.idecabe2.utils.UiState
import com.example.idecabe2.utils.hide
import com.example.idecabe2.utils.show
import com.example.idecabe2.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    val TAG: String = "RegisterFragment"
    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observer()
        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            if (validation()){
                authViewModel.registerUser(email = email,
                    password = pass,
                    user = getUserObj())
            }
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.navigation_to_login)
        }
    }

    fun observer(){
        authViewModel.register.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.Success -> {
                    binding.progressCircular.hide()
                    binding.btnRegister.setText("Register")
                    findNavController().navigate(R.id.navigation_to_login)
                    toast(state.data)
                }
                is UiState.failure -> {
                    binding.progressCircular.hide()
                    toast(state.error)
                    binding.btnRegister.setText("")
                }
                is UiState.Loading -> {
                    binding.progressCircular.show()

                }
            }
        }
    }

    //get User Object
    fun getUserObj(): User{
        return User(
            user_id = "",
            full_name = binding.etFullname.text.toString(),
            email = binding.etEmail.text.toString(),
            date = Date()
        )
    }



    //validation form
    fun validation(): Boolean{
        var isValid = true
        val email = binding.etEmail.text.toString()
        val fullName = binding.etFullname.text.toString()
        val password = binding.etPassword.text.toString()
        val conf_pass = binding.etConfirmPassword.text.toString()

        if (email.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_email))
        }
        if (fullName.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_fullname))
        }
        if (password.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_pass))
        }
        if (conf_pass.isNullOrEmpty()){
            isValid = false
            toast("Please enter Confirm Password")
        }
        if (conf_pass != password ) {
            isValid = true
            toast("Password & Confirm Password not identic")
        }
        if (password?.length!! < 6){
            isValid = false
            toast(getString(R.string.longer_pass))
        }
        return isValid
    }
}