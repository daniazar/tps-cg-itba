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
   power 140.0
   p 1.0 1.0 1.0
}


shader {
   name earth
   type phong
   texture "scenes/textures/bWorld.jpg"
   spec { "sRGB linear" 1.0 1.0 1.0 } 50
}

shader {
   name mars
   type phong
   texture "scenes/textures/mars.jpg"
   spec { "sRGB linear" 1.0 1.0 1.0 } 50
}

shader {
   name uranus
   type phong
   texture "scenes/textures/uranus.jpg"
   spec { "sRGB linear" 1.0 1.0 1.0 } 50
}

shader {
   name jupiter
   type phong
   texture "scenes/textures/jupiter.jpg"
   spec { "sRGB linear" 1.0 1.0 1.0 } 50
}

object {
  shader uranus
  type sphere
  c 9 6 0
  r 3.5
}

object {
  shader jupiter
  type sphere
  c -3 30 0
  r 9
}

object {
  shader mars
  type sphere
  c 2 20 0
  r 4
}

object {
  shader earth
  type sphere
  c 6 10 0
  r 3.9
}
