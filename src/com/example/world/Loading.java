package com.example.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.example.nano.CanvasL;
import com.example.nano.R;
import com.example.world.Constants.Animation_Base;
import com.example.world.Constants.Animation_Direction;
import com.example.world.Constants.Animation_Mode;
import com.example.world.Constants.Role_Link;
import com.example.world.Constants.Role_Type;

public class Loading {
	public static void loadResources(ResourceManager resourceManager) {
		if (null == resourceManager) {
			return;
		}
        InputStream is = null;
        try {
        	//is=MainActivity.this.getResources().openRawResource(R.raw.cet6);
        	is = resourceManager.canvas.openFile(R.raw.resources);
        	InputStreamReader ir = new InputStreamReader(is, "UTF-8");
	 		
	 		 BufferedReader buffreader = new BufferedReader(ir);
	 		 String data;
		     while((data = buffreader.readLine()) != null) {
		    	 createResource(data, resourceManager);
		     }
        } catch(Exception e) {
        	
        } finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void createResource(String resourceString, ResourceManager resourceManager) {
		if (null == resourceManager)
		{
			return;
		}
		String[] lineString = resourceString.split("\r\n");
		
		int paramCount = 0;
		String commandString;
		String[] commandStrings;
		String command;
		String[] parameters;
		for (int i = 0; i < lineString.length; i++)
		{
			commandString = parseParameter(lineString[i]);
			commandStrings = commandString.split("@");
			command = commandStrings[0];
			if (command.equals(""))
			{
				continue;
			}
			if (commandStrings.length > 1)
			{
				parameters = commandStrings[1].split("\\|");
				paramCount = parameters.length;
			}
			else
			{
				parameters = null;
				paramCount = 0;
			}
			
			if (paramCount >= 1 && paramCount < 2)
			{
				resourceManager.addResource(resourceManager.createResource(command, Integer.parseInt(parameters[0])));
			}
			else if (paramCount < 3)
			{
				resourceManager.addResource(resourceManager.createResource(command,Integer.parseInt(parameters[0]), null, Integer.parseInt(parameters[1])));
			}
			else if (paramCount < 4)
			{
				resourceManager.addResource(resourceManager.createResource(command, Integer.parseInt(parameters[0]), resourceManager.getResource(Integer.parseInt(parameters[1])), Integer.parseInt(parameters[2])));
			}
		}
	}
	
	public static String parseParameter(String lineString)
	{
		String[] lineString1 = lineString.split("\t");
		int retCount = 0;
		String retString = "";
		for (int i = 0; i < lineString1.length; i ++)
		{
			if (!lineString1[i].equals(""))
			{
				if (retCount == 0)
				{
					retString = lineString1[i] + "@";
				}
				else
				{
					if (retCount >  1)
					{
						retString += "|";
					}
					retString +=  lineString1[i];
				}
				retCount ++;
			}
		}
		return retString;
	}
	public static Roles loadRole(ResourceManager resourceManager, float scale) {
		return loadRole(resourceManager, scale, null);
	}
	
	public static Roles loadRole(ResourceManager resourceManager, float scale, String name) {
		if (null == resourceManager) {
			return null;
		}
		if (null == name) {
			name = "player";
		}
		Roles role = null;
        InputStream is = null;
        try {
        	//is=MainActivity.this.getResources().openRawResource(R.raw.cet6);
        	int rID = resourceManager.canvas.getFileIndex(name, "raw");
        	is = resourceManager.canvas.openFile(rID);
        	InputStreamReader ir = new InputStreamReader(is, "UTF-8");
	 		
	 		 BufferedReader buffreader = new BufferedReader(ir);
	 		 String bufferString = "";
	 		 String data;
		     while((data = buffreader.readLine()) != null) {
		    	 bufferString += data + "\r\n";
		     }
		     g_roles.put(name, bufferString);
	    	 role = createRole(resourceManager, bufferString, scale);
        } catch(Exception e) {
        	
        } finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        
        return role;
	}
	
	public static Roles createRole(ResourceManager resm, String roleString, float scale) {
		
		String[] lineString = roleString.split("\r\n");
		
		Roles role = new Roles();
		Animations animation = null;
		ObjectManager objectManager = null;
		Resources resource = null;
		RectF destination = null;
		RectF truncation = null;
		RectF flatting = null;
		float tall = 0;
		int reverse = 0;
		
		int index;
		int paramCount = 0;
		String commandString;
		String[] commandStrings;
		String command;
		String[] parameters;
		for (int i = 0; i < lineString.length; i++)
		{
			commandString = parseParameter(lineString[i]);
			commandStrings = commandString.split("@");
			command = commandStrings[0];
			if (command.equals(""))
			{
				continue;
			}
			if (commandStrings.length > 1)
			{
				parameters = commandStrings[1].split("\\|");
				paramCount = parameters.length;
				if (parameters[0].equals(""))
				{
					parameters = null;
					paramCount = 0;
				}
			}
			else
			{
				parameters = null;
				paramCount = 0;
			}
			
			if (command.equals("animation"))
			{
				if (null != role && null != animation)
				{
					role.animations.addAnimation(animation, animation.uniqueID);
					animation = null;
				}
				if (paramCount >= 1)
				{
					if (paramCount == 1) {
						index = getDirection(parameters[0]);
					} else if (paramCount == 2) {
						index = getDirection(parameters[0], parameters[1]);
					} else {
						index = 0;
					}
					
					if (index >= 0)
					{
						animation = new Animations();
						animation.mode = Animation_Mode.Still;
						animation.uniqueID = index;
					}
				}
			}
			else if (command.equals("following"))
			{
				if (null != role && paramCount >= 1)
				{
					index = Integer.parseInt(parameters[0]);
					if (index >= 0)
					{
						role.following = index;
					}
				}
			}
			else if (command.equals("step"))
			{
				if (null != animation && null != objectManager)
				{
					animation.addSequence(objectManager, objectManager.uniqueID);
					objectManager = null;
				}
				if (paramCount >= 1)
				{
					index = Integer.parseInt(parameters[0]);
					if (index >= 0)
					{
						objectManager = new ObjectManager();
						objectManager.uniqueID = index;

						if (null != animation && null != objectManager)
						{
							if (animation.mode == Animation_Mode.Step || animation.mode == Animation_Mode.Auto)
							{
								objectManager.callbackFunction = CanvasL.trigger;
							}
						}
					}
				}
			}
			else if (command.equals("resource"))
			{
				if (null != objectManager && null != resource)
				{
					Objects object = new Objects();
					object.reverse = reverse;
					objectManager.addStepExternal(object, resource, destination, truncation, resource.uniqueID, scale);
					resource = null;
				}
				if (paramCount >= 1)
				{
					index = Integer.parseInt(parameters[0]);
					if (index > 0)
					{
						resource = resm.getResource(index);
					}
					reverse = 0;
				}
			}
			else if (command.equals("reverse"))
			{
				if (paramCount >= 1 && null != resource)
				{
					reverse = Integer.parseInt(parameters[0]);
				}
			}
			else if (command.equals("position"))
			{
				if (paramCount >= 4)
				{
					destination = new RectF();
					destination.X = Integer.parseInt(parameters[0]);
					destination.Y = Integer.parseInt(parameters[1]);
					destination.Width = Integer.parseInt(parameters[2]);
					destination.Height = Integer.parseInt(parameters[3]);
				}
			}
			else if (command.equals("truncate"))
			{
				if (paramCount >= 4)
				{
					truncation = new RectF();
					truncation.X = Integer.parseInt(parameters[0]);
					truncation.Y = Integer.parseInt(parameters[1]);
					truncation.Width = Integer.parseInt(parameters[2]);
					truncation.Height = Integer.parseInt(parameters[3]);
				}
			}
			else if (command.equals("flatting"))
			{
				if (paramCount >= 4)
				{
					flatting = new RectF();
					flatting.X = Integer.parseInt(parameters[0]);
					flatting.Y = Integer.parseInt(parameters[1]);
					flatting.Width = Integer.parseInt(parameters[2]);
					flatting.Height = Integer.parseInt(parameters[3]);
				}
			}
			else if (command.equals("tall"))
			{
				if (paramCount >= 1)
				{
					index = Integer.parseInt(parameters[0]);
					if (index >= 0)
					{
						tall = index;
					}
				}
			}
			else if (command.equals("scale"))
			{
				if (paramCount >= 1)
				{
					scale =  (float) (Float.parseFloat(parameters[0]) / 100.0);
					if (scale < 0)
					{
						scale = 1;
					}
				}
			}
			else if (command.equals("delay"))
			{
				if (paramCount >= 1 && null != animation)
				{
					index = Integer.parseInt(parameters[0]);
					if (null != animation && index > 0)
					{
						animation.delay = index;
					}
				}
			}
			else if (command.equals("mode"))
			{
				if (paramCount >= 1 && null != animation)
				{
					if (parameters[0].equals("Animation_Step"))
					{
						animation.mode = Animation_Mode.Step;
					}
					else if (parameters[0].equals("Animation_Auto"))
					{
						animation.mode = Animation_Mode.Auto;
					}
					else if (parameters[0] .equals("Animation_Loop"))
					{
						animation.mode = Animation_Mode.Loop;
					}
				}
			}
		}
		
		if (null != flatting)
		{
			role.scale = scale;
			tall *= scale;
			flatting.scale(scale);
			role.setFlatting(flatting, tall);
		}
		
		return role;
	}
	public static int getDirection()
	{
		return getDirection("0", "0");
	}

	public static int getDirection(String base)
	{
		return getDirection(base, "0");
	}

	public static int getDirection(String base, String direction)
	{

		int index_base = -1;
		if (base.equals("still"))
		{
			index_base = Animation_Base.Still;
		}
		else if (base.equals("move"))
		{
			index_base = Animation_Base.Move;
		}
		else if (base.equals("run"))
		{
			index_base = Animation_Base.Run;
		}
		else if (base.equals("attack"))
		{
			index_base = Animation_Base.Attack;
		}
		else if (base.equals("dead"))
		{
			index_base = Animation_Base.Dead;
		}
		else
		{
			index_base = Integer.parseInt(base);
		}
		
		int index = -1;
		if (direction.equals("down"))
		{
			index = Animation_Direction.Down;
		}
		else if (direction.equals("left_down"))
		{
			index = Animation_Direction.Left_Down;
		}
		else if (direction.equals("left"))
		{
			index = Animation_Direction.Left;
		}
		else if (direction.equals("left_up"))
		{
			index = Animation_Direction.Left_Up;
		}
		else if (direction.equals("up"))
		{
			index = Animation_Direction.Up;
		}
		else if (direction.equals("right_up"))
		{
			index = Animation_Direction.Right_Up;
		}
		else if (direction.equals("right"))
		{
			index = Animation_Direction.Right;
		}
		else if (direction.equals("right_down"))
		{
			index = Animation_Direction.Right_Down;
		}
		else if (direction.equals("still"))
		{
			index = Animation_Direction.Undefined;
		}
		else
		{
			index = Integer.parseInt(direction);
		}
		
		return index_base + index;
	}

	public static void loadMap(ResourceManager resourceManager, World world) {
		loadMap(resourceManager, world, null);
	}

	public static void loadMap(ResourceManager resourceManager, World world, String name) {
		if (null == resourceManager) {
			return;
		}
		if (null == world) {
			return;
		}
		if (null == name) {
			name = "map";
		}
        InputStream is = null;
        try {
        	//is=MainActivity.this.getResources().openRawResource(R.raw.cet6);
        	int rID = resourceManager.canvas.getFileIndex(name, "raw");
        	is = resourceManager.canvas.openFile(rID);
        	InputStreamReader ir = new InputStreamReader(is, "UTF-8");
	 		
	 		 BufferedReader buffreader = new BufferedReader(ir);
	 		 String bufferString = "";
	 		 String data;
		     while((data = buffreader.readLine()) != null) {
		    	 bufferString += data + "\r\n";
		     }
	    	 createMap(resourceManager, world, bufferString);
        } catch(Exception e) {
        	
        } finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void createMap(ResourceManager resourceManager, World world, String mapString) {
		if (null == world) {
			return;
		}
		if (null == resourceManager) {
			return;
		}

		String[] lineString = mapString.split("\r\n");
		String[] indexString;
		
		int index;
		MultiLinkList<Roles> temp1 = new MultiLinkList<Roles>(Role_Link.Temp1);
		MultiLinkList<Roles> temp2 = new MultiLinkList<Roles>(Role_Link.Temp2);
		Roles tprole = null;
		Roles lprole = null;
		MultiLinkList<Roles> temp = temp1;
		for (int i = 0; i < lineString.length; i++)
		{
			indexString = lineString[i].split("\t");
			for (int j = 0; j < indexString.length; j++)
			{
				index = Integer.parseInt(indexString[j]);
				Roles role = new Roles();
				Animations animation = new Animations();
				animation.delay = 1;
				role.animations.addAnimation(animation, 112)
					.addSequence(new ObjectManager(), 120)
					.addStep(new Objects(resourceManager.getResource(index), new RectF(0, 0, 96, 47), new RectF(0, 0, 96, 47)), 102);
				role.setFlatting(new RectF(j * 80, i * 45, 80, 45), 47);
				world.addRole(role, Role_Type.Back);

				if (null != lprole) {
					role.left = lprole;
					lprole.right = role;
				}
				lprole = role;

				if (temp == temp1) {
					temp2.insertLink(role);
				}
				else {
					temp1.insertLink(role);
				}
				if (i > 0) {
					if (null != tprole) {
						role.up = tprole;
						tprole.down = role;
						tprole = temp.next(tprole);
					}
				}
			}
			lprole = null;
			if (i > 0) {
				temp.clearLink();
			}
			if (temp == temp1) {
				temp = temp2;
			}
			else {
				temp = temp1;
			}
			tprole = temp.link;
		}
	}
	public static void loadScene(ResourceManager resourceManager, World world) {
		loadScene(resourceManager, world, null);
	}
	
	public static void loadScene(ResourceManager resourceManager, World world, String name) {
		if (null ==  resourceManager) {
			return;
		}
		if (null == world) {
			return;
		}
		if (null == name) {
			name = "scene";
		}

        InputStream is = null;
        try {
        	//is=MainActivity.this.getResources().openRawResource(R.raw.cet6);
        	int rID = resourceManager.canvas.getFileIndex(name, "raw");
        	is = resourceManager.canvas.openFile(rID);
        	InputStreamReader ir = new InputStreamReader(is, "UTF-8");
	 		
	 		 BufferedReader buffreader = new BufferedReader(ir);
	 		 String bufferString = "";
	 		 String data;
		     while((data = buffreader.readLine()) != null) {
		    	 bufferString += data + "\r\n";
		     }
	    	 createScene(resourceManager, world, bufferString);
        } catch(Exception e) {
        	
        } finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static HashMap<String, String> g_roles = new HashMap<String, String>();
	public static void createScene(ResourceManager resm, World world, String sceneString) {
		if (null == resm) {
			return;
		}
		if (null == world) {
			return;
		}
		String[] lineString = sceneString.split("\r\n");
		String[] indexString;
		
		Roles role = null;
		float scale = 1;
		int index = 0;
		String data = "";
		for (int i = 0; i < lineString.length; i++)
		{
			indexString = lineString[i].split("\t");
			if (indexString.length >= 3)
			{
				index = 0;
				if (indexString.length >= 4)
				{
					index = Integer.parseInt(indexString[3]);
				}
				if (index < 0)
				{
					index = 0;
				}
				scale = 1;
				if (indexString.length >= 5)
				{
					scale = Float.parseFloat(indexString[4]);
				}
				if (scale < 0 || scale > 10)
				{
					scale = 1;
				}
				data = g_roles.get(indexString[0]);
				if (null == data || data.equals(""))
				{
					role = loadRole(resm, scale, indexString[0]);
				}
				else
				{
					role = createRole(resm, data, scale);
				}
				if (null != role)
				{
					role.uniqueID = index;
					role.moveFlat(Integer.parseInt(indexString[1]), Integer.parseInt(indexString[2]));
					world.addRole(role);
				}
			}
		}
	}
}