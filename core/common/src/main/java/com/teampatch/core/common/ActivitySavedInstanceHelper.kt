package com.teampatch.core.common

import android.os.Bundle

interface ActivitySavedInstanceHelper {
    fun saveState(bundle: Bundle)
    fun restoreState(bundle: Bundle)
}