package com.grillecube.server;

public class Main
{
	public static void	main(String[] args)
	{
		Game	game;
		
		game = new Game();
		game.start();
		game.loop();
		game.stop();
	}
}
