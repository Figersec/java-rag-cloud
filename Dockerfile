FROM harbor.kailinedu.cn/library/java/openjdk8-kp:1.0.6

ENV TZ=Asia/Shanghai

WORKDIR /app

EXPOSE 8080 8090 9999

ADD *.jar app.jar

CMD ["/app/init.sh"]