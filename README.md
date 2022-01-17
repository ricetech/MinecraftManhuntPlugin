# MinecraftManhuntPlugin

Thanks for checking out the Minecraft Manhunt Plugin!

This plugin allows Spigot & Paper-based Minecraft servers to run a version of Manhunt.
Keep reading to find the rules for this version of manhunt as well as the available helper commands!

Not sure what Minecraft Manhunt is? It's a Minecraft adaptation of Manhunt, where a team of Runners
tries to complete an objective without getting killed by the Hunters. Check out the
[Guide for Players](https://github.com/ricetech/MinecraftManhuntPlugin/wiki/Guide-for-Players) for more information on how the game works!

Note: This plugin does not work on Bukkit servers. You need to use either Spigot or Paper.

## Rules & How to Play

To find out how to play and see the rules, check out the 
[Guide for Players](https://github.com/ricetech/MinecraftManhuntPlugin/wiki/Guide-for-Players).

## Installation

1. Download the latest version of the plugin `.jar` from the 
[Releases](https://github.com/ricetech/MinecraftManhuntPlugin/releases) page.

2. Place the `.jar` file into your server's `plugins` folder.

3. Restart your server and enjoy!.

## Commands

Check out the [Commands](https://github.com/ricetech/MinecraftManhuntPlugin/wiki/Commands)
page for more information on available commands.

## Plugin Features

- Player-tracking compass
  - Has "exact location" and "rough location" modes
  - Allows Hunters to have a rough idea of where a Runner is located without giving away
    their exact location
  - Tracks players anywhere, even through Nether and End portals
  - Can even track offline players!
  - Easy to use - use the command once, and simply right-click with the compass to refresh!
- Portal-tracking compass
  - Who hasn't gotten lost in The Nether before?
- Countdown command
- Reset command
  - Easily prepare the world for a new game of Manhunt
  - Resets things such as time, health and hunger with just one command
- Team Management
  - Pre-made Runners, Hunters, Eliminated and Spectators teams
  - Includes commands and interactive messages to switch teams
  - Includes a command to generate random teams if you're feeling indecisive
- Game Management
  - Commands to start and stop the game
  - Automatically stops the game if all Runners are eliminated
  - Prevents game-related message spam when a game isn't running
- Death & Teleport Handling
  - Automatically eliminates Runners who die to a Hunter
  - Asks players if they died to a Player or Natural Causes
  - Allows players who died to Natural Causes to teleport to their teammates with one click
  - Doesn't require granting permission to use the `/tp` command
- Inventory Management
  - Restore the inventory of a player
  - Useful if someone takes an accidental trip into a lava pool

## Roadmap

To see if any features will be added to the plugin soon, check out the 
[GitHub Projects page](https://github.com/ricetech/MinecraftManhuntPlugin/projects/1).

## Credits

Thanks to [johnzhoudev](https://github.com/johnzhoudev) for the original plugin idea and for allowing me to use
some of his code. You can find his original version of this plugin 
[here](https://github.com/johnzhoudev/CompassTrackerMinecraft).

## Contributing

If you'd like to directly contribute to the plugin, feel free to open a pull request!
If you're not that familiar with coding, you can also 
[open an issue](https://github.com/ricetech/MinecraftManhuntPlugin/issues/new)
for me to investigate. Lastly, if you like my work, I warmly welcome donations
through [Ko-fi](https://ko-fi.com/the_ricetech)!
