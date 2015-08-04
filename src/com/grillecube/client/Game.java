
package com.grillecube.client;

import java.util.ArrayList;

import com.grillecube.client.mod.blocks.ModBlocks;
import com.grillecube.client.mod.renderer.particles.ModParticles;
import com.grillecube.client.renderer.MainRenderer;
import com.grillecube.client.ressources.ResourceManager;
import com.grillecube.client.window.GLWindow;
import com.grillecube.client.world.WorldClient;
import com.grillecube.common.mod.ModLoader;

import fr.toss.lib.Logger;
import fr.toss.lib.Logger.Level;

public class Game
{
	/** game instance */
	private static Game	_instance;
	
	/** Mod loaded */
	private ModLoader	_mod_loader;
	
	/** Logger */
	private Logger	_logger;
	
	/** Game window */
	private GLWindow	_window;
	
	/** World */
	private WorldClient	_world;
	
	/** Renderer */
	private MainRenderer	_renderer;
	
	/** Resource manager */
	private ResourceManager	_resources;
	
	/** Game state */
	public static final long STATE_RUNNING = 1;
	private long _state;

	private ArrayList<Thread>	_threads;
	
	public Game()
	{
		_instance = this;
		this._logger = new Logger(System.out);
		this._threads = new ArrayList<Thread>();
		this._state = 0;
		this._window = new GLWindow();
		this._renderer = new MainRenderer(this._window);
		this._world = new WorldClient();
		this._resources = new ResourceManager();
		this._mod_loader = new ModLoader();
		this._mod_loader.loadMods("./mods");
		
		//TODO : default mods are injected here
		this._mod_loader.injectMod(new ModBlocks());
		this._mod_loader.injectMod(new ModParticles());
	}
	
	public void	start()
	{
		this._logger.log(Level.FINE, "Starting game...");
		this._state = 0;
		this._window.start();
		
		this._mod_loader.initializeAll(this);
		
		this._renderer.start(this);
		this._resources.start();
		this._world.start();
				
		this._logger.log(Level.FINE, "Game started!");
	}

	/** main game loop (dedicated to rendering) */
	public void loop()
	{
		this.setState(STATE_RUNNING);
				
		long	prev = System.currentTimeMillis();
		int		frames = 0;
		
		for (Thread thrd : this._threads)
		{
			thrd.start();
		}

		while (this._window.shouldClose() == false)
		{
			if (System.currentTimeMillis() - prev >= 1000)
			{
				log(Logger.Level.DEBUG, "fps: " + frames);
				frames = 0;
				prev = System.currentTimeMillis();
			}
			
			this._window.prepareScreen();
			this._renderer.update();
			this._renderer.render();
			this._window.flushScreen();
			
			frames++;
		}
		
		this.unsetState(STATE_RUNNING);
	}

	/** stop the game properly */
	public void	stop()
	{
		this._logger.log(Level.FINE, "Stopping game...");

		for (Thread thrd : this._threads)
		{
			try
			{
				thrd.join();
			}
			catch (InterruptedException e)
			{
				log(Logger.Level.ERROR, "interupted");
				thrd.interrupt();
			}
		}
		this._mod_loader.deinitializeAll(this);
		this._window.stop();
		this._renderer.stop();
		this._logger.log(Level.FINE, "Stopped");
	}
	
	/** register a thread which will be launch when the game will be launched, and stopped when the game ends*/
	public void	registerThread(Thread thrd)
	{
		this._threads.add(thrd);
	}
	
	public boolean	hasState(long state)
	{
		return ((this._state & state) == state);
	}
	
	public void		setState(long state)
	{
		this._state = this._state | state;
	}
	
	public void		unsetState(long state)
	{
		this._state = this._state & ~(state);
	}
	
	public void		switchState(long state)
	{
		this._state = this._state ^ state;
	}

	public static Game	instance()
	{
		return (_instance);
	}
	
	public static void	log(Level level, String message)
	{
		_instance.getLogger().log(level, message);
	}

	public Logger	getLogger()
	{
		return (this._logger);
	}

	public WorldClient getWorld()
	{
		return (this._world);
	}

	public MainRenderer	getRenderer()
	{
		return (this._renderer);
	}
	
	public ResourceManager	getResourceManager()
	{
		return (this._resources);
	}

	public GLWindow getGLWindow()
	{
		return (this._window);
	}
}
