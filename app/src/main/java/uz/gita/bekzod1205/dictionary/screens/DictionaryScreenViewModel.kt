package uz.gita.bekzod1205.dictionary.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.bekzod1205.dictionary.db.entity.WordEntity

class DictionaryScreenViewModel : ViewModel() {
    val scrollPosition = MutableLiveData<Pair<Int, Int>>()
    val wordList = MutableLiveData<List<WordEntity>>(emptyList())
    val searchQuery = MutableLiveData<String>()

}