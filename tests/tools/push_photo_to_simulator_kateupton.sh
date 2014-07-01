#!/bin/bash

echo "photo script opend"

if [ -z "$1" ]; then
    echo "Error: No iPhone Simulator version supplied. Usage: $0 [version]"
    exit -1
fi

#navigate to simulator directory
media_base="$HOME/Library/Application Support/iPhone Simulator/$1/Media"
photo_data="$media_base/PhotoData"
photo_dir="$media_base/DCIM/100APPLE"

if [ ! -d "$media_base" ]; then
    mkdir -p "$photo_dir"
fi

kate_pic="$photo_dir/kate.jpg"

if [ ! -f "$kate_pic" ]; then
    pushd `pwd`
    if [ -d "$photo_data" ]; then
        cd "$photo_data"
        rm -rf *
    fi
    if [ -d "$photo_dir" ]; then
        cd "$photo_dir"
        rm -rf *
    fi
    popd
  
    #get kate
    curl -L -o "$kate_pic" file:///./zautomation/tests/tools/img/kate-upton-by-yu-tsai-1791080246.jpg

    if [ $? -ne 0 ]; then
        echo "Error: could not download image"
        exit -1
    fi
fi

echo "Done"
exit 0