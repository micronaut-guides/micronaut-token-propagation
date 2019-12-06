FROM oracle/graalvm-ce:19.3.0-java8 as graalvm
COPY . /home/app/gateway
WORKDIR /home/app/gateway
RUN gu install native-image
RUN native-image --no-server -cp build/libs/*-all.jar

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/gateway /app/gateway
ENTRYPOINT ["/app/gateway","-Xmx68m"]