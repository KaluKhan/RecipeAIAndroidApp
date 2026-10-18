package com.sankhlatraders.recipeai.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Details : Screen("detail/{recipeId}") {
        fun createRoute(recipeId: String) = "detail/$recipeId"
    }

    object AiAgent : Screen("ai_agent")
    object ContactUs : Screen("contact_us")
}

sealed class DrawerItem(val route: String, title: String, iconName: String) {
    object Home : DrawerItem(Screen.Main.route, "Home", "Home")
    object ContactUs : DrawerItem(Screen.ContactUs.route, "Contact Us", "ContactMail")
}