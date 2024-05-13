FROM nginx:latest

RUN mkdir /app

WORKDIR /app

RUN mkdir ./dist

ADD ./frontend/app/dist ./dist

RUN rm /etc/nginx/conf.d/default.conf

COPY ./nginx_dev.conf /etc/nginx/conf.d

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
