@echo off
::This file compiles all of the class files into a Java archive file titled 'NN 2-D Sidescroller'.

cd C:/Users/Owner/Java/"NN 2-D Sidescroller"/bin

jar cvfm "NN 2-D Sidescroller".jar manifest.txt geneticAlgorithm/GeneticAlgorithm.class geneticAlgorithm/Genome.class graphics/StdDraw.class main/Controller.class marioGame/Enemy.class marioGame/Goomba.class marioGame/Ground.class marioGame/Level.class marioGame/MarioGame.class marioGame/Player.class marioGame/Toucheable.class neuralNetwork/NeuralNetwork.class neuralNetwork/Neuron.class neuralNetwork/NeuronLayer.class graphics/"16-bit Mario".png graphics/"Mario brick block".png graphics/"Super Mario World goomba".gif graphics/"Super Mario World small Mario jumping".png graphics/"Super Mario World small Mario walking".gif graphics/Minecrafter.ttf
