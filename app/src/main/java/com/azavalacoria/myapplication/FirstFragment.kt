package com.azavalacoria.myapplication

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azavalacoria.myapplication.base.FragmentBase
import com.azavalacoria.myapplication.config.AppPrefs
import com.azavalacoria.myapplication.databinding.FragmentFirstBinding
import com.azavalacoria.myapplication.utils.CryptoHelper
import com.azavalacoria.myapplication.utils.FileHelper
import com.azavalacoria.myapplication.utils.RSAHelper
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : FragmentBase() {

    private var _binding: FragmentFirstBinding? = null
    private val permissionList = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA
    )
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            val plainText = binding.editTextTextPersonName.editableText.toString()
            val helper = CryptoHelper()
            val encoded = helper.encode(plainText)
            val sha2 = helper.toSha256(encoded)
            if (encoded.isNotEmpty()) {
                binding.textviewFirst.text = "$encoded \n $sha2"
            } else {
                binding.textviewFirst.text = "No pudo ser encriptado"
            }
        }

        currentPermissions(this.permissionList)
        Log.d("TAG", AppPrefs::class.java.canonicalName)
        this.makeKeys()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun writeFile() {
        Thread {
            val helper = FileHelper(requireActivity())
            helper.makeTextFile("ejemplo", true)
            helper.writeText("texto de ejemplo")
            val appPrefs = AppPrefs(requireContext())
            Log.d("TAG", "Archivo para ${AppPrefs.PRIVATE_KEY}: ${appPrefs.getKeyString(AppPrefs.PRIVATE_KEY)}" )
        }.start()
    }

    private fun makeKeys() {
        val helper = RSAHelper(requireContext())
        helper.store()
        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = formatter.format(current)
        helper.exportPublicKey("${requireContext().externalCacheDir!!.absolutePath}/${formatted}.key")
    }

}