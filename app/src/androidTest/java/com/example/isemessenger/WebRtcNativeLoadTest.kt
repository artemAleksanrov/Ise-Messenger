package com.example.isemessenger

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.webrtc.PeerConnectionFactory

@RunWith(AndroidJUnit4::class)
class WebRtcNativeLoadTest {
    @Test
    fun nativeLibraryInitializes() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions()
        )
    }
}
