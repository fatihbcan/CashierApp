package com.example.cashierapp.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.example.cashierapp.R
import com.example.cashierapp.databinding.FragmentCashierBinding
import com.example.cashierapp.utils.Progress
import com.example.cashierapp.viewmodel.CashierViewModel
import com.google.zxing.Result
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint
import me.dm7.barcodescanner.zxing.ZXingScannerView

@AndroidEntryPoint
class CashierFragment : Fragment(R.layout.fragment_cashier) , ZXingScannerView.ResultHandler {

    private var _binding : FragmentCashierBinding? = null
    private val binding get() = _binding!!
    private lateinit var scannerView: ZXingScannerView

    private val viewModel : CashierViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCashierBinding.bind(view)

        scannerView = binding.scanner

        viewModel.customerToken.observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                binding.buttonLayout.visibility = View.GONE
                binding.scanner.visibility = View.VISIBLE
            } else {
                binding.buttonLayout.visibility = View.VISIBLE
                binding.scanner.visibility = View.GONE
            }
        }

        binding.fail.setOnClickListener {
            viewModel.updateData(Progress.FAIL.value)
        }

        binding.success.setOnClickListener {
            viewModel.updateData(Progress.SUCCESS.value)
        }

        binding.progress.setOnClickListener {
            viewModel.updateData(Progress.IN_PROGRESS.value)
        }

    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        scannerView.startCamera() // Start camera on resume
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera() // Stop camera on pause
    }

    override fun handleResult(rawResult: Result?) {

        if(rawResult != null){
            scannerView.stopCamera() // Stop camera on pause
            viewModel.updateToken(rawResult.text)
            viewModel.sendQRReadedLog()
        }
    }


}