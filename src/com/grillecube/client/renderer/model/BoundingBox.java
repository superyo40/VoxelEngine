package com.grillecube.client.renderer.model;

import org.lwjgl.util.vector.Vector3f;

/*
   o-------------------o
  /|       ^          /|
 o-------------------o |
 | |       |       ^ | |
 | |       |      /  | |
 | |     h |     /   | |
 | |     e |    /    | |
 | |     i |   /     | |
 | |     g |  /      | |
 | |     h | / depth | |
 | |     t |/        | |
 | |       C-------->| |
 | |         width   | |
 | |                 | |
 | |                 | |
 | |                 | |
 | |                 | |
 | |                 | |
 | o_______________ _|_|
 |/                  | /
 o-------------------o
 
 C : center (_center)
 depth: x axis (in our engine) (_size.x)
 height: y axis (in our engine) (_size.y)
 width: z axis (in our engine) (_size.z)
 
*/

public class BoundingBox
{
	//TODO : implements this class
	//TODO : where should the center be? (center of the box, or bottom-left-front-corner?)
	
	private Vector3f _center;
	private Vector3f _size;
	
	public BoundingBox(Vector3f center, Vector3f size)
	{
		this._center = center;
		this._size = size;
	}
	
	/**
	 * @param point: point to test
	 * @return true if the point is in the box
	 */
	public boolean isPointInside(Vector3f point)
	{
		return (false);
	}
}
