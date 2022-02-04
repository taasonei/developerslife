package com.github.taasonei.developerslife

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class GifViewModel : ViewModel() {
    private val _gif = MutableLiveData<GifModel>()

    val gif: LiveData<GifModel>
        get() = _gif

    init {
        getGif()
    }

    private fun getGif() {
        viewModelScope.launch {
            try {
                val result = GifApi.retrofitService.getRandomGif()
                val uri = Uri.parse(result.gifURL)
                val httpsUri = "https://" + uri.authority + uri.path
                _gif.value = result.copy(
                    id = result.id,
                    description = result.description,
                    gifURL = httpsUri
                )
            } catch (e: Exception) {
            }
        }
    }

}
