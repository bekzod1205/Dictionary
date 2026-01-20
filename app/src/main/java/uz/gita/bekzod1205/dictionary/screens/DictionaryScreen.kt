package uz.gita.bekzod1205.dictionary.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.bekzod1205.dictionary.R
import uz.gita.bekzod1205.dictionary.adapter.WordAdapter
import uz.gita.bekzod1205.dictionary.databinding.FragmentDictionaryScreenBinding
import uz.gita.bekzod1205.dictionary.db.AppDatabase
import java.util.Locale
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class DictionaryScreen : Fragment(R.layout.fragment_dictionary_screen),
    TextToSpeech.OnInitListener {
    private var _binding: FragmentDictionaryScreenBinding? = null
    private val binding get() = _binding!!
    private val database = AppDatabase.getInstance()
    private lateinit var adapter: WordAdapter
    private lateinit var tts: TextToSpeech
    private var isTTSReady = false
    private val viewModel: DictionaryScreenViewModel by activityViewModels<DictionaryScreenViewModel>()
    private lateinit var layoutManager: LinearLayoutManager
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null


    @OptIn(ExperimentalUuidApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDictionaryScreenBinding.bind(view)
        tts = TextToSpeech(requireContext(), this)
        loadAdapter()
//        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query == null) { /*load all list*/
//                } else {
//                    runnable?.let { handler.removeCallbacks(it) }
//                    search(query)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText == null) { /*load all list*/
//                } else {
//                    runnable?.let { handler.removeCallbacks(it) }
//                    runnable = Runnable {
//                        search(newText)
//                    }
//                    handler.postDelayed(runnable!!, 1_000)
//                }
//                return true
//            }
//
//        })
        binding.searchEditText.addTextChangedListener { it ->
            if (it == null) { /*load all list*/
            } else {
                runnable?.let { handler.removeCallbacks(it) }
                runnable = Runnable {
                    search(it.toString())
                }
                handler.postDelayed(runnable!!, 200)
            }
        }
        binding.searchEditText.setOnEditorActionListener{ _, actionID, _ ->
            if (actionID == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString().trim()
                search(query)
                true
            } else {
                false
            }
        }


        viewModel.scrollPosition.value?.let { (index, offset) ->
            binding.dictionaryRv.post {
                layoutManager.scrollToPositionWithOffset(index, offset)
            }
        }


    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            isTTSReady =
                result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun loadAdapter() {
        adapter = WordAdapter()
        var items = database.getDictionaryDao().getAllWords()
        adapter.submitList(items)
        adapter.setOnBookmarkClickListener { position, word ->
            val id = word.id
            val isFavourite = if (word.isFavourite == 1) 0 else 1
            database.getDictionaryDao().update(id, isFavourite)
            adapter.notifyItemChanged(position)
            if (binding.searchEditText.text!!.isEmpty()) {
                items = database.getDictionaryDao().getAllWords()
                adapter.submitList(items)
            } else {
                val query = binding.searchEditText.text.toString()
                items = database.getDictionaryDao().getWordsByQuery(query)
                adapter.submitList(items)
            }
        }
        adapter.setOnItemClickListener {
            val screen = DetailedScreen()
            screen.arguments = bundleOf("word" to it)

            parentFragmentManager.beginTransaction().replace(R.id.main, screen)
                .addToBackStack(Uuid.random().toHexString()).commit()
        }
        adapter.setOnVoiceClickListener {
            speak(it.english.toString())
        }
        binding.dictionaryRv.adapter = adapter
        layoutManager = LinearLayoutManager(requireContext())
        binding.dictionaryRv.layoutManager = layoutManager
    }

    private fun speak(text: String) {
        if (isTTSReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    private fun search(st: String) {
        val words = database.getDictionaryDao().getWordsByQuery(st)
        adapter.submitList(words)
        adapter.setSpannableText(st)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tts.stop()
        tts.shutdown()
    }

    override fun onResume() {
        super.onResume()
        viewModel.scrollPosition.value?.let { (index, offset) ->
            layoutManager.scrollToPositionWithOffset(index, offset)
        }
        viewModel.searchQuery.value?.let {
            binding.searchEditText.setText(it)
            search(it)
        }
    }

    override fun onPause() {
        super.onPause()
        val index = layoutManager.findFirstVisibleItemPosition()
        val offset = binding.dictionaryRv.getChildAt(0)?.top ?: 0
        viewModel.scrollPosition.value = Pair(index, offset)
        viewModel.searchQuery.value = binding.searchEditText.text.toString()
    }
}

