#!/bin/sh
#
# This script must be run from the org.eclipse.rap.rwt.qx.build project root as
# working directory.

VERSION=0.7.4
REVISION=16878

# point to the directory that contains the generator.py
TOOL=../qx-0.7.4/qooxdoo/frontend/framework/tool

SOURCE=./source
OUTPUT=./output

OUTPUT_FILE=${OUTPUT}/qx.js
OUTPUT_FILE_DEBUG=${OUTPUT}/qx-debug.js

echo "  CLEANING DIRECTORIES"
rm -r -f ${OUTPUT}

SETTINGS="--use-setting=qx.theme:org.eclipse.swt.theme.Default 
  --use-setting=qx.logAppender:qx.log.appender.Native 
  --add-require qx.log.Logger:qx.log.appender.Native
  --use-variant=qx.compatibility:off
  --use-variant=qx.aspects:off"

echo "  GENERATING ${OUTPUT_FILE}"
${TOOL}/generator.py \
  --generate-compiled-script \
  --compiled-script-file ${OUTPUT_FILE} \
  --class-path ${SOURCE}/class/ \
  --version="${VERSION} (r${REVISION})" \
  `echo ${SETTINGS}` \
  --use-variant=qx.debug:off \
  --optimize-strings \
  --optimize-variables \
  --optimize-base-call

echo "  GENERATING ${OUTPUT_FILE_DEBUG}"
${TOOL}/generator.py \
  --generate-compiled-script \
  --compiled-script-file ${OUTPUT_FILE_DEBUG} \
  --class-path ${SOURCE}/class/ \
  --version="${VERSION} (r${REVISION}) [debug]" \
  `echo ${SETTINGS}` \
  --use-variant=qx.debug:on \
  --add-file-ids \
  --add-new-lines

echo "    Size of ${OUTPUT_FILE} is `stat -c %s ${OUTPUT_FILE}` bytes"
echo "    Size of ${OUTPUT_FILE_DEBUG} is `stat -c %s ${OUTPUT_FILE_DEBUG}` bytes"

echo "  DONE"
