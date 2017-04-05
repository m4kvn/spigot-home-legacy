package com.masahirosaito.spigot.homes.datas.strings.commands

data class PrivateCommandStringData(
        val DESCRIPTION: String = "Set your home private or public",
        val USAGE_PRIVATE: String = "Set your default home private or public",
        val USAGE_PRIVATE_NAME: String = "Set your named home private or public",
        val SET_DEFAULT_HOME_PRIVATE: String = "Set your default home &ePRIVATE&r",
        val SET_DEFAULT_HOME_PUBLIC: String = "Set your default home &bPUBLIC&r",
        val SET_NAMED_HOME_PRIVATE: String = "Set your home named <&d[home-name]&r> &ePRIVATE&r",
        val SET_NAMED_HOME_PUBLIC: String = "Set your home named <&d[home-name]&r> &bPUBLIC&r"
)
