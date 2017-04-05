package com.masahirosaito.spigot.homes.datas.strings.commands

data class SetCommandStringData(
        val DESCRIPTION: String = "Set your home or named home",
        val USAGE_SET: String = "Set your location to your default home",
        val USAGE_SET_NAME: String = "Set your location to your named home",
        val SET_DEFAULT_HOME: String = "&bSuccessfully set as &6default home&r",
        val SET_NAMED_HOME: String = "&bSuccessfully set as &6home named <&r[home-name]&6>&r"
)
