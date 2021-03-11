package spiral.bit.dev.simpledecidehelper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import spiral.bit.dev.simpledecidehelper.fragments.MyTasksFragment
import spiral.bit.dev.simpledecidehelper.fragments.ProsConsFragment
import spiral.bit.dev.simpledecidehelper.other.DecisionBottomSheet
import spiral.bit.dev.simpledecidehelper.other.EditBottomSheet
import spiral.bit.dev.simpledecidehelper.other.ProsConsBottomSheet

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @ActivityScoped
    @Provides
    fun provideDecisionBottomSheet() = DecisionBottomSheet()

    @ActivityScoped
    @Provides
    fun provideProsConsBottomSheet() = ProsConsBottomSheet()

    @ActivityScoped
    @Provides
    fun provideEditBottomSheet() = EditBottomSheet()

    @ActivityScoped
    @Provides
    fun provideMyTasksInstance() = MyTasksFragment()

    @ActivityScoped
    @Provides
    fun provideProsConsFragment() = ProsConsFragment()
}