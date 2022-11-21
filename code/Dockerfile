FROM maven:3.8.6-openjdk-11 as mvn_build
# a la mkdir app && cd app
WORKDIR /app
# get our pom into the container
COPY pom.xml .
COPY src src
# build and download deps
RUN mvn clean package -DskipTests
# extract the files from the jar (-x is for extract, -f is to specify a file)
RUN mkdir -p target/dependency && cd target/dependency && jar -xf ../specitem-database*.jar


FROM amazoncorretto:11
WORKDIR /app

#copy built app layer by layer
ARG DEPS=/app/target/dependency
COPY --from=mvn_build ${DEPS}/BOOT-INF/lib /app/lib
COPY --from=mvn_build ${DEPS}/BOOT-INF/classes /app/classes
COPY --from=mvn_build ${DEPS}/META-INF /app/META-INF

#run the app
CMD java -cp .:classes:lib/* amos.specitemdatabase.SpecitemDatabaseApplication






