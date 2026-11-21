package com.example.agroquiriquina

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.agroquiriquina.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateAccount.setOnClickListener {
            val name = binding.etRegName.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val pass = binding.etRegPassword.text.toString()
            val confirmPass = binding.etRegConfirmPassword.text.toString()

            if (name.isEmpty()) {
                binding.etRegName.error = "Ingresa tu nombre"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etRegEmail.error = "Correo inválido"
                return@setOnClickListener
            }
            if (pass.length < 6) {
                binding.etRegPassword.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }
            if (pass != confirmPass) {
                binding.etRegConfirmPassword.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            registrarEnFirebase(name, email, pass)
        }

        binding.tvBackToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun registrarEnFirebase(nombre: String, email: String, pass: String) {
        binding.btnCreateAccount.isEnabled = false

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val usuarioAlias = email.substringBefore("@")

                val nuevoUsuario = UsuarioModel(
                    uid = uid,
                    nombre = nombre,
                    correo = email,
                    telefono = "987654321",
                    usuario = usuarioAlias
                )

                database.getReference("usuarios").child(uid)
                    .setValue(nuevoUsuario)
                    .addOnSuccessListener {
                        binding.btnCreateAccount.isEnabled = true
                        Toast.makeText(requireContext(), "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    .addOnFailureListener { e ->
                        binding.btnCreateAccount.isEnabled = true
                        Toast.makeText(requireContext(), "Error al guardar perfil: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener { e ->
                binding.btnCreateAccount.isEnabled = true
                Toast.makeText(requireContext(), "Error de registro: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}