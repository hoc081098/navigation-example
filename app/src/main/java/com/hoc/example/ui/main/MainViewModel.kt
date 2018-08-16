package com.hoc.example.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.paging.PositionalDataSource
import android.util.Log
import com.hoc.example.SingleLiveEvent
import com.hoc.example.data.ApiService
import com.hoc.example.data.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoDataSource(
    private val apiService: ApiService,
    private val onFailure: (CharSequence) -> Unit
) : PositionalDataSource<Photo>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Photo>) {
        Log.d("MainViewModel", "loadSize=${params.loadSize} startPosition=${params.startPosition}")

        apiService.getPhotos(params.startPosition, params.loadSize)
            .enqueue(object : Callback<List<Photo>> {
                override fun onFailure(call: Call<List<Photo>>?, t: Throwable?) {
                    onFailure(t?.message ?: "Unknown error occurred")
                }

                override fun onResponse(
                    call: Call<List<Photo>>?,
                    response: Response<List<Photo>>
                ) {
                    if (response.isSuccessful) {
                        val body = (response.body()
                            ?: throw NullPointerException("Body is null"))
                        callback.onResult(body)
                    }
                }
            })
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Photo>) {
        Log.d(
            "MainViewModel",
            "size=${params.pageSize} placeholdersEnabled=${params.placeholdersEnabled}" +
                " requestedLoadSize=${params.requestedLoadSize} requestedStartPosition=${params.requestedStartPosition}"
        )

        apiService.getPhotos(params.requestedStartPosition, params.requestedLoadSize)
            .enqueue(object : Callback<List<Photo>> {
                override fun onFailure(call: Call<List<Photo>>?, t: Throwable?) {
                    onFailure(t?.message ?: "Unknown error occurred")
                }

                override fun onResponse(
                    call: Call<List<Photo>>?,
                    response: Response<List<Photo>>
                ) {
                    if (response.isSuccessful) {
                        val body = (response.body()
                            ?: throw NullPointerException("Body is null"))
                        callback.onResult(body, 0, body.size)
                    }
                }
            })
    }
}

class PhotoDataSourceFactory(
    private val apiService: ApiService,
    private val onFailure: (CharSequence) -> Unit
) : DataSource.Factory<Int, Photo>() {
    val photoDataSource = MutableLiveData<PhotoDataSource>()
    override fun create() = PhotoDataSource(apiService, onFailure)
        .also(photoDataSource::postValue)
}

class MainViewModel(apiService: ApiService) : ViewModel() {
    private val factory = PhotoDataSourceFactory(apiService, ::onFailure)
    private val isLoadingMutable = MutableLiveData<Boolean>().apply { value = false }
    private val errorMessageMutable = SingleLiveEvent<CharSequence>()

    val listPhotos: LiveData<PagedList<Photo>>
    val isLoading: LiveData<Boolean> = isLoadingMutable
    val errorMessage: LiveData<CharSequence> = errorMessageMutable

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()
        listPhotos = LivePagedListBuilder(factory, config)
            .build()
    }

    fun refreshData() {
        factory.photoDataSource.value?.run {
            addInvalidatedCallback { isLoadingMutable.postValue(false) }
            invalidate()
        }
    }

    private fun onFailure(charSequence: CharSequence) {
        errorMessageMutable.value = charSequence
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}
