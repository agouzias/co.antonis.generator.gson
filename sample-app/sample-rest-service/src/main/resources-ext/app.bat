REM java  -Xmx128m -cp BOOT-INF/classes/.;BOOT-INF/lib/* com.vms.daq.commander.Start
REM java  -Xmx256m -Dlog4j.configurationFile=./config/log4j2.xml --Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar  daq.commander.jar
REM --spring.profiles.active=prod

java  -Xmx256m  -jar sample-rest-service-1.snap.jar

