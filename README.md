# World's Hardish Game
Developed by Farran Regan and Josh Abramson  
Brown University Department of Computer Science  
CSCI 1950n Fall 2022

## Inspiration
This project was inspired by the [World's Hardest Game](https://www.coolmathgames.com/0-worlds-hardest-game), an iconic flash game developed by Snubby Land and published by Armor Games.  

## Development
We first developed a robust, generalizable game engine that can be used to create almost any type of 2D video game. Then, we used this engine to build the World's Hardish Game, inspired by the World's Hardest Game. Although not featured in this game, our engine supports procedural map generation, physics, enemy AI, and most other common game features.  
<br>
This project, both engine and game, were developed entirely from scratch, with absolutely minimal boilerplate and external packages. The only external dependency used is JavaFX, which was used primarily for painting to a canvas using `GraphicsContext` and capturing user input events. All objects, such as `Shape` and `TextField`, were written from scratch.

## Playing the game
To play the World's Hardish Game, clone this repository and run the `last/Main.java` file.