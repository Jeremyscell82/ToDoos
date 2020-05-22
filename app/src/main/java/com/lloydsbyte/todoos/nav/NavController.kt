package com.lloydsbyte.todoos.nav

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.lloydsbyte.todoos.*

class NavController(val sfm: FragmentTransaction) {

    enum class NavAction {
        LOGIN,
        HOME,
        SETTINGS
    }

    fun launchFragment(fragId: NavAction) {
        var fragmentToLaunch: Fragment = PlaceholderFragment()
        var animationKey = FragmentAnimations.ANIMATION_KEY.NO_ANIM
        var addToStack = false

        when (fragId) {
            NavAction.LOGIN -> {
                fragmentToLaunch = LoginFragment()
                animationKey = FragmentAnimations.ANIMATION_KEY.FADE_IN
            }
            NavAction.HOME -> {
                fragmentToLaunch = HomeFragment()
                animationKey = FragmentAnimations.ANIMATION_KEY.FADE_IN
                addToStack = true
            }
            NavAction.SETTINGS -> {
                fragmentToLaunch = SettingsFragment()
                animationKey = FragmentAnimations.ANIMATION_KEY.NO_ANIM
                addToStack = true
            }
        }

        FragmentAnimations()
            .addCustomAnimations(ft = sfm, key = animationKey)

        sfm.replace(R.id.main_container, fragmentToLaunch)
        if (addToStack) sfm.addToBackStack("")
        sfm.commit()
    }

//    fun launchFragmentWithTransition(fragId: NavAction, card: View, title: View){
//        val fragment = when(fragId){
//            NavAction.CRUD_PROTOMODEL -> {
//                CrudProtoModelFragment()
//            }
//            NavAction.CRUD_USER -> {
//                CrudUserControlFragment()
//            }
//            else -> {
//                // DO NOTHING
//                null
//            }
//        }
//
//        if (fragment != null){
//            sfm.addSharedElement(card, card.transitionName)
//                .addSharedElement(title, title.transitionName)
//                .addToBackStack("")
//                .replace(R.id.main_container, fragment, fragId.toString())
//                .commit()
//        }
//    }
}