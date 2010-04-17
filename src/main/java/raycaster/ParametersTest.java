package raycaster;

public class ParametersTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Parameters parameters = new Parameters(args);
		parameters.ParseParameters();
		
		System.out.println(parameters.i);
		System.out.println(parameters.size);
	}
}
