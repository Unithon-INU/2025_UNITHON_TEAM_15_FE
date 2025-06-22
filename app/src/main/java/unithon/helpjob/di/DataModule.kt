package unithon.helpjob.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.DefaultAuthRepository
import unithon.helpjob.data.repository.DefaultDocumentRepository
import unithon.helpjob.data.repository.DefaultEmploymentCheckRepository
import unithon.helpjob.data.repository.DocumentRepository
import unithon.helpjob.data.repository.EmploymentCheckRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepository(
        defaultAuthRepository: DefaultAuthRepository
    ): AuthRepository

    @Singleton
    @Binds
    abstract fun bindEmploymentCheckRepository(
        defaultEmploymentCheckRepository: DefaultEmploymentCheckRepository
    ): EmploymentCheckRepository

    @Singleton
    @Binds
    abstract fun bindDocumentRepository(
        defaultDocumentRepository: DefaultDocumentRepository
    ): DocumentRepository
}