# An Educational Failure

This is a failed experiment at a double pendulum. Box2D cannot
simulate an *ideal* pendulum. Numerical integration causes
[excessive damping](http://www.box2d.org/forum/viewtopic.php?f=3&t=4841),
which quickly makes the pendulum uninteresting.
