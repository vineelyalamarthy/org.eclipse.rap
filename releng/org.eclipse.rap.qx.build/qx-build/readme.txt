(Very) short manual on how to build qooxdoo.jar from source.

1. Obtain the desired qooxdoo version from SVN, we recommend to fetch it into
   your workspace. The code that is currently used by RAP resides in the 
   qx-build/source folder.
2. Adjust the variables at the beginning of custom/build-custom.sh to match the
   location of the just obtained qooxdoo source.
3. Run custom/build-custom.sh (requires Cygwin on Windows)
   The generator.py script that is invoked by build-custom.sh, requires some 
   tools that need to be installed. See here for a description:
   http://qooxdoo.org/documentation/user_manual/requirements
3. Up on successful build two files are placed in the custom/output folder.
   The qx.js is optimized for size and execution speed and thus hardly human
   readable. Therefore another file qx-debug.js is provided to ease debugging,
   tracing call stacks and so on. 