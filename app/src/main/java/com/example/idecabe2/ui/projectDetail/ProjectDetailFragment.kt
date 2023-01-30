package com.example.idecabe2.ui.projectDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.R
import com.example.idecabe2.databinding.FragmentHomeBinding
import com.example.idecabe2.ui.home.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectDetailFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val projectDetailViewModel: ProjectDetailViewModel by viewModels()

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



    override fun onDestroyView() {
        super.onDestroyView()
    }
}