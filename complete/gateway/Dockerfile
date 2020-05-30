FROM oracle/graalvm-ce:20.1.0-java8 as graalvm
RUN gu install native-image

COPY . /home/app/gateway
WORKDIR /home/app/gateway

RUN native-image --no-server -cp build/libs/gateway-*-all.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/gateway/gateway /app/gateway
ENTRYPOINT ["/app/gateway"]
