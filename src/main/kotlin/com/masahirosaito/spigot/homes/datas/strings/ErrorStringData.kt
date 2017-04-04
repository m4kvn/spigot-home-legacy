package com.masahirosaito.spigot.homes.datas.strings

data class ErrorStringData(
        val NO_COMMAND: String = "&cCommand <[command-name]> does not exist&r",
        val NO_PERMISSION: String = "&cYou don't have permission <[permission-name]>&r",
        val NO_ONLINE_PLAYER: String = "&cPlayer <[player-name]> does not exist&r",
        val NO_OFFLINE_PLAYER: String = "&cPlayer <[player-name]> does not exist&r",
        val NO_VAULT: String = "&cFee function stopped because Vault can not be found.&r",
        val NO_ECONOMY: String = "&cFee function stopped because the Economy plugin can not be found.&r",
        val NO_DEFAULT_HOME: String = "&c[player-name]'s default home does not exist&r",
        val NO_NAMED_HOME: String = "&c[player-name]'s home named <[home-name]> does not exist&r",
        val NO_HOME: String = "&c[player-name]'s home is empty&r",
        val HOME_LIMIT: String = "&cYou can not set more homes (Limit: &r[home-limit-num]&c)&r",
        val DEFAULT_HOME_IS_PRIVATE: String = "&c[player-name]'s default home is PRIVATE&r",
        val NAMED_HOME_IS_PRIVATE: String = "&c[player-name]'s home named <[home-name]>} is PRIVATE&r",
        val NO_RECEIVED_INVITATION: String = "&cYou have not received an invitation&r",
        val ALREADY_HAS_INVITATION: String = "&c[player-name] already has another invitation&r",
        val NOT_ALLOW_BY_CONFIG: String = "&cNot allowed by the configuration of this server&r",
        val ARGUMENT_INCORRECT: String = "&cThe argument is incorrect\n[command-usage]&r",
        val INVALID_COMMAND_SENDER: String = "&cCommandSender is invalid&r"
)
