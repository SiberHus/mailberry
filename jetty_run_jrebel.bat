cd /d "%~dp0"

set MAVEN_OPTS=-noverify -javaagent:%REBEL_HOME%\jrebel.jar %MAVEN_OPTS%
set JAVA_OPTS=-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m
mvn jetty:run -Dmaven.test.skip=true -Dmailberry.mode=demo_