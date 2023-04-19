# Role Playing Game

For the final project of Object Oriented Programming, your team should create an RPG game. While there are some requirements to the outcome, which are listed below, you are mostly free in how you give shape to this project. For example, you may choose to have your RPG reflect an epic quest in a fantasy setting, an anxious protagonist buying groceries from a list, or fireside conversations among friends camping in the wild.

To take full advantage of object oriented programming, each member of the team should be responsible for different aspects of the game. Unlike most assignments, where you are expected to work together for every step on the assignment, this final project explicitly asks you to divide responsibilities among your team members. Of course, you should still discuss, cooperate, and help one another out. However, each member should become an expert on their part of the program, which they will also demonstrate during the final demos.

Below are the requirements for your RPG game. During assessment, we will focus on the design choices you have made while making the game. For example, your use of interfaces and abstract classes, proper encapsulation, design patterns, as well as how easy it is for a third party to understand the game logic and expand it with new variations and features. In addition, we look at programming style (indentation, variable naming, etc) and the level of ambition. That is, if your final program meets all the requirements below, but only just barely, it may not result in a sufficient grade.

Your RPG should feature the following:
- The player must be able to interact with NPC characters. The form of this interaction (e.g. conversation, combat, gestures) is free. The interaction should, in some cases, have lasting effects on the game state. 
- There must be traversable locations. That is, the player should be able to interact with their environment.
- There must be an inventory system that can potentially hold multiple items. This could be used to, for example, keep track of items in a grocery cart, members of a party, or conversation topics available to the player.
- There must be multiple types of player classes that meaningfully change the options open to the player. For example, you may distinguish between warriors that wield armor and wizards that can use magic, player characteristics like empathy or logic that change dialogue options, or social status that grants or prevents access to certain locations.
- The player should be able to save and restore their game state at any time they choose.
- The game logic itself should be independent of the way it is presented to the player. That is, your game should follow the guidelines of the Model-View-Controller pattern. Note, however, that you are free to choose whether the game has a text-based interface, a GUI, or even some other way of connecting the player to the game. The game should be playable on the university computers without additional hardware or software.
- Each class should contain the `@author` Javadoc tag to indicate the main author. During the demo, questions about that class will be asked to the listed `@author`. Additionally, the progamming style and internal design choices of a class contribute only to the grade of the listed `@author`.

