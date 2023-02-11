package com.example.idecabe2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.R
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.databinding.FragmentAddProjectBinding
import com.example.idecabe2.databinding.FragmentHomeBinding
import com.example.idecabe2.ui.auth.AuthViewModel
import com.example.idecabe2.utils.UiState
import com.example.idecabe2.utils.hide
import com.example.idecabe2.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()


    //connect to adapter
    val adapter by lazy {
        HomeAdapter (
                onItemClicked = {pos, item ->
                    findNavController().navigate(R.id.action_to_project_detail_fragment)
                    }
                )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Connect to adapter
        binding.recyclerViewhome.adapter  = adapter
        //Ambil data Project View Model


        authViewModel.getSession {
            homeViewModel.getProjects(it)
            if (homeViewModel.getProjects(it).equals(0)){
                binding.textData.setText("You haven't created \na Project yet")
            }
        }


        homeViewModel.getProjects.observe(viewLifecycleOwner){state ->
                when(state) {
                    is UiState.Loading -> {
                        binding.progressBar.show()
                    }
                    is UiState.Success -> {
                        binding.progressBar.hide()
                        adapter.updateList(state.data.toMutableList())
                    }
                    is UiState.failure -> {
                        binding.progressBar.hide()
                    }
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

}