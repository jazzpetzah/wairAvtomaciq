#!/bin/bash

Track=dev

S3_PATH=`s3cmd ls s3://z-lohika/ios/${Track}/ | tail -n 1 | awk '{print $4}'`
ARCHIVE_NAME=$(basename "$S3_PATH")
DEST_NAME="ZClient_ios.app"
DEST_DIR="/Project"
TMP_DIR=`python -c "import tempfile; print(tempfile.mkdtemp())"`
chmod 755 "${TMP_DIR}"
PWD=`pwd`

pushd "$PWD"
cd "$TMP_DIR"
s3cmd get "$S3_PATH" "$TMP_DIR/$ARCHIVE_NAME"
cpio -id < "$TMP_DIR/$ARCHIVE_NAME"
rm -f "$TMP_DIR/$ARCHIVE_NAME"
mkdir -p "$DEST_DIR"
rm -Rf "$DEST_DIR/$DEST_NAME" || true
mv -f "$TMP_DIR" "$DEST_DIR/$DEST_NAME"
popd