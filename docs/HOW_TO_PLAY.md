# How to Play

This document is a quick start guide on how to play Manhunt using the
[Minecraft Manhunt Plugin](https://github.com/ricetech/MinecraftManhuntPlugin).

For a full breakdown of the rules that go with this game, please see the
[rules](./RULES_FULL.md) document.

## Game Intro

- Two teams of players face each other - Runners and Hunters.
- The Runners are trying to complete a Minecraft advancement before getting killed by the Hunters.
- Runners who get killed by Hunters become Eliminated, and no longer contribute to the winning objective.
However, they can still play and help the Runners!
- Everyone has unlimited lives.
- If all Runners become Eliminated, the Hunters win.
- If the Runners complete their objective with at least 1 Runner surviving,
the Runners win.
- See the full list of rules in the [rules](./RULES_FULL.md) document.
- An extra feature of this plugin is that deaths and kills are automatically tracked.
  - You'll notice the Deaths counter in the sidebar of the screen.
  - Kills can be viewed by holding TAB - the number of kills a player has is displayed
  next to their name.

## Before the Game

- Consider creating a new world to play in.
  - If you want to reuse your world, consider offsetting the world spawnpoint by 1000+ blocks
    so that you have a new area to play in using `/setworldspawn 1000 85 1000`, for example.
- Make sure the spawnpoint/starting area is in a playable area.
  - This is because all players will be teleported to the world spawn when the game starts.
  - You can use `/setworldspawn` to move the spawn point elsewhere.
  - Make sure there are enough resources in the area for everyone to survive initially
    (wood, stone, caves)
- Once all players are present, create the teams.
  - To let the players select their own teams, use `/maketeams`.
  - To randomize the teams, use `/maketeams random`.
- Decide on a winning objective for the Runners. Here are some examples:
  - "Short version": Acquire a Blaze Rod by killing a blaze. Blaze rods acquired from other places like chests don't count.
  - "Long version": Construct, activate and jump into an End Portal.
  - "Full speedrun version": Kill the Ender Dragon.
- When everyone is ready, use `/startgame 60` to start the game.
  - This command will reset everyone's positions, gamemodes, inventory and hunger,
    along with the world time and more.
  - The number dictates how long (in seconds) of a headstart the Runners get
    before the Hunters can start chasing them.
    
## During the Game

This section is divided by team.

### General tips
- If you get separated from your team, use `/track` to locate them.
- During the day, your main focuses should be:
  - Regrouping with any separated teammates
  - Getting food
  - Getting wood
  - Getting any other resources that are unavailable underground or in The Nether
- Once night falls, you should aim to move into a cave to avoid the large numbers of
  hostile mobs on the surface. In caves, focus on:
  - Collecting ores
  - Smelting ores and food (you can split one player off to handle this)
  - Crafting armor, tools and weapons
  - Keeping an eye out for opposing players
- Only craft what you need. Many players spend too much time on collecting resources
and crafting tools that they don't end up using during the game.
- Make sure you have enough food, tools and resources before you enter The Nether.
- If you get lost in The Nether, you can use `/trackportal` to find your way back
to your Nether Portal.

### Runners
- You'll have a head start before the Hunters can come after you.
- During the head start period, aim to collect enough wood to make some tools.
- Seek out any structures such as villages close to spawn - they can contain valuable supplies.
  - Make sure to not spend too much time near the start area - try to get away soon to
    avoid the hunters.
- During the day, always work on increasing the distance gap with the Hunters.
Only stop for resources when absolutely necessary.
- During both day and night, work on building a Nether Portal once you have
  or almost have enough supplies to survive in The Nether.
  - Either find a lava pool to build one from, or a ruined portal.
- In the Nether, search for a Nether Fortress. Use the generation patterns to find
  them quicker:
  - Try to search diagonally east-west to increase your chances of finding
    the first fortress.
  - Once you find a fortress, search north-south to find more.
- Focus on collecting Blaze Rods from Nether Fortresses.
- Focus on killing Endermen for Ender Pearls.
  - Finding a Warped Forest can make this easier since more Endermen spawn there.
  
### Hunters
- You cannot do anything (including collect resources) during the head start period.
- Try organizing your team so that each player is always tracking a different Runner.
  This can help you notice if a Runner is separated from the group, which you can
  play to your advantage.
- After the head start period, work on collecting just enough resources to make tools and weapons.
- Since you are always chasing after the Runners, you must be as efficient as possible
when deciding how many resources to collect.
  - Collecting less resources can be to your advantage if it allows you to
  catch up to the Runners earlier and catch them off-guard.
- During the day, always focus on catching up to the Runners.
- Instead of chasing the Runner's Nether Portal, consider building your own if
the Runners are far away.
  
## Endgame

- The game ends if:
  - The Runners achieve their objective (Runners Win)
  - All of the Runners become Eliminated (Hunters Win)
- The game will end automatically if the Hunters win.
However, if the Runners win, you will need to use the `/stopgame` command as
  the plugin can't detect achievements yet.
  
## For Operators

If you're a server operator (or if you've been granted the `mmh.correction` permission),
you have access to some commands that you can use to fix miscalculations in the game.

See the [Operator Commands section](./COMMANDS.md#operator-commands) of the Commands documentation
for more details on how to use these commands.

## Suggestions and Improvements

These instructions are only a guideline - feel free to modify them to suit your playstyle.

If you have a suggestion for an improvement or modification to these instructions, feel free to
[open an issue](https://github.com/ricetech/MinecraftManhuntPlugin/issues/new) and flag it with the "Documentation" label.
