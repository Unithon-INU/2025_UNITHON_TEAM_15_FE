package unithon.helpjob.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import unithon.helpjob.data.network.HelpJobApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideHelpJobApiService(retrofit: Retrofit): HelpJobApiService {
        return retrofit.create(HelpJobApiService::class.java)
    }
}