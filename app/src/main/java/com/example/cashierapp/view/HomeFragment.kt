package com.example.cashierapp.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.cashierapp.R
import com.example.cashierapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)


        binding.cashierCard.setOnClickListener {
            goCashierFragment()
        }

        binding.customerCard.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToCustomerFragment()
            findNavController().navigate(action)
        }
    }


    companion object {
        val TAG: String = CashierFragment::class.java.simpleName
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