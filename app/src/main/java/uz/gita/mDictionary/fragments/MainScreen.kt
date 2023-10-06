package uz.gita.mDictionary.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import uz.gita.lesson21.R
import uz.gita.mDictionary.adapter.WordAdapter
import uz.gita.mDictionary.data.local.database.WordDatabase
import uz.gita.mDictionary.data.local.shared.Shared
import uz.gita.lesson21.databinding.FragmentMainBinding
import uz.gita.mDictionary.dialogs.MoreDialog

class MainScreen : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter : WordAdapter
    private val worDao by lazy { WordDatabase.getInstance().getWordDao() }
    private lateinit var handler: Handler
    private var wordlist: RecyclerView? = null
    private var searchView: SearchView? = null
    private lateinit var empty: LottieAnimationView
    private val shared by lazy { Shared.getShared() }
    private lateinit var dialog : MoreDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
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





    private fun loadAdapters() {
        adapter = WordAdapter(shared.getLanguage())
        wordlist?.adapter = adapter
        Log.d("MS", "loadAdapters")
        adapter.submitCursor(worDao.getAllWords())
    }


    private fun setEvents() {
        adapter.setFavListener { position, favRes, id ->
            worDao.updateFav(id, favRes)
            adapter.submitCursor(worDao.getAllWords())
            adapter.notifyItemChanged(position)
        }

        adapter.setTouchListener {
            dialog = MoreDialog(it)
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
                    when (shared.getLanguage()) {
                    0 -> {
                        if (query.trim().length > 0) {
                            if (worDao.searchViaEnglish(query).count > 0) {
                                adapter.submitCursor(worDao.searchViaEnglish(query))
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
                            adapter.submitCursor(worDao.getAllWords())
                            adapter.setQuery(it.trim())
                            adapter.notifyDataSetChanged()
                        }
                    }
                        else -> {
                            if (query.trim().length > 0) {
                                if (worDao.searchViaUzbek(query).count > 0) {
                                    adapter.submitCursor(worDao.searchViaUzbek(query))
                                    adapter.setQuery(it.trim())
                                    empty.visibility = View.GONE
                                    wordlist?.visibility = View.VISIBLE
                                    adapter.notifyDataSetChanged()
                                    adapter.setFavListener { position, favRes, id ->
                                        worDao.updateFav(id, favRes)
                                        adapter.submitCursor(worDao.searchViaUzbek(it))
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
                                adapter.submitCursor(worDao.getAllWords())
                                adapter.setQuery(it.trim())
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    newText?.let {
                        when (shared.getLanguage()) {
                            0 ->{
                                if (it.trim().length > 0) {
                                    if (worDao.searchViaEnglish(newText).count > 0) {
                                        adapter.submitCursor(worDao.searchViaEnglish(newText))
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
                                    adapter.submitCursor(worDao.getAllWords())
                                    adapter.setQuery(it.trim())
                                    empty.visibility = View.INVISIBLE
                                    adapter.notifyDataSetChanged()
                                }
                            }
                            else -> {
                                if (it.trim().length > 0) {
                                    if (worDao.searchViaUzbek(newText).count > 0) {
                                        adapter.submitCursor(worDao.searchViaUzbek(newText))
                                        adapter.setQuery(it.trim())
                                        empty.visibility = View.GONE
                                        wordlist?.visibility = View.VISIBLE
                                        adapter.notifyDataSetChanged()
                                        adapter.setFavListener { position, favRes, id ->
                                            worDao.updateFav(id, favRes)
                                            adapter.submitCursor(worDao.searchViaUzbek(it))
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
                                    adapter.submitCursor(worDao.getAllWords())
                                    adapter.setQuery(it.trim())
                                    empty.visibility = View.INVISIBLE
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }



                    }

                }, 200)
                return true
            }

        })

    }


    override fun onPause() {
        super.onPause()
        if(::dialog.isInitialized){
            dialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnChanger.setOnClickListener {
            shared.setLanguage(if (shared.getLanguage() == 0) 1 else 0)
            loadAdapters()
            setEvents()
        }
    }



}