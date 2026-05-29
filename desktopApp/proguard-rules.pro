# OkHttp and Ktor optional dependencies
-dontwarn okhttp3.internal.graal.**
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn com.oracle.svm.core.annotate.**
-dontwarn org.graalvm.nativeimage.hosted.**

# Ktor logging (refers to android.util.Log)
-dontwarn io.ktor.client.plugins.logging.LoggerJvmKt

# Skiko/Compose internal warnings
-dontwarn org.jetbrains.skiko.swing.JbrSharedTexturesAdapter
-dontwarn com.jetbrains.SharedTextures
