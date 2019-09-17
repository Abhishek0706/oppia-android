package org.oppia.app.player.audio

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.oppia.app.databinding.AudioFragmentBinding
import org.oppia.app.fragment.FragmentScope
import org.oppia.app.viewmodel.ViewModelProvider
import javax.inject.Inject

/** The controller for [AudioFragment]. */
@FragmentScope
class AudioFragmentPresenter @Inject constructor(
  private val fragment: Fragment,
  private val viewModelProvider: ViewModelProvider<AudioViewModel>
) {

  fun handleCreateView(inflater: LayoutInflater, container: ViewGroup?): View? {
    val binding = AudioFragmentBinding.inflate(inflater, container, /* attachToRoot= */ false)
    binding.let {
      it.viewModel = getAudioViewModel()
      it.fragmentManager = getFragmentManager()
      it.lifecycleOwner = fragment
    }
    return binding.root
  }

  private fun getAudioViewModel(): AudioViewModel {
    return viewModelProvider.getForFragment(fragment, AudioViewModel::class.java)
  }

  private fun getFragmentManager(): FragmentManager {
    return fragment.childFragmentManager
  }

  fun languageSelected(language: String) {
    getAudioViewModel().setAudioLanguageCode(language)
  }
}
