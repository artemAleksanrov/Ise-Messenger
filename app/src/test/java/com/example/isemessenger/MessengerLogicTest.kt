package com.example.isemessenger

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MessengerLogicTest {
    @Test
    fun newAndPreviousScreensUseOppositeDirections() {
        assertTrue(isOpeningNewScreen(Screen.Chats, Screen.Chat))
        assertTrue(isOpeningNewScreen(Screen.Chat, Screen.Profile))
        assertFalse(isOpeningNewScreen(Screen.Profile, Screen.Chat))
        assertFalse(isOpeningNewScreen(Screen.Chat, Screen.Chats))
    }

    @Test
    fun realWaveformNormalizationKeepsShapeInRange() {
        val waveform = normalizeAudioWaveform(floatArrayOf(0f, 0.1f, 0.8f, 0.2f, 0f))

        assertEquals(5, waveform.size)
        assertTrue(waveform.all { it in 0.18f..1f })
        assertTrue(waveform[2] > waveform[0])
        assertTrue(normalizeAudioWaveform(FloatArray(5)).isEmpty())
    }

    @Test
    fun githubReleaseVersionMustBeNewerThanInstalledVersion() {
        assertTrue(isVersionNewer("v1.1.0", "1.0"))
        assertTrue(isVersionNewer("release-2.0", "1.9.9"))
        assertFalse(isVersionNewer("v1.0.0", "1.0"))
        assertFalse(isVersionNewer("v0.9.9", "1.0"))
        assertFalse(isVersionNewer("latest", "1.0"))
    }
}
