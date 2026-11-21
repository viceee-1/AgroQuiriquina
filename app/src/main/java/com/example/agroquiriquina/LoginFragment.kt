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
import com.example.agroquiriquina.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSavedSession()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInputs(email, password)) {
                iniciarSesionFirebase(email, password)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverPasswordFragment)
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun iniciarSesionFirebase(email: String, password: String) {
        binding.btnLogin.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                binding.btnLogin.isEnabled = true
                saveSessionPreference(email, binding.cbKeepSession.isChecked)
                findNavController().navigate(R.id.action_loginFragment_to_userProfileFragment)
            }
            .addOnFailureListener { e ->
                binding.btnLogin.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    "Error al ingresar: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
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
        val keepSession = prefs.getBoolean("KEEP_SESSION", false)

        if (keepSession && auth.currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_userProfileFragment)
        } else if (!keepSession && auth.currentUser != null) {
            auth.signOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}