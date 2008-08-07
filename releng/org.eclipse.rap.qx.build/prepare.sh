#!/bin/sh
#
# This script has been used to create the folders
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

# print usage info
print_usage() {
  echo "Usage: $0 <target> [args]"
  echo "possible targets are:"
  echo "  unzip             extract zip archive"
  echo "  source            create or overwrite source folder"
  echo "  patch <file>+     apply given patch files"
  echo "  patch-all         apply all patches in patches dir"
  echo "  source-replace    create source-replace overlay folder"
  echo "  clean             delete temporary folders"
  echo "  all               clean, unzip, source, patch-all, source-replace, clean"
}

# print failure notice and exit
fail() {
  echo "failed"
  exit 1
}

# make sure we're in the project root
ensure_pwd() {
  if [ "` basename $PWD`" != "$PROJECT" ]; then
    echo "cd to $PROJECT first"
    fail
  fi
  echo "working directory is $PROJECT, good."
}

# make sure the zip file exists
ensure_zipfile() {
  if [ ! -f "$ZIP_FILE" ]; then
    echo "missing zip file $ZIP_FILE, place this file into the project root directory first"
    fail
  fi
  echo "Found $ZIP_FILE, good."
}

# make sure the extracted folder exists
ensure_extracted() {
  if [ ! -d "qx-$VERSION/source/class/" ]; then
    echo "missing directory qx-$VERSION/source/class/, call unzip before"
    fail
  fi
  echo "Directory qx-$VERSION/source/class/ exists, good."
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
  rsync -a --delete "qx-$VERSION/source/class/" qx-build/source/class/ || fail
  echo ok
}

# Apply a single patch file
apply_patch() {
  echo applying "$1"
  patch -p0 -b -B tmp-replace/ -i "$1" || fail
}

# Apply patches in qx-build/patches
apply_all_patches() {
  echo applying patches ...
  rm -rf tmp-replace
  echo --------------------
  for file in qx-build/patches/$VERSION/*.diff; do
    apply_patch "$file"
  done
  echo --------------------
  echo ok
}

# create or overwrite source-replace folder
create_source_replace() {
  echo creating source-replace folder ...
  rsync -a --delete tmp-replace/qx-build/source/ qx-build/source-replace/ || fail
  rm -rf tmp-replace
  # Now for every patched file, the original version is kept in the source-replace
  # folder. Rsync helps to replace these original files with the patched ones in
  # order to create the "overlay directory".
  rsync -a --existing qx-build/source/ qx-build/source-replace || fail
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
  "unzip")
    ensure_pwd
    ensure_zipfile
    extract_zipfile
    ;;
  "source")
    ensure_pwd
    ensure_extracted
    create_source
    ;;
  "patch")
    ensure_pwd
    ensure_extracted
    while [ $# -ge 1 ]; do
      apply_patch "$1"
      shift
    done
  ;;
  "patch-all")
    ensure_pwd
    ensure_extracted
    apply_all_patches
    ;;
  "source_replace")
    ensure_pwd
    ensure_extracted
    create_source_replace
    ;;
  "clean")
    ensure_pwd
    clean_up
    ;;
  "all")
    ensure_pwd
    clean_up
    ensure_zipfile
    extract_zipfile
    create_source
    apply_all_patches
    create_source_replace
    create_source
    clean_up
    ;;
  *)
    print_usage
    exit 1
    ;;
esac
