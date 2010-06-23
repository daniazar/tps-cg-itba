image {
  resolution 640 480
  aa 0 2
  filter mitchell
}

camera {
  type pinhole
  eye    0 0 -7
  target 0 0 1
  up     0 1 0
  fov    60
  aspect 1.333333
}

light {
   type point
   color { "sRGB linear" 1.000 1.000 1.000 }
   power 140.0
   p 0 10 -10
}

shader {
  name violet
  type phong
  diffuse { "sRGB linear" 1 0 1 }
  spec { "sRGB linear" 1 1 1 } 10
}


shader {
  name cyan
  type phong
  diffuse { "sRGB linear" 0 1 1 }
  spec { "sRGB linear" 1 1 1 } 10
}

shader {
  name yellow
  type phong
  diffuse { "sRGB linear" 1 1 0 }
  spec { "sRGB linear" 1 1 1 } 10
}


shader {
	name orange
	type phong
	diffuse { "sRGB linear" 0.9 0.6 0.6 }
  	spec { "sRGB linear" 1 1 1 } 10
}

shader {
	name mirror
	type mirror
	refl { "sRGB linear" 1.0 1.0 1.0 }
}

shader {
	name glass
	type glass
	eta 0.98
	color { "sRGB linear" 1.0 1.0 1.0 }
	absorbtion.distance 1
	absorbtion.color { "sRGB linear" 1.0 1.0 1.0 }
}

object {
	shader mirror
	type sphere
	c -1 -1 0
	r 1
}

object {
	shader glass
	type sphere
	c 0 0 -4
	r 0.5
}

object {
	shader violet
	type sphere 
	c 1 -1 0
	r 1
}

object {
	shader yellow
	type sphere 
	c -1 1 0
	r 1
}

object {
	shader cyan
	type sphere 
	c 1 1 0
	r 1
}

object {
	shader orange
	type sphere
	c 0 0 20
	r 20
}
