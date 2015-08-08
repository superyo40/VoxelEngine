package com.grillecube.client.ressources;

import java.util.ArrayList;

public class ResourceManager
{
	/** Resources */
	private ArrayList<IResource> _resources;
	
	/** texture loader */
	private TextureManager _texture_loader;
	
	/** Block resources */
	private BlockManager _block_manager;
	
	/** Font resources */
	private FontManager _font_manager;

	public ResourceManager()
	{
		this._resources = new ArrayList<IResource>();
		this._texture_loader = new TextureManager();
		this._block_manager = new BlockManager(this);
		this._font_manager = new FontManager();
	}
	
	/** inject resources */
	public void	addRessource(IResource resource)
	{
		this._resources.add(resource);
	}

	/** load every game resources */
	public void	start()
	{		
		for (IResource res : _resources)
		{
			res.load(this);
		}
		this._block_manager.createBlocks();
	}
	
	/** load every game resources */
	public void	stop()
	{
		for (IResource res : _resources)
		{
			res.unload(this);
		}
	}

	public BlockManager getBlockManager()
	{
		return (this._block_manager);
	}

	public TextureManager getTextureManager()
	{
		return (this._texture_loader);
	}
}
