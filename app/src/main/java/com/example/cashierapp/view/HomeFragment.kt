package com.example.cashierapp.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cashierapp.R
import com.example.cashierapp.databinding.FragmentHomeBinding
import com.example.cashierapp.utils.UserTypes
import com.example.cashierapp.viewmodel.HomeFragmentViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        binding.crashTester.setOnLongClickListener {
            throw Exception("crash test")
        }

        viewModel.navigationDecider.observe(viewLifecycleOwner){
            when(it){
                UserTypes.CUSTOMER.value -> {
                    val action = HomeFragmentDirections.actionHomeFragmentToCustomerFragment()
                    findNavController().navigate(action)
                    viewModel.setNavDefault()
                }
                UserTypes.CASHIER.value -> {
                    goCashierFragment()
                    viewModel.setNavDefault()
                }
            }
        }


        binding.cashierCard.setOnClickListener {
            viewModel.processFirebaseActionsAndNavigate(UserTypes.CASHIER.value)
        }

        binding.customerCard.setOnClickListener{
            viewModel.processFirebaseActionsAndNavigate(UserTypes.CUSTOMER.value)
        }
    }


    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                goToCashierFragment()
            }
        }

    private fun goCashierFragment() {
        activity?.let {
            if (hasPermissions(activity as Context, PERMISSIONS)) {
                goToCashierFragment()
            } else {
                permReqLauncher.launch(
                    PERMISSIONS
                )
            }
        }
    }

    // util method
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun goToCashierFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToCashierFragment()
        findNavController().navigate(action)
    }

}