@echo off
set JAVAFX_SDK=C:\javafx-sdk-17.0.11
set PATH_TO_FX=%JAVAFX_SDK%\lib

javac -d -cp lib/mariadb-java-client-2.7.12.jar;%PATH_TO_FX%\javafx.base.jar;%PATH_TO_FX%\javafx.controls.jar;%PATH_TO_FX%\javafx.fxml.jar;%PATH_TO_FX%\javafx.graphics.jar src/com/example/*.java

java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -Dprism.order=sw -Dprism.verbose=true -cp out;lib/mariadb-java-client-2.7.12.jar com.example.Main
pause
