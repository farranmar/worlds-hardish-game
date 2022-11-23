# Nin I Handin README
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
| Your engine must contain a physics behavior that correctly holds and updates mass, force, impulse, velocity, and acceleration. | The physics component in `engine/game/components/PhysicsComponent` stores all of these values, and updates them appropriately on each tick. When two objects collide, the impulse generated is calculated in the `onCollide` function of `GameObjects` in `engine/gae/objects/GameObject`. |
| Your engine must handle static objects and restitution. | Restitutions are stored in the `CollideComponent` of each collidable object, and used in the `onCollide` function in `GameObject` to calculate impulses. Static objects are also handled in this `onCollide` function, where they are not moved during collisions and treated as though they have infinite mass when calculating impulses. |
| Your game must contain a player-controller unit above an immobile platform. | The small square is a player-controllable unit, and there are two immobile platforms that it can stand on. |
| The player-controller unit must fall when in the air. | When going off the platform or jumping, the player falls through the air. |
| The player-controller unit must not fall through the platform. | The player can stand on platforms without falling through, which is impemented by turning off gravity when downward rays (`GravityRay`s in `nin/game/objects/GravityRay`) on the bottom two corners collide with the platform. |
| The player-controller unit must have a constant downward acceleration. | The player has a constant downward acceleration, which is blocked by platforms but visible when falling through the air. |
| The player-controller unit must be able to jump, but only when standing on top of a platform. | The player can jump by pressing shift, and can only do so when standing on a platform. |
| Your game must have three objects with visibly different restitution values that can collide with each other. | The three larger squares all have different restitution values (green=0.1, purple=0.75, blue=1) and can collide with each other by being pushed into each other by the player. |
| Your game never crashes. | Through extensive testing, I have never been able to get my game to crash. |

## Secondary Requirements:
| Requirement | Location in code or steps to view in game  |
|---|---|
| Your engine must meet all primary engine requirements | See above |
Unfortunately, due to spending extensive time trying to debug my physics, I didn't have time to implement the secondary requirements. However, I plan on making sure I can support convex polygon in my engine by the end of Nin II.

## Extras:
| Requirement | Location in code or steps to view in game  |
|---|---|

--------------------------------------------------------------

Instructions on how to run

Known bugs: The blocks with nonzero restitution values don't stop bouncing as they're supposed to. During debugging, I ran into two possible causes, both of which I couldn't find a way to solve.
One issue is that occasionally, the `nanosSincePreviousTick` variable would hold a value as large as many billions, resulting in the code thinking there was upwards of 5 minutes of time between ticks (when there most certainly was not). I couldn't find what was causing this issue,
as I never altered the `nanosSincePreviousTick` value when passing it around the `onTick` functions. The other issue I found was that occasionally, the downward velocity of the block would increase from one bounce to the next, when it should only decrease (unless of course the restitution is greater than 1, which it is not for any of my objects).
I spent a couple hours trying to find where this was occuring, but I couldn't figure out what was causing it.

Hours spent on assignment: 22
