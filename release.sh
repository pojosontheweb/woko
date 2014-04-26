# first we build 
mvn clean install -Pwebtests
# 2nd step deploy (2 steps needed because of javadoc aggregation plugin)
mvn deploy -Prelease -DskipTests
