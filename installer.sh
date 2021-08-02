#!/usr/bin/env bash

REPO=katan-cli

git clone https://github.com/KatanPanel/$REPO.git --depth=1 || {
  echo >&2 "Clone failed: $?"
  exit 1
}

cd $REPO || exit

chmod +x gradlew
./gradlew install || {
  echo >&2 "Install failed: $?"
  exit 1
}

cd ..
rm -rf $REPO