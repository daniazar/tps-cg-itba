image {
  resolution 640 480
  aa 0 2
  filter mitchell
}

light {
   type point
   color { "sRGB linear" 1.000 1.000 1.000 }
   power 100.0
   p 1.0 0.0 0.0
}

camera {
  type pinhole
  eye    0 0 0
  target 0 1 0
  up     0 0 1
  fov    60
  aspect 1.333333
}

shader {
  name default
  type constant
  color { "sRGB linear" 0 0 1 }
}

object {
  shader default
  type sphere
  c 0 10 0
  r 4
}


