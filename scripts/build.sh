#!/bin/sh


#################################################
##                                             ##
## build script for garvel (UNIX-like systems) ##
##                                             ##
#################################################


## base vars
JAVAC=
JAVAC_FLAGS="-Xlint"
CLASSPATH="."
JAVA=

## project vars
PROJECT_NAME=garvel
PROJECT_ROOT=`pwd`
BUILD_DIR=${PROJECT_ROOT}/build
SRC_ROOT=${PROJECT_ROOT}/src
MAIN_CLASS=Main

## JAR vars
TARGET_DIR=${PROJECT_ROOT}/target
TARGET_NAME=garvel.jar
TARGET_ENTRY_POINT=com/tzj/garvel/cli/CLI
GARVEL_WRAPPER=garvel.sh


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


## delete the target dir if present
function delete_target_dir()
{
    if [[ -e ${TARGET_DIR} && -d ${TARGET_DIR} ]]
    then
        rm -rf ${TARGET_DIR}
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

    ${JAVAC} ${JAVAC_FLAGS} -d ${BUILD_DIR} ${files} > /dev/null

    compile_code=$?
    if [[ ${compile_code} -ne "0" ]]
    then
        delete_build_dir 3
    fi

    create_project_jars

    popd > /dev/null
    echo
}

## check if test was succesful
function check_success
{
    if [[ "$2" -ne "0" ]]
    then
        echo "test $1 failed"
        exit 7
    fi
}


## run all the tests
function run_tests()
{
    echo
    echo "Running tests"
    echo

    `garvel new foo`
    check_success "test_new" $?
    echo "test_new.... success"

    echo "All tests passed successfully"
}

## create a wrapper for the garvel jar file
function create_garvel_script()
{
    echo
    echo "Creating wrapper script for garvel"

    if [[ -z ${TARGET_DIR} ]]
    then
        create_target_dir
    fi

    touch ${TARGET_DIR}/${GARVEL_WRAPPER}
    echo '#!/bin/sh' >> ${TARGET_DIR}/${GARVEL_WRAPPER}
    echo >> ${TARGET_DIR}/${GARVEL_WRAPPER}
    echo "java -jar ${TARGET_DIR}/${TARGET_NAME}" >> ${TARGET_DIR}/${GARVEL_WRAPPER}

    chmod +ux ${TARGET_DIR}/${GARVEL_WRAPPER}
    echo "Finished creating wrapper script for garvel at ${TARGET_DIR}"
    echo "Add the following line to your bashrc or bash_profile file:"
    echo "\"alias ${PROJECT_NAME}=${TARGET_DIR}/${GARVEL_WRAPPER}\""
    echo
}

## delete build data (takes exit code as input)
function delete_build_dir()
{
    echo "Deleting build directory"
    echo
    if [[ -e ${BUILD_DIR} ]]
    then
        rm -rf ${BUILD_DIR}
        exit $1
    fi
    echo

    echo "Finished deleting build directory"
}


#
# steps
#
check_java
delete_target_dir
create_build_dir
compile_sources
#run_tests
create_garvel_script
delete_build_dir
exit 0

