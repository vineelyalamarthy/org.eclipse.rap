
  Howto build the qooxdoo Javascript files for RAP


Contents
--------

1. Patching qooxdoo
2. Building qooxdoo
3. Contents of the qx.build project


1. Patching qooxdoo
-------------------

  For any change we need to make to the qooxdoo codebase, we create a patch.
  The patches are in the unified format and can be easily created using Eclipse
  (Team -> Create Patch) or using the diff utility on the command line (diff -u).

  * Make your changes to the qooxdoo source code files in the source directory.

  * In Eclipse, select the source folder and click Team -> Create Patch in the
    context menu.

  * Save the patch in the patches/<VERSION> directory. The file name must start
    with a two-digit number, that is incremented for every new patch. This
    number is meant to ensure that the patches are applied in the correct order
    (there may be more than one patch for the same file). The patch file must
    have the extension .diff. It is also a good idea to include a bug number
    and/or the name of the affected class in the file name to make it easier to
    identify patches.

  * Amend the paths in the patch to start with qx-build/source/.

  * Make sure the patch has Unix line endings.

  * It is possible to strip or replace comments in the patch files. Everything
    before the first line stating with --- is considered a comment.

2. Building qooxdoo
-------------------

  * Open up a shell (you can use Cygwin on Windows)

  * cd to the qx.build project's root directory

    cd <workspace>/org.eclipse.rap.qx.build

  * Prepare the sources
    The prepare.sh script extracts the qx zip file to the source directory and
    applies all patches. Make sure that the VERSION variable in the script
    matches the current qooxdoo version.

    ./prepare.sh all

  * Build qooxdoo
    The build-custom.sh script feeds the qooxdoo generator with the necessary
    input. Make sure that the variables VERSION, REVISION, and TOOL in this
    script are set correctly.

    ./build-custom.sh

  * Copy the results into the rwt.q07 fragment
    The results of the qx build (qx.js and qx-debug.js) can be found in the
    output directory. They must be copied into the rwt.q07 fragment.

    cp output/*.js ../org.eclipse.rap.rwt.q07/js/

  * Refresh the rwt.q07 project
    Don't forget to refresh resources after copying the *.js files.


3. Contents of the qx.build project
-----------------------------------

  * patches/

    This directory contains all the patches that we need to apply to the qooxdoo
    codebase. Patches are contained in subdirectories named after the base
    version to which they apply.

  * source/

    This directory contains the source of the current qooxdoo version with all
    related patches applied.

  * qx-<VERSION>.zip

    These zip archives contain the stripped-down base versions of qooxdoo used
    by RAP. The prepare.sh script extracts the zip archive to the source folder
    before applying the patches.

  * prepare.sh

    Bash script to prepare the source directory for the build process.

  * build-custom.sh

    Bash script to create the qooxdoo Javascript files for RAP using the qooxdoo
    generator.
