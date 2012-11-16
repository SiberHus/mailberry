cd /d "%~dp0"

set JAVA_OPTS=-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=768m
mvn jetty:run -Dmaven.test.skip=true -Dmailberry.mode=demo_