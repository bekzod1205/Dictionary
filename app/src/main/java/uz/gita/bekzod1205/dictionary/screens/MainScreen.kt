package uz.gita.bekzod1205.dictionary.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import uz.gita.bekzod1205.dictionary.R
import uz.gita.bekzod1205.dictionary.databinding.FragmentMainScreenBinding
import uz.gita.bekzod1205.dictionary.utils.openScreenSaveStack


class MainScreen : Fragment(R.layout.fragment_main_screen) {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainScreenBinding.bind(view)
        parentFragmentManager.beginTransaction().replace(R.id.container, DictionaryScreen()).commit()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dictionary -> {
                    openScreenSaveStack(DictionaryScreen())
                    true
                }

                R.id.saved -> {
                    openScreenSaveStack(SavedScreen())
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}