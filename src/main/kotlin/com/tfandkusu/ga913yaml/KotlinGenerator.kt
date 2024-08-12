package com.tfandkusu.ga913yaml

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.tfandkusu.ga913yaml.model.Screen

object KotlinGenerator {
    private const val PACKAGE = "com.tfandkusu.ga913android.analytics"
    private const val DIRECTORY = "ga913-android/app/src/main/java/com/tfandkusu/ga913android/analytics"
    private const val ROOT_CLASS = "AnalyticsEvent"
    private const val SCREEN_CLASS = "Screen"
    private const val EVENT_NAME_PROPERTY = "eventName"

    fun generate(screens: List<Screen>) {
        val fileSpec =
            FileSpec
                .builder(PACKAGE, ROOT_CLASS)
                .addFileComment(
                    "https://github.com/tfandkusu/ga913-yaml/ による自動生成コードです。編集しないでください。",
                ).addType(
                    generateAnalyticsEventClass(screens),
                ).build()
        fileSpec.writeTo(System.out)
    }

    private fun generateAnalyticsEventClass(screens: List<Screen>): TypeSpec =
        TypeSpec
            .objectBuilder(ROOT_CLASS)
            .addType(
                TypeSpec
                    .classBuilder(SCREEN_CLASS)
                    .addModifiers(KModifier.SEALED)
                    .addProperty(
                        PropertySpec
                            .builder(
                                EVENT_NAME_PROPERTY,
                                String::class,
                            ).addModifiers(KModifier.ABSTRACT)
                            .build(),
                    ).apply {
                        screens.forEach { screen ->
                            addType(generateScreenClass(screen))
                        }
                    }.build(),
            ).addType(
                TypeSpec
                    .classBuilder("Action")
                    .addModifiers(KModifier.SEALED)
                    .addProperty(
                        PropertySpec
                            .builder(
                                EVENT_NAME_PROPERTY,
                                String::class,
                            ).addModifiers(KModifier.ABSTRACT)
                            .build(),
                    ).addProperty(
                        PropertySpec
                            .builder(
                                "eventParameters",
                                MAP.parameterizedBy(STRING, ANY),
                            ).addModifiers(KModifier.ABSTRACT)
                            .build(),
                    ).build(),
            ).build()

    private fun generateScreenClass(screen: Screen): TypeSpec =
        TypeSpec
            .objectBuilder(screen.className)
            .addModifiers(KModifier.DATA)
            .superclass(
                ClassName(
                    PACKAGE,
                    ROOT_CLASS,
                ).nestedClass(SCREEN_CLASS),
            ).addProperty(
                PropertySpec
                    .builder(
                        EVENT_NAME_PROPERTY,
                        String::class,
                    ).addModifiers(KModifier.OVERRIDE)
                    .initializer("%S", screen.value)
                    .build(),
            ).build()
}
