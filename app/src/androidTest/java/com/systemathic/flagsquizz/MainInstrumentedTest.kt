package com.systemathic.flagsquizz

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.ui.activity.main.MainActivity
import com.systemathic.flagsquizz.ui.activity.main.MainPresenter
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainInstrumentedTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    lateinit var user : User
    lateinit var context: Context
    lateinit var mainPresenter: MainPresenter

    @Before
    fun setUp(){
        context = InstrumentationRegistry.getTargetContext()
        mainPresenter = (mActivityTestRule.activity.getPresenter() as MainPresenter)
        user = mainPresenter.getUser()

    }

    @Test
    fun checkButtons(){
        onView(withId(R.id.button1)).check(matches(withText(equalTo(context.getString(R.string.btn_play)))))
        onView(withId(R.id.button2)).check(matches(withText(equalTo(context.getString(R.string.btn_score)))))
    }

    @Test
    fun checkBottomNav(){
        onView(withId(R.id.menu_item_quit)).check(matches(BottomNavigationItemViewMatcher.withTitle(context.getString(R.string.bottom_nav_3))))
        onView(withId(R.id.menu_item_settings)).check(matches(BottomNavigationItemViewMatcher.withTitle(context.getString(R.string.bottom_nav_2))))
        onView(withId(R.id.menu_item_contact)).check(matches(BottomNavigationItemViewMatcher.withTitle(context.getString(R.string.bottom_nav_1))))

        onView(withId(R.id.menu_item_quit)).check(matches(BottomNavigationItemViewMatcher.withIsChecked(true)))
        onView(withId(R.id.menu_item_settings)).check(matches(BottomNavigationItemViewMatcher.withIsChecked(false)))
        onView(withId(R.id.menu_item_contact)).check(matches(BottomNavigationItemViewMatcher.withIsChecked(false)))
    }

    @Test
    fun getScore(){
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.btnDial1)).check(matches(isDisplayed()))
        onView(withId(R.id.textTitleDial)).check(matches(withText(containsString("Score"))))
        onView(withId(R.id.btnDial1)).perform(click())
        onView(withId(R.id.btnDial1)).check(matches(not(isDisplayed())))
        user.score = 3
        mainPresenter.save()
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.textDial)).check(matches(withText(containsString("Total"))))
    }
}