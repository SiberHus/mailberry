cd /d "%~dp0"

rmdir /S /Q dist
mkdir dist

svn export . dist\mailberry

cd dist

7z a mailberry.zip * -bd tzip  -r

copy ..\stars-examples\target\*.war .
copy ..\stars\target\*.jar .
copy ..\license.txt .

rmdir /S /Q stars-examples

cd ..

7z a dist.zip dist\* -bd -tzip -r

pause