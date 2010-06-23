package org.cg.raycaster;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.cg.primitives.Box;
import org.cg.primitives.Plane;
import org.cg.primitives.Primitive;
import org.cg.primitives.Sphere;
import org.cg.primitives.Triangle;
import org.cg.rendering.Camera;
import org.cg.rendering.Material;
import org.cg.rendering.PointLight;
import org.cg.rendering.shader.Shader;
import org.cg.rendering.shader.ShaderType;
import org.cg.util.Parser;
import org.cg.util.Parser.ParserException;

public class SunflowScene {

	public static ArrayList<Primitive> objects = new ArrayList<Primitive>();
	public static ArrayList<PointLight> lights = new ArrayList<PointLight>();
	public static Hashtable<Integer, Material> materials = new Hashtable<Integer, Material>();
	public static Map<String, Shader> shadersMap = new HashMap<String, Shader>();
	
	public static Camera cam;
	public static Point resolution;
	private static Parser p;
	private static Point3f pos;
	private static Vector3f dir;
	private static Vector3f up;
	private static float fovx;
	private static float aspect;
	public static int AAMax;
	public static int AAMin;
	public static int bucket;
	public static boolean SoftShadows = false;
	public static float DeltaShadow = 1f;
	public static void startScene(String name) throws Exception{
		ParseFile(name);	
	}
	
	private static void ParseFile(String name) throws Exception {

	
			p = new Parser(name);
            while (true) {
            	try{
	                String token = p.getNextToken();
	                if (token == null)
	                    break;
	                else if (token.equals("shader")) {
	                    parseShader(); 
	                }else if (token.equals("image")){
	                	parseImageBlock();
	                }else if (token.equals("camera")) {
	                    parseCamera();
	                }else if (token.equals("light")) {
	                    parseLightBlock();
	                }else if (token.equals("object")) {
	                    parseObjectBlock();
	                }else if (token.equals("bucket")) {
	                    parseBucketBlock();
	                }else {
	                	if(p.peekNextToken("{"))
	                		p.parseBlock();
	                	//este syso no va -- debugging
	                	System.out.println("Token " + token + " is unsupported");
	                }
            	}catch(UnsupportedException  e){
            		System.out.println(e.getMessage());
            	}
            }
            cam = new Camera(pos, dir, resolution, up, fovx);
	
	}

	private static void parseBucketBlock() throws ParserException, IOException {

		bucket = p.getNextInt();
		String token = p.getNextToken();
		if(!token.equals("row"))
		{
			System.out.println("Only \"row\" is implemented");
			
		}
		
	}

	private static void parseShader() throws ParserException, IOException, UnsupportedException {
		System.out.println("parsing shader");
		p.checkNextToken("{");
		p.checkNextToken("name");
		String name = p.getNextToken();
		
		Shader shader = new Shader(name);
		
		p.checkNextToken("type");
		try{
			if (p.peekNextToken("phong")) {
	            @SuppressWarnings("unused")
				String tex = null;
	            Color diffuse = null;
	            int samples = 0;
	            Color specular;
	            float power;
	            
	            if (p.peekNextToken("texture"))
	                 shader.setTexturePath(p.getNextToken());
	            else {
	            	p.checkNextToken("diffuse");
	                diffuse = parseColor();
	            }
	            p.checkNextToken("spec");
	            specular = parseColor();
	            
	            power = p.getNextFloat();
				
	            //TODO determine what this does
	            if (p.peekNextToken("samples"))
	                samples = p.getNextInt();
				
				System.out.println("type: phong name :"+ name+ " diffuse: " + diffuse + " samples: " + samples + 
									" specular: " + specular + " power: " + power);
				
				if(SoftShadows)
				{
					float comp[] = specular.getColorComponents(null);
					specular = new Color(comp[0]/7,comp[1]/7,comp[2]/7);
				}
				Material material = new Material(diffuse, 0, 0, 1, 0.5f, specular, power, 1);
				shader.setMaterial(material);
				shader.setType(ShaderType.PHONG);
				shadersMap.put(name, shader);
				
			}else if (p.peekNextToken("mirror")){
		         p.checkNextToken("refl");
		         Color color = parseColor();
		         
		         System.out.println("type: mirror name :"+ name+" color:" + color);
		         
		         Material material = new Material(color, 1, 0, 0, 0, color, 10, 0);
		         shader.setMaterial(material);
		         shader.setType(ShaderType.MIRROR);
		         shadersMap.put(name, shader);
		         
			}else if (p.peekNextToken("constant")) {
	            // backwards compatibility -- peek only
	            p.peekNextToken("color");
	            Color color = parseColor();
	            System.out.println("type: constant name :"+ name+" color:" + color);
	            
	            Material material = new Material(color, 0, 0, 1, 0, color, 0, 0);
	            shader.setMaterial(material);
	            shader.setType(ShaderType.CONSTANT);
	            shadersMap.put(name, shader);
	            
	        }else if (p.peekNextToken("glass")){
				 p.checkNextToken("eta");
		         float eta = p.getNextFloat();
		         
		         p.checkNextToken("color");
		         Color color = parseColor();
		         
		         float absorbtionDistance = 0;
		         if (p.peekNextToken("absorbtion.distance"))
		             absorbtionDistance = p.getNextFloat();
		         
		         Color absorbtionColor = null;
				 if (p.peekNextToken("absorbtion.color"))
		             absorbtionColor = parseColor();
				 
				System.out.println("type glass name :"+ name+" parameters:" + eta + color.toString() + absorbtionColor + absorbtionDistance);
				Material material = new Material(color, 0, eta, 0.98f, 0, color, 1, 0);
				shader.setMaterial(material);
				shader.setType(ShaderType.GLASS);
				shadersMap.put(name, shader);
		     } else{
		    	 String tok = p.getNextToken();
		    	 throw  new UnsupportedException(tok);
		     }
		}catch(UnsupportedException e){
	    	 p.parseBlock();
	    	 throw e;
		}
        p.checkNextToken("}");

	}

	private static void parseImageBlock() throws ParserException, IOException, UnsupportedException {
		System.out.println("parsing image");
		resolution = new Point();


		int samples = 0;
 
        p.checkNextToken("{");
        if (p.peekNextToken("resolution")) {
            resolution.x= p.getNextInt();
            resolution.y= p.getNextInt();
        }
        if (p.peekNextToken("aa")) {
            
            AAMin = p.getNextInt();
            AAMax = p.getNextInt();
            if(AAMax == 1)
            {
            	System.out.println("AntiAliasing must be 0,2,3,4");
            	throw new UnsupportedException("AntiAliasing value is invalid");
            }
            
            	
            
        }
        
        if (p.peekNextToken("samples"))
            samples = p.getNextInt();
        
        if (p.peekNextToken("contrast")){
        	System.out.println("constrast is unsupported");
        	p.getNextFloat();
        }
        if (p.peekNextToken("filter")){
        	System.out.println("filter is unsupported");
            p.getNextToken();
        }
        if (p.peekNextToken("jitter")){
        	System.out.println("jitter is unsupported");
            p.getNextBoolean();
        }

        if (p.peekNextToken("show-aa")) {
        	System.out.println("Deprecated: show-aa ignored");
            p.getNextBoolean();
        }
        if (p.peekNextToken("output")) {
        	System.out.println("Deprecated: output statement ignored");
            p.getNextToken();
        }
        p.checkNextToken("}");
        
	}

	private static void parseObjectBlock() throws IOException, ParserException, UnsupportedException {
		System.out.println("parsing object");
		p.checkNextToken("{");
        Matrix4f transform = null;
        String name = null;
        String shader = "";
        
        if (p.peekNextToken("noinstance"))
        	System.out.println("token noinstance is not supported");
        
        if (p.peekNextToken("shaders")) {
            int n = p.getNextInt();
            String[] shaders = new String[n];
            for (int i = 0; i < n; i++)
                shaders[i] = p.getNextToken();
            System.out.println("multiple shaders are not implemented");
            shader = shaders[0];
        }else{
	        p.checkNextToken("shader");
	        shader = p.getNextToken();
        }
        
        String[] modifiers;
		if (p.peekNextToken("modifiers")) {
			System.out.println("modifiers are not supported");
            int n = p.getNextInt();
            modifiers = new String[n];
            for (int i = 0; i < n; i++)
                modifiers[i] = p.getNextToken();
        } else if (p.peekNextToken("modifier")){
			System.out.println("modifiers are not supported");
            modifiers = new String[] { p.getNextToken() };
        }
		
		
        if (p.peekNextToken("transform"))
            transform = parseMatrix();
        
        if (p.peekNextToken("accel")){
        	System.out.println("accel is not supported");
        }
        
        p.checkNextToken("type");
        	String type = p.getNextToken();
        	
        if (p.peekNextToken("name"))
        	name = p.getNextToken();
        if (type.equals("sphere")) {

            p.checkNextToken("c");
            float x = p.getNextFloat();
            float y = p.getNextFloat();
            float z = p.getNextFloat();
            Point3f c = new Point3f(x, y ,z);
            p.checkNextToken("r");
            float radius = p.getNextFloat();
            Shader sphereShader = shadersMap.get(shader);
            if(sphereShader == null) {
            	throw new UnsupportedException("Shader doesn't exist " + shader);
            }
            objects.add(new Sphere(c, radius, sphereShader.getMaterial(), sphereShader)); 
			
        } else if (type.equals("plane")) {
            p.checkNextToken("p");
            Point3f center = parsePoint();
            if (p.peekNextToken("n")) {
               Vector3f normal = parseVector();
               Shader planeShader = shadersMap.get(shader);
               if(planeShader == null) {
               	throw new UnsupportedException("Shader doesn't exist " + shader);
               }
               
               objects.add(new Plane(normal, center, planeShader.getMaterial(), planeShader));
               
            } else {
                p.checkNextToken("p");
                Point3f point1 = parsePoint();
                p.checkNextToken("p");
                Point3f point2 = parsePoint();
                
                Shader planeShader = shadersMap.get(shader);
                if(planeShader == null) {
                	throw new UnsupportedException("Shader doesn't exist " + shader);
                }
                
                objects.add(new Plane(new Vector3f(center),new Vector3f(point1),new Vector3f(point2), planeShader.getMaterial(), planeShader));
                
            }
            
            
        }else if (type.equals("generic-mesh")) {
            // parse vertices
            p.checkNextToken("points");
            int np = p.getNextInt();
            float[] points = parseFloatArray(np * 3);
            // parse triangle indices
            p.checkNextToken("triangles");
            int nt = p.getNextInt();
            int[] triangles = parseIntArray(nt * 3);
            // parse normals
            p.checkNextToken("normals");
            float[] normals = null;
            if (p.peekNextToken("vertex")){
                 normals = parseFloatArray(np * 3);
                
            }else if (p.peekNextToken("facevarying")){
                normals = parseFloatArray(nt * 9);
            }else
            p.checkNextToken("none");
            // parse texture coordinates
            p.checkNextToken("uvs");
            float[] uvs = null;
			if (p.peekNextToken("vertex")){
                uvs = parseFloatArray(np * 2);
                
            }else if (p.peekNextToken("facevarying")){
                uvs = parseFloatArray(nt * 6);
            }  
            else
                p.checkNextToken("none");
            int[] faceshaders;
			if (p.peekNextToken("face_shaders"))
				faceshaders = parseIntArray(nt);	
			
			Shader triangleShader = shadersMap.get(shader);
            if(triangleShader == null) {
            	throw new UnsupportedException("Shader doesn't exist " + shader);
            }
            
			for(int i = 0; i < triangles.length; ) {
				Point3f p1 = new Point3f(points[triangles[i] * 3], points[triangles[i] * 3 + 1], points[triangles[i] * 3 + 2]);
				Point3f p2 = new Point3f(points[triangles[i + 1] * 3], points[triangles[i + 1] * 3 + 1], points[triangles[i + 1] * 3 + 2]);
				Point3f p3 = new Point3f(points[triangles[i + 2] * 3], points[triangles[i + 2] * 3 + 1], points[triangles[i + 2] * 3 + 2]);
				
				Point2f uv1;
				Point2f uv2;
				Point2f uv3;
				
				if(uvs != null){
					uv1 = new Point2f(uvs[i*2] ,uvs[i*2+1]);
					uv2 = new Point2f(uvs[(i+1)*2] ,uvs[(i+1)*2+1]);
					uv3 = new Point2f(uvs[(i+2)*2] ,uvs[(i+2)*2+1]);
				}else{
					uv1 = new Point2f(0,0);
					uv2 = new Point2f(1,0);
					uv3 = new Point2f(0,1);
				}
				
				Shader triShader = shadersMap.get(shader);
                if(triShader == null) {
                	throw new UnsupportedException("Shader doesn't exist " + shader);
                }
                if(normals != null)
                {
                	Vector3f nVec0 = new Vector3f(normals[triangles[i]*3],normals[triangles[i] * 3 + 1],normals[triangles[i] * 3 + 2]);
                	Vector3f nVec1 = new Vector3f(normals[triangles[i+1]*3],normals[triangles[i+1] * 3 + 1],normals[triangles[i+1] * 3 + 2]);
                	Vector3f nVec2 = new Vector3f(normals[triangles[i+2]*3],normals[triangles[i+2] * 3 + 1],normals[triangles[i+2] * 3 + 2]);
                	Vector3f nVec = new Vector3f((nVec0.x+nVec1.x+nVec2.x)/3,(nVec0.y+nVec1.y+nVec2.y)/3,(nVec0.z+nVec1.z+nVec2.z)/3);
                	nVec.normalize();
                	objects.add(new Triangle(p1, p2, p3, triangleShader.getMaterial(),triShader,nVec, uv1,uv2,uv3));
                }
                else
                	objects.add(new Triangle(p1, p2, p3, triangleShader.getMaterial(),triShader,uv1,uv2,uv3));					
				i += 3;
			}
			//objects.addAll(trianglesList);
        }else if(type.equals("box")){        	
        	Point3f p0 = new Point3f(0,0,0);
        	Point3f p1 = new Point3f(1,1,1);
        	
        	Shader boxShader = shadersMap.get(shader);
            if(boxShader == null) {
            	throw new UnsupportedException("Shader doesn't exist " + shader);
            }
            
        	objects.add(new Box(p0, p1, boxShader.getMaterial(), boxShader,transform));
        	
        	
        }else{
        	p.parseBlock();
        	throw new UnsupportedException(type);
        }
        
        p.checkNextToken("}");
	}

	private static void parseLightBlock() throws ParserException, IOException, UnsupportedException {
		System.out.println("parsing light");
		Point3f position;
		Color color;
		Float pow = null;
		p.checkNextToken("{");
		p.checkNextToken("type");
		if (p.peekNextToken("point")) {

			if (p.peekNextToken("color")) {	            	
				try{
					color = parseColor();
				}catch(UnsupportedException e){
					p.moveToToken("}");
					throw e;
				}	            	
			    p.checkNextToken("power");
			    pow = p.getNextFloat();
			} else {
			    p.checkNextToken("power");
			    color = parseColor();
			}
			p.checkNextToken("p");
			position = parsePoint();


            if(SoftShadows)
            {
    			float comp[] = color.getColorComponents(null);
    			for(int i = 0; i < 3; i++)
    				color = new Color(comp[0]/7,comp[1]/7,comp[2]/7);
            }

            lights.add(new PointLight(position, color, pow));
            
            if(SoftShadows)
            {

                    lights.add(new PointLight(new Point3f(position.x+DeltaShadow,position.y,position.z),
                    		color,pow));
                    lights.add(new PointLight(new Point3f(position.x-DeltaShadow,position.y,position.z),
                    		color,pow));
                    lights.add(new PointLight(new Point3f(position.x,position.y+DeltaShadow,position.z),
                    		color,pow));
                    lights.add(new PointLight(new Point3f(position.x,position.y-DeltaShadow,position.z),
                    		color,pow));
                    lights.add(new PointLight(new Point3f(position.x,position.y,position.z+DeltaShadow),
                    		color,pow));
                    lights.add(new PointLight(new Point3f(position.x,position.y,position.z-DeltaShadow),
                                    color,pow));
            }

    


			

	     }else{
	    	 String tok = p.getNextToken();
 	    	 p.parseBlock();
	    	 throw  new UnsupportedException(tok);

	     }
	     p.checkNextToken("}");
	     System.out.println("position: " + position.toString() +" color: "+ color);
	}
	
    private static Point3f parsePoint() throws IOException {
        float x = p.getNextFloat();
        float y = p.getNextFloat();
        float z = p.getNextFloat();
        return new Point3f(x, y, z);
    }

	private static void parseCamera() throws ParserException, IOException, UnsupportedException {
		System.out.println("parsing camera");  
		p.checkNextToken("{");
		p.checkNextToken("type");
		String type = p.getNextToken();	        
		if (type.equals("pinhole")) {
			p.checkNextToken("eye");
            pos = parsePoint();
            p.checkNextToken("target");
            Vector3f target = parseVector(); 
            target.sub(pos);
            dir = target;
            dir.normalize();
            p.checkNextToken("up");
            up = parseVector();
            p.checkNextToken("fov");
            fovx = p.getNextFloat();
            p.checkNextToken("aspect");
            aspect = p.getNextFloat();
            
            System.out.println("eye : " + pos.toString() + " target: " + dir+ " up: "+ up+" fov: "+ fovx + "aspect: " + aspect);
			}else {
				  p.parseBlock();//ignore the rest of the block
				  throw  new UnsupportedException(type);
			}
	      
	      p.checkNextToken("}");

	}

    private static Color parseColor() throws ParserException, IOException, UnsupportedException {
    	Color c = null;
    	if (p.peekNextToken("{")) {
            String space = p.getNextToken();
            if (space.equals("sRGB linear")) {
                float r = p.getNextFloat();
                float g = p.getNextFloat();
                float b = p.getNextFloat();
                c = new Color(r, g, b);
                p.checkNextToken("}");
            } else {
            	p.moveToToken("}");
            	throw new UnsupportedException(space);
            }
            
        } else {
            float r = p.getNextFloat();
            float g = p.getNextFloat();
            float b = p.getNextFloat();
            return new Color(r, g, b);
        }
        return c;
    }
    
    private static Vector3f parseVector() throws IOException {
        float x = p.getNextFloat();
        float y = p.getNextFloat();
        float z = p.getNextFloat();
        return new Vector3f(x, y, z);
    }
    
    private static int[] parseIntArray(int size) throws IOException {
        int[] data = new int[size];
        for (int i = 0; i < size; i++)
            data[i] = p.getNextInt();
        return data;
    }

    private static float[] parseFloatArray(int size) throws IOException {
        float[] data = new float[size];
        for (int i = 0; i < size; i++)
            data[i] = p.getNextFloat();
        return data;
    }

	@SuppressWarnings("serial")
	public static class UnsupportedException extends Exception {
        private UnsupportedException(String token) {
            super(String.format("Token: %s is unsupported ", token));
        }
    }	
    
	 private static Matrix4f parseMatrix() throws IOException, ParserException, UnsupportedException {
		 if (p.peekNextToken("row")) {
			throw new UnsupportedException("row");
	     } else if (p.peekNextToken("col")) {
			throw new UnsupportedException("row");
	     } else {
	            Matrix4f m = new Matrix4f();
	            m.setIdentity();
	            p.checkNextToken("{");
	            while (!p.peekNextToken("}")) {
	                Matrix4f t = new Matrix4f();
	                t.setIdentity();	
	                if (p.peekNextToken("translate")) {
	                	Vector3f v = parseVector();
	                    m.setTranslation(v);
	                } else if (p.peekNextToken("scaleu")) {
	                    float s = p.getNextFloat();
	                    t.setScale(s);
	                } else if (p.peekNextToken("scale")) {
	                	float x = p.getNextFloat();
	                    float y = p.getNextFloat();
	                    float z = p.getNextFloat();
	                    t.m00 = t.m00 * x;
	                    t.m11 = t.m11 * y;
	                    t.m22 = t.m22 * z;
	                } else if (p.peekNextToken("rotatex")) {
	                    float angle = p.getNextFloat();
	                    t.rotX((float) Math.toRadians(angle));
	                } else if (p.peekNextToken("rotatey")) {
	                    float angle = p.getNextFloat();
	                    t.rotY((float) Math.toRadians(angle));
	                } else if (p.peekNextToken("rotatez")) {
	                    float angle = p.getNextFloat();
	                    t.rotZ((float) Math.toRadians(angle));
	                } else if (p.peekNextToken("rotate")) {
	                    p.getNextToken();
	                    p.getNextToken();
	                    p.getNextToken();
	                    p.getNextToken();
	                    System.out.println("rotate is not supported");
	                } else
	                    throw new UnsupportedException(p.getNextToken());
	                if (t != null)
	                    m.mul(t);
	            }
	            return m;
	        }
	    }
	
}
