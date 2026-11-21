package com.example.agroquiriquina

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.agroquiriquina.R
import com.example.agroquiriquina.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSavedSession()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInputs(email, password)) {
                saveSessionPreference(email, binding.cbKeepSession.isChecked)
                // Navegar al perfil/dashboard principal
                findNavController().navigate(R.id.action_loginFragment_to_userProfileFragment)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverPasswordFragment)
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Ingresa un correo electrónico válido"
            return false
        }
        if (password.length < 6) {
            binding.etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        }
        return true
    }

    private fun saveSessionPreference(email: String, keepSession: Boolean) {
        val prefs = requireContext().getSharedPreferences("AgroPrefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("KEEP_SESSION", keepSession)
            .putString("USER_EMAIL", email)
            .apply()
    }

    private fun checkSavedSession() {
        val prefs = requireContext().getSharedPreferences("AgroPrefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("KEEP_SESSION", false)) {
            findNavController().navigate(R.id.action_loginFragment_to_userProfileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}