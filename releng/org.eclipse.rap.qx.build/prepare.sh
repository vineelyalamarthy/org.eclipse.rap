#!/bin/sh
#
# This script has been used to create the folders source/ and source-replace/.
#
# * This IPZilla bug contains the approved qooxdoo sources:
#   https://dev.eclipse.org/ipzilla/show_bug.cgi?id=2057
#
# * Get the 3rd attachment and copy the zip file into the qx-build directory:
#   https://dev.eclipse.org/ipzilla/attachment.cgi?id=1216
#
# * Open a console and cd to the qx-build directory
#
# * Run this script
#

WORKING_DIR=qx-build

VERSION=0.7.4

ZIP_FILE=qx-$VERSION.zip

# print usage info
print_usage() {
  echo "Usage: $0 <target> [args]"
  echo "possible targets are:"
  echo "  source            overwrite source folder with contents from zip file"
  echo "  patch <file>+     apply given patch files"
  echo "  patch-all         apply all patches in patches dir"
  echo "  clean             delete temporary folders"
  echo "  all               clean, source, patch-all, clean"
}

# print failure notice and exit
fail() {
  echo "failed"
  exit 1
}

# make sure we're in the right working directory
ensure_wd() {
  if [ "`basename \"$PWD\"`" != "$WORKING_DIR" ]; then
    echo "cd to $WORKING_DIR first"
    fail
  fi
  echo "working directory is $WORKING_DIR, good."
}

# make sure the zip file exists
ensure_zipfile() {
  if [ ! -f "$ZIP_FILE" ]; then
    echo "missing zip file $ZIP_FILE, place this file into $WORKING_DIR first"
    fail
  fi
  echo "found $ZIP_FILE, good."
}

# extract zip file into a directory named qx-VERSION/
extract_zipfile() {
  echo extracting ...
  rm -rf qx-$VERSION
  unzip -q -d qx-$VERSION "$ZIP_FILE" || fail
  echo ok
}

# create or overwrite source folder
create_source() {
  echo creating source folder ...
  rsync -a -c --delete --exclude="CVS/" "qx-$VERSION/source/class/" source/class/ || fail
  echo ok
}

# Apply a single patch file
apply_patch() {
  echo applying "$1"
  patch -p1 -i "$1" || fail
}

# Apply patches in qx-build/patches
apply_all_patches() {
  echo applying patches ...
  echo --------------------
  for file in patches/$VERSION/*.diff; do
    apply_patch "$file"
  done
  echo --------------------
  echo ok
}

clean_up() {
  echo cleaning up ...
  rm -rf qx-$VERSION
  echo ok
}

TARGET=$1
if [ $# -gt 0 ]; then
  shift
fi

case "$TARGET" in
  "source")
    ensure_wd
    ensure_zipfile
    extract_zipfile
    create_source
    ;;
  "patch")
    ensure_wd
    while [ $# -ge 1 ]; do
      apply_patch "$1"
      shift
    done
  ;;
  "patch-all")
    ensure_wd
    apply_all_patches
    ;;
  "clean")
    ensure_wd
    clean_up
    ;;
  "all")
    ensure_wd
    clean_up
    ensure_zipfile
    extract_zipfile
    create_source
    apply_all_patches
    clean_up
    ;;
  *)
    print_usage
    exit 1
    ;;
esac
