# Coords

Coords is a Minecraft mod for working with world coordinates.

Installation only on the server is enough.

Modloader: Fabric.

Fabric API required.

## Features

The primary command is `/cc`. This will display your coordinates in chat for all players.

You can also get coordinates of a specific player with `/cc <Player Name>`.

### Saving Coordinates

- Save Coordinates using the `/ccs <Save Name>` command.
- Or use the `/ccsc` command to save coordinates without being in the spot.
    - Example - `/ccsc 58 75 -19 minecraft:overworld Main Base`

### Accessing Saved Coordinates

- To see a list of saved coordinates, use `/ccl`.
    - You can also use `/ccl <Search Term>` to search for a specfic coordinate.

- Clicking on any name in the list will display the coord in full and bring up 5 options.
    - `[GET]`
        - Click on this to display the coord in chat for everyone.
    - `[DEL]`
        - Click on this to delete the coord.
    - `[REN]`
        - Click on this to rename the coord.
        - You'll need to specify the new name.
        - After you press enter, the coord will be renamed.
    - `[DIS]`
        - Click on this to display the coord above your hotbar.
        - Use `/cc-display-clear` to remove the display.
    - `[CPY]`
        - Click on this to copy the coord into your clipboard.
        - Copied string example - `/execute in minecraft:overworld run tp @s 10 23 53`

### Future Plans

- None currently... Leave a suggestion through the issues tab if you have any!
