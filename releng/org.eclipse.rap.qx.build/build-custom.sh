#!/bin/sh
#
# This script must be run from the org.eclipse.rap.rwt.qx.build project root as
# working directory.

VERSION=0.7.4
REVISION=16878

# point to the directory that contains the generator.py
TOOL=../qx-0.7.4/qooxdoo/frontend/framework/tool

TEMP=./temp
SOURCE=./source
OUTPUT=./output

OUTPUT_FILE=${OUTPUT}/qx.js
OUTPUT_FILE_DEBUG=${OUTPUT}/qx-debug.js

echo "  CLEANING DIRECTORIES"
rm -r -f ${OUTPUT}
rm -r -f ${TEMP}


echo "  CREATING WORKING COPY"
mkdir -p ${TEMP}/class
cp -r ${SOURCE}/class/* ${TEMP}/class


echo "  DELETING REPOSITORY META DATA"
SVN=`find ${TEMP} -name ".svn" -type d`
for DIRECTORY in ${SVN} ; do
  if [ -d ${DIRECTORY} ] ; then
    rm -rf ${DIRECTORY}
  else
    echo "  Directory ${DIRECTORY} does not exist."
  fi
done 


echo "  STRIPPING DOWN SDK SOURCES"
rm -r ${TEMP}/class/qx/event/message
rm -r ${TEMP}/class/qx/io/local
rm -r ${TEMP}/class/qx/theme/classic
rm -r ${TEMP}/class/qx/theme/ext
rm -r ${TEMP}/class/qx/theme/icon
rm -r ${TEMP}/class/qx/util/fsm
rm -r ${TEMP}/class/qx/ui/listview
rm -r ${TEMP}/class/qx/ui/pageview/buttonview
rm -r ${TEMP}/class/qx/ui/pageview/radioview
rm -r ${TEMP}/class/qx/ui/component
rm -r ${TEMP}/class/qx/ui/splitpane
rm -r ${TEMP}/class/qx/ui/treevirtual
rm ${TEMP}/class/qx/OO.js
rm ${TEMP}/class/qx/core/MLegacyInit.js
rm ${TEMP}/class/qx/client/NativeWindow.js
rm ${TEMP}/class/qx/dev/ObjectSummary.js
rm ${TEMP}/class/qx/dev/Pollution.js
rm ${TEMP}/class/qx/dev/TimeTracker.js
rm ${TEMP}/class/qx/dev/Tokenizer.js
rm ${TEMP}/class/qx/html/ElementFromPoint.js
rm ${TEMP}/class/qx/html/Form.js
rm ${TEMP}/class/qx/html/Location.js
rm ${TEMP}/class/qx/html/Textile.js
rm ${TEMP}/class/qx/xml/Document.js
rm ${TEMP}/class/qx/xml/Element.js
rm ${TEMP}/class/qx/util/StringBuilder.js
rm ${TEMP}/class/qx/util/EditDistance.js
rm ${TEMP}/class/qx/util/format/DateFormat.js
rm ${TEMP}/class/qx/util/format/Format.js
rm ${TEMP}/class/qx/util/format/NumberFormat.js
rm ${TEMP}/class/qx/ui/layout/impl/GridLayoutImpl.js
rm ${TEMP}/class/qx/ui/layout/impl/DockLayoutImpl.js
rm ${TEMP}/class/qx/ui/layout/impl/FlowLayoutImpl.js
rm ${TEMP}/class/qx/theme/*.js
rm ${TEMP}/class/qx/locale/Date.js
rm ${TEMP}/class/qx/locale/Number.js
rm ${TEMP}/class/qx/locale/String.js
rm ${TEMP}/class/qx/log/appender/Alert.js
rm ${TEMP}/class/qx/log/appender/Div.js
rm ${TEMP}/class/qx/log/appender/RingBuffer.js
rm ${TEMP}/class/qx/log/appender/Forward.js
rm ${TEMP}/class/qx/log/appender/JsUnit.js
rm ${TEMP}/class/qx/net/Protocol.js
rm ${TEMP}/class/qx/ui/basic/Inline.js
rm ${TEMP}/class/qx/ui/basic/VerticalSpacer.js
rm ${TEMP}/class/qx/ui/layout/GridLayout.js
rm ${TEMP}/class/qx/ui/layout/DockLayout.js
rm ${TEMP}/class/qx/ui/layout/FlowLayout.js
rm ${TEMP}/class/qx/ui/menu/Button.js
rm ${TEMP}/class/qx/ui/menu/ButtonLayoutImpl.js
rm ${TEMP}/class/qx/ui/menu/CheckBox.js
rm ${TEMP}/class/qx/ui/menu/Layout.js
rm ${TEMP}/class/qx/ui/menu/Menu.js
rm ${TEMP}/class/qx/ui/menu/MenuLayoutImpl.js
rm ${TEMP}/class/qx/ui/menu/RadioButton.js
rm ${TEMP}/class/qx/ui/menu/Manager.js
rm ${TEMP}/class/qx/ui/form/ComboBox.js
rm ${TEMP}/class/qx/ui/form/ComboBoxEx.js
rm ${TEMP}/class/qx/ui/form/RepeatButton.js
rm ${TEMP}/class/qx/ui/toolbar/*.js
rm ${TEMP}/class/qx/ui/menubar/*.js
rm ${TEMP}/class/qx/ui/embed/Flash.js
rm ${TEMP}/class/qx/ui/embed/Gallery.js
rm ${TEMP}/class/qx/ui/embed/GalleryList.js
rm ${TEMP}/class/qx/ui/embed/IconHtmlEmbed.js
rm ${TEMP}/class/qx/ui/embed/LinkEmbed.js
rm ${TEMP}/class/qx/ui/embed/NodeEmbed.js
rm ${TEMP}/class/qx/ui/embed/TextEmbed.js
rm ${TEMP}/class/qx/ui/form/PasswordField.js
rm ${TEMP}/class/qx/ui/form/TextArea.js
rm ${TEMP}/class/qx/ui/groupbox/CheckGroupBox.js
rm ${TEMP}/class/qx/ui/groupbox/RadioGroupBox.js
rm ${TEMP}/class/qx/ui/resizer/Resizer.js
rm ${TEMP}/class/qx/ui/selection/DomSelectionManager.js
rm ${TEMP}/class/qx/util/GuiBuilder.js
rm ${TEMP}/class/qx/io/remote/Rpc.js
rm ${TEMP}/class/qx/io/remote/ScriptTransport.js
rm ${TEMP}/class/qx/io/Json.js


# *** command line switches to optimize generated output ***
#  --use-variant=qx.debug:off \
#  --add-new-lines \
#  --add-file-ids \
#  --optimize-strings \
#  --optimize-variables \
#  --optimize-base-call \
#  --optimize-private \

#  --add-file-ids \
#  --add-new-lines \

#  --print-modules \

SETTINGS="--use-setting=qx.theme:org.eclipse.swt.theme.Default 
  --use-setting=qx.logAppender:qx.log.appender.Native 
  --add-require qx.log.Logger:qx.log.appender.Native
  --use-variant=qx.compatibility:off
  --use-variant=qx.aspects:off"

#  --include-without-dependencies=qx.client.NativeWindow 
INCLUDES="--include=oo 
  --include=core 
  --include=ui_core 
  --include=ui_window 
  --include=log 
  --include=ui_treefullcontrol 
  --include=ui_tooltip 
  --include=ui_tabview 
  --include=ui_toolbar 
  --include=ui_splitpane 
  --include=ui_popup 
  --include=ui_form 
  --include=ui_menu 
  --include=ui_layout 
  --include=ui_basic 
  --include=ui_dragdrop 
  --include=io_remote 
  --include-without-dependencies=qx.ui.embed.Iframe 
  --include-without-dependencies=qx.html.Window 
  --include-without-dependencies=qx.ui.basic.ScrollBar 
  --include-without-dependencies=qx.ui.basic.ScrollArea
  --include-without-dependencies=qx.client.History"

echo "  GENERATING ${OUTPUT_FILE}"
${TOOL}/generator.py \
  --generate-compiled-script \
  --compiled-script-file ${OUTPUT_FILE} \
  --class-path ${TEMP}/class/ \
  --version="${VERSION} (r${REVISION})" \
  `echo ${SETTINGS}` \
  --use-variant=qx.debug:off \
  --optimize-strings \
  --optimize-variables \
  --optimize-base-call \
  `echo ${INCLUDES}`


echo "  GENERATING ${OUTPUT_FILE_DEBUG}"
${TOOL}/generator.py \
  --generate-compiled-script \
  --compiled-script-file ${OUTPUT_FILE_DEBUG} \
  --class-path ${TEMP}/class/ \
  --version="${VERSION} (r${REVISION}) [debug]" \
  `echo ${SETTINGS}` \
  --use-variant=qx.debug:on \
  --add-file-ids \
  --add-new-lines \
  `echo ${INCLUDES}`


echo "    Size of ${OUTPUT_FILE} is `stat -c %s ${OUTPUT_FILE}` bytes"
echo "    Size of ${OUTPUT_FILE_DEBUG} is `stat -c %s ${OUTPUT_FILE_DEBUG}` bytes"


echo "  CLEANING WORKING DIRECTORY"
rm -r -f ${TEMP}


echo "  DONE"
