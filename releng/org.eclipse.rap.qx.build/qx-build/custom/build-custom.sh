#!/bin/bash
VERSION=0.7
QOOXDOO=../../../qooxdoo
TOOL=${QOOXDOO}/frontend/framework/tool
TEMP=./temp
SOURCE=${QOOXDOO}/frontend/framework/source  
OUTPUT=./output

echo "  CLEANING DIRECTORIES"
rm -r -f ${OUTPUT}
rm -r -f ${TEMP}


echo "  CREATING WORKING COPY"
mkdir -p ${TEMP}/class
mkdir -p ${TEMP}/resource/static
cp -r ${SOURCE}/class/* ${TEMP}/class
cp -r ${SOURCE}/resource/static/* ${TEMP}/resource/static


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
rm -r -f ${TEMP}/resource/static/stringbuilder
rm -r ${TEMP}/class/qx/theme
rm -r ${TEMP}/class/qx/util/fsm
rm -r ${TEMP}/class/qx/ui/listview
rm -r ${TEMP}/class/qx/ui/pageview/buttonview
rm -r ${TEMP}/class/qx/ui/pageview/radioview
rm -r ${TEMP}/class/qx/ui/component
rm -r ${TEMP}/class/qx/ui/table
rm -r ${TEMP}/class/qx/ui/tablevarrowheight
rm -r ${TEMP}/class/qx/ui/tree
rm -r ${TEMP}/class/qx/ui/treevirtual
rm ${TEMP}/class/qx/dev/ObjectSummary.js
rm ${TEMP}/class/qx/dev/Pollution.js
rm ${TEMP}/class/qx/dev/TimeTracker.js
rm ${TEMP}/class/qx/xml/Document.js
rm ${TEMP}/class/qx/xml/Element.js
rm ${TEMP}/class/qx/xml/Namespace.js
rm ${TEMP}/class/qx/renderer/layout/DockLayoutImpl.js
rm ${TEMP}/class/qx/renderer/layout/FlowLayoutImpl.js
rm ${TEMP}/class/qx/theme/icon/CrystalClear.js
rm ${TEMP}/class/qx/theme/icon/Nuvola.js
rm ${TEMP}/class/qx/theme/icon/VistaInspirate.js
rm ${TEMP}/class/qx/theme/icon/NuoveXT.js
rm ${TEMP}/class/qx/log/AlertAppender.js
rm ${TEMP}/class/qx/log/DivAppender.js
rm ${TEMP}/class/qx/log/RingBufferAppender.js
rm ${TEMP}/class/qx/log/ForwardAppender.js
rm ${TEMP}/class/qx/log/JsUnitAppender.js
rm ${TEMP}/class/qx/manager/selection/VirtualSelectionManager.js
rm ${TEMP}/class/qx/ui/basic/Inline.js
rm ${TEMP}/class/qx/ui/layout/DockLayout.js
rm ${TEMP}/class/qx/ui/layout/FlowLayout.js
rm ${TEMP}/class/qx/ui/form/RepeatButton.js
rm ${TEMP}/class/qx/ui/embed/Flash.js
rm ${TEMP}/class/qx/ui/embed/Gallery.js
rm ${TEMP}/class/qx/ui/embed/GalleryList.js
rm ${TEMP}/class/qx/ui/embed/IconHtmlEmbed.js
rm ${TEMP}/class/qx/ui/embed/LinkEmbed.js
rm ${TEMP}/class/qx/ui/embed/NodeEmbed.js
rm ${TEMP}/class/qx/ui/embed/TextEmbed.js
rm ${TEMP}/class/qx/ui/splitpane/HorizontalSplitPane.js
rm ${TEMP}/class/qx/ui/splitpane/VerticalSplitPane.js
rm ${TEMP}/class/qx/ui/groupbox/CheckGroupBox.js
rm ${TEMP}/class/qx/ui/groupbox/RadioGroupBox.js
rm ${TEMP}/class/qx/ui/resizer/Resizer.js
rm ${TEMP}/class/qx/util/GuiBuilder.js
rm ${TEMP}/class/qx/io/remote/Rpc.js
rm ${TEMP}/class/qx/io/Json.js


#  --add-new-lines \
#  --add-file-ids \
#  --optimize-strings \

#  --print-modules \

echo "  GENERATING custom.js"
${TOOL}/generator.py \
  --generate-compiled-script \
  --compiled-script-file ${OUTPUT}/script/custom.js \
  --class-path ${TEMP}/class/ \
  --use-setting=qx.theme:org.eclipse.swt.theme.Default \
  --add-require qx.log.Logger:qx.log.NativeAppender \
  --include=oo \
  --include=core \
  --include=ui_core \
  --include=ui_window \
  --include=log \
  --include=ui_treefullcontrol \
  --include=ui_tooltip \
  --include=ui_comboboxex \
  --include=ui_tabview \
  --include=ui_toolbar \
  --include=ui_splitpane \
  --include=ui_popup \
  --include=ui_form \
  --include=ui_menu \
  --include=ui_layout \
  --include=ui_basic \
  --include=ui_dragdrop \
  --include=io_remote \
  --include-without-dependencies=qx.client.NativeWindow \
  --include-without-dependencies=qx.ui.embed.Iframe \
  --include-without-dependencies=qx.html.Window \
  --add-file-ids \
  --add-new-lines \
  --version=${VERSION} 


echo "  COPYING RESOURCES"
mkdir -p ${OUTPUT}/resource/static
cp -r ${TEMP}/resource/static ${OUTPUT}/resource


echo "  BUILDING JAR"
cd ${OUTPUT}
zip -q -r qooxdoo-${VERSION}.jar .
cd ..
echo "  DONE"