package com.example.agroquiriquina

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.agroquiriquina.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var userRef: DatabaseReference? = null
    private var userListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarPerfilRealtimeDatabase()

        binding.bottomNavigation.selectedItemId = R.id.nav_user
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    val prefs = requireContext().getSharedPreferences("AgroPrefs", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("KEEP_SESSION", false).apply()
                    auth.signOut()
                    findNavController().navigate(R.id.action_userProfileFragment_to_loginFragment)
                    true
                }
                else -> true
            }
        }
    }

    private fun cargarPerfilRealtimeDatabase() {
        val uid = auth.currentUser?.uid ?: return
        userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid)
        userRef?.keepSynced(true)

        userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(UsuarioModel::class.java) ?: return
                binding.tvProfileName.text = usuario.nombre
                binding.tvProfileEmail.text = usuario.correo
                binding.tvProfilePhone.text = usuario.telefono
                binding.tvProfileUsername.text = usuario.usuario
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error de lectura: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
        userRef?.addValueEventListener(userListener!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListener?.let { userRef?.removeEventListener(it) }
        _binding = null
    }
}