(Very) short manual on how to build client.js from source.

1. This bundle must be placed in the same folder as org.eclipse.rap.rwt.q07.
2. Before running build.sh, set the variable $QXROOT to match the
   location of the qooxdoo 0.7.4 installation. Otherwise "../qx-0.7.4" is assumed.
3. Run build.sh (requires Cygwin on Windows).
   The generator.py script that is invoked by build.sh, requires some
   tools that need to be installed. See here for a description:
   http://qooxdoo.org/documentation/user_manual/requirements
3. Up on successful build the client.js in org.eclipse.rap.rwt.q07/resources is 
   replaced with the new version. This file is optimized for size and execution 
   speed and thus hardly human readable. For debugging, use the debug-mode
   in RAP which uses the unchanged JavaScript-files.