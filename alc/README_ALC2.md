# Alc 2 Handin README
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
| Your engine must support collision detection between points, circles, and AABs. This includes a collision system and collision behaviors. | I created a `Collidable` component (`engine/game/components/Collidable`) that holds a `Shape` (`engine/game/objects/shapes/Shape`) that can be either an AAB or Circle. The `Collidable` component uses the `Shape` methods to check whether an object is colliding with another object, and calls the appropriate `onCollide` function if they do. Additionally, I created a collision system (`engine/game/systems/CollisionSystem`) that uses the `Collidable` components to check whether objects have collided on every tick. |
| Your game should display at least 2 “units” in your viewport. | Upon running the game, there are 4 units visible: air, earth, fire, and water. |
| The player should be able to move the “units” around the screen. | The player can move the units by clicking and dragging them around the screen. |
| The “units” should have sprites. | Each unit has a sprite, which is found in the `alc/resources` folder. The files are loaded into `Image`s by the `Resource` class (also in the `alc/resources` folder), then drawn by the `HasSprite` component (`engine/game/components/HasSprite`) |
| You must have at least 2 base “units”/elements, which can be combined to form a new element. | There are four base elements: air, earth, fire, and water. |
| You must be able to add elements to the work-space by dragging them from the menu. | Dragging items from the menu creates a clone that is added tot he game world and can be seem on the map. |
| Your game must complete the debugger. Using the debugger, the TAs should quickly be able to verify collisions between any pair of the following: points, circles, and AABs. | Week 2 of the debugger works. |

## Secondary Requirements:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Your engine must meet all primary engine requirements | See above |
| Your engine must include a sprite component. | A `HasSprite` component is located in `engine/game/components/HasSprite` |
| Sprites should only be loaded once. | Each sprite is loaded once by the `Resource` class (`alc/resources/Resource`), then saved as an `Image` and this `Image` is passed around when drawing the sprite. |
| Your game must meet all primary game requirements. | See above |
| You must be able to remove elements from the work-space by dragging them to the menu or some sort of “trash” area. | There is a trash bin located below the menu, and dragging units to the trash will delete them. |
| You must have at least 4 base “units”/elements and at least 3 “units”/elements the player can make through combinations, one of which must be made by combining two non-base “units”/elements, and one of which must be a final “unit”/element (cannot be combined with anything). | There are 4 base unit elements (air, earth, fire, water) and 5 units the player can make through combinations (sun, sprout, tree, dead tree, mushroom). Of the units made through combinations, 2 are made through combining non-base elements (tree and mushroom) and one is a final element (mushroom). See the instructions file for a list of all possible unit combinations. |



## Extras:
| Requirement | Location in code or steps to view in game  |
|---|---|

--------------------------------------------------------------

Instructions on how to run

Known bugs: None

Hours spent on assignment: 20
