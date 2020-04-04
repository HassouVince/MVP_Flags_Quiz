package com.systemathic.flagsquizz


import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.view.View
import com.systemathic.flagsquizz.ui.activity.quizz.QuizzActivity
import com.systemathic.flagsquizz.ui.activity.quizz.QuizzPresenter
import kotlinx.android.synthetic.main.activity_quizz.*
import kotlinx.android.synthetic.main.fragment_buttons.*
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.runners.MethodSorters
import org.koin.standalone.KoinComponent

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
@LargeTest
class QuizzInstrumentedTest : KoinComponent {
    @get: Rule
    var mActivityTestRule = ActivityTestRule<QuizzActivity>(QuizzActivity::class.java)

    lateinit var context: Context
    lateinit var quizzPresenter: QuizzPresenter

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getTargetContext()
        quizzPresenter = mActivityTestRule.activity.getPresenter() as QuizzPresenter
    }

    fun prepare(){
        mActivityTestRule.finishActivity()
        mActivityTestRule.launchActivity(null)
        quizzPresenter = mActivityTestRule.activity.getPresenter() as QuizzPresenter
        quizzPresenter.getUser().name = "USER"
        quizzPresenter.getUser().age = 25
        quizzPresenter.getUser().score = 0
        quizzPresenter.getUser().idsQPUsed.clear()
        quizzPresenter.getUser().tempQuestionsChecked.clear()
        quizzPresenter.save()

        while (mActivityTestRule.activity.layoutFragQuizLoading.visibility == View.VISIBLE){
            Thread.sleep(   100)
        }

        Thread.sleep(1000)
    }

    @Test
    fun playGoodAnswer() {

        prepare()

        onView(withId(R.id.imagePresentation)).check(matches(isDisplayed()))
        onView(withText(quizzPresenter.getQuestion().answers[quizzPresenter.getQuestion().indexGoodAnswer]))
            .perform(click())

        onView(withId(R.id.tvInfosPresentation)).check(matches(withText(containsString(context.getString(R.string.good_answer)))))
        onView(withId(R.id.imagePresentation)).check(matches(isDisplayed()))
        onView(withId(R.id.layoutFragQuizButtons)).check(matches(isDisplayed()))
        onView(withId(R.id.tvUser)).check(matches(withText(("" + quizzPresenter.getUser().score + " pts"))))
        onView(withText(mActivityTestRule.activity.button1.text.toString()))
            .check(matches(not(anyOf(
                withText(mActivityTestRule.activity.button2.text.toString()),
                withText(mActivityTestRule.activity.button3.text.toString()),
                withText(mActivityTestRule.activity.button4.text.toString())))))
        checkButtonContainsAnswer()
    }

    @Test
    fun playWrongAnswer() {

        prepare()
        quizzPresenter.getUser().idsQPUsed.add("2|0")

        val index = if(quizzPresenter.getQuestion().indexGoodAnswer == 3) 2 else  quizzPresenter.getQuestion().indexGoodAnswer+1
        onView(withText(quizzPresenter.getQuestion().answers[index])).perform(click())
        onView(withId(R.id.tvInfosPresentation)).check(matches(withText(containsString(context.getString(R.string.good_answer_was)))))
        onView(withId(R.id.imagePresentation)).check(matches(isDisplayed()))
        onView(withId(R.id.layoutFragQuizButtons)).check(matches(isDisplayed()))
        onView(withText(mActivityTestRule.activity.button1.text.toString()))
            .check(matches(not(anyOf(
                withText(mActivityTestRule.activity.button2.text.toString()),
                withText(mActivityTestRule.activity.button3.text.toString()),
                withText(mActivityTestRule.activity.button4.text.toString())))))
        checkButtonContainsAnswer()
    }

    fun checkButtonContainsAnswer(){
        val indice = quizzPresenter.getQuestion().indexGoodAnswer
        if(indice == 0){
            onView(withText(mActivityTestRule.activity.button1.text.toString()))
                .check(matches(withText(quizzPresenter.getQuestion().answers[quizzPresenter.getQuestion().indexGoodAnswer])))
        }else if(indice == 1){
            onView(withText(mActivityTestRule.activity.button2.text.toString()))
                .check(matches(withText(quizzPresenter.getQuestion().answers[quizzPresenter.getQuestion().indexGoodAnswer])))
        }else if(indice == 2){
            onView(withText(mActivityTestRule.activity.button3.text.toString()))
                .check(matches(withText(quizzPresenter.getQuestion().answers[quizzPresenter.getQuestion().indexGoodAnswer])))
        }else if(indice == 3){
            onView(withText(mActivityTestRule.activity.button4.text.toString()))
                .check(matches(withText(quizzPresenter.getQuestion().answers[quizzPresenter.getQuestion().indexGoodAnswer])))
        }
    }


}
