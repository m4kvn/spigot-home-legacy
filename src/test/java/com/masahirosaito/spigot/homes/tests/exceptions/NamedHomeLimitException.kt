package com.masahirosaito.spigot.homes.tests.exceptions

class NamedHomeLimitException(limit: Int) : Exception("You can not set more homes (Limit: $limit)")
