package com.masahirosaito.spigot.homes.datas.strings.commands

data class DeleteCommandStringData(
        val DESCRIPTION: String = "Delete your homes",
        val USAGE_DELETE: String = "Delete your default home",
        val USAGE_DELETE_NAME: String = "Delete your named home",
        val DELETE_DEFAULT_HOME: String = "&bSuccessfully delete your default home&r",
        val DELETE_NAMED_HOME: String = "&bSuccessfully delete your named home <&r[home-name]&b>&r"
)
