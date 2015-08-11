package com.grillecube.client.renderer.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.grillecube.common.logger.Logger;

public class ModelLoader
{
	/** load a model (from json format) */
	public static Model	fromJsonFile(String filepath)
	{
		Logger.get().log(Logger.Level.FINE, "Loading model: " + filepath);
		try
		{
			return (ModelLoader.fromJsonObject(new JSONObject(readFile(filepath))));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return (null);
		}
	}
	
	/** load a model frm a json object */
	public static Model fromJsonObject(JSONObject object)
	{	
		try
		{
			Model model = new Model(object.getString("name"));
			JSONArray parts = object.getJSONArray("ModelParts");
			for (int i = 0 ; i < parts.length() ; i++)
			{
				model.addPart(jsonObjectToModelPart(parts.getJSONObject(i)));
			}
			return (model);
		}
		catch (Exception e)
		{
			Logger.get().log(Logger.Level.ERROR, "wrong json file format");
			e.printStackTrace();
			return (null);
		}
	}
	
	private static ModelPart jsonObjectToModelPart(JSONObject object)
	{
		ModelPart part = null;
		
		try 
		{
			part = new ModelPart(object.getString("name"));
		}
		catch (JSONException e)
		{
			part = new ModelPart();
		}
		
		JSONArray animations = object.getJSONArray("Animations");
		for (int i = 0 ; i < animations.length() ; i++)
		{
			part.addAnimation(jsonObjectToAnimation(animations.getJSONObject(i)));			
		}
		
		JSONArray boxes = object.getJSONArray("BoundingBoxes");
		for (int i = 0 ; i < boxes.length() ; i++)
		{
			part.addBoundingBox(jsonObjectToBoundingBox(boxes.getJSONObject(i)));			
		}
		
		JSONArray vertices = object.getJSONArray("Vertices");
		part.setVertices(JsonHelper.jsonArrayToFloatArray(vertices));
		
		return (part);
	}

	/** return a boundingbox from the given jsonobject */
	private static BoundingBox jsonObjectToBoundingBox(JSONObject object)
	{		
		return (new BoundingBox(JsonHelper.getJSONVector3f(object, "center"), JsonHelper.getJSONVector3f(object, "size")));
	}

	/** return an animation for a json object */
	private static Animation jsonObjectToAnimation(JSONObject object)
	{
		Animation animation = null;
		int id;
		String name;
		
		try {
			id = object.getInt("id");
		} catch (JSONException exception) {
			Logger.get().log(Logger.Level.ERROR, "Missing animation id!");
			return (null);
		}
		
		try {
			name = object.getString("name");
		} catch (JSONException exception) {
			Logger.get().log(Logger.Level.WARNING, "Missing animation name!");
			name = "undefined";
		}
		animation = new Animation(name, id);
		
		JSONArray frames = object.getJSONArray("AnimationFrames");
		for (int i = 0 ; i < frames.length() ; i++)
		{
			animation.addFrame(jsonObjectToAnimationFrame(frames.getJSONObject(i)));
		}

		return (animation);
	}

    		
	/** return an animation frame from a json object */
	private static AnimationFrame jsonObjectToAnimationFrame(JSONObject object)
	{
		return (new AnimationFrame(
				JsonHelper.jsonObjectToFloat(object.get("time")),
				JsonHelper.getJSONVector3f(object, "translate"),
				JsonHelper.getJSONVector3f(object, "rotate"),
				JsonHelper.getJSONVector3f(object, "scale"),
				JsonHelper.getJSONVector3f(object, "offset")));
	}
	

	/** return a String which contains the full file bytes 
	 * @throws IOException */
	private static String readFile(String filepath) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(filepath));
		return (new String(encoded, StandardCharsets.UTF_8));
	}
}
