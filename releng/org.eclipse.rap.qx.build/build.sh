#!/bin/sh
#
# This script must be run from the org.eclipse.rap.rwt.qx.build project root as
# working directory.

VERSION=0.7.4
REVISION=16878

# point to a qx 0.7.4 base directory (contains the generator.py)
test "$QXROOT" || QXROOT=../qx-0.7.4

SOURCE=../org.eclipse.rap.rwt.q07/js
OUTPUT=../org.eclipse.rap.rwt.q07/resources

OUTPUT_FILE=${OUTPUT}/client.js

SETTINGS="--use-setting=qx.theme:org.eclipse.swt.theme.Default 
  --use-setting=qx.logAppender:qx.log.appender.Native 
  --add-require qx.log.Logger:qx.log.appender.Native
  --use-variant=qx.compatibility:off
  --use-variant=qx.aspects:off"

echo "  GENERATING ${OUTPUT_FILE}"
$QXROOT/qooxdoo/frontend/framework/tool/generator.py \
  --generate-compiled-script \
  --compiled-script-file ${OUTPUT_FILE} \
  --class-path ${SOURCE} \
  --version="${VERSION} (r${REVISION})" \
  `echo ${SETTINGS}` \
  --use-variant=qx.debug:off \
  --exclude=debug-settings \
  --optimize-strings \
  --optimize-variables \
  --optimize-base-call

echo "    Size of ${OUTPUT_FILE} is `stat -c %s ${OUTPUT_FILE}` bytes"
echo "  DONE"