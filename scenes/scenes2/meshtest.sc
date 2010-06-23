image {
  resolution 640 480
  aa 0 2
  filter mitchell
}

bucket 64 row 

camera {
  type pinhole
  eye    0 0 -10
  target 0 0 0
  up     0 1 0
  fov    60
  aspect 1.3333333
}

light {
   type point
   color { "sRGB linear" 1.000 1.000 1.000 }
   power 100.0
   p 0.0 0.0 -10.0
}

shader {
  name default
  type constant
  color { "sRGB linear" 1 1 0 }
}
/*
object {
  shader default
  type sphere
  c 0 10 0
  r 1
}

object {
  shader default
  type sphere
  c 2 10 0
  r 1
}


object {
  shader default
  type sphere
  c 4 10 0
  r 1
}


object {
  shader default
  type sphere
  c 6 10 0
  r 1
}

object {
  shader default
  type sphere
  c 8 10 0
  r 1
}
*/
object {
	shader default
	type generic-mesh
	name "Triangle"
	points 3
		-1 0 0
		0 1 0
		1 0 0
	triangles 1
		0 1 2
	normals none
	uvs none
}


object {
	shader default
	type generic-mesh
	name "Triangle"
	points 3
		-1 2 0
		0 3 0
		1 2 0
	triangles 1
		0 1 2
	normals vertex
		0 0 1
		0 0 1
		0 0 1
	uvs none
}
