#!/bin/bash

CWD=$('pwd')
PW=`date +%s | sha256sum | base64 | head -c 32`
OPENSSL=`which openssl`
KEYDIR="resources/.keys"
PRIVKEY="${KEYDIR}/private.pem"
PUBKEY="${KEYDIR}/public.pem"

if [ ! -d "$KEYDIR" ]; then
    mkdir $KEYDIR
else
    rm -f $PRIVKEY $PUBKEY
fi

echo $PW > dat.ph
$OPENSSL genrsa -des3 -passout file:$KEYDIR/dat.ph -out $PRIVKEY 4096
$OPENSSL rsa -in $PRIVKEY -passin file:$KEYDIR/dat.ph -outform PEM -pubout -out $PUBKEY
