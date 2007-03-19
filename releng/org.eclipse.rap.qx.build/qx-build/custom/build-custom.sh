#!/bin/bash
VERSION=0.6.5
TOOL=../sdk/frontend/framework/tool
TEMP=./temp
SOURCE=../sdk/frontend/framework/source  
OUTPUT=./output

echo "  CLEANING DIRECTORIES"
rm -r ${OUTPUT}
rm -r ${TEMP}

echo "  CREATING WORKING COPY"
mkdir -p ${TEMP}
cp -r ${SOURCE}/* ${TEMP}

echo "  STRIPPING DOWN SDK SOURCES"
rm -r ${TEMP}/resource/icon

rm -r ${TEMP}/class/qx/dev
rm -r ${TEMP}/class/qx/xml
rm -r ${TEMP}/class/qx/util/fsm
rm -r ${TEMP}/class/qx/ui/listview
rm -r ${TEMP}/class/qx/ui/pageview/buttonview
rm -r ${TEMP}/class/qx/ui/component
rm -r ${TEMP}/class/qx/ui/tree
rm -r ${TEMP}/class/qx/ui/treevirtual
rm ${TEMP}/class/qx/lang/Prototypes.js
rm ${TEMP}/class/qx/renderer/layout/DockLayoutImpl.js
rm ${TEMP}/class/qx/renderer/layout/FlowLayoutImpl.js
rm ${TEMP}/class/qx/theme/icon/CrystalClear.js
rm ${TEMP}/class/qx/theme/icon/Nuvola.js
rm ${TEMP}/class/qx/lang/Generics.js
rm ${TEMP}/class/qx/theme/icon/VistaInspirate.js
rm ${TEMP}/class/qx/theme/icon/NuoveXT.js
rm ${TEMP}/class/qx/log/AlertAppender.js
rm ${TEMP}/class/qx/log/DivAppender.js
rm ${TEMP}/class/qx/log/RingBufferAppender.js
rm ${TEMP}/class/qx/log/ForwardAppender.js
rm ${TEMP}/class/qx/manager/selection/TreeSelectionManager.js
rm ${TEMP}/class/qx/manager/selection/VirtualSelectionManager.js
rm ${TEMP}/class/qx/theme/appearance/Classic.js
rm ${TEMP}/class/qx/ui/basic/Inline.js
rm ${TEMP}/class/qx/ui/layout/DockLayout.js
rm ${TEMP}/class/qx/ui/layout/FlowLayout.js
rm ${TEMP}/class/qx/ui/form/RepeatButton.js
rm ${TEMP}/class/qx/ui/table/RemoteTableModel.js
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

echo "  GENERATING custom.js"
${TOOL}/generator.py \
  --generate-compiled-script \
  --compiled-script-file ${OUTPUT}/script/custom.js \
  --class-path ${TEMP}/class/ \
  --define-runtime-setting qx.io.image.ImagePreloaderSystem:3000 \
  --optimize-strings \
  --version=${VERSION} 

echo "  COPYING RESOURCES"
mkdir -p ${OUTPUT}/resource/static
cp -r ${TEMP}/resource/static ${OUTPUT}/resource
mkdir -p ${OUTPUT}/resource/appearance
cp -r ${TEMP}/resource/appearance ${OUTPUT}/resource

echo "  BUILDING JAR"
cd ${OUTPUT}
zip -q -r qooxdoo-${VERSION}.jar .
cd ..
echo "  DONE"