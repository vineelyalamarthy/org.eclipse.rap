#!/bin/bash
#
# This scipt has been used to create the folders
# qx-build/source/ and
# qx-build/source-replace
# in this project.
#
# * This IPZilla bug contains the approved qooxdoo sources:
#   https://dev.eclipse.org/ipzilla/show_bug.cgi?id=2057
#
# * Get the 3rd attachment and copy the zip file into this project:
#   https://dev.eclipse.org/ipzilla/attachment.cgi?id=1216
#
# * Open a console and cd to this project
#
# * Run this script
#

PROJECT=org.eclipse.rap.tools

VERSION=0.7.3

ZIP_FILE=qx-$VERSION.zip

# make sure we're in the project root
if [ "`pwd`" != "$PROJECT" ]; then
  echo "pwd is $PROJECT, good."
else
  echo "cd to $PROJECT first"
  exit 1
fi

# make sure the zip file exists
if [ -f "$ZIP_FILE" ]; then
  echo "Found $ZIP_FILE, good."
else
  echo "place file $ZIP_FILE into the project root directory first"
  exit 1
fi

# extract zip file into a directory named qx-VERSION/
echo extracting ...
rm -rf qx-$VERSION
unzip -q -d qx-$VERSION "$ZIP_FILE" || exit 1

rsync -a --delete qx-$VERSION/source/class/ qx-build/source/class/

# Apply patches in qx-build/patches
echo applying patches ...
rm -rf tmp-replace
echo --------------------
for file in qx-build/patches/*.diff
do
  echo applying $file
  patch -p0 -b -B tmp-replace/ -i $file
done
echo --------------------

rsync -a --delete tmp-replace/qx-build/source/ qx-build/source-replace/
rm -rf tmp-replace

# Now for every patched file, the original version is kept in the source-replace
# folder. Rsync helps to replace these original files with the patched ones in
# order to create the "overlay directory".

rsync -a --existing qx-build/source/ qx-build/source-replace

rsync -a --delete qx-$VERSION/source/class/ qx-build/source/class/
rm -rf qx-$VERSION

echo done
