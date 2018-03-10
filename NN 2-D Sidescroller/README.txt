Release: 2017/09/08
Version 1.2: 2017/11/16
_________________________________________________________________
~Version 1.2:
-Reverted genetic algorithm back to stable release version
-Fixed portability issue with graphics
-Fixed/Added correct font
_________________________________________________________________
~Version 1.1:
-Changed genetic algorithm to mutate more efficiently.
_________________________________________________________________


Hello user! This program was coded by William Ritchie, after following AI-Junkie's neural network guide and genetic algorithm guide. This program, unaltered, will simulate a simple Mario platformer and hook up a neural network and genetic algorithm so that the computer can "play itself". It generates 10 instances of Marios for the genetic algorithm and displays the Mario's x position and y position, as well as which Mario the program is displaying on screen (designated by "ID"). The program halts one of its Marios when either the timer (shown in the top right of the screen) reaches true 0 (-1), or when the Mario instance falls below a certain y value. All Mario's are given one task: Go as far right as possible/end its run at the highest x value possible.
AI junkie's website can be found here: http://www.ai-junkie.com/ann/evolved/nnt1.html

The circles around the Player instance are visual indicators of what the Player instance "sees" either nothing, Block, or Goomba.
*NOTE* Run the java archive file from a command line/terminal to view outputs from the program like the individual fitness scores for each Player instance, the average fitness score, the generation count, and crossover alerts.
If you repeatedly receive fitness scores of 0 or there is no improvement for at least 3 generations in a row, restart the application. You just got a bad start and it would've taken a long time for the Marios to learn!

------------------------------------------------------------------------------------------------------------------
For no IDE users:
Enter the "bin" folder and double click on "NN 2-D Sidescroller".jar.

For Eclipse IDE users:
Enter the src folder and open the main folder. Inside, click Controller.java. Compile and run!

For manual compilation:
Run the makefile "make.bat" in command line and then execute!
------------------------------------------------------------------------------------------------------------------

Coded in Eclipse Oxygen (java language)
I can be reached at: williamritchie@hotmail.ca or ritchi14@myumanitoba.ca