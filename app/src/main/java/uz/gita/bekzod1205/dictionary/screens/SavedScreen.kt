package uz.gita.bekzod1205.dictionary.screens

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.bekzod1205.dictionary.R
import uz.gita.bekzod1205.dictionary.adapter.WordAdapter
import uz.gita.bekzod1205.dictionary.databinding.FragmentSavedScreenBinding
import uz.gita.bekzod1205.dictionary.db.AppDatabase
import java.util.Locale


class SavedScreen : Fragment(R.layout.fragment_saved_screen), TextToSpeech.OnInitListener {
    private var _binding: FragmentSavedScreenBinding? = null
    private val binding get() = _binding!!
    private val adapter = WordAdapter()
    private val database = AppDatabase.getInstance()
    private lateinit var tts: TextToSpeech
    private var isTTSReady = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedScreenBinding.bind(view)
        tts = TextToSpeech(requireContext(), this)
        adapter.submitList(database.getDictionaryDao().getFavourites())
        adapter.setOnBookmarkClickListener { position, it ->
            val id = it.id
            val isFavourite = if (it.isFavourite == 1) 0 else 1
            database.getDictionaryDao().update(id, isFavourite)
            adapter.submitList(database.getDictionaryDao().getFavourites())
        }
        adapter.setOnVoiceClickListener {
            speak(it.english.toString())
        }
        binding.dictionaryRv.adapter = adapter
        binding.dictionaryRv.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            isTTSReady =
                result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    private fun speak(text: String) {
        if (isTTSReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tts.stop()
        tts.shutdown()
    }
}