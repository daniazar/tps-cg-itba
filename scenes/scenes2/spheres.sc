image {
  resolution 640 480
  aa 0 2
  filter mitchell
}

camera {
  type pinhole
  eye    0 -20 0
  target 0 1 0
  up     0 0 1
  fov    180
  aspect 1.333333
}

light {
   type point
   color { "sRGB linear" 1.000 1.000 1.000 }
   power 100.0
   p 1.0 3.0 6.0
}

shader {
  name default
  type constant
  color { "sRGB linear" 1 1 0 }
}

shader {
   name texture
   type phong
   texture "scenes/textures/images.jpeg"
   spec { "sRGB linear" 1.0 1.0 1.0 } 50
}

shader {
   name earth2
   type phong
   texture "scenes/textures/bWorld.jpg"
   spec { "sRGB linear" 1.0 1.0 1.0 } 50
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
	shader earth2
	type generic-mesh
	name "Cylinder"
	points 4
		1 10 0
		-9 10 8
		-9 10 0
		1 10 8
	triangles 2
		0 1 2
		0 3 1
	normals none
	uvs facevarying
	0.000000 1.000000 0.000000 0.000000 1.000000 0.000000
	0.000000 1.000000 0.000000 0.000000 1.000000 0.000000
}
/*
object {
  shader earth2
  type sphere
  c 6 10 0
  r 4
}
*/