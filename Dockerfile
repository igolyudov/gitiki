FROM maven:3.6.3-openjdk-17-slim AS build
RUN mkdir /app
COPY . /app
WORKDIR /app
RUN mvn -Dmaven.test.skip=true compile package

FROM bellsoft/liberica-openjdk-debian:17.0.5
ENV GITWIKI_FILE gitiki-0.0.1.jar
ENV GITWIKI_HOME /app
RUN mkdir -p $GITWIKI_HOME/gitiki
RUN mkdir -p $GITWIKI_HOME
COPY --from=build /app/target/$GITWIKI_FILE $GITWIKI_HOME

WORKDIR $GITWIKI_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $GITWIKI_FILE"]
