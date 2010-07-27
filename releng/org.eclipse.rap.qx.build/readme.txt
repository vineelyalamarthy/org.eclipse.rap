(Very) short manual on how to build qx.js/qx-debug.js from source.

1. The code that is currently used by RAP resides in the source/ folder.
2. Adjust the variables at the beginning of build-custom.sh to match the
   location of the just obtained qooxdoo source.
3. Run build-custom.sh (requires Cygwin on Windows).
   The generator.py script that is invoked by build-custom.sh, requires some
   tools that need to be installed. See here for a description:
   http://qooxdoo.org/documentation/user_manual/requirements
3. Up on successful build two files are placed in the output folder.
   The qx.js is optimized for size and execution speed and thus hardly human
   readable. Therefore another file qx-debug.js is provided to ease debugging,
   tracing call stacks and so on.

The code in this bundle is derived from qooxdoo 0.7.4.
See bug 257022: Migrate to qooxdoo 0.7.4
https://bugs.eclipse.org/bugs/show_bug.cgi?id=257022

  Amended the JSON license to reflect the license that Eclipse has been
  permitted to use for this code, which is the JSON License with the following
  term removed: "The software shall be used for Good not Evil."
  File: qx-build/source/class/qx/io/Json.js
