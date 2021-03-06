
package com.grillecube.client;

import java.util.ArrayList;

import com.grillecube.client.event.IEvent;
import com.grillecube.client.lib.SharedLibraryLoader;
import com.grillecube.client.mod.blocks.ModBlocks;
import com.grillecube.client.mod.renderer.particles.ModParticles;
import com.grillecube.client.renderer.MainRenderer;
import com.grillecube.client.ressources.ResourceManager;
import com.grillecube.client.window.GLWindow;
import com.grillecube.client.world.World;
import com.grillecube.client.world.WorldManager;
import com.grillecube.common.logger.Logger;
import com.grillecube.common.logger.Logger.Level;
import com.grillecube.common.mod.ModLoader;

public class Game
{
	/** game instance */
	private static Game	_instance;
	
	/** Mod loaded */
	private ModLoader _mod_loader;
	
	/** Game window */
	private GLWindow _window;
	
	/** World */
	private WorldManager _world_manager;
	
	/** Renderer */
	private MainRenderer _renderer;
	
	/** Resource manager */
	private ResourceManager	_resources;
	
	/** Game state */
	private GameStateFactory _state_factory;
	private GameState _state;

	/** game threads */
	private ArrayList<Thread> _threads;
	
	/** game events */
	private ArrayList<IEvent>[] _events;

	@SuppressWarnings("unchecked")
	public Game()
	{
		_instance = this;
		SharedLibraryLoader.load();
		this._threads = new ArrayList<Thread>();
		this._events = new ArrayList[GameEvent.MAX_ID];
		for (int i = 0 ; i < GameEvent.MAX_ID ; i++)
		{
			this._events[i] = new ArrayList<IEvent>();
		}
		this._state_factory = new GameStateFactory();
		this._state = GameState.DEFAULT;
		this._window = new GLWindow();
		this._world_manager = new WorldManager();
		this._renderer = new MainRenderer(this);
		this._resources = new ResourceManager();
		this._mod_loader = new ModLoader();
	}
	
	/** main start function : prepare windows, setGL context, start mods, world, resources ... */
	public void	start()
	{
		this.injectMods();
		
		this.invokeEvents(GameEvent.PRE_START);
		
		Logger.get().log(Level.FINE, "Starting game...");
		
		this._window.start();
		this._mod_loader.initializeAll(this);
		this._renderer.start(this);
		this._resources.start();
		this._world_manager.start();
		
		Logger.get().log(Level.FINE, "Game started!");
		
		this.invokeEvents(GameEvent.POST_START);		
	}
	
	/** invoke every events with the given ID */
	private void invokeEvents(int eventID)
	{
		for (IEvent event : this._events[eventID])
		{
			event.invoke(this, eventID);
		}
	}
	
	/** add an event callback */
	public void registerEventCallback(IEvent callback, int eventID)
	{
		this._events[eventID].add(callback);
	}

	/** load every mods */
	private void injectMods()
	{
		this._mod_loader.loadMods("./mods");
		
		this._mod_loader.injectMod(new ModBlocks());
		this._mod_loader.injectMod(new ModParticles());
	}

	/** main game loop (dedicated to rendering) */
	public void loop()
	{
		this._state.set(GameState.RUNNING);
		
		for (Thread thrd : this._threads)
		{
			thrd.start();
		}
		
		while (this.isRunning())
		{
			this._window.prepareScreen();
			this._renderer.update();
			this._renderer.render();
			this._window.flushScreen();
			
			if (this._window.shouldClose())
			{
				this.stop();
			}
		}
		
		this.stopAll();
	}

	/** request the game to stop */
	public void stop()
	{
		this._state.unset(GameState.RUNNING);
	}
	
	/** stop the game properly */
	private void stopAll()
	{
		Logger.get().log(Level.FINE, "Stopping game...");

		this._state.unset(GameState.RUNNING);

		for (Thread thrd : this._threads)
		{
			try
			{
				thrd.join();
			}
			catch (InterruptedException e)
			{
				Logger.get().log(Logger.Level.ERROR, "interupted");
				thrd.interrupt();
			}
		}
		this._mod_loader.deinitializeAll(this);
		this._window.stop();
		this._renderer.stop();
		Logger.get().log(Level.FINE, "Stopped");
		
		this.invokeEvents(GameEvent.STOP);
	}
	
	/** register a thread which will be launch when the game will be launched, and stopped when the game ends*/
	public void	registerThread(Thread thrd)
	{
		this._threads.add(thrd);
	}

	/** register a new game state */
	public GameState registerNewState()
	{
		return (this._state_factory.registerNewState());
	}
	
	/** return current game state */
	public GameState getState()
	{
		return (this._state);
	}

	/** return game instance */
	public static Game instance()
	{
		return (_instance);
	}

	/** return the world */
	public WorldManager getWorldManager()
	{
		return (this._world_manager);
	}

	/** return the current world the player is in */
	public World getCurrentWorld()
	{
		return (this._world_manager.getWorld(WorldManager.WORLD_DEFAULT));
	}
	
	/** return the main renderer */
	public MainRenderer	getRenderer()
	{
		return (this._renderer);
	}
	
	/** return the main resource manager */
	public ResourceManager getResourceManager()
	{
		return (this._resources);
	}

	/** return the window in use */
	public GLWindow getGLWindow()
	{
		return (this._window);
	}

	/** return true if the game is running */
	public boolean isRunning()
	{
		return (this.getState().has(GameState.RUNNING));
	}

	/** return the given events lists */
	public ArrayList<IEvent> getEvents(int eventID)
	{
		return (this._events[eventID]);
	}
}
