package com.ashin.flagfinder.di

import android.content.Context
import com.ashin.flagfinder.FlagApplication
import com.ashin.flagfinder.utils.PreferenceManger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context):   FlagApplication{
        return app as FlagApplication
    }
    @Provides
    @Singleton
    fun providesPreferenceMgr(myApplication: FlagApplication): PreferenceManger {
        return PreferenceManger(myApplication.applicationContext)
    }


}