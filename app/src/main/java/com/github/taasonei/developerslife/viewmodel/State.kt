package com.github.taasonei.developerslife.viewmodel

sealed class State<out T> {

    class Loading<out T> : State<T>()

    data class Success<out T>(val data: T) : State<T>()

    data class Error<out T>(val error: String) : State<T>()

}
