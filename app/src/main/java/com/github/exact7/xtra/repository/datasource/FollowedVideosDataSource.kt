package com.github.exact7.xtra.repository.datasource

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.github.exact7.xtra.api.KrakenApi
import com.github.exact7.xtra.model.video.Video
import com.github.exact7.xtra.ui.videos.BroadcastType
import com.github.exact7.xtra.ui.videos.Sort
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.Executor

class FollowedVideosDataSource(
        userToken: String,
        private val broadcastTypes: BroadcastType,
        private val language: String?,
        private val sort: Sort,
        private val api: KrakenApi,
        networkExecutor: Executor,
        private val compositeDisposable: CompositeDisposable) : BasePositionalDataSource<Video>(networkExecutor) {

    private val userToken: String = "OAuth $userToken"

    override fun loadInitial(params: PositionalDataSource.LoadInitialParams, callback: PositionalDataSource.LoadInitialCallback<Video>) {
        super.loadInitial(params, callback)
        api.getFollowedVideos(userToken, broadcastTypes, language, sort, params.requestedLoadSize, 0)
                .subscribe({ callback.onSuccess(it.videos) }, { callback.onFailure(it, params)} )
                .addTo(compositeDisposable)
    }

    override fun loadRange(params: PositionalDataSource.LoadRangeParams, callback: PositionalDataSource.LoadRangeCallback<Video>) {
        super.loadRange(params, callback)
        api.getFollowedVideos(userToken, broadcastTypes, language, sort, params.loadSize, params.startPosition)
                .subscribe({ callback.onSuccess(it.videos) }, { callback.onFailure(it, params) })
                .addTo(compositeDisposable)
    }

    class Factory(
            private val userToken: String,
            private val broadcastTypes: BroadcastType,
            private val language: String?,
            private val sort: Sort,
            private val api: KrakenApi,
            private val networkExecutor: Executor,
            private val compositeDisposable: CompositeDisposable) : BaseDataSourceFactory<Int, Video, FollowedVideosDataSource>() {

        override fun create(): DataSource<Int, Video> =
            FollowedVideosDataSource(userToken, broadcastTypes, language, sort, api, networkExecutor, compositeDisposable).also(sourceLiveData::postValue)
    }
}