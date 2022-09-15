package com.example.cashierapp.di

import com.example.cashierapp.utils.UserUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SingletonObjects {

    @Singleton
    @Provides
    fun provideUserUtil() = UserUtil()
}