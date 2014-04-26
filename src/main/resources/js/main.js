var pluginClass = com.nickardson.bukkitdebug.BukkitDebug,
    plugin = pluginClass.getPlugin(pluginClass);

function print(args) {
    if (args == undefined) {
        args = "undefined";
    }
    java.lang.System.out.println(String(args));
}
