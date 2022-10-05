# Alc 1 Handin README
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
| The engine must have a “viewport” UI element that draws the GameWorld from a different coordinate system onto the screen coordinate system. The viewport must support panning (translation of the currently viewable game state). | The Viewport class is located in `engine/display/Viewport`. It contains a `GameWorld`, and displays the game world on the screen by translating game world coordinates to screen coordinates uing an `Affine` in the `onDraw` function. It supports panning through the `onKeyPressed` (and `onKeyReleased` functions).
| Your engine must have a System class that contains GameObjects. | The `GameSystem` class is located in `engine/game/systems/GameSystem` and contains an ArrayList of game objects. |
| Your engine must have a class representing the game world. That class must hold at least one system. | The `GameWorld` class is located in `engine/game/world/GameWorld` and it contains an ArrayList of systems. |
| The viewport should be visible once the game is run (either directly or through a menu). The player must be able to pan the viewport (the viewport cannot display a solid color). | When you run the game and press the "play" button, the viewport is visible and takes up the entire screen. You can pan with either the arrow keys or wasd. |
| There must be a “unit” visible in the viewport. | There is a white and gray rectangular menu containing a unit, which is a unit. |
| The “unit” must respond to being clicked (only when the unit itself is clicked). | When you click and drag the unit ("unit"), it will be dragged around the screen. |
| The “unit” must be able to be dragged freely. | This is true. When you drag the unit, it moves freely around the screen. |
| The “unit” should be drawn and moved using separate components. | The unit is drawn using the `Drawable` component, although this `onDraw` method is overridden to draw a simple unit instead of an image. It is dragged using the `Draggable` component. Both of these components are located in `engine/game/components`. |


## Secondary Requirements:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Your engine must meet all primary engine requirements | See above |
| The viewport must support zooming (scaling of the currently viewable GameState). When zooming, the viewport must either keep the center of the game in the same position or keep the location currently under the mouse in the same position. | You can zoom in an out be scrolling. The center of the screen is not maintained, which is a bug that I plan to fix for Alc 2. |
| The player must be able to zoom the map (make sure the unit still responds to being clicked!). | See above. The player can zoom in and out on the map, and the unit still can be dragged properly, however the center is not maintained when zooming. |
| There must be a menu from which the player can make a copy of a “unit” by clicking and dragging it, without the original being destroyed or moved. | There is a menu from which the player can click and drag the unit ("unit"). Dragging will create a duplicate of the unit in the menu that the player drags. |

## Extras:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Give your viewport limits on viewing parameters | The user cannot pan out of view of the game world. They can only zoom out far enough to show the whole world, then no further. They can zoom in to 1/8th of the world, but no further. This can be seen by panning to edges of the screen, where once you reach an edge, it will stop panning. Additionally, you can zoom out to see the whole screen or in to see part of it, and it will stop once you've reached the limits and stop zooming. |

--------------------------------------------------------------

Instructions on how to run

Known bugs: As described above, zooming does not work properly, as the center is not maintained properly. Additionally, resizing will cause some objects to shift slightly to the bottom right. Both of these bugs, I plan to fix for the next submission of Alchemy. Finally, when the mouse is released, a click event is fired. This means that if a user drags a unit over to the back button, then releases it while the mouse is over the back button, it will click the button and send the user back to the menu.

Hours spent on assignment: 30
