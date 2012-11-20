@echo off
javah -classpath bin/classes -d jni-headers/ -force coms.Vm
pause
