To apply custom cucumber report plugin - replace existing jar files in  $JENKINS_HOME/plugins/cucumber-reports/WEB-INF/lib/   with classes.jar and cucumber-reporting-0.0.xx.jar


To configure plugins’ projects in Eclipse - add maven projects “cucumber-reports” and “cucumber-reporting”. In “cucumber-reports” configure build path and add dependency to ”cucumber-reporting” project.