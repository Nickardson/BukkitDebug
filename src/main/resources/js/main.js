var pluginClass = com.nickardson.bukkitdebug.BukkitDebug,
    plugin = pluginClass.getPlugin(pluginClass);

function write(string) {
    output.write(string);
}

function print(args) {
    args = String(args) + "\n";
    write(args.replace("\n", "<br/>"));
}

function stdout(args) {
    if (args == undefined) {
        args = "undefined";
    }
    java.lang.System.out.println(String(args));
}
