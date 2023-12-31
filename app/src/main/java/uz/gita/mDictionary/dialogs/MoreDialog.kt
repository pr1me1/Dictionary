package uz.gita.mDictionary.dialogs

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import uz.gita.mDictionary.data.local.model.WordEntity
import uz.gita.lesson21.databinding.ItemWordMoreBinding
import java.util.Locale

class MoreDialog(private val word: WordEntity) : DialogFragment(), TextToSpeech.OnInitListener {
    private lateinit var binding: ItemWordMoreBinding
    private var tts: TextToSpeech? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = ItemWordMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tts = TextToSpeech(requireContext(),this)
        binding.txtMain.text = word.english
        binding.txtTranslation.text = word.uzbek
        binding.txtType.text = word.type
        binding.txtTranscript.text = word.transcript
        binding.txtCountable.text = word.countable
            binding.btnPlay.setOnClickListener {
            speakOut()
        }
    }


    override fun onResume() {
        super.onResume()
        dialog!!.window!!.attributes.gravity= Gravity.CENTER
        val layoutParams: WindowManager.LayoutParams = dialog!!.getWindow()!!.getAttributes()

        layoutParams.width = 950

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
                binding.btnPlay.isEnabled = true
            }

        } else {
        }
    }

    private fun speakOut() {
        val text = binding.txtMain.text
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}