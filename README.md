
```
 ./mvnw clean package -DskipTests=true

# Run aside garden-runc
java -jar /tmp/garden-exporter-0.0.1-SNAPSHOT.jar
```

* UDP 3457 => UDP endpoint for garden
* TCP 3458 => HTTP endpoint for prometheus


```
$ curl localhost:3458/actuator/prometheus
# HELP memoryStats_lastGCPauseTimeNS_count  
# TYPE memoryStats_lastGCPauseTimeNS_count gauge
memoryStats_lastGCPauseTimeNS_count 349543.0
# HELP memoryStats_numMallocs_count  
# TYPE memoryStats_numMallocs_count gauge
memoryStats_numMallocs_count 2.7369397E7
# HELP numGoRoutines_count  
# TYPE numGoRoutines_count gauge
numGoRoutines_count 409.0
# HELP memoryStats_numBytesAllocatedHeap_count  
# TYPE memoryStats_numBytesAllocatedHeap_count gauge
memoryStats_numBytesAllocatedHeap_count 1.9688536E7
# HELP memoryStats_numFrees_count  
# TYPE memoryStats_numFrees_count gauge
memoryStats_numFrees_count 2.7299548E7
# HELP memoryStats_numBytesAllocatedStack_count  
# TYPE memoryStats_numBytesAllocatedStack_count gauge
memoryStats_numBytesAllocatedStack_count 6651904.0
# HELP numCPUS_count  
# TYPE numCPUS_count gauge
numCPUS_count 4.0
# HELP memoryStats_numBytesAllocated_count  
# TYPE memoryStats_numBytesAllocated_count gauge
memoryStats_numBytesAllocated_count 1.9688536E7
# HELP ContainerCreationDuration_nanos  
# TYPE ContainerCreationDuration_nanos gauge
ContainerCreationDuration_nanos 4.27993064E8
```

