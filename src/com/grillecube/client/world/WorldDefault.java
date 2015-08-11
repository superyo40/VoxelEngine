package com.grillecube.client.world;

import org.lwjgl.util.vector.Vector3f;

import com.grillecube.client.renderer.model.Models;
import com.grillecube.client.world.entity.EntityModeled;

public class WorldDefault extends World
{
	@Override
	public String getName()
	{
		return ("Default");
	}

	@Override
	public void start()
	{
		for (int x = -8 ; x < 8 ; x++)
		{
			for (int z = -8 ; z < 8 ; z++)
			{
				for (int y = -2 ; y < 2 ; y++)
				{
					this.spawnTerrain(new Terrain(new TerrainLocation(x, y, z)));
				}
			}
		}
		
		this.spawnEntity(new EntityModeled(Models.getModel(Models.PLAYER))
				{
					@Override
					protected void updateEntity() {}
			
				}, new Vector3f(-4, 0, 0));
	}
}
