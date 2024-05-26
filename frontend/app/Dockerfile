FROM node:18 as build-stage
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY nginx_dev.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]
