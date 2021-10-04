package com.aliakseipalianski.myapplication.dependency

import org.koin.test.KoinTestRule

object KoinTestRuleProvider {

    fun provide() = KoinTestRule.create {
        modules(testModule())
    }
}