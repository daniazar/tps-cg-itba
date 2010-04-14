package raycaster;
import java.awt.Point;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameters {

	public static final String pointSeparator = "x";
	
	//nombre de archivo input
	public String i;
	
	//nombre de archivo output
	public String o;
	
	public Point size;
	public double fov;
	public String cm;
	public String cv;
	public boolean time = false;
	
	private String paramsString;
	
	public Parameters(String[] args) {
		StringBuilder params = new StringBuilder();
		for (String string : args) {
			params.append(string).append(" ");
		}
		
		System.out.println(params);
		paramsString = params.toString();
		
	}

	public void ParseParameters() {
		Matcher m = Pattern.compile("-([a-z]+)\\s([^\\s]+)?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(paramsString);
		while(m.find()) {
			System.out.println(m.group(1) + " " + m.group(2));
			
			
			try {
				Field aField = this.getClass().getField(m.group(1));
				//If a field is not followed by a value then it's a boolean
				if(m.group(2) == null) {
					aField.setBoolean(this, true);
				
				} else if(aField.getType() == double.class) { //Find what kind is the field and assign
					aField.setDouble(this, new Double(m.group(2)));
				} else if(aField.getType() == Point.class) {
					String[] values = m.group(2).split(pointSeparator);
					Point point = new Point();
					point.x = Integer.valueOf(values[0]);
					point.y = Integer.valueOf(values[1]);
					aField.set(this, point);
				} else {	//It's a String
					aField.set(this, m.group(2));
				}
			} catch (Exception e) {
				System.err.println("There was an error when parsing the command line arguments : " + e.getMessage());
			}
		}
		System.out.println(this);
	}
	
	@Override
	public String toString() {
		return "Parameters [cm=" + cm + ", cv=" + cv + ", fov=" + fov + ", i="
				+ i + ", o=" + o + ", size=" + size + ", time=" + time + "]";
	}

	public String getI() {
		return i;
	}

	public void setI(String i) {
		this.i = i;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public Point getSize() {
		return size;
	}

	public void setSize(Point size) {
		this.size = size;
	}

	public double getFov() {
		return fov;
	}

	public void setFov(double fov) {
		this.fov = fov;
	}

	public String getCm() {
		return cm;
	}

	public void setCm(String cm) {
		this.cm = cm;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public boolean isTime() {
		return time;
	}

	public void setTime(boolean time) {
		this.time = time;
	}
}
