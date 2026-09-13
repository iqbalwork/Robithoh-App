package com.iqbalwork.robithoh.core

import com.iqbalwork.robithoh.shared.BuildKonfig
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BuildKonfigTest {

    @Test
    fun testBuildKonfigValuesPresent() {
        assertNotNull(BuildKonfig.BASE_URL)
        assertTrue(BuildKonfig.BASE_URL.isNotBlank())
        assertNotNull(BuildKonfig.ENVIRONMENT)
        assertTrue(BuildKonfig.ENVIRONMENT.isNotBlank())
    }
}
