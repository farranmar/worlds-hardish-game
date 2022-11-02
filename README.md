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
| If using behavior trees: You must include a behavior tree, selector, and sequence class. You must also include a node and action/condition interface. | All behavior tree-related classes (`BTNode`, `Composite`, `Sequence`, `Selector`, `Action`, `Condition`, etc) can be found in `engine/game/ai`.  |
| If using GOAP: You must include a game state class, an action class/interface, and a condition class/interface. You must be able to search over an action set using a predefined start state and goal condition. | NA |
| In both cases, your engine must have an A* implementation of pathfinding. | The `engine/support/pathfinding/Pathfinder` class takes in a Graph (found in `engine/support/graph/Graph`) and uses the A* algorithm to find the lowest-cost path between 2 nodes. A heuristic and cost function (`engine/support/pathfinding/Heuristic`) are both supplied to do the calculations. |
| Your game should have a map containing passable and impassable tiles. | The map contains passable floor and impassable map tiles. |
| Your game must have a unit that can be controlled by the player. | The player can control the green blob-like unit. |
| Your game must have at least one enemy unit that moves around deterministically (i.e., the same actions by the player result in the same enemy behavior). There must be a visible reaction when the player and item collide. | Each game generates with 10 enemies on the map. They each move around fully deterministically based on the map, their position, and the player position. When the player and enemy collide, the player dies and melts into a puddle, and a "game over" screen is displayed. |
| The player-controlled unit must not be able to leave the map. | The map is surrounded by impassable tiles, so the player cannot leave the map. |
| The enemy unit should use your engine’s AI framework. All of the AI tools included in the engine requirements should be used when constructing your AI. All of the behaviors defined for your enemy should be visible at some point when playing the game. | The enemy units each use a behavior tree created through the ai framework in my engine. If the enemy is "in range" of a player (ie, in the same row or column as the player such that the player could shoot them) and can get out of range in 2 steps or less, then it will move out of range. Note that if the enemy is with 3 moves of the player it won't do this, because it will prioritizing attacking if it is that close. Otherwise, if the enemy has a path to the player in 10 moves or less, it will move towards the player. When constructing this path, tiles that are "in range" cost more than other tiles, to prioritize the enemy's safety while attacking a player. |
| It must be possible to start a new game without restarting the program. | After losing or winning, the player can choose to restart the game. |


## Secondary Requirements:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Your engine must meet all primary engine requirements | See above |
| The enemy unit should move according to a path generated using A*. | When attacking the player, the enemy uses a path generated by the A* algorithm. The heuristic used is simply the manhattan distance between the start and end tiles. Moving to a tile that is "in range" of the player (ie where the enemy could be shot) has a higher cost than moving to an out-of-range tile. |
| Your game must meet at least two of the extra game requirements (that weren’t used for last week’s game requirements): Add a floating minimap [x2] | I have a floating minimap in the top right corner that displays the map as well as the player's location on it. |

## Extras:
| Requirement | Location in code or steps to view in game  |
|---|---|

--------------------------------------------------------------

Instructions on how to run

Known bugs: None

Hours spent on assignment: 14
