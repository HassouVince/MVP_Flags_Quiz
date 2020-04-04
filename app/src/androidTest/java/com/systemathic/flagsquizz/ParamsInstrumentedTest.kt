package com.systemathic.flagsquizz

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.systemathic.flagsquizz.ui.activity.params.ParamsActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ParamsInstrumentedTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule<ParamsActivity>(ParamsActivity::class.java)

    @Test
    fun setParams(){

        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, click())
        )
        onView(withId(R.id.edTextParams)).perform(
            ViewActions.typeText("NEW USER"),
            ViewActions.closeSoftKeyboard()
        )
        Thread.sleep(1000)
        onView(withId(R.id.imgChoicesValid)).perform(click())
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>
                (1, click())
        )
        onView(withId(R.id.edTextParams)).perform(
            ViewActions.typeText("32"),
            ViewActions.closeSoftKeyboard()
        )
        Thread.sleep(1000)
        onView(withId(R.id.imgChoicesValid)).perform(click())

        onView(withId(R.id.tvInfosPresentation)).check(matches(
                withText(Matchers.containsString("NEW USER"))
            )
        )
        onView(withId(R.id.tvInfosPresentation)).check(matches(
                withText(Matchers.containsString("32"))
            )
        )

        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>
                (8, click())
        )

        onView(withText(Matchers.containsString("OK"))).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.menu_item_settings)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.tvInfosPresentation)).check(
            matches(
                withText(Matchers.containsString("NEW USER"))
            )
        )
        onView(withId(R.id.tvInfosPresentation)).check(
            matches(
                withText(Matchers.containsString("32"))
            )
        )
    }
}