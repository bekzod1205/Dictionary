package uz.gita.bekzod1205.dictionary.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.bekzod1205.dictionary.R
import uz.gita.bekzod1205.dictionary.databinding.ItemWordBinding
import uz.gita.bekzod1205.dictionary.db.entity.WordEntity
import uz.gita.bekzod1205.dictionary.utils.createSpannable


class WordAdapter : RecyclerView.Adapter<WordAdapter.WordVH>() {
    private var cursor: Cursor? = null
    private var onItemClickListener: ((WordEntity) -> Unit)? = null
    private var onBookmarkClickListener: ((position: Int, WordEntity) -> Unit)? = null
    private var onVoiceClickListener: ((WordEntity) -> Unit)? = null

    inner class WordVH(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.bookmark.setOnClickListener {
                cursor?.moveToPosition(adapterPosition)
                val id = cursor?.getInt(cursor?.getColumnIndex("id") ?: 0) ?: -1
                val english = cursor?.getString(cursor?.getColumnIndex("english") ?: 1) ?: ""
                val wordtype = cursor?.getString(cursor?.getColumnIndex("type") ?: 2) ?: ""
                val transcript = cursor?.getString(cursor?.getColumnIndex("transcript") ?: 3) ?: ""
                val uzbek = cursor?.getString(cursor?.getColumnIndex("uzbek") ?: 4) ?: ""
                val countable = cursor?.getString(cursor?.getColumnIndex("countable") ?: 5) ?: ""
                val favourite = cursor?.getInt(cursor?.getColumnIndex("is_favourite") ?: 4) ?: -1
                val item =
                    WordEntity(id = id, english, wordtype, transcript, uzbek, countable, favourite)
                onBookmarkClickListener?.invoke(adapterPosition, item)
            }
        }

        fun bind(position: Int) {
            val item = getItem(position)
            binding.tvWord.text = item.english
            if (text.isNotEmpty()) {
                binding.tvWord.text = item.english.toString().createSpannable(text)
            }

            binding.tvTranslation.text = item.uzbek
            if (item.isFavourite == 1) {
                binding.bookmark.setImageResource(R.drawable.bookmark_filled)
            } else {
                binding.bookmark.setImageResource(R.drawable.bookmark)
            }
            binding.voice.setOnClickListener { onVoiceClickListener?.invoke(item) }
            binding.card.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }

    fun setOnItemClickListener(listener: (WordEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnVoiceClickListener(listener: (WordEntity) -> Unit) {
        onVoiceClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: (position: Int, WordEntity) -> Unit) {
        onBookmarkClickListener = listener
    }

    fun submitList(mCursor: Cursor) {
        cursor = mCursor
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): WordEntity {
        cursor?.moveToPosition(position)
        val id = cursor?.getInt(cursor?.getColumnIndex("id") ?: 0) ?: -1
        val english = cursor?.getString(cursor?.getColumnIndex("english") ?: 1) ?: ""
        val wordtype = cursor?.getString(cursor?.getColumnIndex("type") ?: 2) ?: ""
        val transcript = cursor?.getString(cursor?.getColumnIndex("transcript") ?: 3) ?: ""
        val uzbek = cursor?.getString(cursor?.getColumnIndex("uzbek") ?: 4) ?: ""
        val countable = cursor?.getString(cursor?.getColumnIndex("countable") ?: 5) ?: ""
        val favourite = cursor?.getInt(cursor?.getColumnIndex("is_favourite") ?: 4) ?: -1



        return WordEntity(id = id, english, wordtype, transcript, uzbek, countable, favourite)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordVH(ItemWordBinding.bind(view))
    }

    override fun onBindViewHolder(
        holder: WordVH,
        position: Int
    ) = holder.bind(position)

    override fun getItemCount(): Int = cursor?.count ?: 0

    var text = ""

    fun setSpannableText(text: String) {
        this.text = text
    }
}
