package uz.gita.bekzod1205.dictionary.screens

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.gita.bekzod1205.dictionary.R
import uz.gita.bekzod1205.dictionary.databinding.FragmentDetailedScreenBinding
import uz.gita.bekzod1205.dictionary.db.entity.WordEntity
import java.util.Locale

class DetailedScreen : Fragment(R.layout.fragment_detailed_screen), TextToSpeech.OnInitListener {
    private var _binding: FragmentDetailedScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var tts: TextToSpeech
    private var isTTSReady = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailedScreenBinding.bind(view)
        val word = arguments?.getSerializable("word") as WordEntity
        tts = TextToSpeech(requireContext(), this)
        binding.tvWord.text = word.english
        binding.tvTranscription.text = word.transcript
        binding.tvType.text = word.type
        binding.tvTranslation.text = word.uzbek

        binding.voice.setOnClickListener {
            speak(word.english.toString())
        }
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