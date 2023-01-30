package com.example.idecabe.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.idecabe2.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        binding = FragmentAccountBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textNotifications
//        accountViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        binding.textLogout.setOnClickListener{
            if(userId.isNotEmpty()){
                logOut()
            }
        }
        return binding.root
    }

    private fun logOut(){
        auth = FirebaseAuth.getInstance()
        auth.signOut()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}