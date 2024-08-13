package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Screen
import io.outfoxx.swiftpoet.BOOL
import io.outfoxx.swiftpoet.DeclaredTypeName
import io.outfoxx.swiftpoet.FileSpec
import io.outfoxx.swiftpoet.PropertySpec
import io.outfoxx.swiftpoet.STRING
import io.outfoxx.swiftpoet.TypeSpec

object SwiftGenerator {
    private const val ROOT_STRUCT = "AnalyticsEvent"
    private val SCREEN_PROTOCOL = DeclaredTypeName.typeName(".AnalyticsEventScreen")

    fun generate(screens: List<Screen>) {
        FileSpec
            .builder(ROOT_STRUCT)
            .addComment("https://github.com/tfandkusu/ga913-yaml/ による自動生成コードです。編集しないでください。")
            .addType(
                generateAnalyticsEventScreenProtocol(),
            ).addType(
                TypeSpec
                    .structBuilder(ROOT_STRUCT)
                    .addType(
                        generateScreenStruct(screens),
                    ).build(),
            ).build()
            .writeTo(System.out)
    }

    private fun generateAnalyticsEventScreenProtocol(): TypeSpec =
        TypeSpec
            .protocolBuilder(SCREEN_PROTOCOL)
            .addDoc("画面遷移イベントのプロトコル")
            .addProperty(
                PropertySpec
                    .abstractBuilder("screenName", STRING)
                    .abstractGetter()
                    .addDoc("Analytics イベント名")
                    .build(),
            ).addProperty(
                PropertySpec
                    .abstractBuilder("isConversionEvent", BOOL)
                    .abstractGetter()
                    .addDoc("コンバージョンイベントフラグ")
                    .build(),
            ).build()

    private fun generateScreenStruct(screens: List<Screen>): TypeSpec =
        TypeSpec
            .structBuilder("Screen")
            .apply {
                screens.forEach { screen ->
                    addType(
                        generateScreenStruct(screen),
                    )
                }
            }.build()

    private fun generateScreenStruct(screen: Screen): TypeSpec =
        TypeSpec
            .structBuilder(screen.className)
            .addSuperType(SCREEN_PROTOCOL)
            .addProperty(
                PropertySpec
                    .builder("screenName", STRING)
                    .initializer("%S", screen.eventName)
                    .build(),
            ).addProperty(
                PropertySpec
                    .builder("isConversionEvent", BOOL)
                    .initializer("%L", screen.isConversionEvent)
                    .build(),
            ).build()
}
