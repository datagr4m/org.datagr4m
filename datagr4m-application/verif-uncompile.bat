cd target
mkdir uncompiled-datagr4m-application
cd uncompiled-datagr4m-application
jar xvf ../original-com-datagr4m-application-1.0-SNAPSHOT.jar
"../../tools/jad" -r -d readable/ **/*.class
pause
