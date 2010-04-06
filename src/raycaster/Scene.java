package raycaster;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.*;




import javax.vecmath.Point2i;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Scene {

	public static ArrayList<Object> objects = new ArrayList<Object>();
	public static ArrayList<Light> lights = new ArrayList<Light>();
	public static Hashtable<Integer, Material> materials = new Hashtable<Integer, Material>();
	
	static String filename = "scene.xml";
	public static Camera cam;
	
	private static void ParseFile(File file)
	{
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();

		  Document doc = db.parse(file);
		  doc.getDocumentElement().normalize();
		  NodeList nodeLst;
		  
		  nodeLst = doc.getElementsByTagName("light");
		  ParseLights(nodeLst);
		  nodeLst = doc.getElementsByTagName("material");
		  ParseMaterials(nodeLst);
		  nodeLst = doc.getElementsByTagName("sphere");
		  ParseSpheres(nodeLst);
		  nodeLst = doc.getElementsByTagName("plane");
		  ParsePlanes(nodeLst);
		  nodeLst = doc.getElementsByTagName("camera");
		  ParseCamera(nodeLst);
		  nodeLst = doc.getElementsByTagName("triangle");
		  ParseTriangle(nodeLst);
		  nodeLst = doc.getElementsByTagName("quadrilateral");
		  ParseQuadrilateral(nodeLst);
		  
		  
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void ParseQuadrilateral(NodeList nodeLst) {
		for (int s = 0; s < nodeLst.getLength(); s++) {
		  	
		    Node node = nodeLst.item(s);
		    
		    if (node.getNodeType() == Node.ELEMENT_NODE) {
		  
		    	Element quadrilateralNode = (Element)node;
		    	
		    	Node pt1 = quadrilateralNode.getElementsByTagName("pt1").item(0).getFirstChild();
		    	Node pt2 = quadrilateralNode.getElementsByTagName("pt2").item(0).getFirstChild();
		    	Node pt3 = quadrilateralNode.getElementsByTagName("pt3").item(0).getFirstChild();
		    	Node pt4 = quadrilateralNode.getElementsByTagName("pt4").item(0).getFirstChild();
		    	
		    	Node matNode = quadrilateralNode.getElementsByTagName("matID").item(0).getFirstChild();
		    	Scanner sc = new Scanner(pt1.getNodeValue());
		    	sc.useDelimiter(";");
		    	
		    	float x = sc.nextFloat();
		    	float y = sc.nextFloat();
		    	float z = sc.nextFloat();
		    	
		    	Point3f p1 = new Point3f(x,y,z);
		    	sc = new Scanner(pt2.getNodeValue());
		    	sc.useDelimiter(";");
		    	
		    	x = sc.nextFloat();
		    	y = sc.nextFloat();
		    	z = sc.nextFloat();
		    	
		    	Point3f p2 = new Point3f(x,y,z);
		    	
		    	sc = new Scanner(pt3.getNodeValue());
		    	sc.useDelimiter(";");
		    	
		    	x = sc.nextFloat();
		    	y = sc.nextFloat();
		    	z = sc.nextFloat();
		    	
		    	Point3f p3 = new Point3f(x,y,z);
		    	sc = new Scanner(pt4.getNodeValue());
		    	sc.useDelimiter(";");
		    	
		    	x = sc.nextFloat();
		    	y = sc.nextFloat();
		    	z = sc.nextFloat();
		    	
		    	Point3f p4 = new Point3f(x,y,z);
		    	
		    	
		    	Integer id = Integer.parseInt(matNode.getNodeValue());
		    	objects.add(new Quadrilateral(p1,p2,p3,p4, materials.get(id)));
		    }
		}
	}

	private static void ParseTriangle(NodeList nodeLst) {
		  for (int s = 0; s < nodeLst.getLength(); s++) {
			  	
			    Node node = nodeLst.item(s);
			    
			    if (node.getNodeType() == Node.ELEMENT_NODE) {
			  
			    	Element triangleNode = (Element)node;
			    	
			    	Node pt1 = triangleNode.getElementsByTagName("pt1").item(0).getFirstChild();
			    	Node pt2 = triangleNode.getElementsByTagName("pt2").item(0).getFirstChild();
			    	Node pt3 = triangleNode.getElementsByTagName("pt3").item(0).getFirstChild();
			    	
			    	Node matNode = triangleNode.getElementsByTagName("matID").item(0).getFirstChild();
			    	Scanner sc = new Scanner(pt1.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	float x = sc.nextFloat();
			    	float y = sc.nextFloat();
			    	float z = sc.nextFloat();
			    	
			    	Point3f p1 = new Point3f(x,y,z);
			    	sc = new Scanner(pt2.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	x = sc.nextFloat();
			    	y = sc.nextFloat();
			    	z = sc.nextFloat();
			    	
			    	Point3f p2 = new Point3f(x,y,z);
			    	
			    	sc = new Scanner(pt3.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	x = sc.nextFloat();
			    	y = sc.nextFloat();
			    	z = sc.nextFloat();
			    	
			    	Point3f p3 = new Point3f(x,y,z);
			    	
			    	Integer id = Integer.parseInt(matNode.getNodeValue());
			    	objects.add(new Triangle(p1,p2,p3,materials.get(id)));
			    	
			    }

			  }
		
	}

	private static void ParseLights(NodeList nodeLst)
	{
		  for (int s = 0; s < nodeLst.getLength(); s++) {
			  	
			    Node node = nodeLst.item(s);
			    
			    if (node.getNodeType() == Node.ELEMENT_NODE) {
			  
			    	Element lighttNode = (Element)node;
			    	
			    	
			    	Node posNode = lighttNode.getElementsByTagName("position").item(0).getFirstChild();
			    	Node intensityNode = lighttNode.getElementsByTagName("intensity").item(0).getFirstChild();
			    	
			    	Scanner sc = new Scanner(posNode.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	float x = sc.nextFloat();
			    	float y = sc.nextFloat();
			    	float z = sc.nextFloat();
			    	
			    	Point3f position = new Point3f(x,y,z);
			    	
			    	sc = new Scanner(intensityNode.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	float r = sc.nextFloat();
			    	float g = sc.nextFloat();
			    	float b = sc.nextFloat();
			    	
			    	Color intensity = new Color(r, g, b);
			    	lights.add(new Light(position,intensity));

			    }

			  }
	}
	
	private static void ParseMaterials(NodeList nodeLst)
	{
		  for (int s = 0; s < nodeLst.getLength(); s++) {
			  	
			    Node node = nodeLst.item(s);
			    
			    if (node.getNodeType() == Node.ELEMENT_NODE) {
			  
			    	Element matNode = (Element)node;
			    	
			    	Node idNode = matNode.getElementsByTagName("id").item(0).getFirstChild();
			    	Node diffuseNode = matNode.getElementsByTagName("diffuse").item(0).getFirstChild();
			    	Node reflectionNode = matNode.getElementsByTagName("reflection").item(0).getFirstChild();
			    	
			    	int id = Integer.parseInt(idNode.getNodeValue());
			    	
			    	float reflection =  Float.parseFloat( reflectionNode.getNodeValue());
			    	
			    	Scanner sc = new Scanner(diffuseNode.getNodeValue());
			    	sc.useDelimiter(";");
			    	float r = sc.nextFloat();
			    	float g = sc.nextFloat();
			    	float b = sc.nextFloat();
			    	
			    	Color c = new Color(r, g, b);
			    	
			    	materials.put(id, new Material(c, reflection));

			    	
	
			    	
			    }

			  }
	}
	private static void ParseSpheres(NodeList nodeLst)
	{
		  for (int s = 0; s < nodeLst.getLength(); s++) {
			  	
			    Node node = nodeLst.item(s);
			    
			    if (node.getNodeType() == Node.ELEMENT_NODE) {
			  
			    	Element sphereNode = (Element)node;
			    	
			    	Node centerNode = sphereNode.getElementsByTagName("center").item(0).getFirstChild();
			    	Node radiusNode = sphereNode.getElementsByTagName("radius").item(0).getFirstChild();
			    	Node matNode = sphereNode.getElementsByTagName("matID").item(0).getFirstChild();
			    	Scanner sc = new Scanner(centerNode.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	float x = sc.nextFloat();
			    	float y = sc.nextFloat();
			    	float z = sc.nextFloat();
			    	
			    	Point3f center = new Point3f(x,y,z);
			    	float radius =  Float.parseFloat( radiusNode.getNodeValue());
			    	
			    	Integer id = Integer.parseInt(matNode.getNodeValue());
			    	objects.add(new Sphere(center, radius,materials.get(id)));
			    	
			    }

			  }
	}
	
	private static void ParsePlanes(NodeList nodeLst)
	{
		  for (int s = 0; s < nodeLst.getLength(); s++) {
			  	
			    Node node = nodeLst.item(s);
			    
			    if (node.getNodeType() == Node.ELEMENT_NODE) {
			  
			    	Element planeNode = (Element)node;
			    	
			    	Node nNode = planeNode.getElementsByTagName("normal").item(0).getFirstChild();
			    	Node pNode = planeNode.getElementsByTagName("point").item(0).getFirstChild();
			    	Node matNode = planeNode.getElementsByTagName("matID").item(0).getFirstChild();
			    	Scanner sc = new Scanner(pNode.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	float x = sc.nextFloat();
			    	float y = sc.nextFloat();
			    	float z = sc.nextFloat();
			    	
			    	Point3f p = new Point3f(x,y,z);
			    	
			    	sc = new Scanner(nNode.getNodeValue());
			    	sc.useDelimiter(";");
			    	
			    	x = sc.nextFloat();
			    	y = sc.nextFloat();
			    	z = sc.nextFloat();
			    	
			    	Vector3f n = new Vector3f(x,y,z);
	
			    	
			    	Integer id = Integer.parseInt(matNode.getNodeValue());
			    	Plane pl = null;

			    	try
			    	{
			    		pl =  new Plane(n, p,materials.get(id)); 
				    	objects.add(pl);
			    	}catch(Exception e)
			    	{
			    		e.printStackTrace();
			    	}
	
			    }

			  }
	}
	
	private static void ParseCamera(NodeList nodeLst)
	{
		Element camNode = (Element)nodeLst.item(0);
		Node dimNode = camNode.getElementsByTagName("dimensions").item(0).getFirstChild();
		Node posNode = camNode.getElementsByTagName("position").item(0).getFirstChild();
		Node dirNode = camNode.getElementsByTagName("direction").item(0).getFirstChild();
		
		Scanner sc = new Scanner(dimNode.getNodeValue());
		sc.useDelimiter(";");
		System.out.println(dimNode.getNodeValue());
		int dimx = sc.nextInt();
		int  dimy = sc.nextInt();
		
		sc = new Scanner(posNode.getNodeValue());
		sc.useDelimiter(";");
		float px = sc.nextFloat();
		float py = sc.nextFloat();
		float pz = sc.nextFloat();
		
		sc = new Scanner(dirNode.getNodeValue());
		sc.useDelimiter(";");
		float dirx = sc.nextFloat();
		float diry = sc.nextFloat();
		float dirz = sc.nextFloat();
		
		Point2i dim = new Point2i(dimx,dimy);
		Point3f pos = new Point3f(px,py,pz);
		Vector3f dir = new Vector3f(dirx, diry, dirz);
		cam = new Camera(pos, dir, dim);
		
	}
	
	public static void main(String[] args)
	{
		File scene = null;
		try {
			scene = new File(new URI(Scene.class.getResource(filename).toString()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		ParseFile(scene);
		cam.Raytrace();
		
	}
}
	