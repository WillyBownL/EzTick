// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //id("com.google.gms.google-services")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    //alias(libs.plugins.google.gms.google.services) apply false
    //alias(com.google.gms.google-services) apply false
}