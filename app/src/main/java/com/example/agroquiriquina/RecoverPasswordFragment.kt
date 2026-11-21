package com.example.agroquiriquina

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.agroquiriquina.R
import com.example.agroquiriquina.databinding.FragmentRecoverPasswordBinding

class RecoverPasswordFragment : Fragment() {

    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendRecovery.setOnClickListener {
            val email = binding.etEmailRecover.text.toString().trim()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailRecover.error = "Correo inválido"
                return@setOnClickListener
            }

            // Muestra mensaje azul del mockup: "Revisa tu bandeja de entrada!"
            binding.tvInboxNotice.visibility = View.VISIBLE

            // Simulación de redirección tras validar código/enlace
            binding.tvInboxNotice.setOnClickListener {
                findNavController().navigate(R.id.action_recoverPasswordFragment_to_resetPasswordFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}