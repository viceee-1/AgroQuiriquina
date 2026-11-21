package com.example.agroquiriquina

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.agroquiriquina.R
import com.example.agroquiriquina.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmReset.setOnClickListener {
            val newPass = binding.etNewPassword.text.toString()
            val repeatPass = binding.etRepeatPassword.text.toString()

            if (newPass.length < 6) {
                binding.etNewPassword.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }
            if (newPass != repeatPass) {
                binding.etRepeatPassword.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}