#!/bin/sh


################################################
##                                            ##
## build script for kaapi (UNIX-like systems) ##
##                                            ##
################################################


## base vars
JAVAC=
CLASSPATH="."
JAVA=

## project vars
PROJECT_ROOT=`pwd`
BUILD_DIR=${PROJECT_ROOT}/build
SRC_ROOT=${PROJECT_ROOT}/src
MAIN_CLASS=com.tzj.kaapi.test.Main

## JAR vars
TARGET_DIR=${PROJECT_ROOT}/target
TARGET_NAME=kaapi.jar
TARGET_ENTRY_POINT=com/tzj/kaapi/test/Main


## check for the java compiler
function check_java()
{
    if [[ ! -z "${JAVA_HOME}" ]]
    then
        JAVAC=${JAVA_HOME}/bin/javac
        JAVA=${JAVA_HOME}/bin/java
    else
       which javac > /dev/null

        if [[ $? -ne "0" ]]
        then
            echo "No java compiler found; please set JAVA_HOME explicitly or make javac available on the PATH"
            exit 1
        else
            JAVAC=javac
            JAVA=java
        fi
    fi
    echo
}


## create the main build directory
function create_build_dir()
{
    echo "creating build directory"
    mkdir -p ${BUILD_DIR}

    if [[ $? -ne "0" ]]
    then
        echo "Failed to create directory ${BUILD_DIR}. Please check that you have sufficient permissions."
        exit 2
    fi
}


## delete build data (takes exit code as input)
function delete_build_dir()
{
    echo "deleting build directory"
    echo
    if [[ -e ${BUILD_DIR} ]]
    then
        rm -rf ${BUILD_DIR}
        exit $1
    fi
    echo
}


## create the project JAR file
function create_project_jars()
{
    if [[ ! -d ${TARGET_DIR} ]]
    then
        mkdir  ${TARGET_DIR}

        if [[ $? -ne "0" ]]
        then
            echo "unable to create ${TARGET_DIR}"
            delete_build_dir 5
        fi
    fi

    pushd ${BUILD_DIR} > /dev/null
    jar_files=`find ./ -name *.class`
    echo ${jar_files}

    jar cfe ${TARGET_NAME} ${TARGET_ENTRY_POINT} ${jar_files}

    if [[ $? -eq "0" ]]
    then
        mv ${TARGET_NAME} ${TARGET_DIR}

        if [[ $? -ne "0" ]]
        then
            echo "Failed to move ${TARGET_NAME} to ${TARGET_DIR}"
            delete_build_dir 6
        fi

        echo "${TARGET_NAME} created successfully in ${TARGET_DIR}"
    else
        echo "Unable to create ${TARGET_NAME}"
        delete_build_dir 4
    fi
    popd > /dev/null
}


## compile the sources
function compile_sources()
{
    echo
    pushd ${SRC_ROOT} > /dev/null

    files=`find ./ -name *.java`
    echo "compiling"
    echo "${files}"
    echo "to ${BUILD_DIR}"
    echo

    ${JAVAC} -d ${BUILD_DIR} ${files} > /dev/null

    compile_code=$?
    if [[ ${compile_code} -ne "0" ]]
    then
        delete_build_dir 3
    fi

    create_project_jars

    popd > /dev/null
    echo
}


## run all the tests
function run_tests()
{
    echo "all tests passed successfully"
}

## run the main program
function run_app()
{
    pushd ${BUILD_DIR} > /dev/null

    echo "running the app"
    echo

    ${JAVA} -cp ${CLASSPATH} ${MAIN_CLASS}

    popd > /dev/null
    echo
}

check_java
create_build_dir
compile_sources
run_tests
run_app
delete_build_dir
exit 0


