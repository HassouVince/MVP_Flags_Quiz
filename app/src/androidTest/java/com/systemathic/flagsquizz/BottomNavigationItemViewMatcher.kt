package com.systemathic.flagsquizz

import android.support.design.internal.BottomNavigationItemView
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View

object BottomNavigationItemViewMatcher {

    fun withIsChecked(isChecked: Boolean): BoundedMatcher<View, BottomNavigationItemView> {
        return object :
            BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {
            override fun describeTo(description: org.hamcrest.Description?) {
                if (triedMatching) {
                    description!!.appendText(" is checked: $isChecked")
                    description.appendText(" / Result: " + (!isChecked).toString())
                }
            }

            private var triedMatching: Boolean = false

            override fun matchesSafely(item: BottomNavigationItemView): Boolean {
                triedMatching = true
                return item.itemData.isChecked == isChecked
            }
        }
    }

    fun withTitle(titleTested: String): BoundedMatcher<View, BottomNavigationItemView> {
        return object :
            BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {
            override fun describeTo(description: org.hamcrest.Description?) {
                if (triedMatching) {
                    description!!.appendText("with title: $titleTested")
                    description.appendText(" Result: " + title.toString())
                }
            }

            private var triedMatching: Boolean = false
            private var title: String? = null

            override fun matchesSafely(item: BottomNavigationItemView): Boolean {
                this.triedMatching = true
                this.title = item.itemData.title.toString()
                return title == titleTested
            }
        }
    }
}