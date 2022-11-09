# TIC Handin README
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
| Your engine must draw the current screen on every “draw” event (originating from support code). Each screen must be able to define the way in which it is drawn independently from other screens | In `engine/Application`, when the `onDraw` function is called on an `Application`, it runs the `onDraw` function of each `Screen` associated with that `Application` (in the protected `screens` field). Each screen draws itself by drawing the UIElements on that screen, which are unique to each instance of `Screen`. Additionally, `Screen` subclasses can override the `onDraw` function for further customization. |
| Your engine must be able to process mouse events (originating from support code) and allow each screen to define how they are handled | The only mouse events being processed are clicks and moves, which are the only two events that I wanted the user to be able to use to interact with the app. `Application` calls the corresponding methods (`onMouseClicked` or `onMouseMoved`) on each `Screen`, and each `Screen` calls the corresponding functions on its `UIElement`s. These functions will handle the input appropriately, calling the relevant functions and updating relevant variables as necessary. |
| Your engine must be able to process keyboard events (originating from the support code) and allow each screen to define how they are handled | As my implementation of Tic is played entirely by clicking the screen, the only keyboard event that it needed to handle was hitting the escape key to quit the game. This is handled in the `onKeyPressed` function in `engine/Application`, so when the escape key is pressed, the app will shutdown. Pressing any other key will be ignored. |
| Your current screen must update itself on every “tick” event (originating from the support code) | In `engine/Screen`, the `Screen` class includes an `onTick` function that calls the `onTick` function associated with each `UIElement` of that Screen. Thus, each `UIElement` will be updated appropriately. |
| Your engine must be able to process resizable windows (originating from support code) and adjust the internal state when the draw area is resized. It must also ensure that size information is preserved when the current screen changes – if the current screen changes after a resize event, the new current screen must be aware of the correct window size as well | Each `Screen` object has fields that hold the current window size and current screen size. When the `Application` is resized, it call `onResize` on each `Screen`, so every screen will update its windowSize and screenSize values (whether the screen is currently being displayed or not). Additionally, it will call `onResize` on each `UIElement` on the screen, so that each `UIElement` updates its size and position to scale with the newly sized window. |
| Your engine should have a basic UI toolkit. At a minimum, this toolkit should allow a game to display text and rectangles | In `engine/uiElements`, both a general `UIElement` class and several more specific subclasses are defined. This allows the game to display a background, text, a timer, buttons, and an arrow-shaped back button. |
| Your engine must have a correct and easily extensible implementation of a button | This is in `engine/uiElements/Button`, and is currently extended by `BackButton` in `engine/uiElements` and by `Space` in `tic/uiElements`. |
| Your handin must meet all playtesting requirements | The app is able to be playtested. |
| A 3x3 block board must be accessible from the screen once the application is run (either directly or through a menu) | A 3x3 block board is visible when you run the app, then click the "play" button on the menu screen that appears. This is a `Board` object created in the `GameScreen` class in `tic/screens/GameScreen` |
| An X or an O must appear on a box when that box is clicked | Once you run the application, click "play" from the menu screen, and click any box on the board, an X or O should appear depending on whose turn it is. This is handled by the `Board` class, which contains `Space` objects (where each `Space` is a button on the board that can be clicked). |
| Your game must implement the rules of Tic-Tac-Toe: two players, X and O, take turns marking squares on a 3x3 grid with their respective symbols. If a player succeeds in placing three symbols in a horizontal, vertical, or diagonal row, that player wins. If all the squares are filled without either player completing a row, the game is a draw. Both players can be human players: you do not need an AI opponent for this assignment | This can be seen by running the application, clicking the "play" button, then playing through the game by placing X's and O's on the board. Game logic such as switch which player's turn it is and resetting the timer is handled in the `GameScreen` class in `tic/screens/GameScreen` |
| Have at least two screens: an in-game screen and another screen, such as a title screen | I have 3 screens: the menu screen (`MenuScreen`) that has navigation buttons and appears when the app is started, the in-game screen (`GameScreen`) where you play that shows the board, timer, etc, and the game over screen (`GameOverScreen`) that appears when the game is over and shows who won and navigation buttons. These screens are instantiated in `tic/App` |
| Clearly display which player’s turn it is | There is a `Text` object at the bottom of the in-game screen that displays whose turn it is. This is created in the `GameScreen` class |
| At the end of the game, effectively communicate which player won, or if it was a draw | Once the game is over, a new screen (`GameOverScreen`) appears that displays who won or if it was a draw in large text. |
| Display the state of the game on a block board that scales with window size. The board must remain block at all times, no matter the window’s aspect ratio | This should be visible by resizing the window.  |
| Your game must implement keyboard events (e.g., exit game on escape) | This is implemented in the `onKeyPressed` function in `engine/Application`. |
| Your game must never crash. | Through testing, I haven't found a way to make the game crash. |


## Secondary Requirements:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Your engine must meet all primary engine requirements | See above |
| Your buttons should display differently when they are hovered | Each `Button` and `Text` element has a field called `highlighted` that is set when the mouse moves (in `onMouseMoved`). If the mouse is over the button/text, it will display in white instead of pink. |
| Each player’s turn should have a time limit. If the player does not make a move when the time expires, it becomes the other player’s turn and the timer resets | The `GameScreen` has a `Timer` object that handles this. It counts down 10 seconds, then runs out. `GameScreen` checks every tick whether the timer has run out, and if it has, it switches the player's turn. |
| Show the countdown timer (e.g., shrinking bar, text in seconds) | A `Timer` object is drawn on the `GameScreen`. This is visible when playing the game. It only starts running after the first player plays a move (not when you click the play button), which indicates tha game starting. |
| It must be possible to start a new game without restarting the program | The `GameOverScreen` includes a "restart" button, and clicking on it will cause you to restart the game. You can also restart in the middle of a game by exiting to the menu (through the back button in the upper left), then clicking "play" again. |

## Extras:
| Requirement | Location in code or steps to view in game  |
|---|---|
| [Copy and paste from handout] | ```File path, function name, or steps to replicate``` |

--------------------------------------------------------------

Instructions on how to run

Known bugs: `UIElement`s that are updated when the mouse moves are **only** updated when the mouse moves. So, for example, if one hovers over a space on the board so that a ghost X appears, then waits without moving for the timer to run out and the turn to switch, when the turn switches to O, the text at the bottom will update to say its O's turn and the timer will reset, but the ghost X will still be an X (until you move or click the mouse, at which point it would switch to an O). This issue also appears with buttons being highlighted.

Hours spent on assignment: 25
