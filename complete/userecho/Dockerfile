FROM oracle/graalvm-ce:20.1.0-java8 as graalvm
RUN gu install native-image

COPY . /home/app/userecho
WORKDIR /home/app/userecho

RUN native-image --no-server -cp build/libs/userecho-*-all.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/userecho/userecho /app/userecho
ENTRYPOINT ["/app/userecho"]
