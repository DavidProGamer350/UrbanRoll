FROM openjdk:17
COPY "./target/ParcialCorte3-1.5.8.jar" "app.jar"
EXPOSE 8103
ENTRYPOINT [ "java", "-jar", "app.jar" ]