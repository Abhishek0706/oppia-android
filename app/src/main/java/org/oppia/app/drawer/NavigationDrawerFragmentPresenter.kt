package org.oppia.app.drawer

import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import org.oppia.app.R
import org.oppia.app.fragment.FragmentScope
import org.oppia.app.help.HelpActivity
import org.oppia.app.home.HomeActivity
import org.oppia.app.profile.ProfileActivity
import javax.inject.Inject

/** The presenter for [NavigationDrawerFragment]. */
@FragmentScope
class NavigationDrawerFragmentPresenter @Inject constructor(
  private val fragment: Fragment
) : NavigationView.OnNavigationItemSelectedListener {
  private lateinit var navView: NavigationView
  private lateinit var drawerToggle: ActionBarDrawerToggle
  private lateinit var drawerLayout: DrawerLayout
  private var previousMenuItemId: Int? = null

  fun handleCreateView(inflater: LayoutInflater, container: ViewGroup?): View? {
    val view: View? = inflater.inflate(R.layout.fragment_drawer, container, false)
    navView = view!!.findViewById(R.id.fragment_drawer_nav_view)
    navView.setNavigationItemSelectedListener(this)
    fragment.setHasOptionsMenu(true)
    return view
  }

  private fun openActivityByMenuItemId(menuItemId: Int) {
    if (previousMenuItemId != menuItemId && menuItemId != 0) {
      val intent = when (NavigationDrawerItem.valueFromNavId(menuItemId)) {
        NavigationDrawerItem.HOME -> {
          Intent(fragment.activity, HomeActivity::class.java)
        }
        NavigationDrawerItem.HELP -> {
          Intent(fragment.activity, HelpActivity::class.java)
        }
        NavigationDrawerItem.SWITCH_PROFILE -> {
          Intent(fragment.activity, ProfileActivity::class.java)
        }
      }
      fragment.activity!!.startActivity(intent)
      fragment.activity!!.finish()
    } else {
      drawerLayout.closeDrawers()
    }
  }

  /**
   * Initializes the navigation drawer for the specified [DrawerLayout] and [Toolbar], which the host activity is
   * expected to provide. The [menuItemId] corresponds to the menu ID of the current activity, for navigation purposes.
   */
  fun setUpDrawer(drawerLayout: DrawerLayout, toolbar: Toolbar, menuItemId: Int) {
    when (NavigationDrawerItem.valueFromNavId(menuItemId)) {
      NavigationDrawerItem.HOME -> {
        navView.menu.getItem(NavigationDrawerItem.HOME.ordinal).isChecked = true
      }
      NavigationDrawerItem.HELP -> {
        navView.menu.getItem(NavigationDrawerItem.HELP.ordinal).isChecked = true
      }
      NavigationDrawerItem.SWITCH_PROFILE -> {
        navView.menu.getItem(NavigationDrawerItem.SWITCH_PROFILE.ordinal).isChecked = false
      }
    }
    this.drawerLayout = drawerLayout
    previousMenuItemId = menuItemId
    drawerToggle = object : ActionBarDrawerToggle(
      fragment.activity,
      drawerLayout,
      toolbar,
      R.string.drawer_open_content_description,
      R.string.drawer_close_content_description
    ) {
      override fun onDrawerOpened(drawerView: View) {
        super.onDrawerOpened(drawerView)
        fragment.activity!!.invalidateOptionsMenu()
      }

      override fun onDrawerClosed(drawerView: View) {
        super.onDrawerClosed(drawerView)
        fragment.activity!!.invalidateOptionsMenu()
      }
    }
    drawerLayout.setDrawerListener(drawerToggle)
    /* Synchronize the state of the drawer indicator/affordance with the linked [drawerLayout]. */
    drawerLayout.post { drawerToggle.syncState() }
  }

  override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
    if (NavigationDrawerItem.valueFromNavId(menuItem.itemId) == NavigationDrawerItem.SWITCH_PROFILE) {
      AlertDialog.Builder(fragment.requireContext(), R.style.AlertDialogTheme)
        .setMessage(R.string.home_activity_back_dialog_message)
        .setNegativeButton(R.string.home_activity_back_dialog_cancel) { dialog, _ ->
          dialog.dismiss()
          drawerLayout.closeDrawers()
        }
        .setPositiveButton(R.string.home_activity_back_dialog_exit) { _, _ ->
          openActivityByMenuItemId(menuItem.itemId)
        }.create().show()
      return false
    } else {
      openActivityByMenuItemId(menuItem.itemId)
    }
    return true
  }
}
