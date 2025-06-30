package com.teampatch.harmony

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
fun NavController.currentBackStackEntryAsStateWithLifecycle(): State<NavBackStackEntry?> {
    return currentBackStackEntryFlow.collectAsStateWithLifecycle(null)
}