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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCustomerBinding.bind(view)

        try {
            val bmp = encodeAsBitmap("Hello world!")
            binding.qrCode.setImageBitmap(bmp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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