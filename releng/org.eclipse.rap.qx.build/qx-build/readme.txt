(Very) short manual on how to build qooxdoo.jar from source.

1. Obtain the desired qooxdoo version from SVN, we recommend to fetch it into
   your workspace.
2. Adjust the variables at the beginning of custom/build-custom.sh to match the
   location of the just obtained qooxdoo source.
3. Run custom/build-custom.sh (requires Cygwin on Windows)
   In addition, the generator.py script that is invoked by build-custom.sh, 
   has some requirements which are described here:
   http://qooxdoo.org/documentation/user_manual/requirements
3. Up on successful build a file named qooxdoo-x.x.x.x.jar is placed in the
   custom/output folder.