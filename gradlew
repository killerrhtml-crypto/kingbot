#!/usr/bin/env sh
DIR="$(cd "$(dirname "$0")" && java -version >/dev/null 2>&1 && pwd)"
exec java -jar gradle/wrapper/gradle-wrapper.jar "$@"
