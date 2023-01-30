/*
 * Copyright (c) 2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions
import com.isaakhanimann.journal.ui.main.navigation.routers.NoArgumentRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToComboSettings
import com.isaakhanimann.journal.ui.main.navigation.routers.navigateToFAQ
import com.isaakhanimann.journal.ui.tabs.safer.settings.FAQScreen
import com.isaakhanimann.journal.ui.tabs.safer.settings.SettingsScreen
import com.isaakhanimann.journal.ui.tabs.safer.settings.combinations.CombinationSettingsScreen


fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(
        startDestination = NoArgumentRouter.SettingsRouter.route,
        route = TabRouter.Settings.route,
    ) {
        composableWithTransitions(NoArgumentRouter.SettingsRouter.route) {
            SettingsScreen(
                navigateToFAQ = navController::navigateToFAQ,
                navigateToComboSettings = navController::navigateToComboSettings
            )
        }
        composableWithTransitions(NoArgumentRouter.FAQRouter.route) { FAQScreen() }
        composableWithTransitions(NoArgumentRouter.CombinationSettingsRouter.route) { CombinationSettingsScreen() }
    }
}