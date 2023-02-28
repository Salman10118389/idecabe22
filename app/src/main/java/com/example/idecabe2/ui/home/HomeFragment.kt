package com.example.idecabe2.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.MainActivity
import com.example.idecabe2.R
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.databinding.FragmentAddProjectBinding
import com.example.idecabe2.databinding.FragmentHomeBinding
import com.example.idecabe2.ui.auth.AuthViewModel
import com.example.idecabe2.utils.UiState
import com.example.idecabe2.utils.hide
import com.example.idecabe2.utils.show
import com.example.idecabe2.utils.toast
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "project"
private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment(), postClickedItem  {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private var doublePressed = false

    //connect to adapter
    private lateinit var adapter: HomeAdapter
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
        adapter = HomeAdapter(this)
        binding.recyclerViewhome.adapter  = adapter
        //Ambil data Project View Model
        authViewModel.getSession {
            if (it == null){
                val intent = Intent(activity?.applicationContext,MainActivity::class.java)
                startActivity(intent)
            }else {
                homeViewModel.getProjects(it)
                if (homeViewModel.getProjects(it).equals(0)){
                    binding.textData.setText("You haven't created \na Project yet")
                }
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

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (doublePressed){
                        activity?.finish()
                    }
                    doublePressed = true
                    Toast.makeText(activity?.applicationContext, "One More Back Press to Exit", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed(Runnable{
                        doublePressed = false
                    }, 2000)
                }
            })
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun clickedPostItem(project: Project) {
        val bundle = Bundle()
        bundle.putParcelable("project", project)
        Log.d(TAG, "clickedPostItem: name of project is $bundle")
        findNavController().navigate(R.id.action_to_project_detail_fragment, bundle)
    }


}