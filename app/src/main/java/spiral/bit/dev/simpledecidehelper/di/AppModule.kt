package spiral.bit.dev.simpledecidehelper.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import spiral.bit.dev.simpledecidehelper.adapters.DecisionAdapter
import spiral.bit.dev.simpledecidehelper.adapters.ProsConsAdapter
import spiral.bit.dev.simpledecidehelper.data.DecisionsDatabase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDecisionsDb(@ApplicationContext context: Context): DecisionsDatabase =
        Room.databaseBuilder(
            context,
            DecisionsDatabase::class.java,
            "decisions_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDecisionsDao(db: DecisionsDatabase) = db.getDecisionsDao()

    @Singleton
    @Provides
    fun provideProsConsDao(db: DecisionsDatabase) = db.getProsConsDao()

    @Singleton
    @Provides
    fun provideDecisionAdapter() = DecisionAdapter()

    @Singleton
    @Provides
    fun provideProsConsAdapter() = ProsConsAdapter()
}