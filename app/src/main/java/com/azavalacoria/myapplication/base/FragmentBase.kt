package com.azavalacoria.myapplication.base

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class FragmentBase : Fragment() {

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    fun currentPermissions(permissions: Array<String>) {
        val permissionsToAsk = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
                permissionsToAsk.add(permission)
            }
        }
        if (permissionsToAsk.isNotEmpty()) {
            val array = permissionsToAsk.toTypedArray()
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissionsToAsk[0])) {
                ActivityCompat.requestPermissions(requireActivity(), array, 1)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), array, 1)
            }
        } else if (permissionsToAsk.isEmpty()) {
            showToast("No hay permisos que conceder")
        }
    }
}