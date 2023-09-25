package uz.gita.lesson21.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import uz.gita.lesson21.R
import uz.gita.lesson21.adapter.WordAdapter
import uz.gita.lesson21.data.local.database.WordDatabase
import uz.gita.lesson21.databinding.FragmentMainBinding
import uz.gita.lesson21.dialogs.MoreDialog

class SavedScreen : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter : WordAdapter
    private val worDao by lazy { WordDatabase.getInstance().getWordDao() }
    private lateinit var handler: Handler
    private lateinit var context: Context
    private var wordlist: RecyclerView? = null
    private var searchView: SearchView? = null
    private lateinit var empty: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        if (container != null) {
            context = container.context
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        wordlist = view.findViewById(R.id.word_list)
        searchView = view.findViewById(R.id.search_view)
        empty = binding.empty
        loadAdapters()
        setEvents()
        setSearch()

    }

    private fun setPlaceHolderVisibility(boolean: Boolean) {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        loadAdapters()
//        setEvents()
//        setSearch()

    }



    private fun loadAdapters() {
        adapter = WordAdapter(0)
        wordlist?.adapter = adapter
        if (worDao.getSavedWords().count > 0)
        adapter.submitCursor(worDao.getSavedWords())
        else {
            adapter.submitCursor(worDao.getSavedWords())
            wordlist?.visibility = GONE
            empty.visibility = View.VISIBLE
            empty.playAnimation()
        }
    }


    private fun setEvents() {
        adapter.setFavListener { position, favRes, id ->
            worDao.updateFav(id, favRes)
            if (worDao.getSavedWords().count > 0) {
                adapter.submitCursor(worDao.getSavedWords())
                adapter.notifyItemRemoved(position)
            } else {
                adapter.submitCursor(worDao.getSavedWords())
                adapter.notifyItemRemoved(position)
                adapter.submitCursor(worDao.getSavedWords())
                wordlist?.visibility = GONE
                empty.visibility = View.VISIBLE
                empty.playAnimation()
            }
        }

        adapter.setTouchListener {
            val dialog = MoreDialog(it)
            dialog.show(parentFragmentManager, null)
        }
    }

    private fun setSearch() {
        handler = Handler(Looper.getMainLooper())
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                query?.let {
                    if (query.trim().length > 0) {
                        if (worDao.searchViaEnglish(query).count > 0) {
                            adapter.submitCursor(worDao.searchViaEnglishSaved(query))
                            adapter.setQuery(it.trim())
                            empty.visibility = View.GONE
                            wordlist?.visibility = View.VISIBLE
                            adapter.notifyDataSetChanged()
                            adapter.setFavListener { position, favRes, id ->
                                worDao.updateFav(id, favRes)
                                adapter.submitCursor(worDao.searchViaEnglish(it))
                                adapter.notifyItemChanged(position)
                            }

                            adapter.setTouchListener {
                                val dialog = MoreDialog(it)
                                dialog.show(parentFragmentManager, null)
                            }
                        } else {
                            wordlist?.visibility = View.GONE
                            empty.visibility = View.VISIBLE
                            empty.playAnimation()
                        }
                    } else {
                        wordlist?.visibility = View.VISIBLE
                        adapter.submitCursor(worDao.getSavedWords())
                        adapter.setQuery(it.trim())
                        adapter.notifyDataSetChanged()
                    }




                }
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    newText?.let {
                        if (it.trim().length > 0) {
                            if (worDao.searchViaEnglish(newText).count > 0) {
                                adapter.submitCursor(worDao.searchViaEnglishSaved(newText))
                                adapter.setQuery(it.trim())
                                empty.visibility = View.GONE
                                wordlist?.visibility = View.VISIBLE
                                adapter.notifyDataSetChanged()
                                adapter.setFavListener { position, favRes, id ->
                                    worDao.updateFav(id, favRes)
                                    adapter.submitCursor(worDao.searchViaEnglish(it))
                                    adapter.notifyItemChanged(position)
                                }

                                adapter.setTouchListener {
                                    val dialog = MoreDialog(it)
                                    dialog.show(parentFragmentManager, null)
                                }
                            } else {
                                wordlist?.visibility = View.GONE
                                empty.visibility = View.VISIBLE
                                empty.playAnimation()
                            }
                        } else {
                            wordlist?.visibility = View.VISIBLE
                            adapter.submitCursor(worDao.getSavedWords())
                            adapter.setQuery(it.trim())
                            adapter.notifyDataSetChanged()
                        }


                    }

                }, 200)
                return true
            }

        })
    }

}