package uz.gita.mDictionary.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.lesson21.R
import uz.gita.mDictionary.data.local.model.WordEntity
import uz.gita.lesson21.databinding.ItemWordBinding
import uz.gita.mDictionary.utils.spannable

class WordAdapter(private val language: Int) : RecyclerView.Adapter<WordAdapter.Holder>() {
    private lateinit var cursor: Cursor
    private lateinit var favListener:((Int, Int, Int) -> Unit)
    private lateinit var touchListener: ((WordEntity) -> Unit)
    private var query = ""

    fun setQuery(block: String) {
        query = block
    }

    fun submitCursor(block: Cursor) {
        cursor = block
    }

    fun setFavListener(block: ((Int, Int, Int) -> Unit)) {
        favListener= block
    }

    fun setTouchListener(block: (WordEntity) -> Unit) {
        touchListener = block
    }


    @SuppressLint("Range")
    inner class Holder(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.isFavourite.setOnClickListener {
                cursor.let {
                    it.moveToPosition(adapterPosition)
                    val result =if (it.getInt(it.getColumnIndex("is_favourite")) == 0) 1 else 0
                    favListener.invoke(adapterPosition, result, it.getInt(it.getColumnIndex("id")) )
                }
            }


            binding.root.setOnClickListener {
                cursor.let {
                    it.moveToPosition(adapterPosition)
                    touchListener.invoke(
                        WordEntity(
                        it.getInt(0),
                        it.getString(1),
                        it.getString(2),
                        it.getString(3),
                        it.getString(4),
                        it.getString(5),
                        it.getInt(6)
                    )
                    )
                }
            }
        }


        fun bind() {
            cursor.moveToPosition(adapterPosition)
            if (language == 0) {
                if (query.isEmpty()) {
                    binding.txtMain.text = cursor.getString(1)
                } else {
                    val s = cursor.getString(1)
                    binding.txtMain.text = s.spannable(query, itemView.context)
                }
                binding.txtTranslation.text = cursor.getString(4)
            }
            else{
                if (query.isEmpty()) {
                    binding.txtMain.text = cursor.getString(4)
                } else {
                    val s = cursor.getString(4)
                    binding.txtMain.text = s.spannable(query, itemView.context)
                }
                binding.txtTranslation.text = cursor.getString(1)

            }



            binding.txtType.text = cursor.getString(2)
            binding.isFavourite.setImageResource(if (cursor.getInt(6) == 0) R.drawable.ic_unsaved else R.drawable.ic_saved)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int {
        cursor.let { return cursor.count }

    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind()

}
