## Setup

1. Place BukkitDebug.jar in the server's plugins folder.  If the server is already running, run the `/reload` command, otherwise start it.
2. Edit the `config.yml` file in `plugins/BukkitDebug`.
  * Change `password` to something more secure.
  * Change `enabled` to `true`.
3. Restart your server, or run the `/reload` command.
4. Navigate to the BukkitDebug web page.
  * If the server is on your local machine, simply point your web browser to [localhost:13370](http://localhost:13370)
  * If the server is on a remote computer, port-forward port 13370.  This step varies based on your own setup.  The page is now accessible at `(Server IP):13370`


## Developers - How to build

Maven makes it a snap, simply clone the project,

    git clone https://github.com/Nickardson/BukkitDebug.git

And run Maven's `install` lifecycle.