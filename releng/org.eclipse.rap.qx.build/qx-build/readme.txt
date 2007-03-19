(Very) short manual on how to build qooxdoo.jar from source.

1. Place the qooxdoo source download you wish to build in the sdk folder
   (which may have to be created first).
2. Run custom/build-custom.sh (requires Cygwin on Windows)
   In addition the qooxdoo make script that is invoked by build-custom.sh 
   has some requirement which are described here:
   http://qooxdoo.org/documentation/user_manual/requirements
3. Up on successful build a file named qooxdoo-x.x.x.jar is placed in the
   custom/output folder.