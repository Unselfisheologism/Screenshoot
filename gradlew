#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a symlink
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    link=`expr "$PRG" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MSYS* | MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$cygwin" = true -o "$msys" = true ] ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    APP_BASE_NAME=`cygpath --windows "$APP_BASE_NAME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
    
    JAVACMD=`cygpath --unix "$JAVACMD"`

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=`find -L / -maxdepth 3 -type d -name java_home 2>/dev/null`
    SEP=""
    for dir in $ROOTDIRSRAW ; do
        ROOTDIR="$dir/../.."
        if [ -d "$dir" ]; then
            ROOTDIR=`cygpath --path --ignore --mixed "$ROOTDIR"`
            CLASSPATH="$ROOTDIR/lib:$CLASSPATH"
            JAVACMD="$ROOTDIR/bin/java"
            [ -n "$JAVA_HOME" ] && \
              JAVA_HOME=`cygpath --path --ignore --mixed "$JAVA_HOME"`
            MAX_FD_LIMIT=`ulimit -H -n`
            if [ $? -eq 0 ] ; then
                if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
                    MAX_FD="$MAX_FD_LIMIT"
                fi
                ulimit -n $MAX_FD
                if [ $? -ne 0 ] ; then
                    warn "Could not set maximum file descriptor limit: $MAX_FD"
                fi
            else
                warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
            fi
        fi
        break
    done
fi

# For Darwin, add options to specify how the application appears in the dock
if $darwin; then
    GRADLE_OPTS="$GRADLE_OPTS \"-Xdock:name=$APP_NAME\" \"-Xdock:icon=$APP_HOME/media/gradle.icns\""
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$cygwin" = true -o "$msys" = true ] ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
    JAVACMD=`cygpath --windows "$JAVACMD"`
    
    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=`find -L / -maxdepth 3 -type d -name java_home 2>/dev/null`
    SEP=""
    for dir in $ROOTDIRSRAW ; do
        ROOTDIR="$dir/../.."
        if [ -d "$dir" ]; then
            ROOTDIR=`cygpath --path --ignore --mixed "$ROOTDIR"`
            CLASSPATH="$ROOTDIR/lib:$CLASSPATH"
            JAVACMD="$ROOTDIR/bin/java"
            [ -n "$JAVA_HOME" ] && \
              JAVA_HOME=`cygpath --path --ignore --mixed "$JAVA_HOME"`
            MAX_FD_LIMIT=`ulimit -H -n`
            if [ $? -eq 0 ] ; then
                if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
                    MAX_FD="$MAX_FD_LIMIT"
                fi
                ulimit -n $MAX_FD
                if [ $? -ne 0 ] ; then
                    warn "Could not set maximum file descriptor limit: $MAX_FD"
                fi
            else
                warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
            fi
        fi
        break
    done
fi

# Escape application args
save () {
    for i do printf %s\\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=`save "$@"`

# Collect all arguments for the java command, following the shell quoting and substitution rules
eval "set -- $DEFAULT_JVM_OPTS" '"$@"'

# by default we should be in the correct project dir, but allow the user to override this with -f / --file
if [ "$(uname)" = "Darwin" ]; then
    realpath() {
        if [ -d "$1" ]; then
            cd "$1" 2>/dev/null || return 1
            pwd -P
            return 0
        else
            return 1
        fi
    }
elif command -v realpath > /dev/null 2>&1; then
    realpath() {
        realpath "$1"
    }
else
    realpath() {
        python3 -c "import sys; import os.path; print(os.path.realpath(sys.argv[1]))" "$1"
    }
fi

# Determine the Java command to use in order to perform the syntax check.
not_found=0
if [ ! -x "$JAVACMD" ] ; then
    if [ "x$JAVA_HOME" != x ]; then
        JAVACMD="$JAVA_HOME/bin/java"
    else
        JAVACMD="java"
    fi
fi
if ! command -v "$JAVACMD" > /dev/null 2>&1; then
    echo "Error: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
    not_found=1
fi
if [ "$1" = "-?" ] || [ "$1" = "-h" ] || [ "$1" = "--help" ] ; then
    echo "usage: $0 [<option>...] [<task>...]"
    echo
    echo "where <option> includes:"
    echo "  -b, --build-cache   Enable the Gradle build cache for this build."
    echo "  -c, --continue      Execute tasks even if a previous task has failed."
    echo "  -D, --system-prop   Set a system property of the JVM."
    echo "  -d, --debug         Log in debug mode (log level = DEBUG)."
    echo "  -g, --gradle-user-home <directory>"
    echo "                      Specifies the gradle user home directory."
    echo "  -I, --init-script <file>"
    echo "                      Specifies an initialization script."
    echo "  -i, --info          Set log level to info."
    echo "  -m, --max-workers <int>"
    echo "                      Configure max number of workers that Gradle may use."
    echo "  -p, --project-dir <directory>"
    echo "                      Specifies the start directory for Gradle. Default is current directory."
    echo "  -q, --quiet         Log warnings only."
    echo "  -s, --stacktrace    Print out the stacktrace for all exceptions."
    echo "  -S, --full-stacktrace"
    echo "                      Print out the full (very verbose) stacktrace for all exceptions."
    echo "  -v, --version       Print version info."
    echo "  -w, --warn          Log at warn level."
    echo "  --scan              Scan build with build scan."
    echo "  --no-scan           Suppress build scan."
    echo "  --refresh-dependencies"
    echo "                      Refresh the state of dependencies."
    echo "  --write-locks       Write dependency locks."
    echo "  --update-locks      Perform a partial update of the dependency lock file."
    echo "  --write-verification-metadata"
    echo "                      Write dependency verification metadata."
    echo "  --refresh-keys      Refresh the public keys used for dependency verification."
    echo "  --export-keys       Export the public keys used for dependency verification."
    exit 0
fi
if [ "$not_found" = "1" ] ; then
    echo
    echo "* ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
    echo
    exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
    JAVA_HOME=`realpath "$(command -v "java")"` 2>/dev/null
    [ $? -ne 0 ] || JAVA_HOME="$(dirname "$JAVA_HOME")/.." 2>/dev/null
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$cygwin" = true ] || [ "$msys" = true ] ; then
    JAVA_HOME=`cygpath --path --mixed "$JAVA_HOME"`
    [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
fi

# Concatenate all options together
GRADLE_OPTS="$DEFAULT_JVM_OPTS" "$JAVA_OPTS" "$GRADLE_OPTS"
GIT_COMMIT=`git -C "$APP_HOME" rev-parse --short HEAD 2>/dev/null`
if [ -z "$GIT_COMMIT" ] ; then
    GIT_COMMIT='unknown'
fi
GIT_BRANCH=`git -C "$APP_HOME" rev-parse --abbrev-ref HEAD 2>/dev/null`
if [ -z "$GIT_BRANCH" ] ; then
    GIT_BRANCH='unknown'
fi

exec "$JAVACMD" $GRADLE_OPTS \
    -Dorg.gradle.appname="$APP_BASE_NAME" \
    -Dorg.gradle.java.home="$JAVA_HOME" \
    -Dorg.gradle.version.git.commit="$GIT_COMMIT" \
    -Dorg.gradle.version.git.branch="$GIT_BRANCH" \
    -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain \
    "$APP_ARGS"
