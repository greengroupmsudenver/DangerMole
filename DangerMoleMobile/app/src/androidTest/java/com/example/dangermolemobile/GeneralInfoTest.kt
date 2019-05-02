package com.example.dangermolemobile


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GeneralInfoTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(CameraActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Test
    fun generalInfoTest() {
        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view_camera),
                                        0)),
                        3),
                        isDisplayed()))
        navigationMenuItemView.perform(click())

        val textView = onView(
                allOf(withId(R.id.genInfo), withText("Melanocytic Nevi are a subcategory of melanocytic tumors containing nevus cells. While the majority of moles are benign, it is important to regularly check your moles for changes in shape, texture, symmetry and color as these are signs of melanoma. Danger Mole uses these metrics to gauge the overall health of the mole by analyzing it against a trained neural network using TensorFlow’s framework. Further resources on melanoma can be found at the official dot gov site:"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        1),
                                0),
                        isDisplayed()))
        textView.check(matches(withText("Melanocytic Nevi are a subcategory of melanocytic tumors containing nevus cells. While the majority of moles are benign, it is important to regularly check your moles for changes in shape, texture, symmetry and color as these are signs of melanoma. Danger Mole uses these metrics to gauge the overall health of the mole by analyzing it against a trained neural network using TensorFlow’s framework. Further resources on melanoma can be found at the official dot gov site:")))

        val textView2 = onView(
                allOf(withId(R.id.govLink), withText(" Official resource on skin cancer "),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                                        1),
                                1),
                        isDisplayed()))
        textView2.check(matches(withText(" Official resource on skin cancer ")))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

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