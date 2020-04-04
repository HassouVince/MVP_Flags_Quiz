package com.systemathic.flagsquizz.di


import android.content.Context
import android.content.SharedPreferences
import com.systemathic.flagsquizz.app_repository.UserRepositoryImp
import com.systemathic.flagsquizz.model.question.Question

import com.systemathic.flagsquizz.model.question_pack.QuestionsPack
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.model.question.QuestionManagerImp
import com.systemathic.flagsquizz.model.question_pack.QuestionPackManagerImp
import com.systemathic.flagsquizz.ui.activity.form.FormContract
import com.systemathic.flagsquizz.ui.activity.form.FormPresenter
import com.systemathic.flagsquizz.ui.activity.main.MainContract
import com.systemathic.flagsquizz.ui.activity.main.MainPresenter
import com.systemathic.flagsquizz.ui.activity.params.ParamsContract
import com.systemathic.flagsquizz.ui.activity.params.ParamsPresenter
import com.systemathic.flagsquizz.ui.activity.quizz.QuizzContract
import com.systemathic.flagsquizz.ui.activity.quizz.QuizzPresenter
import com.systemathic.flagsquizz.ui.fragments.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val applicationModule = module(override = true) {

    single {UserRepositoryImp}
    single {QuestionManagerImp}
    single {QuestionPackManagerImp}

    factory<MainContract.Presenter> { (view: MainContract.View) -> MainPresenter(view) }
    factory<ParamsContract.Presenter> { (view: ParamsContract.View) -> ParamsPresenter(view) }
    factory<QuizzContract.Presenter> { (view: QuizzContract.View) -> QuizzPresenter(view) }
    factory<FormContract.Presenter> { (view: FormContract.View) -> FormPresenter(view) }

    factory {User()}
    factory {QuestionsPack()}
    factory {Question()}

    factory<DialFragment> { DialFragment().newInstance() }
    factory<EditTextFragment> { EditTextFragment().newInstance() }
    factory<ParamsRvFragment> { ParamsRvFragment().newInstance() }
    factory<FormFragment> { FormFragment().newInstance() }
    factory<ChoicesFragment> { ChoicesFragment().newInstance() }
    factory<PresentationFragment> { PresentationFragment().newInstance() }
    factory<UserFragment> { UserFragment().newInstance() }
    factory<ClickableImageFragment> { ClickableImageFragment().newInstance() }
    factory<LoadingFragment> { LoadingFragment().newInstance() }
    factory<MainButtonsFragment> { MainButtonsFragment().newInstance() }

    single<SharedPreferences> { androidContext().getSharedPreferences("Preferences_Quizz_Kotlin", Context.MODE_PRIVATE) }
}



