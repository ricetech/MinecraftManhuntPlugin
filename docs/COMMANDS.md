# Commands

This document contains a list of commands provided by the 
[Minecraft Manhunt Plugin](https://github.com/ricetech/MinecraftManhuntPlugin) and how to use them.

For more information on how to use these commands in the context of the game,
check out the [How to Play guide](./HOW_TO_PLAY.md).

## Permissions

You don't need a permissions plugin to use the Manhunt plugin. However, if you're using a permissions plugin like
[LuckPerms](https://luckperms.net), you can refer to the `Permissions` column in the table below to see what permissions
you need to grant to allow usage of a command.

If you aren't using a permissions plugin, you can ignore the `Permissions` column.

## List of Commands

### Game Management Commands

Commands for managing or viewing the state of the Manhunt game.

These commands are granted to all players by default.

| Command    | Description                                                                                          | Arguments                                                                                                                                                                                                   | Permissions | Syntax                              |
|------------|------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|-------------------------------------|
| clearteams | Removes all players from all teams.                                                                  | None.                                                                                                                                                                                                       | `mmh.game`  | `/clearteams`                       |
| maketeams  | Allows players to switch teams.                                                                      | Arg 1 (optional): <br>  - Default `select` allows players to select a team.<br>  - `random` randomizes teams. <br>    Favors Hunters if odd number of players.                                              | `mmh.game`  | `/maketeams [random/select]`        |
| startgame  | - Starts a game of Manhunt.<br>- Resets players using `/reset`.<br>- Enables game-specific features. | Arg 1 - countdown:<br>  - Starts a timer using `/countdown` command.<br>  - Requires a number.<br>Arg 2 - skip reset (optional):<br>  - Defaults to `false`.<br>  - If `true`, `/reset` will not be called. | `mmh.game`  | `/startgame <seconds> [true/false]` |
| stopgame   | - Stops a game of Manhunt.                                                                           | None.                                                                                                                                                                                                       | `mmh.game`  | `/stopgame`                         |

### Utility Commands

Useful commands that can be used outside of the Manhunt game.

These commands are granted to all players by default.

| Command   | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | Arguments                                 | Permissions  | Syntax                 |
|-----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------|--------------|------------------------|
| countdown | Starts a generic countdown timer.<br>Only one timer can be running at once.<br>You can cancel a running timer using<br>`/countdown 0`.                                                                                                                                                                                                                                                                                                                                                                                                                                                | Arg 1 - Duration:<br>- Requires a number. | `mmh.player` | `/countdown <seconds>` |
| reset     | Performs the following actions/commands:<br>- Remove all potion effects<br>- Clear inventory<br>- Set gamemodes (Survival or Spectator)<br>- Move all Eliminated players to Runners<br>- Teleport all players to world spawn<br>- Give max health<br>- Give max saturation<br>- Give max food level<br>- Set exhaustion to zero<br>- Reset XP<br>- `/time set 0`<br>- `/gamerule doDaylightCycle true`<br>- `/gamerule doWeatherCycle false`<br>- `/weather set clear`<br>- `/kill @e[type=Item]`<br>- `/advancement revoke @a everything`<br>- Reset the state of the Manhunt plugin | None.                                     | `mmh.player` | `/reset`               |

### Player (in-game) Commands

Commands used by players to play the game of Manhunt.

These commands are granted to all players by default.

| Command     | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | Arguments           | Permissions  | Syntax          |
|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------|--------------|-----------------|
| listteams   | List the teams and the players on each team.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | None.               | `mmh.player` | `/listteams`    |
| track       | Tracks a player.<br>- Requires a compass in your inventory.<br>- The compass will point towards the target.<br>- The compass does *not* update automatically.<br>- Update the compass by right-clicking with the compass.<br>  You don't need to use `/track` again unless you want to change<br>  your tracking target.<br>- Teammates can track each other and are given precise positions.<br>- Runners & Eliminated count as teammates.<br>- Hunters can track Runners but are given rough positions.<br>- Tracking a player in a different world will lead you to <br>  their portal (or your portal if they don't have one).<br>- Offline players can be tracked provided they disconnected<br>  while the plugin was running. | Arg 1 - Player Name | `mmh.player` | `/track <name>` |
| trackportal | Track the portal that you used to enter The Nether.<br>- Requires a compass in your inventory.<br>- The compass will point towards your portal.<br>- The compass does *not* update automatically.<br>- Update the compass by right-clicking with the compass.<br>- Only works in The Nether.<br>- Replaces any target you are tracking with `/track`.                                                                                                                                                                                                                                                                                                                                                                                | None.               | `mmh.player` | `/trackportal`  |

### Operator commands

Commands not required to play the game, but can be used to rectify errors in the game.

These commands are granted to server operators by default.

| Command          | Description                                                                                                                                                                                              | Arguments                                                                                                                                                                                                                    | Permissions      | Syntax                                                            |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------|-------------------------------------------------------------------|
| creditkill       | Credit a kill to another player. <br>Useful if the game says a player died to natural causes <br>even if another player killed them.                                                                     | Arg 1: Player Name                                                                                                                                                                                                           | `mmh.correction` | `/creditkill <player>`                                            |
| eliminate        | Eliminate a player. <br>For use if the game didn't eliminate them automatically. <br>Also kills the player and credits the kill to the killer.                                                           | Arg 1: Victim<br>- Requires a valid Player name.<br>- Player must be online.<br>- Player must be a Runner.<br><br>Arg 2: Killer<br>- Requires a valid Player name.<br>- Player must be online.<br>- Player must be a Hunter. | `mmh.correction` | `/eliminate <victim> <killer>`                                    |
| offerteamtp      | Offer the list of teammates to teleport to <br>(or death location if no teammates are available) to a player.<br>Replaces the menu normally provided if a player states <br>they died to natural causes. | Arg 1: Player Name                                                                                                                                                                                                           | `mmh.correction` | `/offerteamtp <player>`                                           |
| restoreinventory | Offer a player's previous inventory <br>(stored at the point where they died) <br>to the player to allow them to select items to restore.                                                                | Arg 1: Player Name                                                                                                                                                                                                           | `mmh.correction` | `/restoreinventory <player>`                                      |
| setplayerteam    | Forcibly change a player's team, whether they are eligible to switch or not.                                                                                                                             | Arg 1: Player Name<br><br>Arg 2: Team Name<br>- One of <br>  `runners/hunters/eliminated/spectators`                                                                                                                         | `mmh.correction` | `/setplayerteam <player> <runners/hunters/eliminated/spectators>` |
| uneliminate      | Un-eliminate a player. Restores them to the Runners team.                                                                                                                                                | Arg 1: Player Name                                                                                                                                                                                                           | `mmh.correction` | `/uneliminate <player>`                                           |

---

### Internal Commands

**Internal commands should never be called by players**.

Internal commands are used as triggers for clickable elements in other commands.

All commands are protected by "eligibility" - the commands do not work unless an event in the plugin code
grants eligibility for a command to a player.

A reference is kept here for use by developers.

| Command          | Description                                                                                                                                     | Arguments                                                                                                                                                                | Permissions  | Syntax                                              |
|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|-----------------------------------------------------|
| changeteam       | Change to the specified team.                                                                                                                   | Arg 1:<br>- One of<br>`runners/hunted/eliminated/spectators`                                                                                                             | `mmh.player` | `/changeteam <runners/hunted/eliminated/spectators` |
| reseteligibility | Cancel eligibility for:<br>- `/teamtp`<br>- `/selfelim`<br>- `/changeteam`                                                                      | None.                                                                                                                                                                    | `mmh.player` | `/reseteligibility`                                 |
| selfelim         | Runners: Eliminate yourself.<br>Hunters/eliminated:<br>Prevents teleporting to a teammate.                                                      | None.                                                                                                                                                                    | `mmh.player` | `/selfelim`                                         |
| teamtp           | Teleport to a teammate.                                                                                                                         | Arg 1: Player Name<br>- Alternatively, `$elf` to teleport<br>  to your death location.<br>- Teleporting to your death location<br>  grants invincibility for 25 seconds. | `mmh.player` | `/teamtp <player>`                                  |
| tpoptions        | List all available teammates to<br>teleport to.<br>If no teammates are available,<br>provides the option to teleport<br>to your death location. | None.                                                                                                                                                                    | `mmh.player` | `/tpoptions`                                        |
