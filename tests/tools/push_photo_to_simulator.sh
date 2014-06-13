#!/bin/bash

if [ -z "$1" ]; then
    echo "Error: No iPhone Simulator version supplied. Usage: $0 [version]"
    exit -1
fi

#navigate to simulator directory
media_base="$HOME/Library/Application Support/iPhone Simulator/$1/Media"

if [ ! -d "$media_base" ]; then
    echo "Error: iPhone Simulator directory not found at $media_base"
    exit -1
fi

echo "Found iPhone Simulator $1 Media directory at $media_base"

echo "Removing PhotoData Directory"
photo_data="$media_base/PhotoData"
rm -r "$photo_data"

photo_dir="$media_base/DCIM/100APPLE"
cat_pic="$photo_dir/cat.jpg"

if [ ! -f "$cat_pic" ]; then
    #get a cat
    curl -L -o cat.jpg http://thecatapi.com/api/images/get?type=jpg

    if [ $? -ne 0 ]; then
        echo "Error: could not download image"
        exit -1
    fi
	
	if [ ! -d "$photo_dir" ]; then
		mkdir -p "$photo_dir"
	fi
    
	mv cat.jpg "$cat_pic"
fi

echo "Done"
exit 0