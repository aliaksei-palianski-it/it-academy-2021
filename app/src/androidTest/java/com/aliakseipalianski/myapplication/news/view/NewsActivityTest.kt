package com.aliakseipalianski.myapplication.news.view


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aliakseipalianski.myapplication.R
import junit.framework.Assert
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NewsActivityTest {

    private fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(NewsActivity::class.java)

    @Test
    fun newsActivityTest() {
        onView(isRoot()).perform(waitFor(3000))

        val recyclerView = onView(
            allOf(
                withId(R.id.newsRecycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    3
                )
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(isRoot()).perform(waitFor(2000))
        val appCompatImageButton = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatImageButton.perform(click())

        onView(isRoot()).perform(waitFor(2000))
        val appCompatEditText = onView(
            allOf(
                withId(R.id.searchInput),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatEditText.perform(replaceText("home"), closeSoftKeyboard())

        onView(isRoot()).perform(waitFor(2000))
        val recyclerView2 = onView(
            allOf(
                withId(R.id.newsRecycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    3
                )
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))


        onView(isRoot()).perform(waitFor(2000))
        val appCompatImageButton2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatImageButton2.perform(click())

        onView(isRoot()).perform(waitFor(2000))
        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.searchInput), withText("home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatEditText2.perform(replaceText("apple"))

        onView(isRoot()).perform(waitFor(2000))
        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.searchInput), withText("apple"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatEditText3.perform(closeSoftKeyboard())

        onView(isRoot()).perform(waitFor(2000))
        val recyclerView3 = onView(
            allOf(
                withId(R.id.newsRecycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    3
                )
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))


        onView(isRoot()).perform(waitFor(2000))
        val appCompatImageButton3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatImageButton3.perform(click())

        onView(isRoot()).perform(waitFor(2000))
        val recyclerView4 = onView(
            allOf(
                withId(R.id.historyRecycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        onView(isRoot()).perform(waitFor(2000))
        val recyclerView5 = onView(
            allOf(
                withId(R.id.newsRecycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    3
                )
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        recyclerView5.perform(actionOnItemAtPosition<ViewHolder>(14, click()))

        onView(isRoot()).perform(waitFor(2000))
        val appCompatImageButton4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatImageButton4.perform(click())

        onView(isRoot()).perform(waitFor(2000))
        val appCompatImageView = onView(
            allOf(
                withId(R.id.clearHistory),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.container),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(2000))
        appCompatImageView.perform(click())

        onView(isRoot()).perform(waitFor(2000))
        mActivityTestRule.scenario.recreate()

        onView(isRoot()).perform(waitFor(2000))
        onView(withId(R.id.historyRecycler)).check { view, _ ->
            Assert.assertEquals(0, (view as? RecyclerView)?.adapter?.itemCount)
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
