package spiral.bit.dev.simpledecidehelper.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import spiral.bit.dev.simpledecidehelper.fragments.CompletedTasksFragment
import spiral.bit.dev.simpledecidehelper.fragments.MyTasksFragment
import spiral.bit.dev.simpledecidehelper.fragments.ProsConsFragment
import spiral.bit.dev.simpledecidehelper.other.*

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @ActivityScoped
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(MAIN_PREFS_NAME, MAIN_PREFS_MODE)
}