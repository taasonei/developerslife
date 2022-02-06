package com.github.taasonei.developerslife.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.taasonei.developerslife.network.GifApi
import com.github.taasonei.developerslife.network.GifApiModel
import kotlinx.coroutines.launch
import java.util.*

class GifViewModel : ViewModel() {

    companion object {
        private const val SCHEME = "https://"
        private const val ZERO = 0L
        private const val ONE = 1L
    }

    private val _count = MutableLiveData(ZERO)
    val count: LiveData<Long>
        get() = _count

    private var stackNext = Stack<GifApiModel>()
    private var stackPrevious = Stack<GifApiModel>()

    private val _gif = MutableLiveData<State<GifApiModel>>(State.Loading())
    val gif: LiveData<State<GifApiModel>>
        get() = _gif

    init {
        loadGif()
    }

    private fun loadGif() {
        viewModelScope.launch {
            try {
                val result = GifApi.retrofitService.getRandomGif()
                if (!result.gifURL.isNullOrEmpty()
                    && result.description != null
                ) {
                    val httpsUri = convertToHttps(result.gifURL)
                    val item = result.copy(
                        id = result.id,
                        description = result.description,
                        gifURL = httpsUri
                    )
                    _gif.value = State.Success(item)
                }
            } catch (e: Exception) {
                _gif.value = State.Error(e.toString())
            }
        }
    }

    fun loadNextGif() {
        if (stackNext.isNotEmpty()) {
            if (gif.value is State.Success<GifApiModel>) {
                stackPrevious.push((gif.value as State.Success<GifApiModel>).data)
            }
            _gif.value = State.Success(stackNext.pop())
            if (gif.value is State.Success<GifApiModel>) {
                _count.value = count.value?.plus(ONE)
            }
            Log.d("test", count.value.toString())
        } else {
            if (gif.value is State.Success<GifApiModel>) {
                stackPrevious.push((gif.value as State.Success<GifApiModel>).data)
            }
            loadGif()
            if (gif.value is State.Success<GifApiModel>) {
                _count.value = count.value?.plus(ONE)
            }
            Log.d("test", count.value.toString())

        }

    }

    fun loadPreviousGif() {
        if (stackPrevious.isNotEmpty()) {
            if (gif.value is State.Success<GifApiModel>) {
                stackNext.push((gif.value as State.Success<GifApiModel>).data)
            }
            _gif.value = State.Success(stackPrevious.pop())
            if (gif.value is State.Success<GifApiModel>) {
                _count.value = count.value?.minus(ONE)
            }
        }
    }

    fun reloadGif() {
        when (gif.value) {
            is State.Success<GifApiModel> -> {
                val currentValue = (gif.value as State.Success<GifApiModel>).data
                if (currentValue != null && currentValue.id != null) {
                    viewModelScope.launch {
                        try {
                            val result =
                                GifApi.retrofitService.getGifById(currentValue.id.toString())
                            if (!result.gifURL.isNullOrEmpty()
                                && result.description != null
                            ) {
                                val httpsUri = convertToHttps(result.gifURL)
                                val item = result.copy(
                                    id = result.id,
                                    description = result.description,
                                    gifURL = httpsUri
                                )
                                _gif.value = State.Success(item)
                            }
                        } catch (e: Exception) {
                            _gif.value = State.Error(e.toString())
                        }
                    }
                } else loadGif()
            }
            else -> loadGif()
        }
    }

    private fun convertToHttps(url: String)
            : String {
        val uri = Uri.parse(url)
        return SCHEME + uri.authority + uri.path
    }

}
