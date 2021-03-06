package com.grillecube.client.opengl.object;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

import com.grillecube.client.opengl.GLH;
import com.grillecube.client.opengl.Shader;

public abstract class ProgramCompute implements GLObject
{	
	private int	_programID;

	private int	_compute_shaderID;
	
	public ProgramCompute(String filepath)
	{
		this._compute_shaderID = Shader.loadShader(filepath, GL43.GL_COMPUTE_SHADER);
		
		this._programID = GL20.glCreateProgram();
		GL20.glAttachShader(this._programID, this._compute_shaderID);
		GL20.glLinkProgram(this._programID);
		GL20.glValidateProgram(this._programID);
		GLH.glhAddObject(this);
	}
	
	/** delete the program */
	@Override
	public void delete()
	{
		GL20.glDetachShader(this._programID, this._compute_shaderID);
		GL20.glDeleteShader(this._compute_shaderID);
		GL20.glDeleteProgram(this._programID);
	}
	
	/** start using the program */
	public void	useStart()
	{
		GL20.glUseProgram(this._programID);
	}
	
	public void	compute(int workerX, int workerY, int workerZ)
	{
		GL43.glDispatchCompute(workerX, workerY, workerZ);
		GL42.glMemoryBarrier(GL43.GL_SHADER_STORAGE_BARRIER_BIT);
	}
	
	/** stop using the program */
	public void	useStop()
	{
		GL20.glUseProgram(0);
	}
}
