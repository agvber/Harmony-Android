package com.teampatch.core.data.di

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.teampatch.core.data.di.annotation.MemoryCardRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
internal object RecorderServiceViewModelModule {

    @MemoryCardRecorder
    @Provides
    fun provideMemoryCardRecorder(
        @ApplicationContext appContext: Context,
    ): MediaRecorder = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        MediaRecorder()
    } else {
        MediaRecorder(appContext)
    }
        .apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
}