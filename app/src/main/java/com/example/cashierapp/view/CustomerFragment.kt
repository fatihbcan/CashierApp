package com.example.cashierapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cashierapp.R
import com.example.cashierapp.databinding.FragmentCashierBinding
import com.example.cashierapp.databinding.FragmentCustomerBinding
import dagger.hilt.android.AndroidEntryPoint
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.cashierapp.utils.Progress
import com.example.cashierapp.viewmodel.CustomerViewModel

import com.google.zxing.BarcodeFormat

import com.google.zxing.MultiFormatWriter

import com.google.zxing.common.BitMatrix

import com.google.zxing.WriterException
import java.lang.Exception


@AndroidEntryPoint
class CustomerFragment : Fragment(R.layout.fragment_customer) {

    private var _binding : FragmentCustomerBinding? = null
    private val binding get() = _binding!!
    private val WIDTH = 500


    private val customerViewModel : CustomerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCustomerBinding.bind(view)

        customerViewModel.status.observe(viewLifecycleOwner){
            when(it){
                Progress.IN_PROGRESS.value -> binding.root.setBackgroundColor(Color.YELLOW)
                Progress.SUCCESS.value -> binding.root.setBackgroundColor(Color.GREEN)
                Progress.FAIL.value -> binding.root.setBackgroundColor(Color.RED)
            }

            if(it.isNullOrEmpty() || it.equals(Progress.IDLE.value, true)){
                binding.qrCode.visibility = View.VISIBLE
                binding.resultText.visibility = View.GONE
            } else {
                binding.qrCode.visibility = View.GONE
                binding.resultText.visibility = View.VISIBLE
            }

            binding.resultText.text = it
        }

        try {
            val bmp = encodeAsBitmap(customerViewModel.getAuthToken())
            binding.qrCode.setImageBitmap(bmp)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        customerViewModel.eventListener()

    }


    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String?): Bitmap? {
        val result: BitMatrix
        var bitmap: Bitmap? = null
        try {
            result = MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE, WIDTH, WIDTH, null
            )
            val w = result.width
            val h = result.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (result[x, y]) Color.BLACK else Color.WHITE
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h)
        } catch (iae: Exception) {
            iae.printStackTrace()
            return null
        }
        return bitmap
    }


}