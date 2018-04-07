#!/bin/sh

## place holder for a proper shell script for UNIX-like systems

JAVAC=javac
CLASSPATH="."
JAVA=java

if [[ -z "${JAVA_HOME}" ]]
then
    `which javac` &1>/dev/null

    if [[ $? -ne "0" ]]
    then
        echo "No java compiler found; please set JAVA_HOME explicitly or make javac available on the PATH"
        exit 1
     fi
fi

SRC_ROOT=src
MAIN_CLASS=com.tzj.kaapi.main.Main

pushd ${SRC_ROOT}

files=`find ./ -name *.java`
echo "compiling ${files}"
${JAVAC} ${files}

echo "running the app"
${JAVA} -cp ${CLASSPATH} ${MAIN_CLASS}

files=`find ./ -name *.class`
for file in ${files}
do
    if [[ -e ${file} ]]
    then
        echo "removing ${file}"
        rm ${file}
    fi
done

popd






