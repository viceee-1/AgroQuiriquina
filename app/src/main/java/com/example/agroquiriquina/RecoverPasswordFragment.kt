package com.example.agroquiriquina

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.agroquiriquina.databinding.FragmentRecoverPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class RecoverPasswordFragment : Fragment() {

    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
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

            binding.btnSendRecovery.isEnabled = false
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    binding.btnSendRecovery.isEnabled = true
                    binding.tvInboxNotice.visibility = View.VISIBLE
                }
                .addOnFailureListener { e ->
                    binding.btnSendRecovery.isEnabled = true
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}