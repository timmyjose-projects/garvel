#!/bin/sh


#################################################
##                                             ##
## build script for garvel (UNIX-like systems) ##
##                                             ##
#################################################

# setup paths correctly so that the script
# can be invoked from any directory
curr_dir=$(dirname -- "$0")
cd -- ${curr_dir}
cd ../
PROJECT_ROOT=`pwd`
echo ${PROJECT_ROOT}


## base vars
JAVAC=
JAVAC_FLAGS="-Xlint -source 1.7 -target 1.7"
CLASSPATH="."
JAVA=

## project vars
PROJECT_NAME=garvel
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
    echo "[ Creating build directory ]"
    echo
    mkdir -p ${BUILD_DIR}

    if [[ $? -ne "0" ]]
    then
        echo "Failed to create directory ${BUILD_DIR}. Please check that you have sufficient permissions."
        exit 2
    fi
    echo "[ Created build directory ]"
}


## create the project JAR file
function create_project_jars()
{
    echo
    echo "[ Creating project JAR ${TARGET_NAME} ]"

    if [[ ! -d ${TARGET_DIR} ]]
    then
        mkdir  ${TARGET_DIR}

        if [[ "$?" -ne "0" ]]
        then
            echo "Unable to create ${TARGET_DIR}"
            delete_build_dir 5
        fi
    fi

    pushd ${BUILD_DIR} > /dev/null
    CLASS_FILES=`find ./ -name *.class`

    jar cfe ${TARGET_NAME} ${TARGET_ENTRY_POINT} ${CLASS_FILES}

    if [[ "$?" -eq "0" ]]
    then
        mv ${TARGET_NAME} ${TARGET_DIR}

        if [[ "$?" -ne "0" ]]
        then
            echo "Failed to move ${TARGET_NAME} to ${TARGET_DIR}"
            delete_build_dir 6
        fi

        echo
        echo "[ Created project JAR ${TARGET_NAME} in ${TARGET_DIR} ]"
    else
        echo "[ Unable to create ${TARGET_NAME} ]"
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
    echo "[ Compiling source files "
    echo
    echo "${files}"
    echo
    echo "to ${BUILD_DIR} ]"
    echo

    ${JAVAC} ${JAVAC_FLAGS} -d ${BUILD_DIR} ${files} > /dev/null

    compile_code=$?
    if [[ ${compile_code} -ne "0" ]]
    then
        delete_build_dir 3
    fi

    echo "[ Finished compiling source files ]"

    create_project_jars

    popd > /dev/null
    echo
}

## check if test was successful
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
    echo "[ Running tests ]"
    echo

    `garvel version` >/dev/null
    check_success "test_version" $?
    echo "test_version... success"

    `garvel new foo` >/dev/null
    check_success "test_new" $?
    echo "test_new... success"

    echo "[ All tests passed successfully ]"
}

## create a wrapper for the garvel jar file
function create_garvel_script()
{
    echo "[ Creating wrapper script for ${PROJECT_NAME} ]"
    echo
    if [[ -z ${TARGET_DIR} ]]
    then
        create_target_dir
    fi

    script_path="${TARGET_DIR}/${GARVEL_WRAPPER}"
    touch ${script_path}
    echo '#!/bin/sh' >> ${script_path}
    echo >> ${script_path}

    # setup so that the script can be invoked from anywhere
    echo "script_dir=\$(dirname -- "\$0")" >> ${script_path}
    echo "cd -- \${script_dir}" >> ${script_path}
    echo "cd ../" >> ${script_path}
    echo >> ${script_path}
    echo "java -jar ${TARGET_DIR}/${TARGET_NAME} \$@" >> ${script_path}

    chmod +ux ${TARGET_DIR}/${GARVEL_WRAPPER}
    echo "[ Finished creating wrapper script ${GARVL_WRAPPER} for ${PROJECT_NAME} in ${TARGET_DIR} ]"
    echo "Add the following line to your .bashrc or .bash_profile file:"
    echo "    \"alias ${PROJECT_NAME}=${TARGET_DIR}/${GARVEL_WRAPPER}\""
    echo
}

## delete build data (takes exit code as input)
function delete_build_dir()
{
    echo "[ Deleting build directory ]"
    echo
    if [[ -e ${BUILD_DIR} ]]
    then
        rm -rf ${BUILD_DIR}
        if [[ "$1" -ne "0" ]]
        then
            echo "[ Finished deleting build directory. Exiting with error code $1 ]"
            echo
        else
            echo "[ Finished deleting build directory. Build was successful. ]"
            echo
        fi
        exit $1
    fi
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
delete_build_dir 0
