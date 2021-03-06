package com.grillecube.client.renderer.model;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.grillecube.client.opengl.GLH;
import com.grillecube.client.opengl.object.VertexArray;
import com.grillecube.client.opengl.object.VertexBuffer;
import com.grillecube.common.logger.Logger;

public class ModelPart
{
	/** model part name */
	private String _name;
	
	/** opengl */
	private VertexArray _vao;
	private VertexBuffer _vbo;
	private int	_vertex_count;

	/** animations */
	private ArrayList<Animation> _animations;
	private ArrayList<BoundingBox> _boxes;
	
	/** model part state */
	private static final int STATE_ININITIALIZED = 1;
	private int	_state;

	public ModelPart()
	{
		this("unknown");
	}
	
	public ModelPart(String name)
	{
		this._name = name;
		this._state = 0;
		this._vao = null;
		this._vbo = null;
		this._vertex_count = 0;
		this._animations = new ArrayList<Animation>();
		this._boxes = new ArrayList<BoundingBox>();
	}

	/** initalize opengl vao / vbo */
	private void prepareMesh()
	{
		this._vertex_count = 0;
		this._vao = GLH.glhGenVAO();
		this._vbo = GLH.glhGenVBO();
		
		this._vao.bind();
		this._vbo.bind(GL15.GL_ARRAY_BUFFER);
		
		this._vao.setAttribute(0, 3, GL11.GL_FLOAT, false, 3 * 4 + 4 * 4, 0); //xyz
		this._vao.setAttribute(1, 4, GL11.GL_FLOAT, false, 3 * 4 + 4 * 4, 3 * 4); //rgba
		
		this._vbo.unbind(GL15.GL_ARRAY_BUFFER);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		this._vao.unbind();
	}
	
	/** send vertices to opengl */
	private void setMeshData(float[] vertices)
	{
		this._vbo.bufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		this._vertex_count = vertices.length / 7;
	}
	
	/** set modelaprt vertices, should only be called once ! */
	public void	setVertices(float[] vertices)
	{
		if (this.hasState(STATE_ININITIALIZED))
		{
			Logger.get().log(Logger.Level.ERROR, "Tried to set vertices severals time on the same ModelPart (setVertices())");
			return ;
		}
		this.prepareMesh();
		this.setMeshData(vertices);
		this.setState(STATE_ININITIALIZED);
	}
	
	/** return animations for this modelpart */
	public ArrayList<Animation> getAnimations()
	{
		return (this._animations);
	}
	
	/** return the animation at index i */
	public Animation getAnimationAt(int i)
	{
		if (i < 0 || i >= this._animations.size())
		{
			return (null);
		}
		return (this._animations.get(i));
	}
	
	/** return the animation with the given id */
	public Animation getAnimationByID(int id)
	{
		for (Animation animation : this._animations)
		{
			if (animation.getID() == id)
			{
				return (animation);
			}
		}
		return (null);
	}
	
	/** add an animation to the modelpart */
	public void addAnimation(Animation animation)
	{
		this._animations.add(animation);
	}
	
	/** add a bounding box to this model part */
	public void addBoundingBox(BoundingBox box)
	{
		this._boxes.add(box);
	}
	
	/** set a state */
	public void	setState(int state)
	{
		this._state = this._state | state;
	}
	
	/** unset a state */
	public void	unsetState(int state)
	{
		this._state = this._state & ~(state);
	}
	
	/** return true if the state is set */
	public boolean	hasState(int state)
	{
		return ((this._state & state) == state);
	}

	/** render the model part */
	public void render()
	{
		this._vao.bind();
		this._vao.enableAttribute(0);
		this._vao.enableAttribute(1);
		
		this._vao.draw(GL11.GL_QUADS, 0, this._vertex_count);

		this._vao.disableAttribute(0);
		this._vao.disableAttribute(1);
		this._vao.unbind();	
	}

	/** get model part name */
	public String getName()
	{
		return (this._name);
	}

	/** return the number of vertex for this model part */
	public int	getVertexCount() 
	{
		return (this._vertex_count);
	}
	
	/** delete the model */
	@SuppressWarnings("unused")
	private void delete()
	{
		GLH.glhDeleteVAO(this._vao);
		GLH.glhDeleteVBO(this._vbo);
	}
}
