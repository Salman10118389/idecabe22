package com.example.idecabe2.ui.account

import android.accounts.Account
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.MainActivity
import com.example.idecabe2.R
import com.example.idecabe2.databinding.FragmentAccountBinding
import com.example.idecabe2.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    companion object {
        fun newInstance() = AccountFragment()
    }
   private val accountViewModel: AccountViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.logout.setOnClickListener{
            accountViewModel.logout {
                 val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}