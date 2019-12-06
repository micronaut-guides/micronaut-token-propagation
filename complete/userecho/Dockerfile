FROM oracle/graalvm-ce:19.3.0-java8 as graalvm
COPY . /home/app/userecho
WORKDIR /home/app/userecho
RUN gu install native-image
RUN native-image --no-server -cp build/libs/*-all.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/userecho /app/userecho
ENTRYPOINT ["/app/userecho","-Xmx68m"]