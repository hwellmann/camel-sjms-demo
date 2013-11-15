Demos for Simple (Spring-less) JMS with Apache Camel
====================================================

The module sjms-demo4 includes a test case for CAMEL-6950.

This module uses a patched and renamed version of camel-sjms
called camel-sjmsee.

Building:

    git clone -b sjmsee https://github.com/hwellmann/camel.git
    cd camel
    mvn -pl :camel-sjmsee -am -DskipTests=true
    
    cd ..
    git clone https://github.com/hwellmann/camel-sjms-demo.git
    cd camel-sjms-demo
    mvn clean install
    
Setup:

* JBoss AS 7.1.3 with ActiveMQ 5.9.0 RAR configured as resource adapter with external broker
* Deploy sjms-demo4.war This is a trivial calculator webapp where UI and backend communicate over (S)JMS.
* Stop the broker and restart it. 
* After 10 s the webapp will be working again.

    