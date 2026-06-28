#!/bin/sh
set -eu

cd "$(dirname "$0")"

cleanup() {
  ./mvnw -q clean >/dev/null 2>&1 || true
}
trap cleanup EXIT

profiles=$(sed -n 's:.*<id>\(kotlin-compat-[^<]*\)</id>.*:\1:p' pom.xml)

if [ -z "$profiles" ]; then
  echo "No kotlin-compat profiles found in pom.xml" >&2
  exit 1
fi

for profile in $profiles; do
  echo "Running Kotlin compatibility profile: $profile"
  ./mvnw -q -P"$profile" '-Dtest=Kotlin*CompatibilityTest' clean test "$@"
done
