# WebRTC calls Java from native code through JNI. These entry points are not all
# visible to R8's static analysis, so release shrinking must preserve them.
-keep class org.webrtc.** { *; }
-keep class org.jni_zero.** { *; }

# Keep the application's WebRTC bridge and callback implementations intact as
# well; native callbacks can otherwise target renamed release-only methods.
-keep class com.example.isemessenger.CallEngine { *; }
-keep class com.example.isemessenger.CallEngine$* { *; }
-keep class com.example.isemessenger.SimpleSdpObserver { *; }
-keep class com.example.isemessenger.SimpleSdpObserver$* { *; }
