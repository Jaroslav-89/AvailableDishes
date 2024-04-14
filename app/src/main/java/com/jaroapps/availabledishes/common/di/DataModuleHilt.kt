package com.jaroapps.availabledishes.common.di

import android.content.Context
import androidx.room.Room
import com.jaroapps.availabledishes.common.data.convertors.DishDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.ProductDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.TagDbConvertor
import com.jaroapps.availabledishes.common.data.db.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModuleHilt {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, "database.db").build()
}