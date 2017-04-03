package com.masahirosaito.spigot.homes.datas.strings

data class ErrorStringData(
        val NO_COMMAND: String = "Command <[command-name]> does not exist",
        val NO_PERMISSION: String = "You don't have permission <[permission-name]>",
        val NO_ONLINE_PLAYER: String = "Player <[player-name]> does not exist",
        val NO_OFFLINE_PLAYER: String = "Player <[player-name]> does not exist",
        val NO_VAULT: String = "Fee function stopped because Vault can not be found.",
        val NO_ECONOMY: String = "Fee function stopped because the Economy plugin can not be found.",
        val NO_DEFAULT_HOME: String = "[player-name]'s default home does not exist",
        val NO_NAMED_HOME: String = "[player-name]'s home named <[home-name]> does not exist",
        val HOME_LIMIT: String = "You can not set more homes (Limit: &r[home-limit-num]&c)",
        val DEFAULT_HOME_IS_PRIVATE: String = "[player-name]'s default home is PRIVATE",
        val NAMED_HOME_IS_PRIVATE: String = "[player-name]'s home named <[home-name]>} is PRIVATE",
        val NO_RECEIVED_INVITATION: String = "You have not received an invitation",
        val ALREADY_HAS_INVITATION: String = "[player-name] already has another invitation"
)
