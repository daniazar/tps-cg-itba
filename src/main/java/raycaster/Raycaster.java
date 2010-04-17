package raycaster;

public class Raycaster {
	
	public static void main(String[] args) throws Exception
	{
		long starttime = System.currentTimeMillis();

		Parameters p = new Parameters(args);
		p.ParseParameters();
		Scene.startScene(p.getI());
		Camera c = Scene.cam;
		c.setColorMode(p.getCm(), p.getCv());
		c.setImageDim(p.getSize().x, p.getSize().y);
		c.setImageFov((float)p.getFov());
		c.Raytrace();
		
		if(p.time)
			System.out.println("Render took " + (System.currentTimeMillis() - starttime) + "ms");
	}

}
