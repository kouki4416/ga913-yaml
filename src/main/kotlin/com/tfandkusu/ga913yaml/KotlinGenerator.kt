package com.tfandkusu.ga913yaml

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.tfandkusu.ga913yaml.model.Action
import com.tfandkusu.ga913yaml.model.ParameterType
import com.tfandkusu.ga913yaml.model.Screen
import kotlin.reflect.KClass

object KotlinGenerator {
    private const val PACKAGE = "com.tfandkusu.ga913android.analytics"
    private const val DIRECTORY = "ga913-android/app/src/main/java/com/tfandkusu/ga913android/analytics"
    private const val ROOT_CLASS = "AnalyticsEvent"
    private const val SCREEN_CLASS = "Screen"
    private const val ACTION_CLASS = "Action"
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
                    .classBuilder(ACTION_CLASS)
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
                    ).apply {
                        screens.forEach { action ->
                            addType(generateActionScreenClass(action))
                        }
                    }.build(),
            ).build()

    private fun generateScreenClass(screen: Screen): TypeSpec =
        TypeSpec
            .objectBuilder(screen.className)
            .addKdoc(screen.description)
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

    private fun generateActionScreenClass(screen: Screen): TypeSpec =
        TypeSpec
            .objectBuilder(screen.className)
            .apply {
                screen.actions.forEach { action ->
                    addType(generateActionClass(action))
                }
            }.build()

    private fun generateActionClass(action: Action): TypeSpec =
        TypeSpec
            .classBuilder(action.value)
            .addKdoc(action.description)
            .addModifiers(KModifier.DATA)
            .superclass(
                ClassName(
                    PACKAGE,
                    ROOT_CLASS,
                ).nestedClass(ACTION_CLASS),
            ).primaryConstructor(
                FunSpec
                    .constructorBuilder()
                    .apply {
                        action.parameters.forEach { parameter ->
                            addParameter(
                                parameter.variable,
                                toKClass(parameter.type),
                            )
                        }
                    }.build(),
            ).addProperty(
                PropertySpec
                    .builder(
                        EVENT_NAME_PROPERTY,
                        String::class,
                    ).addModifiers(KModifier.OVERRIDE)
                    .initializer("%S", action.value)
                    .build(),
            ).apply {
                action.parameters.forEach { parameter ->
                    addProperty(
                        PropertySpec
                            .builder(
                                parameter.variable,
                                toKClass(parameter.type),
                            ).initializer(parameter.variable)
                            .build(),
                    )
                }
            }.build()

    private fun toKClass(parameterType: ParameterType): KClass<*> =
        when (parameterType) {
            ParameterType.STRING -> String::class
            ParameterType.INT -> Int::class
            ParameterType.LONG -> Long::class
            ParameterType.FLOAT -> Float::class
            ParameterType.DOUBLE -> Double::class
            ParameterType.BOOLEAN -> Boolean::class
        }
}
