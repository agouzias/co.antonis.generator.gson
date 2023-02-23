--------------------
-- Notes
--------------------

Multi module created based on https://github.com/tbroyer/gwt-maven-archetypes/
mvn archetype:generate \
   -DarchetypeGroupId=net.ltgt.gwt.archetypes \
   -DarchetypeVersion=LATEST \
   -DarchetypeArtifactId=<artifactId>

Archetypes:
    modular-webapp
    modular-requestfactory
    dagger-guice-rf-activities


A. Web App Modular

shared ->   deps:       gwt-servlet/provided)
            plugins:    maven-source-plugin (copy src in jar)

client ->   deps:       shared (dual with sources), gwt-dev, gwt-user,
            plugins:    gwt-maven-plugin

Use module-name tp
1. Module name is the 'name'.gwt.xml file (OR rename-to)
2. Update at 'index.html' , 'name/name.nocache.js'

