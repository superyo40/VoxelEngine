package com.grillecube.client.renderer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector3f;

public class JsonHelper
{
	/** load a vector3f from a JSON array of 3 floats */
	public static Vector3f jsonArrayToVector3f(JSONArray array)
	{
		return (new Vector3f(jsonObjectToFloat(array.get(0)),
								jsonObjectToFloat(array.get(1)),
								jsonObjectToFloat(array.get(2))));
	}
	
	/** json array to float array */
	public static float[] jsonArrayToFloatArray(JSONArray array)
	{
		float[] floats = new float[array.length()];
		
		for (int i = 0 ; i < floats.length ; i++)
		{
			floats[i] = jsonObjectToFloat(array.get(i));
		}
		return (floats);
	}
	
	/** parse object to float 
	 *@see JSONArray.getDouble(int index);
	 **/
	public static float jsonObjectToFloat(Object object)
	{
		try
		{
			if (object instanceof Number)
			{
				return (((Number) object).floatValue());
			}
			return (Float.parseFloat((String)object));
		}
		catch (Exception e)
		{
			throw new JSONException(object + " is not a number.");
	    }
	}

	public static Vector3f getJSONVector3f(JSONObject object, String key)
	{
		return (jsonArrayToVector3f(object.getJSONArray(key)));
	}
}
