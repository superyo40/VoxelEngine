package com.grillecube.client.renderer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.grillecube.common.logger.Logger;

/**
 * 
 * @author Romain
 * 
 * Animation:
 * 		animation_id: 	animation
 *		frames:			animations frames
 */

public class Animation
{
	/** every animation's frames */
	private ArrayList<AnimationFrame> _frames;
	
	/** animation name and id */
	private String _name;
	private int _animationID;
	
	/** animation duration (timer of the last animationframe, or 0 if there is no frames) */
	private float _duration;
	
	/** frame comparator, to always make them sorted by time */
	private Comparator<AnimationFrame> _frame_comparator = new Comparator<AnimationFrame>()
	{
		public int compare(AnimationFrame a1, AnimationFrame a2)
		{
			if (a1.getTime() == a2.getTime())
			{
				return (0);
			}
			if (a1.getTime() < a2.getTime())
			{
				return (-1);
			}
			return (1);
		}
	};

	public Animation()
	{
		this("undefined", 0);
	}
	
	public Animation(String name, int id)
	{
		this._name = name;
		this._animationID = id;
		this._frames = new ArrayList<AnimationFrame>();
		this._duration = 0;
	}
	
	/** add a frame */
	public void addFrame(AnimationFrame frame)
	{
		if (frame == null)
		{
			Logger.get().log(Logger.Level.WARNING, "Tried to add a NULL frame to : " + this.toString());
			return ;
		}
		this._frames.add(frame);
		Collections.sort(this._frames, this._frame_comparator);
		this._duration = this._frames.get(this._frames.size() - 1).getTime();
	}

	public ArrayList<AnimationFrame> getFrames()
	{
		return (this._frames);
	}
	
	public float getDuration()
	{
		return (this._duration);
	}
	
	/** return animation ID */
	public int getID()
	{
		return (this._animationID);
	}
	
	/** return animation name */
	public String getName()
	{
		return (this._name);
	}

	/** return number of frames for this animation */
	public int getFramesCount()
	{
		return (this._frames.size());
	}

	public AnimationFrame getFrameAt(int i)
	{
		int size = this._frames.size();
		
		if (size == 0)
		{
			return (null);
		}
		
		while (i >= this._frames.size())
		{
			--i;
		}
		while (i < 0)
		{
			++i;
		}
		return (this._frames.get(i));
	}
	
	@Override
	public String toString()
	{
		return ("Animation: " + this._name);
	}
}
