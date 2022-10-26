# Wiz I Handin README
#### Fill out this README before turning in this project. Make sure to fill this out again for each assignment!
---
### Banner ID: B01685955
---
### Already uploaded demo onto Slack: Yes
---
## Primary Requirements:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Your handin must meet all global requirements | All global requirements are met. |
| Your handin only crashes under exceptional circumstances (edge cases) | Through extensive testing, I have never been able to get my handin to crash. |
| Your engine must be able to calculate MTVs in addition to normal collision calculations. | MTVs are calculated in `AAB` and `Circle`, located in the `engine/game/objects/shapes` directory. The calculations are identical to those that I used in the Week 3 debugger. |
| Your debugger should be extended to display the respective MTVs for all collision pairs. TAs should quickly be able to verify that the correct MTV is being calculated. | This can be seen by running the debugger. |
| One of the tile types should be passable. One should be impassable. | My map is made of a passable "floor" and an impassable "brick" |
| The player-controller “unit” should not be able to enter the impassable tiles. The unit should be able to enter passable tiles. | The player can move over passable tiles, but no the impassable walls. If the player tries to walk into a wall, they won't move. |
| Your game never crashes. | Through extensive testing, I've enver been able to get my game to crash. |

## Secondary Requirements:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Your engine must meet all primary engine requirements | See above |
| Your engine must implement a procedural map generation algorithm (space portioning or similar complexity) OR support level loading from a text file. | Maps are generated in the `MapGenerator` class, located in `wiz/game/helpers`. This class uses space partitioning to do procedural map generation to generate a map based on a seed value. |
| Your engine must correctly support an animation behavior. | This can be seen when the player character moves om the map. The `HasSprite` component supports using subimages of a single image file, so different frames of animation can be loaded in a single file and used to animate sprites. |
| Your game must use a procedurally generated map OR a map loaded from a level file. | The game uses a procedurally generated map through the `MapGenerator` class. |
| If using a procedurally generated map, the player must be able to select a seed, either using a text box or using buttons with pre-selected seed. | By default, the game runs using a random seed (which is displayed in the bottom left corner during gameplay). By going into settings through the menu screen, the user can choose a pre-selected seed to play on. (It is worth noting that only the map is based on the seed, enemy locations are random and not seeded). |
| The generated or loaded tiles must be stored in a grid (2D-Array). | The `MapGenerator` class returns a 2D-array of the enum `TileType`, which holds whether the tile at that point is passable, impassable, the spawn/exit point, etc. This is used by the `Map` class to create a 2D array of `Tile`s. |
| Your game must include a sprite animation. | The player sprite is animated when it moves |
| Your game must meet at least two of the extra game requirements. | See the following |
| Allow the player or enemies to launch ranged projectiles that damage their target on contact. | By pressing space, the player can launch ranged projectiles. A single projectile will kill any enemy it comes into contact with (destroying an enemy will consume the projectile) |
| Distinguish between impassable tiles that block ranged weapons, and impassable tiles that ranged weapons can still pass through. | Projectiles can pass through floor tiles but not wall, so they will be consumed whenever they hit a wall (impassable tile) |

## Extras:
| Requirement | Location in code or steps to view in game  |
|---|---|

--------------------------------------------------------------

Instructions on how to run

Known bugs: None

Hours spent on assignment: 16
